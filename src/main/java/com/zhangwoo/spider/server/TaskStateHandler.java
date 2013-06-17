package com.zhangwoo.spider.server;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;

import com.meiya.common.DateUtil;
import com.zhangwoo.spider.po.UrlState;
import com.zhangwoo.spider.server.dao.LogDao;

public class TaskStateHandler extends IoHandlerAdapter {
	private Logger logger = Logger.getLogger(TaskStateHandler.class
			.getName());

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		IoSessionConfig cfg = session.getConfig();
		  if (cfg instanceof SocketSessionConfig) {
		   ((SocketSessionConfig) cfg).setKeepAlive(true);
		   ((SocketSessionConfig) cfg).setSoLinger(0);
		   ((SocketSessionConfig) cfg).setTcpNoDelay(true);
		   ((SocketSessionConfig) cfg).setWriteTimeout(1000 * 5);
		  }
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("TaskStateHandler error ",cause);
		if(cause instanceof java.io.IOException)
			session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		UrlState state=(UrlState) message;
		int index=TaskCenter.urlState.indexOf(state);
		if(index!=-1){
			UrlState oldState=TaskCenter.urlState.remove(index);
			state.setBeginTime(oldState.getBeginTime());
			state.setEndTime(DateUtil.formatDateTime());
			state.setRuninfo(((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress());
			TaskCenter.taskState.get(state.getUrlReq().getTask()).add(state);
			TaskCenter.taskCount.put(state.getUrlReq().getTask(),
					TaskCenter.taskCount.get(state.getUrlReq().getTask())-1);
			
			new LogDao().saveLogs(state);
		}
	}
}
