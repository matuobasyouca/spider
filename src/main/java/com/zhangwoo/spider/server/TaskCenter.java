package com.zhangwoo.spider.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import com.meiya.common.DateUtil;
import com.zhangwoo.spider.po.Task;
import com.zhangwoo.spider.po.UrlRequest;
import com.zhangwoo.spider.po.UrlState;

/**
 * 任务中心（任务状态+URL队列），使用MINA框架
 * 
 * @author cchen
 * 
 */
public class TaskCenter {

	private static Logger logger = Logger.getLogger(TaskCenter.class.getName());

	public static Map<Task, Integer> taskCount = new ConcurrentHashMap<Task, Integer>(); // 任务运行计数，在taskQueue队列进出时计数
	public static Queue<UrlRequest> taskQueue = new ConcurrentLinkedQueue<UrlRequest>(); // URL队列，包括了任务和后续URL

	public static List<UrlState> urlState = Collections
			.synchronizedList(new ArrayList<UrlState>()); // 当前运行的URL
	public static Map<Task, List<UrlState>> taskState = new ConcurrentHashMap<Task, List<UrlState>>(); // 当前运行的Task状态

	public static final int QUEUEPORT = 30010;
	public static final int STATEPORT = 30020;
	public static final int URLSAVEPORT = 30030;
	public static final int WEBPORT = 30090;

	public static void main(String[] args) {
		// 启动任务中心端
		startUrlStateCenter();
		startUrlGetterCenter();
		startUrlSaveCenter();
		startWebCenter();

		// 启动任务&状态管理线程[守护]
		TaskManagerHandler taskHandler = new TaskManagerHandler();
		taskHandler.setDaemon(true);
		taskHandler.start();
	}

	/**
	 * 加入URL的核心方法，所有的增加方法都应该从此入口。 在此方法中，会根据Task确认本次过程中是否有该URL，如果无，则加入URL队列
	 * 
	 * @param urlReq
	 */
	public static boolean addUrl(UrlRequest urlReq) {
		if (TaskCenter.taskQueue.contains(urlReq) // 待运行URL队列包含该URL
				|| TaskCenter.urlState.contains(urlReq) // 正在运行的URL队列中包含该URL
				|| urlReq.getTask() == null // 不归属任何任务
				|| TaskCenter.taskState.get(urlReq.getTask()) == null
				|| TaskCenter.taskState.get(urlReq.getTask()).contains(
						new UrlState(urlReq)) // 已运行的任务URL队列包含此URL
		) {
			return false; // 以上情况均放弃该URL
		}
		TaskCenter.taskQueue.add(urlReq);
		TaskCenter.taskCount.put(urlReq.getTask(),
				TaskCenter.taskCount.get(urlReq.getTask()) + 1);
		return true;
	}

	public static void addUrl(List<UrlRequest> urlReqs) {
//		int count = urlReqs.size();
		for (UrlRequest urlReq : urlReqs) {
//			if (!addUrl(urlReq))
//				--count;
			addUrl(urlReq);
		}
		
	}

	/**
	 * 请求单个URL
	 */
	public static UrlRequest getUrl() {
		// logger.debug("now need poll , size "+TaskCenter.taskQueue.size());
		UrlRequest urlReq = TaskCenter.taskQueue.poll();
		if (urlReq!=null) {
			UrlState state = new UrlState(urlReq);
			state.setBeginTime(DateUtil.formatDateTime());
			urlState.add(state); // 正在运行的任务
		}
		return urlReq;
	}

	public static void startWebCenter() {
		Server server = new Server(WEBPORT);

		WebAppContext webApp = new WebAppContext();
		webApp.setContextPath("/spider");
		if(new java.io.File("src/main/webapp").exists())
			webApp.setWar("src/main/webapp");
		else
			webApp.setWar("");
		server.setHandler(webApp);

		try {
			server.start();
		} catch (Exception e) {
			logger.error("Web 服务启动失败  ", e);
		}
	}

	private static void startUrlSaveCenter() {
		try {
			IoAcceptor statusAcceptor = new NioSocketAcceptor(10);
			statusAcceptor.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(
							new ObjectSerializationCodecFactory()));

			statusAcceptor.setHandler(new UrlSaveHandler()); // 处理线程
			statusAcceptor.getSessionConfig().setReadBufferSize(2048);
			statusAcceptor.getSessionConfig().setIdleTime(
					IdleStatus.WRITER_IDLE, 1);
			statusAcceptor.bind(new InetSocketAddress(URLSAVEPORT));

			logger.info("url 接收中心启动 ，占用端口 : " + URLSAVEPORT);
		} catch (Exception e) {
			logger.error("url 接收中心启动失败  ", e);
		}
	}

	private static void startUrlStateCenter() {
		try {
			IoAcceptor statusAcceptor = new NioSocketAcceptor();
			statusAcceptor.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(
							new ObjectSerializationCodecFactory()));

			statusAcceptor.setHandler(new TaskStateHandler()); // 处理线程

			statusAcceptor.getSessionConfig().setReadBufferSize(2048);
			statusAcceptor.getSessionConfig().setIdleTime(
					IdleStatus.WRITER_IDLE, 1);
			statusAcceptor.bind(new InetSocketAddress(STATEPORT));

			logger.info("url 状态中心启动 ，占用端口 : " + STATEPORT);
		} catch (Exception e) {
			logger.error("url 状态中心启动失败  ", e);
		}
	}

	private static void startUrlGetterCenter() {
		try {
			IoAcceptor queueAcceptor = new NioSocketAcceptor();
			queueAcceptor.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(
							new ObjectSerializationCodecFactory()));

			queueAcceptor.setHandler(new TaskQueueHandler()); // 处理线程

			queueAcceptor.getSessionConfig().setReadBufferSize(2048);
			queueAcceptor.getSessionConfig().setIdleTime(
					IdleStatus.WRITER_IDLE, 1);
			queueAcceptor.bind(new InetSocketAddress(QUEUEPORT));

			logger.info("url 分发中心启动 ，占用端口 : " + QUEUEPORT);
		} catch (Exception e) {
			logger.error("url 分发中心启动失败  ", e);
		}
	}
}
