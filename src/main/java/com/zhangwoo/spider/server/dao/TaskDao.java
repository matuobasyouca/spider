package com.zhangwoo.spider.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.meiya.common.DateUtil;
import com.meiya.common.simpledao.BaseDao;
import com.zhangwoo.spider.po.Task;

public class TaskDao {
	private static Logger logger = Logger.getLogger(TaskDao.class);
	BaseDao dao = new BaseDao();

	public List<Task> getRunnableTasks() {
		List<Task> result = new ArrayList<Task>();
		List<Object[]> tmpResult = new ArrayList<Object[]>();

		try {
			tmpResult = dao
					.query("select tid,tname,turl,updatetime,sleeptime from task where runable=1 and nextstart<=now()");
			for (Object[] tmp : tmpResult) {
				Task t = new Task();
				t.setTid(tmp[0].toString());
				t.setTname(tmp[1].toString());
				t.setTurl(tmp[2].toString());
				t.setUpdatetime(tmp[3].toString());
				t.setSleeptime(tmp[4].toString());
				result.add(t);
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return result;
	}

	public void updateTaskNextrun(List<Task> ts) {
		try {
			List<Object[]> parasValues = new ArrayList<Object[]>();
			for (Task t : ts) {
				parasValues.add(new Object[] { t.getTid() });
			}
			dao.save(
					"update task set nextstart=DATE_ADD(now(),INTERVAL sleeptime MINUTE) where tid=?",
					parasValues);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void updateTaskNextrun(Task t) {
		try {
			dao.save("update task set nextstart=DATE_ADD(now(),INTERVAL sleeptime MINUTE) where tid='"
					+ t.getTid() + "'");
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void updateTaskUpdatetime(Task t) {
		try {
			dao.save("update task set updatetime=DATE_ADD(now(),INTERVAL -1 MINUTE) where tid='"
					+ t.getTid() + "'");
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public void saveTask(Task t){
		try {
			dao.save("INSERT INTO `task` (tid,tname,turl,updatetime,runable,nextstart,sleeptime) VALUES " +
					"('"+t.getTid()+"','"+t.getTname()+"'," +
							"'"+t.getTurl()+"','"+DateUtil.formatDate()+"','1','"+DateUtil.formatDate()+"','600');");
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
