package com.zhangwoo.spider.client.process;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

/**
 * 爬虫启动的单个进程。通过本进程来启动线程。本进程目标是7*24运行。 最大线程池设置为20个线程。保持线程池中最多不超过100页面，每隔5秒检查一遍。
 * 
 * @author cchen
 * 
 */
public class SpiderProcess {
	private static Logger logger = Logger.getLogger(SpiderProcess.class);
	public static String TASKHOSTNAME = "localhost";
	public static String RESULTHOSTNAME = "localhost";

	private static final long SLEEPTIME = 2000L; 
	private static final int MIN_POOLSIZE = 20; // 最少20个线程 
//	private static final int MAX_POOLSIZE = 80; // 最多80个线程 
	
	public static void main(String[] args) {
		if(args!=null&&args.length>0){
			if(args.length==1){
				TASKHOSTNAME=args[0];
				RESULTHOSTNAME=args[0];
			}else if(args.length==2){
				TASKHOSTNAME=args[0];
				RESULTHOSTNAME=args[1];
			}
		}
		
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(MIN_POOLSIZE);
		
		while (true) {
			int nowSize = (50 - executor.getQueue().size());
			
			for (int i = 0; i < nowSize; i++) {
				Runnable worker = new SpiderThread();
				executor.execute(worker);
			}
			
			try {
				Thread.sleep(SLEEPTIME);
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
	}

}
