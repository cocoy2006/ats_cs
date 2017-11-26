package molab.main.java.util.shell;

import java.util.logging.Logger;

import com.android.ddmlib.IDevice;

public class ShellHelper {
	
	private static final Logger log = Logger.getLogger(ShellHelper.class.getName());
	
	public static int pid(IDevice device, String packageName) {
		try {
			Ps ps = ps(device, packageName);
			if(ps != null) {
				return ps.getPid();
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		return 0;
	}
	
	public static Ps ps(IDevice device, String packageName) {
		PsReceiver pr = new PsReceiver(packageName);
		try {
			device.executeShellCommand("ps", pr);
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		pr.done();
		return pr.getPs();
	}
	
	public static Top top(IDevice iDevice, String packageName, int sdkVersion) {
		TopReceiver tr = new TopReceiver(packageName, sdkVersion);
		try {
			iDevice.executeShellCommand("top -n 1", tr);
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		tr.done();
		return tr.getTop();
	}
	
	public static long pss(IDevice iDevice, String packageName, int sdkVersion)	{
		MeminfoReceiver mr = new MeminfoReceiver(packageName, sdkVersion);
		try {
			iDevice.executeShellCommand("dumpsys meminfo " + packageName, mr);
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		mr.done();
		return mr.getMemory();
	}
	
	public static float cpuRate(IDevice iDevice, int pid) {
		try {
			float totalTime1 = cpuTotalTime(iDevice);
			float appTime1 = cpuAppTime(iDevice, pid);
			Thread.sleep(1000);
			float totalTime2 = cpuTotalTime(iDevice);
			float appTime2 = cpuAppTime(iDevice, pid);
			return 100 * (appTime2 - appTime1) / (totalTime2 - totalTime1);
		} catch(Exception e) {
			log.severe(e.getMessage());
			e.getStackTrace();
		}
		return 0;
	}
	
	public static long cpuTotalTime(IDevice iDevice) {
		CatReceiver cr = new CatReceiver();
		try {
			String command = "cat /proc/stat";
			iDevice.executeShellCommand(command, cr);
			cr.done();
			String[] cpuInfos = cr.getSb().toString().split(" ");
//			log.info("TotalCpuTime is: " + cr.getSb().toString());
			return Long.parseLong(cpuInfos[2])
            + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
            + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
            + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.getStackTrace();
		}
		return 0;
	}
	
	public static long cpuAppTime(IDevice iDevice, int pid) {
		CatReceiver cr = new CatReceiver();
		try {
			String command = "cat /proc/" + pid + "/stat";
			iDevice.executeShellCommand(command, cr);
			cr.done();
			String[] cpuInfos = cr.getSb().toString().split(" ");
//			log.info("AppCpuTime is: " + cr.getSb().toString());
			return Long.parseLong(cpuInfos[13])
            + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
            + Long.parseLong(cpuInfos[16]);
		} catch (Exception e) {
			log.severe(e.getMessage());
			e.getStackTrace();
		}
		return 0;
	}
	
}
