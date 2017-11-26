package molab.main.java.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * CsScreenshot entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CS_SCREENSHOT", catalog = "MOLAB")
public class CsScreenshot implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer runnerId;
	private String image;
	private String action;
	private String params;
	private String note;
	private Double matches;
	private String description;
	private Long oprTime;
	private int state;
	
	// Classes mapping
	private Script script;

	// Constructors

	/** default constructor */
	public CsScreenshot() {
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

	@Column(name = "RUNNER_ID", nullable = false)
	public Integer getRunnerId() {
		return this.runnerId;
	}

	public void setRunnerId(Integer runnerId) {
		this.runnerId = runnerId;
	}

	@Column(name = "IMAGE", nullable = false)
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "ACTION")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "PARAMS")
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "NOTE")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "MATCHES")
	public Double getMatches() {
		return matches;
	}

	public void setMatches(Double matches) {
		this.matches = matches;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "OPR_TIME")
	public Long getOprTime() {
		return this.oprTime;
	}

	public void setOprTime(Long oprTime) {
		this.oprTime = oprTime;
	}
	
	@Column(name = "STATE")
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SCRIPT_ID")
	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

}