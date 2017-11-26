package molab.main.java.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import molab.main.java.entity.Application;
import molab.main.java.util.init.Adb;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceState;

public class Molab {

	private static final Logger log = Logger.getLogger(Molab.class.getName());
	public static final String LAST_TIME = "opoL8fPcOoiJWZf594oE5Q=="; // 2016.04.29, useless but for atsgz
	public static final String EXPIRE_TIME = "GBDyfEFjcLaDp5Nv1+H1IQ=="; // 2017.04.15
	public static final String CFG_SELF_SERVER = "self_server";
	public static final String CFG_SELF_PORT = "self_port";
	public static final String CFG_WEB_SERVER = "web_server";
	public static final String CFG_WEB_PORT = "web_port";
	public static final String CFG_WEB_NAME = "web_name";
	public static final String CFG_ROM_INFO = "rom_info";
	public static final String PATH_CRSS = "upload/crSs/";
	
//	public static final Map<String, String> BRAND_ROM = new HashMap<String, String>() {
//		{
//			put("meizu", "ro.build.display.id");
//			put("oppo", "ro.rom.different.version");
//			put("vivo", "ro.vivo.os.build.display.id");
//			put("xiaomi", "ro.miui.ui.version.name");
//		}
//	};

	private static Molab molab = null;
	private static Properties props = null;

	public static Molab getInstance() {
		if (molab == null) {
			synchronized (Molab.class) {
				molab = new Molab();
				props = PropertiesUtil.loadProperties(Path.cfg());
			}
		}
		return molab;
	}
	
	public static boolean isAppExist(Application app) {
		if(app.getAliasname() != null) {
			String apk = Path.apk(app.getAliasname());
			File file = new File(apk);
			return file.exists();
		}
		return false;
	}
	
	public String getProperty(String key) {
		if(props.containsKey(key)) {
			return props.getProperty(key);
		}
		return null;
	}
	
	public static void lock(IDevice iDevice) throws IOException {
		if(iDevice != null) {
			log.info("Device state swift to BUSY.");
			iDevice.setState(DeviceState.BUSY);
			// uninstall -3 apps
			Adb.clear(iDevice);
		}
	}
	
	public static void release(IDevice iDevice) throws IOException {
		if(iDevice != null) {
			log.info("Device state swift to ONLINE.");
			iDevice.setState(DeviceState.ONLINE);
			// uninstall -3 apps
//			Adb.clear(iDevice);
		}
	}
	
	public static String host() {
		return String.format(
				"http://%s:%s/%s/", 
				getInstance().getProperty(CFG_WEB_SERVER), 
				getInstance().getProperty(CFG_WEB_PORT), 
				getInstance().getProperty(CFG_WEB_NAME));
	}

}
