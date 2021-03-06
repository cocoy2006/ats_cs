package molab.main.java.dao;

import java.util.List;

import molab.main.java.entity.CsDevice;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * A data access object (DAO) providing persistence and search support for
 * CsDevice entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see molab.main.java.entity.CsDevice
 * @author MyEclipse Persistence Tools
 */
@Repository
public class CsDeviceDao extends BaseDao<CsDevice> {
	private static final Logger log = LoggerFactory
			.getLogger(CsDeviceDao.class);
	// property constants
	public static final String SERVER = "server";
	public static final String PORT = "port";
	public static final String SN = "sn";
	public static final String MODEL_ID = "modelId";
	public static final String OS = "os";
	public static final String IMEI = "imei";
	public static final String USES = "uses";
	public static final String STATE = "state";

	public CsDevice findById(java.lang.Integer id) {
		log.debug("getting CsDevice instance with id: " + id);
		try {
			CsDevice instance = (CsDevice) getHibernateTemplate().get(
					"molab.main.java.entity.CsDevice", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<CsDevice> findByExample(CsDevice instance) {
		log.debug("finding CsDevice instance by example");
		try {
			List<CsDevice> results = (List<CsDevice>) getHibernateTemplate()
					.findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<CsDevice> findByProperty(String propertyName, Object value) {
		log.debug("finding CsDevice instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from CsDevice as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<CsDevice> findByServer(Object server) {
		return findByProperty(SERVER, server);
	}

	public List<CsDevice> findByPort(Object port) {
		return findByProperty(PORT, port);
	}

	public List<CsDevice> findBySn(Object sn) {
		return findByProperty(SN, sn);
	}

	public List<CsDevice> findByModelId(Object modelId) {
		return findByProperty(MODEL_ID, modelId);
	}

	public List<CsDevice> findByOs(Object os) {
		return findByProperty(OS, os);
	}

	public List<CsDevice> findByImei(Object imei) {
		return findByProperty(IMEI, imei);
	}

	public List<CsDevice> findByUses(Object uses) {
		return findByProperty(USES, uses);
	}

	public List<CsDevice> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List<CsDevice> findAll() {
		log.debug("finding all CsDevice instances");
		try {
			String queryString = "from CsDevice";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CsDevice merge(CsDevice detachedInstance) {
		log.debug("merging CsDevice instance");
		try {
			CsDevice result = (CsDevice) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CsDevice instance) {
		log.debug("attaching dirty CsDevice instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CsDevice instance) {
		log.debug("attaching clean CsDevice instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public CsDevice findByRunnerId(int runnerId) {
		String hql = "from CsDevice where id=(select deviceId from CsRunner where id=?)";
		List<CsDevice> list = getHibernateTemplate().find(hql, runnerId);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
}