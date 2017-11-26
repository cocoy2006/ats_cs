package molab.main.java.util.shell;

import java.util.logging.Logger;

import com.android.ddmlib.MultiLineReceiver;

public class PsReceiver extends MultiLineReceiver {

	private static final Logger log = Logger.getLogger(PsReceiver.class.getName());
	private String processName = null;

	public boolean isCancelled = false;
	public Ps ps;

	public PsReceiver(String processName) {
		super();
		setTrimLine(false);
		this.processName = processName;
	}

	@Override
	public void processNewLines(String[] lines) {
		if (isCancelled == false && processName != null) {
			for (String line : lines) {
				if(line.indexOf(processName) > -1) {
					log.info(line);
					String[] indexs = line.replaceAll(" +", " ").split(" ");
					ps = new Ps();
					ps.setPid(Integer.parseInt(indexs[1]));
					break;
				}
			}
		}
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	/**
	 * @return the ps
	 */
	public Ps getPs() {
		return ps;
	}

	/**
	 * @param ps
	 *            the ps to set
	 */
	public void setPs(Ps ps) {
		this.ps = ps;
	}

}
