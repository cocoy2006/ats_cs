package molab.main.java.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import molab.main.java.dao.DeviceDao;
import molab.main.java.entity.Device;
import molab.main.java.util.Molab;
import molab.main.java.util.Path;
import molab.main.java.util.Security;
import molab.main.java.util.Status;
import molab.main.java.util.init.Adb;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;

@Service
public class InitializingService implements InitializingBean {
	
	private static final Logger log = Logger.getLogger(InitializingService.class.getName());
	private static final int DEVICE_TYPE = Status.DeviceType.CS.getInt();
	private Adb adb;
	private Molab molab;
//	private Map<String, String> romInfoMap;
	
	@Autowired
	private DeviceDao dao;

	@Override
	public void afterPropertiesSet() throws Exception {
		adb = Adb.getInstance();
		molab = Molab.getInstance();
		// initialize configuration
		initAtsgz();
		// initialize rom info map
//		initRomInfoMap();
		// initialize device state
		initDevices();
		// initialize device change listener
		AndroidDebugBridge.addDeviceChangeListener(new Listener());
		log.info("Device change listener initialized.");
	}
	
	private void initAtsgz() {
		long lastTime = Security.getLastTime(Path.atsgz());
		if(lastTime == 0) {
			log.severe("System is out of time.");
			System.exit(0);
		} else {
			Security.setLastTime(Path.atsgz(), System.currentTimeMillis());
		}
	}
	
//	private void initRomInfoMap() {
//		romInfoMap = new HashMap<String, String>();
//		String romInfo = molab.getProperty(Molab.CFG_ROM_INFO);
//		String[] brandList = romInfo.split("\\|");
//		for(String brand : brandList) {
//			String[] romList = brand.split(",");
//			romInfoMap.put(romList[0], romList[1]);
//		}
//	}
	
	private void initDevices() {
		List<Device> deviceList = findDevices();
		for(Device device : deviceList) {
			if(adb.getBackend().findAttacedDevice(device.getSn()) == null) {
				log.info(device.getSn() + " undetected, change to OFFLINE.");
				device.setState(Status.DeviceStatus.OFFLINE.getInt());
				dao.update(device);
			}
		}
	}
	
	private List<Device> findDevices() {
		String server = Molab.getInstance().getProperty(Molab.CFG_SELF_SERVER);
		int port = Integer.parseInt(Molab.getInstance().getProperty(Molab.CFG_SELF_PORT));
		String hql = "from Device where server=? and port=? and type=? and state=?";
		return dao.find(hql, server, port, DEVICE_TYPE, Status.DeviceStatus.FREE.getInt());
	}
	
	class Listener implements IDeviceChangeListener {

		public void deviceChanged(IDevice iDevice, int changeMask) {
			if((changeMask & IDevice.CHANGE_STATE) != 0 && iDevice.isOnline()) {
				activeDevice(iDevice);
			}
		}

		public void deviceConnected(IDevice iDevice) {}

		public void deviceDisconnected(IDevice iDevice) {
			removeDevice(iDevice);
		}
		
	}
	
	private void activeDevice(IDevice iDevice) {
		log.info(iDevice.getSerialNumber() + " connected, try to get information...");
		Device device = findDevice(iDevice);
		// exist
		if(device.getId() != null) {
			device.setState(Status.DeviceStatus.FREE.getInt());
			dao.update(device);
			log.info(iDevice.getSerialNumber() + " already exist, change to FREE.");
		// otherwise
		} else { 
			// save device.txt to Path.device()
//			try {
//				Adb.getprop(iDevice, device);
//			} catch (IOException | SyncException | AdbCommandRejectedException | TimeoutException e1) {}
			// manufacturer
			String manufacturer = Adb.getprop(iDevice, IDevice.PROP_DEVICE_MANUFACTURER);
			device.setManufacturer(manufacturer);
			// model
			String model = Adb.getprop(iDevice, IDevice.PROP_DEVICE_MODEL);
			device.setModel(model);
			// os
			String os = Adb.getprop(iDevice, IDevice.PROP_BUILD_VERSION);
			device.setOs(os);
			// sdk
			String sdkStr = Adb.getprop(iDevice, IDevice.PROP_BUILD_API_LEVEL);
			int sdk = 0;
			if(sdkStr != null) {
				sdk = Integer.parseInt(sdkStr);
			}
			device.setSdk(sdk);
			// rom
//			try {
//				String rom = null;
//				if(manufacturer != null) {
//					rom = Adb.getprop(iDevice, romInfoMap.get(manufacturer.toLowerCase()).toString());
//				}
//				device.setRom(rom);
//			} catch(Exception e) {
//				log.severe("Rom info do not found, because of: " + e.getMessage());
//			}
			// width & height
			int width = 0, height = 0;
			try {
				RawImage rImage = iDevice.getScreenshot();
				width = rImage.width;
				height = rImage.height;
			} catch (TimeoutException | AdbCommandRejectedException
					| IOException e) {}
			device.setWidth(width);
			device.setHeight(height);
			// type
			device.setType(DEVICE_TYPE);
			dao.save(device);
			log.info(iDevice.getSerialNumber() + " create successfully.");
		}
	}
	
	private void removeDevice(IDevice iDevice) {
		Device device = findDevice(iDevice);
		// exist
		if(device.getId() != null) {
			device.setState(Status.DeviceStatus.OFFLINE.getInt());
			dao.update(device);
			log.info(iDevice.getSerialNumber() + " removed, change to OFFLINE.");
		}
	}
	
	private Device findDevice(IDevice iDevice) {
		Device device = new Device();
		String server = molab.getProperty(Molab.CFG_SELF_SERVER);
		int port = Integer.parseInt(Molab.getInstance().getProperty(Molab.CFG_SELF_PORT));
		device.setServer(server);
		device.setPort(port);
		device.setSn(iDevice.getSerialNumber());
		String hql = "from Device where server=? and port=? and sn=?";
		List<Device> deviceList = dao.find(hql, server, port, iDevice.getSerialNumber());
		if(deviceList != null && deviceList.size() > 0) {
			device = deviceList.get(0);
		}
		return device;
	}
	
}
