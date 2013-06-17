package com.zhangwoo.spider.server.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.meiya.common.simpledao.BaseDao;
import com.zhangwoo.spider.po.Conversation;

public class ConversationDao {
	Logger logger = Logger.getLogger(ConversationDao.class.getName());;
	BaseDao dao = new BaseDao();

	public void save(List<Conversation> result) {
		List<Object[]> parasValues = new ArrayList<Object[]>();
		for (Conversation c : result) {
			parasValues.add(new Object[] { c.getTitle(), c.getPublishTime(),c.getUpdateTime(),
					c.getContent(), c.getSelfLink(), c.getAuthor(),c.getIsTopic(), c.getTid()});
		}
		try {
			dao.save(
					"insert conversation (title,publishTime,updateTime,content,selflink,author,isTopic,tid) values (?,?,?,?,?,?,?,?)",
					parasValues);
		} catch (SQLException e) {
			if(e.getMessage().indexOf("Duplicate entry")==-1)
				logger.error("",e);
		}
	}

}