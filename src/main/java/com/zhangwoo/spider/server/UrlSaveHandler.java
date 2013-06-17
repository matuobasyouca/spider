package com.zhangwoo.spider.server;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;

import com.meiya.common.DateUtil;
import com.zhangwoo.spider.po.UrlRequest;

public class UrlSaveHandler  extends IoHandlerAdapter {
	private Logger logger = Logger.getLogger(UrlSaveHandler.class
			.getName());

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		logger.error("UrlSaveHandler error ",cause);
		if(cause instanceof java.io.IOException)
			session.close(true);
	}
	
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

	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived(IoSession session, Object message){
		TaskCenter.addUrl((List<UrlRequest>)message);
		session.write("end "+DateUtil.now());
//		session.close(true);
	}

}
