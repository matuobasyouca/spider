package com.zhangwoo.spider.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;

import com.zhangwoo.spider.po.UrlRequest;

public class TaskQueueHandler extends IoHandlerAdapter {
	private Logger logger = Logger.getLogger(TaskQueueHandler.class
			.getName());

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		IoSessionConfig cfg = session.getConfig();
		  if (cfg instanceof SocketSessionConfig) {
		   ((SocketSessionConfig) cfg).setKeepAlive(false);
		   ((SocketSessionConfig) cfg).setSoLinger(0);
		   ((SocketSessionConfig) cfg).setTcpNoDelay(true);
		   ((SocketSessionConfig) cfg).setWriteTimeout(1000 * 5);
		  }
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("TaskQueueHandler error ",cause);
		if(cause instanceof java.io.IOException)
			session.close(true);
	}

	/**
	 * 此处接收客户端请求URL信息，返回单个URL
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		UrlRequest url = TaskCenter.getUrl();
		session.write(url==null?"":url);
	}
}
