package molab.main.java.util.shell;

public class Ps {

	private String user;
	private int pid;
	private int ppid;
	private int vsize;
	private int rss;
	private String wchan;
	private String pc;
	private String s;
	private String name;

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pid
	 */
	public int getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            the pid to set
	 */
	public void setPid(int pid) {
		this.pid = pid;
	}

	/**
	 * @return the ppid
	 */
	public int getPpid() {
		return ppid;
	}

	/**
	 * @param ppid
	 *            the ppid to set
	 */
	public void setPpid(int ppid) {
		this.ppid = ppid;
	}

	/**
	 * @return the vsize
	 */
	public int getVsize() {
		return vsize;
	}

	/**
	 * @param vsize
	 *            the vsize to set
	 */
	public void setVsize(int vsize) {
		this.vsize = vsize;
	}

	/**
	 * @return the rss
	 */
	public int getRss() {
		return rss;
	}

	/**
	 * @param rss
	 *            the rss to set
	 */
	public void setRss(int rss) {
		this.rss = rss;
	}

	/**
	 * @return the wchan
	 */
	public String getWchan() {
		return wchan;
	}

	/**
	 * @param wchan
	 *            the wchan to set
	 */
	public void setWchan(String wchan) {
		this.wchan = wchan;
	}

	/**
	 * @return the pc
	 */
	public String getPc() {
		return pc;
	}

	/**
	 * @param pc
	 *            the pc to set
	 */
	public void setPc(String pc) {
		this.pc = pc;
	}

	/**
	 * @return the s
	 */
	public String getS() {
		return s;
	}

	/**
	 * @param s
	 *            the s to set
	 */
	public void setS(String s) {
		this.s = s;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
