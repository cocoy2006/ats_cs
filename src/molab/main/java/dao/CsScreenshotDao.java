package molab.main.java.dao;

import java.util.List;

import molab.main.java.entity.CsScreenshot;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * A data access object (DAO) providing persistence and search support for
 * CsScreenshot entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see molab.main.java.entity.CsScreenshot
 * @author MyEclipse Persistence Tools
 */
@Repository
public class CsScreenshotDao extends BaseDao<CsScreenshot> {
	private static final Logger log = LoggerFactory
			.getLogger(CsScreenshotDao.class);
	// property constants
	public static final String RUNNER_ID = "runnerId";
	public static final String IMAGE = "image";
	public static final String ACTION = "action";
	public static final String PARAMS = "params";
	public static final String NOTE = "note";
	public static final String OPR_TIME = "oprTime";

	public CsScreenshot findById(java.lang.Integer id) {
		log.debug("getting CsScreenshot instance with id: " + id);
		try {
			CsScreenshot instance = (CsScreenshot) getHibernateTemplate().get(
					"molab.main.java.entity.CsScreenshot", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<CsScreenshot> findByExample(CsScreenshot instance) {
		log.debug("finding CsScreenshot instance by example");
		try {
			List<CsScreenshot> results = (List<CsScreenshot>) getHibernateTemplate()
					.findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<CsScreenshot> findByProperty(String propertyName, Object value) {
		log.debug("finding CsScreenshot instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CsScreenshot as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<CsScreenshot> findByRunnerId(Object runnerId) {
		return findByProperty(RUNNER_ID, runnerId);
	}

	public List<CsScreenshot> findByImage(Object image) {
		return findByProperty(IMAGE, image);
	}
	
	public List<CsScreenshot> findByAction(Object action) {
		return findByProperty(ACTION, action);
	}

	public List<CsScreenshot> findByParams(Object params) {
		return findByProperty(PARAMS, params);
	}
	
	public List<CsScreenshot> findByNote(Object note) {
		return findByProperty(NOTE, note);
	}

	public List<CsScreenshot> findByOprTime(Object oprTime) {
		return findByProperty(OPR_TIME, oprTime);
	}

	public List<CsScreenshot> findAll() {
		log.debug("finding all CsScreenshot instances");
		try {
			String queryString = "from CsScreenshot";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CsScreenshot merge(CsScreenshot detachedInstance) {
		log.debug("merging CsScreenshot instance");
		try {
			CsScreenshot result = (CsScreenshot) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CsScreenshot instance) {
		log.debug("attaching dirty CsScreenshot instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CsScreenshot instance) {
		log.debug("attaching clean CsScreenshot instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}