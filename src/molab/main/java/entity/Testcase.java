package molab.main.java.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Testcase entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TESTCASE", catalog = "MOLAB")
public class Testcase implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer projectId;
	private Integer applicationId;
	private String name;
	private String testcase;
	private Integer width;
	private Integer height;
	private Long createTime;
	private Long modifyTime;
	private Integer state;

	// Constructors

	/** default constructor */
	public Testcase() {
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

	@Column(name = "PROJECT_ID", nullable = false)
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "APPLICATION_ID", nullable = false)
	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TESTCASE", nullable = false)
	public String getTestcase() {
		return this.testcase;
	}

	public void setTestcase(String testcase) {
		this.testcase = testcase;
	}

	@Column(name = "WIDTH", nullable = false)
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Column(name = "HEIGHT", nullable = false)
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "CREATE_TIME")
	public Long getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Long getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "STATE", nullable = false)
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}