package molab.main.java.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CsRunner entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CS_RUNNER", catalog = "MOLAB")
public class CsRunner implements java.io.Serializable {

	// Fields

	private int id;
	private int dispatcherId;
	private int deviceId;
	private String installResult;
	private String loadResult;
	private Blob logcat;
	private int state;

	// Constructors

	/** default constructor */
	public CsRunner() {
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

	@Column(name = "DISPATCHER_ID", nullable = false)
	public Integer getDispatcherId() {
		return this.dispatcherId;
	}

	public void setDispatcherId(Integer dispatcherId) {
		this.dispatcherId = dispatcherId;
	}

	@Column(name = "DEVICE_ID", nullable = false)
	public Integer getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "INSTALL_RESULT")
	public String getInstallResult() {
		return installResult;
	}

	public void setInstallResult(String installResult) {
		this.installResult = installResult;
	}

	@Column(name = "LOAD_RESULT")
	public String getLoadResult() {
		return loadResult;
	}

	public void setLoadResult(String loadResult) {
		this.loadResult = loadResult;
	}

	@Column(name = "LOGCAT")
	public Blob getLogcat() {
		return this.logcat;
	}

	public void setLogcat(Blob logcat) {
		this.logcat = logcat;
	}

	@Column(name = "STATE", nullable = false)
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}