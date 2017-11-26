package molab.main.java.util.shell;

import java.util.logging.Logger;

import com.android.ddmlib.MultiLineReceiver;

public class MeminfoReceiver extends MultiLineReceiver {

	private static final Logger log = Logger.getLogger(TopReceiver.class.getName());
	private String processName = null;
	private int sdkVersion;

	public boolean isCancelled = false;
	public long memory = 0;
	
	MeminfoReceiver(String processName, int sdkVersion) {
		super();
		setTrimLine(false);
		this.processName = processName;
		this.sdkVersion = sdkVersion;
	}
	
	@Override
	public void processNewLines(String[] lines) {
		if (isCancelled == false && processName != null) {
			for (String line : lines) {
				if(sdkVersion > 12) {
					if(line.indexOf("TOTAL") > -1) {
						String[] indexs = line.trim().replaceAll(" +", " ").split(" ");
//						log.info(processName + "'s memory is " + line);
						memory += Long.parseLong(indexs[1]);
					}
				} else {
					if(line.indexOf("Pss") > -1) {
						String[] indexs = line.trim().replaceAll(" +", " ").split(" ");
//						log.info(processName + "'s memory is " + line);
						memory += Long.parseLong(indexs[indexs.length - 1]);
					}
				}
			}
		}
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	/**
	 * @return the memory
	 */
	public long getMemory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(long memory) {
		this.memory = memory;
	}

}
