package molab.main.java.util.init;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import molab.main.java.util.Status.CommandType;
import molab.main.java.util.shell.Result;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.android.monkeyrunner.adb.AdbBackend;

public class Adb {

	private static final Logger log = Logger.getLogger(Adb.class.getName());
	private static final String LIST_PACKAGES = "pm list packages -3";
	private static final String AM_START = "am start -W -n %s/%s"; // @param1 package name, param2 start activity name
	private static final String LIGHT = "input tap 1 1";
	private static final String LOGCAT_CLEAR = "logcat -c";
	private static final String LOGCAT = "logcat -d *:E";
	private static final String TEMP_PNG = "/data/local/tmp/tmp.png";
	private static final String SCREENCAP = "/system/bin/screencap -p " + TEMP_PNG;
	private static final String IP = "ip -f inet addr show wlan0";
	private static final String GET_PROP = "getprop %s";

	private static Adb adb = null;
	private static AdbBackend backend = null;
	private static Set<String> whiteList = new HashSet<String>() {{
		add("com.android.adbkeyboard");
	}};

	private Adb() {
		backend = new AdbBackend();
	}

	public static Adb getInstance() {
		if (adb == null) {
			synchronized (Adb.class) {
				adb = new Adb();
			}
		}
		return adb;
	}
	
	public AdbBackend getBackend() {
		return backend;
	}
	
	public static String getprop(IDevice iDevice, String prop) {
		try {
			if(prop == null) {
				prop = IDevice.PROP_BUILD_DISPLAY;
			}
			Result result = executeSync(iDevice, CommandType.SHELL, String.format(GET_PROP, prop));
			if(result != null && result.getResult() != null && !result.getResult().trim().equals("")) {
				return result.getResult().trim();
			}
		} catch (IOException e) {
			log.severe(String.format("Property %s cannot be found.", prop));
		}
		return null;
	}
	
	public static void clear(IDevice iDevice) throws IOException {
		Result result = executeSync(iDevice, CommandType.SHELL, LIST_PACKAGES);
		if(result != null && !result.getResult().equals("")) {
			String[] pList = result.getResult().split("\\r\\n");
			for(String p : pList) {
				String name = p.substring(8);
				if(!whiteList.contains(name)) {
					uninstall(iDevice, name);
				}
			}
		}
	}
	
	public static Result install(IDevice iDevice, String apk) throws IOException {
		Result result = executeSync(iDevice, CommandType.INSTALL, apk);
		if(result.getResult() == null) {
			result.setResult("INSTALL_SUCCESS");
		}
		log.info(String.format("安装结束: %s, 耗时 %d 毫秒", result.getResult(), result.getTime()));
		return result;
	}
	
	public static Result load(IDevice iDevice, String packageName, String startActivity) throws IOException {
		Result result = executeSync(iDevice, CommandType.SHELL, 
				String.format(AM_START, packageName, startActivity));
		if(result.getResult() != null) {
			result.setResult("LOAD_SUCCESS");
		}
		log.info(String.format("加载结束: %s, 耗时 %d 毫秒", result.getResult(), result.getTime()));
		return result;
	}
	
	public static Result light(IDevice iDevice) throws IOException {
		return executeSync(iDevice, CommandType.SHELL, LIGHT);
	}
	
	public static Result logcatClear(IDevice iDevice) throws IOException {
		return executeSync(iDevice, CommandType.SHELL, LOGCAT_CLEAR);
	}
	
	public static Result logcat(IDevice iDevice) throws IOException {
		return executeSync(iDevice, CommandType.SHELL, LOGCAT);
	}
	
	public static File screencap(IDevice iDevice, String path) throws IOException, SyncException, AdbCommandRejectedException, TimeoutException {
		screencap(iDevice);
		iDevice.pullFile(TEMP_PNG, path);
		return new File(path);
	}
	
	public static Result screencap(IDevice iDevice) throws IOException {
		return executeSync(iDevice, CommandType.SHELL, SCREENCAP);
	}
	
	public static Result uninstall(IDevice iDevice, String packageName) throws IOException {
		Result result = executeSync(iDevice, CommandType.UNINSTALL, packageName);
		if(result.getResult() == null) {
			result.setResult("UNINSTALL_SUCCESS");
		}
		log.info(String.format("卸载结束: %s, 耗时 %d 毫秒", result.getResult(), result.getTime()));
		return result;
	}
	
	public static Result ip(IDevice iDevice) throws IOException {
		Result result = executeSync(iDevice, CommandType.SHELL, IP);
		log.info(String.format("无线网络情况: %s", result.getResult()));
		return result;
	}

	public static Result executeSync(IDevice iDevice, CommandType cmdType, String cmd) throws IOException {
		synchronized(Adb.class) {
			long start = System.currentTimeMillis();
			Result result = null;
			try {
				String r = execute(iDevice, cmdType, cmd);
				long time = System.currentTimeMillis() - start;
				result = new Result(r, time);
			} catch (Exception e) {
				restartAdb(iDevice);
				throw new IOException("Adb crash.");
			}
			return result;
		}
	}
	
	private static String execute(IDevice iDevice, CommandType cmdType, String param) 
			throws InstallException, SyncException, IOException, AdbCommandRejectedException, TimeoutException, ShellCommandUnresponsiveException {
		log.info("Molab command: " + param);
		String result = null;
		switch(cmdType) {
			case INSTALL:
				result = iDevice.installPackage(param, "/data/local/tmp/", true);
				break;
			case UNINSTALL:
				result = iDevice.uninstallPackage(param);
				break;
			case SHELL:
				result = iDevice.shell(param);
				break;
			default:
				break;
		}
		return result;
	}
	
	private static void restartAdb(IDevice iDevice) {
		log.severe("Adb has down, try to reboot device " + iDevice.getSerialNumber());
//		adb.getBackend().getBridge().killAdb();
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {}
//		adb.getBackend().getBridge().startAdb();
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {}
		try {
			iDevice.shell("reboot");
			Thread.sleep(60000);
		} catch (IOException | InterruptedException | TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException e) {}
		log.severe("Device has restarted.");
	}

}
