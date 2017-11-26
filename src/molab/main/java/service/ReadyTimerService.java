package molab.main.java.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import molab.main.java.dao.ApplicationDao;
import molab.main.java.dao.CsDispatcherDao;
import molab.main.java.dao.CsRunnerDao;
import molab.main.java.dao.CsScreenshotDao;
import molab.main.java.dao.DeviceDao;
import molab.main.java.dao.ScriptDao;
import molab.main.java.dao.TestcaseDao;
import molab.main.java.entity.Application;
import molab.main.java.entity.CsDispatcher;
import molab.main.java.entity.CsRunner;
import molab.main.java.entity.CsScreenshot;
import molab.main.java.entity.Device;
import molab.main.java.entity.Script;
import molab.main.java.entity.Testcase;
import molab.main.java.util.HttpUtil;
import molab.main.java.util.ImageUtil;
import molab.main.java.util.Molab;
import molab.main.java.util.Path;
import molab.main.java.util.Status;
import molab.main.java.util.Status.CommandType;
import molab.main.java.util.UiAutomatorUtil;
import molab.main.java.util.init.Adb;
import molab.main.java.util.logcat.LogcatReceiver;
import molab.main.java.util.shell.Result;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.android.uiautomator.tree.UiNode;

@Service
public class ReadyTimerService {
	
	private static final Logger log = Logger.getLogger(ReadyTimerService.class.getName());
	private static final String LOGCAT = "logcat -d *:E";
	
	@Autowired
	private DeviceDao devDao;
	
	@Autowired
	private CsDispatcherDao disDao;
	
	@Autowired
	private CsRunnerDao rd;
	
	@Autowired
	private CsScreenshotDao ssd;
	
	@Autowired
	private ApplicationDao ad;
	
	@Autowired
	private TestcaseDao td;
	
	@Autowired
	private ScriptDao sd;
	
