package molab.main.java.dao;

import java.util.List;

import molab.main.java.entity.Testcase;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * A data access object (DAO) providing persistence and search support for
 * Testcase entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see molab.main.java.entity.Testcase
 * @author MyEclipse Persistence Tools
 */
@Repository
public class TestcaseDao extends BaseDao<Testcase> {
	private static final Logger log = LoggerFactory
			.getLogger(TestcaseDao.class);
	// property constants
	public static final String PROJECT_ID = "projectId";
	public static final String NAME = "name";
	public static final String TESTCASE = "testcase";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String CREATE_TIME = "createTime";
	public static final String MODIFY_TIME = "modifyTime";
	public static final String STATE = "state";

	public Testcase findById(java.lang.Integer id) {
		log.debug("getting Testcase instance with id: " + id);
		try {
			Testcase instance = (Testcase) getHibernateTemplate().get(
					"molab.main.java.entity.Testcase", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Testcase> findByExample(Testcase instance) {
		log.debug("finding Testcase instance by example");
		try {
			List<Testcase> results = (List<Testcase>) getHibernateTemplate()
					.findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Testcase> findByProperty(String propertyName, Object value) {
		log.debug("finding Testcase instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Testcase as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Testcase> findByProjectId(Object projectId) {
		return findByProperty(PROJECT_ID, projectId);
	}

	public List<Testcase> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<Testcase> findByTestcase(Object testcase) {
		return findByProperty(TESTCASE, testcase);
	}

	public List<Testcase> findByCreateTime(Object createTime) {
		return findByProperty(CREATE_TIME, createTime);
	}

	public List<Testcase> findByModifyTime(Object modifyTime) {
		return findByProperty(MODIFY_TIME, modifyTime);
	}
	
	public List<Testcase> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List<Testcase> findAll() {
		log.debug("finding all Testcase instances");
		try {
			String queryString = "from Testcase";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Testcase merge(Testcase detachedInstance) {
		log.debug("merging Testcase instance");
		try {
			Testcase result = (Testcase) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Testcase instance) {
		log.debug("attaching dirty Testcase instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Testcase instance) {
		log.debug("attaching clean Testcase instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public Testcase findByRunnerId(int runnerId) {
		String hql = "FROM Testcase WHERE id=(SELECT testcaseId FROM CsDispatcher WHERE id=(SELECT dispatcherId FROM CsRunner WHERE id=?))";
		List<Testcase> list = getHibernateTemplate().find(hql, runnerId);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
}