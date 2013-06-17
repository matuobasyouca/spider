package com.zhangwoo.spider.client.process;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.zhangwoo.spider.po.UrlRequest;
import com.zhangwoo.spider.server.ResultCenter;
import com.zhangwoo.spider.server.TaskCenter;

public class ClientMessageCenter {
	private static Logger logger = Logger.getLogger(ClientMessageCenter.class);

	private IoSession urlSenderSession;
	private IoSession urlGetterSession;
	private IoSession resultSenderSession;
	private IoSession stateSenderSession;

	// 私有，静态的类自身实例
	private static ClientMessageCenter instance = new ClientMessageCenter();

	// 公开，静态的工厂方法
	public static ClientMessageCenter getInstance() {
		return instance;
	}

	private ClientMessageCenter() {
		reConnect();
	}

	public void reConnect() {
		urlSenderSession = this.createConnector(SpiderProcess.TASKHOSTNAME,
				TaskCenter.URLSAVEPORT);
		urlGetterSession = this.createConnector(SpiderProcess.TASKHOSTNAME,
				TaskCenter.QUEUEPORT, true);
		stateSenderSession = this.createConnector(SpiderProcess.TASKHOSTNAME,
				TaskCenter.STATEPORT);
		resultSenderSession = this.createConnector(SpiderProcess.RESULTHOSTNAME,
				ResultCenter.CONVERSATIONSAVEPORT);
	}

	private IoSession createConnector(String host, int port) {
		return createConnector(host, port, false);
	}

	private IoSession createConnector(String host, int port, boolean canRead) {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(50);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.setHandler(new TaskClientHandler());
		ConnectFuture future = connector.connect(new InetSocketAddress(host,
				port));
		future.awaitUninterruptibly();
		future.getSession().getConfig().setUseReadOperation(canRead);
		return future.getSession();
	}

	public void urlSend(Object message) {
		urlSenderSession.write(message);
	}

	public UrlRequest urlGet() {
		urlGetterSession.write("");
		try {
			return (UrlRequest) urlGetterSession.read().awaitUninterruptibly()
					.getMessage();
		} catch (Exception e) {
			if(!(e.getMessage().indexOf("java.lang.String")>=0&&e instanceof java.lang.ClassCastException))
				logger.error("", e);
			return null;
		}
	}

	public void resultSend(Object message) {
		resultSenderSession.write(message);
	}

	public void stateSend(Object message) {
		stateSenderSession.write(message);
	}

}
