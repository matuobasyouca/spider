package com.zhangwoo.spider.server.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.meiya.common.simpledao.BaseDao;
import com.meiya.common.simpledao.Config;
import com.meiya.common.string.StringUtil;
import com.zhangwoo.spider.po.Conversation;

public class ConversationDao {
	Logger logger = Logger.getLogger(ConversationDao.class.getName());;
	BaseDao dao = new BaseDao();

	public void save(List<Conversation> result) {
		List<Object[]> parasValues = new ArrayList<Object[]>();
		for (Conversation c : result) {
			parasValues.add(new Object[] { c.getTitle(), c.getPublishTime(),
					c.getUpdateTime(), c.getContent(), c.getSelfLink(),
					c.getAuthor(), c.getIsTopic(), c.getTid() });
		}

		String tableName = "conversation";
		if (Config.getProps("saveOneTable").equals("0")) {
			try {
				tableName = StringUtil.match(result.get(0).getSelfLink(),
						"http://([^/]*)")[1];
			} catch (Exception e) {
				logger.error("analyse table name error", e);
			}
		}

		try {
			dao.save(
					"insert `"
							+ tableName
							+ "` (title,publishTime,updateTime,content,selflink,author,isTopic,tid) values (?,?,?,?,?,?,?,?)",
					parasValues);
		} catch (Exception e) {
			if (e.getMessage().indexOf("Duplicate entry") == -1)
				logger.error("", e);

			if (e.getMessage().matches("Table.*?doesn't exist")) {
				try {
					dao.save("CREATE TABLE `" + tableName
							+ "` SELECT * FROM conversation WHERE 1=2");
					dao.save(
							"insert `"
									+ tableName
									+ "` (title,publishTime,updateTime,content,selflink,author,isTopic,tid) values (?,?,?,?,?,?,?,?)",
							parasValues);
				} catch (SQLException e1) {
					logger.error("error happen create table `" + tableName
							+ "` and save data!", e1);
				}
			}
		}
	}

}