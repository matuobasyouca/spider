package com.zhangwoo.spider.server;

import org.apache.log4j.Logger;

import com.meiya.common.DateUtil;

/**
 * 状态管理守护线程，将内存中所有的状态定时结转。
 * 主要是URL
 * @author cchen
 *
 */
public class StateManagerHandler extends Thread {

	private static Logger logger = Logger.getLogger(StateManagerHandler.class.getName());
	
	@Override
	public void run() {
		while (true) {
//			logger.info("now running task no : "
//					+ TaskCenter.taskState.keySet().size());
			logger.info("need to run url : " + TaskCenter.taskQueue.size());
			logger.info("running url : " + TaskCenter.urlState.size());
			logger.info("=========== "+DateUtil.nowDateTime()+" =========== ");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
	
	

}
