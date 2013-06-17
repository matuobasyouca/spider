package com.zhangwoo.spider.server.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.meiya.common.simpledao.BaseDao;
import com.zhangwoo.spider.po.UrlState;

public class LogDao {
	private static Logger logger = Logger.getLogger(LogDao.class);
	BaseDao dao = new BaseDao();

	public void saveLogs(List<UrlState> states) {
		try {
			List<Object[]> parasValues = new ArrayList<Object[]>();
			for (UrlState u : states) {
				parasValues
						.add(new Object[] { u.getRuninfo(), u.getBeginTime(),
								u.getEndTime(), u.getPageLength(),
								u.getUrlSize(), u.getConvSize(),
								u.getUrlReq().getUrl() });
			}
			dao.save("insert log (runinfo,beginTime,endTime,pageLength,urlSize,convSize,url) values (?,?,?,?,?,?,?)",parasValues);
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	public void saveLogs(UrlState u) {
		try {
			List<Object[]> parasValues = new ArrayList<Object[]>();
			parasValues
					.add(new Object[] { u.getRuninfo(), u.getBeginTime(),
							u.getEndTime(), u.getPageLength(),
							u.getUrlSize(), u.getConvSize(),
							u.getUrlReq().getUrl() });
			dao.save("insert log (runinfo,beginTime,endTime,pageLength,urlSize,convSize,url) values (?,?,?,?,?,?,?)",parasValues);
		} catch (SQLException e) {
			logger.error(e);
		}
	}
}
