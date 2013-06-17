package com.zhangwoo.spider.server;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.zhangwoo.spider.po.Conversation;
import com.zhangwoo.spider.server.dao.ConversationDao;

public class ConversationSaveHandler extends IoHandlerAdapter {
	private static Logger logger = Logger.getLogger(ConversationSaveHandler.class.getName());

	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("ConversationSaveHandler error ", cause);
	}

	@SuppressWarnings("unchecked")
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		new ConversationDao().save((List<Conversation>) message);
	}

	public static void main(String[] args) {
//		try {
//			BaseDao dao = new BaseDao();
//			List<Object[]> re = dao.query("select hkeys from kvs");
//			System.out.println(re.get(0)[0]);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
