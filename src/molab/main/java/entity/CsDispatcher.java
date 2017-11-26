package molab.main.java.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CtDispatcher entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CS_DISPATCHER", catalog = "MOLAB")
public class CsDispatcher implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer testcaseId;
	private Integer timeout;
	private Long oprTime;
	private Integer state;

	// Constructors

	/** default constructor */
	public CsDispatcher() {
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
		return testcaseId;
	}

	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}

	@Column(name = "TIMEOUT")
	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	@Column(name = "OPR_TIME")
	public Long getOprTime() {
		return this.oprTime;
	}

	public void setOprTime(Long oprTime) {
		this.oprTime = oprTime;
	}

	@Column(name = "STATE", nullable = false)
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}