	@Scheduled(cron = "0/30 * * * * ?")
    public void run() throws IOException {
		List<CsRunner> runnerList = rd.findByState(Status.RunnerStatus.READY.getInt(), 
				Molab.getInstance().getProperty(Molab.CFG_SELF_SERVER), 
				Integer.parseInt(Molab.getInstance().getProperty(Molab.CFG_SELF_PORT)));
		if(runnerList != null && runnerList.size() > 0) {
			ThreadGroup tGroup = new ThreadGroup("sts");
			for(CsRunner runner : runnerList) {
				Device device = devDao.findByRunnerId(runner.getId());
				IDevice iDevice = Adb.getInstance().getBackend().findAttacedDevice(device.getSn());
				if(iDevice == null || !iDevice.isOnline()) {
					log.severe(String.format("Device %s is not ready.", device.getSn()));
					continue;
				} else {
					Molab.lock(iDevice);
					Testcase testcase = td.findByRunnerId(runner.getId());
					List<Script> scriptList = sd.findByTestcaseId(testcase.getId());
					Application application = ad.findById(testcase.getApplicationId());
					Thread t = new Thread(tGroup, new Running(this, runner, iDevice, testcase, scriptList, application));
					t.start();
				}
			}
			while(tGroup.activeCount() > 0) {
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					log.severe(e.getMessage());
				}
			}
			for(CsRunner runner : runnerList) {
				rd.update(runner);
			}
			clear();
		}
    }
	
	class Running extends Thread {
		
		private ReadyTimerService service;
		private CsDispatcher dispatcher;
		private CsRunner runner;
		private IDevice iDevice;
		private Testcase testcase;
		private List<Script> scriptList;
		private Application app;
		private List<CsScreenshot> ssList = new ArrayList<CsScreenshot>();
		
		public Running(ReadyTimerService service, CsRunner runner, IDevice iDevice, Testcase testcase, List<Script> scriptList, Application application) {
			this.service = service;
			this.runner = runner;
			this.dispatcher = service.disDao.findById(runner.getDispatcherId());
			this.iDevice = iDevice;
			this.testcase = testcase;
			this.scriptList = scriptList;
			this.app = application;
		}
		
		@Override
		public void run() {
			try {
				sts(); // Script Test Suite
				runner.setState(Status.RunnerStatus.END.getInt());
				// update dispatcher if necessary
//				CsDispatcher dispatcher = service.disDao.findById(runner.getDispatcherId());
				if(dispatcher.getState() == Status.DispatcherStatus.START.getInt()) {
					dispatcher.setState(Status.DispatcherStatus.REPORTING.getInt());
					service.disDao.update(dispatcher);
					log.info(String.format("Dispatcher with id %d swift to REPORTING.", dispatcher.getId()));
				}
				// update screenshot
				for(CsScreenshot ss : ssList) {
					service.ssd.save(ss);
				}
			} catch(Exception e) {
				runner.setState(Status.RunnerStatus.READY.getInt());
				log.severe(e.getMessage());
			} finally {
				try {
					Molab.release(iDevice);
				} catch (IOException e) {}
//				service.rd.update(runner);
				log.info(String.format("Runner(id=%d) done.", runner.getId()));
			}
		}
		
		private void sts() throws InstallException, InterruptedException, IOException, SyncException, TimeoutException, AdbCommandRejectedException {
			// installation
			boolean installSuccess = false;
			Result installResult = Adb.install(iDevice, Path.apk(app.getAliasname()));
			if(installResult.getResult() != null) {
				runner.setInstallResult(installResult.getResult());
				if(installResult.getResult().contains("INSTALL_SUCCESS")) { // 安装结果不是NULL，正常
					installSuccess = true;
//					runner.setState(Status.RunnerStatus.INSTALL.getInt());
//					service.rd.update(runner);
					log.info("Install success.");
				}
			}
			
			// launch
			boolean loadSuccess = false;
			if(installSuccess) {
				Result loadResult = Adb.load(iDevice, app.getPackagename(), app.getStartactivity());
				if(loadResult != null) { // 启动结果是NULL，正常
					runner.setLoadResult(loadResult.getResult());
					loadSuccess = true;
//					runner.setState(Status.RunnerStatus.LOAD.getInt());
//					service.rd.update(runner);
					log.info("Load success.");
				}
			}
			
			// play back
			if(loadSuccess) {
				Adb.light(iDevice);
				Adb.logcatClear(iDevice);
				Thread.sleep(5000);
				
//				runner.setState(Status.RunnerStatus.PLAYBACK.getInt());
//				service.rd.update(runner);
				
				RawImage rImage = iDevice.getScreenshot();
				
				for(Script script : scriptList) {
					execution(script, 
							testcase.getWidth(), testcase.getHeight(),
							rImage.width, rImage.height);
					Thread.sleep(5000);
					screencap(script);
					Thread.sleep(5000);
				}
				
				try {
					LogcatReceiver logcatReceiver = new LogcatReceiver();
					iDevice.executeShellCommand(LOGCAT, logcatReceiver);
					logcatReceiver.done();
					StringBuffer logcat = logcatReceiver.getMolab();
					// write into database
					Blob blob = Hibernate.createBlob(logcat.toString().getBytes());
					runner.setLogcat(blob);
					log.info("Logcat done.");
				} catch(Exception e) {
					log.severe(e.getMessage());
				}
			}
			Thread.sleep(5000);
			
			// uninstall
			if(installSuccess) {
				try {
					Adb.uninstall(iDevice, app.getPackagename());
//					runner.setState(Status.RunnerStatus.UNINSTALL.getInt());
//					service.rd.update(runner);
					log.info("Uninstall success.");
				} catch(Exception e) {
					log.severe(e.getMessage());
				}
			}
		}
		
		private void execution(Script script, int width, int height, int newWidth, int newHeight) throws IOException {
			log.info(String.format("%s正在执行第%d步.", iDevice.getSerialNumber(), script.getStep()));
			switch(Action.toAction(script.getAction().toUpperCase())) {
				case TAP:
					tap(script, width, height, newWidth, newHeight);
					break;
				case TEXT:
					text(script.getParams());
					break;
				case SWIPE:
					swipe(script.getParams(), width, height, newWidth, newHeight);
				default:
					break;
			}
		}
		
		private void tap(Script script, int width, int height, int newWidth, int newHeight) throws IOException {
			String[] params_s = script.getParams().split(" ");
			int x = zoom(Integer.parseInt(params_s[0]), width, newWidth); 
			int y = zoom(Integer.parseInt(params_s[1]), height, newHeight);
			// must not be any WebView
			if(!script.getMclass().contains("WebView")) {
				boolean found = false;
				long starttime = System.currentTimeMillis();
				while(true) {
					if(System.currentTimeMillis() - starttime > dispatcher.getTimeout() * 1000) {
						break;
					}
					UiNode node = UiAutomatorUtil.findNode(iDevice, script);
					if(node != null) {
						x = node.x + node.width / 2;
						y = node.y + node.height / 2;
						found = true;
						break;
					} else {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {}
					}
				}
				if(!found) {
					log.info("找不到控件:" + script.getMclass());
					// TODO cs_screenshot.setDescription("Widget not found.")
				}
			}
			
			Adb.executeSync(iDevice, CommandType.SHELL, String.format("input tap %d %d", x, y));
		}
		
		private void text(String params) throws IOException {
			Adb.executeSync(iDevice, CommandType.SHELL, "input text " + params);
		}
		
		private void swipe(String param, int width, int height, int newWidth, int newHeight) throws IOException {
			int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
			if(param.indexOf(" ") > -1) {
				String[] params = param.split(" ");
				x1 = zoom(Integer.parseInt(params[0]), width, newWidth); 
				y1 = zoom(Integer.parseInt(params[1]), height, newHeight);
				x2 = zoom(Integer.parseInt(params[2]), width, newWidth); 
				y2 = zoom(Integer.parseInt(params[3]), height, newHeight);
			} else {
				switch(param) {
					case "left":
						x1 = newWidth - 50;
						x2 = 50;
						y1 = y2 = newHeight / 2;
						break;
					case "right":
						x1 = 50;
						x2 = newWidth - 50;
						y1 = y2 = newHeight / 2;
						break;
					case "up":
						x1 = x2 = newWidth / 2;
						y1 = newHeight - 50;
						y2 = 50;
						break;
					case "down":
						x1 = x2 = newWidth / 2;
						y1 = 50;
						y2 = newHeight - 50;
						break;
					default:
						break;
				}
			}
			
			Adb.executeSync(iDevice, CommandType.SHELL, String.format("input swipe %d %d %d %d", x1, y1, x2, y2));
		}
		
		private int zoom(int in, int inCoord, int outCoord) {
			return outCoord * in / inCoord;
		}
		
		private int ssCount = 1;
		private void screencap(Script script) throws IOException, InterruptedException, SyncException, TimeoutException, AdbCommandRejectedException {
			String name = runner.getId() + "." + ssCount + ".png";
			String tmp = Path.temp(name);
			File png = Adb.screencap(iDevice, tmp);
			BufferedImage bImage = ImageIO.read(png);
			if(bImage != null) {
//				bImage = ImageUtil.zoom(bImage);
				// post to file server
				String host = Molab.host() + "csApi/file";
				HttpUtil.post(host, png);
				// write into database
				CsScreenshot ss = new CsScreenshot();
				ss.setRunnerId(runner.getId());
				ss.setScript(script);
//				ss.setImage(ImageUtil.convert(bImage));
				ss.setImage(name);
				ss.setAction(script.getAction());
				ss.setParams(script.getParams());
				ss.setNote(script.getNote());
				// calculate matches
				try {
					name = script.getScreenshot();
//					if(path != null && path.lastIndexOf("/") >= 0) {
//						name = path.substring(path.lastIndexOf("/") + 1);
						File file = new File(Path.crSs(name));
						if(file.exists()) { // crSs exist on local
							// do nothing
						} else { // otherwise, download from server
							String remote = Molab.host() + Molab.PATH_CRSS + name;
							String local = Path.crSs(name);
							HttpUtil.download(remote, local);
						}
						BufferedImage pImage = ImageIO.read(file);
						if(bImage.getWidth() > pImage.getWidth()) { // bImage ('s width) is bigger than pImage
							bImage = ImageUtil.zoom(bImage, pImage.getWidth(), pImage.getHeight());
						} else if(bImage.getWidth() < pImage.getWidth()) { // otherwise
							pImage = ImageUtil.zoom(pImage, bImage.getWidth(), bImage.getHeight());	
						} else {
							// do nothing if they are same size
						}
						double matches = ImageUtil.matches(bImage, pImage);
						ss.setMatches(matches);
//					}
				} catch(Exception e) {
					log.severe("Mismatch.");
				}
				
				ss.setOprTime(System.currentTimeMillis());
				ssList.add(ss);
				ssCount++;
			}
		}
	}
	
	enum Action {
		WAKE, TAP, KEYEVENT, SWIPE, TEXT, TYPEX, SYNC, REMOVE, INSTALL, REINSTALL, UNINSTALL, START, UPLOAD, RESERVE, ERROR;
		
		public static Action toAction(String action) {
			try {
				return valueOf(action);
			} catch(Exception e) {
				return ERROR;
			}
		}
	}
	
	private void clear() {
		try {
			File temp = new File(Path.temp());
			if(temp.exists() && temp.isDirectory()) {
				File[] files = temp.listFiles();
				for(File file : files) {
					file.delete();
				}
			}
		} catch(Exception e) {
			// TODO nothing
		}
	}
	
}
