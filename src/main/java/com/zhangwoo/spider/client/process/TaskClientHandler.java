package com.zhangwoo.spider.client.process;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;

public class TaskClientHandler extends IoHandlerAdapter {
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
}
