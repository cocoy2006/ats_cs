package molab.main.java.dao;

import java.util.List;

import molab.main.java.entity.CsDispatcher;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * A data access object (DAO) providing persistence and search support for
 * CsDispatcher entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see molab.main.java.entity.CsDispatcher
 * @author MyEclipse Persistence Tools
 */
@Repository
public class CsDispatcherDao extends BaseDao<CsDispatcher> {
	private static final Logger log = LoggerFactory
			.getLogger(CsDispatcherDao.class);
	// property constants
	public static final String USER_ID = "userId";
	public static final String TESTCASE_ID = "testcaseId";
	public static final String OPR_TIME = "oprTime";
	public static final String STATE = "state";

	public CsDispatcher findById(java.lang.Integer id) {
		log.debug("getting CsDispatcher instance with id: " + id);
		try {
			CsDispatcher instance = (CsDispatcher) getHibernateTemplate().get(
					"molab.main.java.entity.CsDispatcher", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<CsDispatcher> findByExample(CsDispatcher instance) {
		log.debug("finding CsDispatcher instance by example");
		try {
			List<CsDispatcher> results = (List<CsDispatcher>) getHibernateTemplate()
					.findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding CsDispatcher instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CsDispatcher as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<CsDispatcher> findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List<CsDispatcher> findByTestcaseId(Object testcaseId) {
		return findByProperty(TESTCASE_ID, testcaseId);
	}

	public List<CsDispatcher> findByOprTime(Object oprTime) {
		return findByProperty(OPR_TIME, oprTime);
	}

	public List<CsDispatcher> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List findAll() {
		log.debug("finding all CsDispatcher instances");
		try {
			String queryString = "from CsDispatcher";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CsDispatcher merge(CsDispatcher detachedInstance) {
		log.debug("merging CsDispatcher instance");
		try {
			CsDispatcher result = (CsDispatcher) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CsDispatcher instance) {
		log.debug("attaching dirty CsDispatcher instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CsDispatcher instance) {
		log.debug("attaching clean CsDispatcher instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}