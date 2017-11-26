package molab.main.java.dao;

import java.util.List;

import molab.main.java.entity.Script;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * A data access object (DAO) providing persistence and search support for
 * Script entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see molab.main.java.entity.Script
 * @author MyEclipse Persistence Tools
 */
@Repository
public class ScriptDao extends BaseDao<Script> {
	private static final Logger log = LoggerFactory.getLogger(ScriptDao.class);
	// property constants
	public static final String TESTCASE_ID = "testcaseId";
	public static final String STEP = "step";
	public static final String ACTION = "action";
	public static final String COMMAND = "command";
	public static final String MID = "mid";
	public static final String MCLASS = "mclass";
	public static final String MTEXT = "mtext";
	public static final String MLEFT = "mleft";
	public static final String MTOP = "mtop";
	public static final String MWIDTH = "mwidth";
	public static final String MHEIGHT = "mheight";
	public static final String NOTE = "note";
	public static final String CREATE_TIME = "createTime";
	public static final String DIFF_TIME = "diffTime";

	public Script findById(java.lang.Integer id) {
		log.debug("getting Script instance with id: " + id);
		try {
			Script instance = (Script) getHibernateTemplate().get(
					"molab.main.java.entity.Script", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Script> findByExample(Script instance) {
		log.debug("finding Script instance by example");
		try {
			List<Script> results = (List<Script>) getHibernateTemplate()
					.findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Script> findByProperty(String propertyName, Object value) {
		log.debug("finding Script instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Script as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Script> findByTestcaseId(Object testcaseId) {
		return findByProperty(TESTCASE_ID, testcaseId);
	}

	public List<Script> findByStep(Object step) {
		return findByProperty(STEP, step);
	}
	
	public List<Script> findByAction(Object action) {
		return findByProperty(ACTION, action);
	}

	public List<Script> findByCommand(Object command) {
		return findByProperty(COMMAND, command);
	}

	public List<Script> findByMid(Object mid) {
		return findByProperty(MID, mid);
	}
	
	public List<Script> findByMclass(Object mclass) {
		return findByProperty(MCLASS, mclass);
	}

	public List<Script> findByMtext(Object mtext) {
		return findByProperty(MTEXT, mtext);
	}

	public List<Script> findByMleft(Object mleft) {
		return findByProperty(MLEFT, mleft);
	}

	public List<Script> findByMtop(Object mtop) {
		return findByProperty(MTOP, mtop);
	}

	public List<Script> findByMwidth(Object mwidth) {
		return findByProperty(MWIDTH, mwidth);
	}

	public List<Script> findByMheight(Object mheight) {
		return findByProperty(MHEIGHT, mheight);
	}

	public List<Script> findByNote(Object note) {
		return findByProperty(NOTE, note);
	}

	public List<Script> findByCreateTime(Object createTime) {
		return findByProperty(CREATE_TIME, createTime);
	}

	public List<Script> findByDiffTime(Object diffTime) {
		return findByProperty(DIFF_TIME, diffTime);
	}

	public List<Script> findAll() {
		log.debug("finding all Script instances");
		try {
			String queryString = "from Script";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List<Script> findAll(int testcaseId) {
		log.debug("finding all Script instances");
		try {
			String queryString = "from Script where testcaseId=? order by step desc limit 1";
			return getHibernateTemplate().find(queryString, testcaseId);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Script merge(Script detachedInstance) {
		log.debug("merging Script instance");
		try {
			Script result = (Script) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Script instance) {
		log.debug("attaching dirty Script instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Script instance) {
		log.debug("attaching clean Script instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}