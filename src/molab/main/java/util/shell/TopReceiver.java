package molab.main.java.util.shell;

import java.util.logging.Logger;

import com.android.ddmlib.MultiLineReceiver;

public class TopReceiver extends MultiLineReceiver {

	private static final Logger log = Logger.getLogger(TopReceiver.class.getName());
	private String processName = null;
	private int sdkVersion;

	public boolean isCancelled = false;
	public Top top;

	TopReceiver(String processName, int sdkVersion) {
		super();
		setTrimLine(false);
		this.processName = processName;
		this.sdkVersion = sdkVersion;
	}

	@Override
	public void processNewLines(String[] lines) {
		if (isCancelled == false && processName != null) {
			for (String line : lines) {
				if (line.indexOf(processName) > -1) {
					log.info(line);
					String[] indexs = line.trim().replaceAll(" +", " ").split(" ");
					top = new Top();
					if(sdkVersion > 12) {
						top.setCpu(indexs[2]);
						top.setRss(indexs[6]);
					} else {
						top.setCpu(indexs[1]);
						top.setRss(indexs[5]);
					}
					break;
				}
			}
		}
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	/**
	 * @return the top
	 */
	public Top getTop() {
		return top;
	}

	/**
	 * @param top
	 *            the top to set
	 */
	public void setTop(Top top) {
		this.top = top;
	}

}
