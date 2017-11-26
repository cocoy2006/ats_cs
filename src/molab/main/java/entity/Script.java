package molab.main.java.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Script entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SCRIPT", catalog = "MOLAB")
public class Script implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer testcaseId;
	private Integer step;
	private String action;
	private String params;
	private String mid;
	private String mclass;
	private String mtext;
	private Integer mleft;
	private Integer mtop;
	private Integer mwidth;
	private Integer mheight;
	private String mpath;
	private String screenshot;
	private String note;
	private Long createTime;
	private Long diffTime;

	// Constructors

	/** default constructor */
	public Script() {
	}

	/** minimal constructor */
	public Script(Integer testcaseId, Integer step, String action, String params) {
		this.testcaseId = testcaseId;
		this.step = step;
		this.action = action;
		this.params = params;
	}

	/** full constructor */
	public Script(Integer testcaseId, Integer step, String action,
			String params, String mid, String mclass, String mtext,
			Integer mleft, Integer mtop, Integer mwidth, Integer mheight,
			String note, Long createTime, Long diffTime) {
		this.testcaseId = testcaseId;
		this.step = step;
		this.action = action;
		this.params = params;
		this.mid = mid;
		this.mclass = mclass;
		this.mtext = mtext;
		this.mleft = mleft;
		this.mtop = mtop;
		this.mwidth = mwidth;
		this.mheight = mheight;
		this.note = note;
		this.createTime = createTime;
		this.diffTime = diffTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TESTCASE_ID", nullable = false)
	public Integer getTestcaseId() {
		return this.testcaseId;
	}

	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}

	@Column(name = "STEP", nullable = false)
	public Integer getStep() {
		return this.step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	@Column(name = "ACTION", nullable = false, length = 10)
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "PARAMS", nullable = false, length = 100)
	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "MID", length = 100)
	public String getMid() {
		return this.mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
	
	@Column(name = "MCLASS", length = 100)
	public String getMclass() {
		return this.mclass;
	}

	public void setMclass(String mclass) {
		this.mclass = mclass;
	}

	@Column(name = "MTEXT", length = 100)
	public String getMtext() {
		return this.mtext;
	}

	public void setMtext(String mtext) {
		this.mtext = mtext;
	}

	@Column(name = "MLEFT")
	public Integer getMleft() {
		return this.mleft;
	}

	public void setMleft(Integer mleft) {
		this.mleft = mleft;
	}

	@Column(name = "MTOP")
	public Integer getMtop() {
		return this.mtop;
	}

	public void setMtop(Integer mtop) {
		this.mtop = mtop;
	}

	@Column(name = "MWIDTH")
	public Integer getMwidth() {
		return this.mwidth;
	}

	public void setMwidth(Integer mwidth) {
		this.mwidth = mwidth;
	}

	@Column(name = "MHEIGHT")
	public Integer getMheight() {
		return this.mheight;
	}

	public void setMheight(Integer mheight) {
		this.mheight = mheight;
	}

	@Column(name = "MPATH")
	public String getMpath() {
		return mpath;
	}

	public void setMpath(String mpath) {
		this.mpath = mpath;
	}

	@Column(name = "SCREENSHOT")
	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "CREATE_TIME")
	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Column(name = "DIFF_TIME")
	public Long getDiffTime() {
		return this.diffTime;
	}

	public void setDiffTime(Long diffTime) {
		this.diffTime = diffTime;
	}

}