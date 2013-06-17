package com.zhangwoo.spider.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.meiya.common.DateUtil;
import com.zhangwoo.spider.po.Task;
import com.zhangwoo.spider.po.UrlRequest;
import com.zhangwoo.spider.po.UrlState;
import com.zhangwoo.spider.server.dao.TaskDao;

/**
 * 任务管理线程
 * 
 * @author cchen
 * 
 */
public class TaskManagerHandler extends Thread {

	private static Logger logger = Logger.getLogger(TaskManagerHandler.class
			.getName());
	private final int EXPIRYTIME = 120; //失效时间，单位 秒

	private List<Task> tasks = Collections
			.synchronizedList(new ArrayList<Task>());

	@Override
	public void run() {
		addTasks();
		for (;;) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("", e);
			}
			int second = Calendar.getInstance().get(Calendar.SECOND);

			if(second%EXPIRYTIME==0){
				checkExpiryUrl();
				stopTasks();
			}
			
			if(second%5==0) // 每5秒添加一次任务
				addTasks();
		}
	}
	
	/**
	 * 检查运行URL中是否有过期，过期需要重新运行。
	 */
	private void checkExpiryUrl(){
		List<UrlState> expiryUrls = new ArrayList<UrlState>();
		for(UrlState url : TaskCenter.urlState){
			if(DateUtil.diffDate(DateUtil.SECOND, DateUtil.parseDateTime(url.getBeginTime()), DateUtil.nowDateTime())>this.EXPIRYTIME){
				expiryUrls.add(url);
				TaskCenter.taskCount.put(url.getUrlReq().getTask(),
						TaskCenter.taskCount.get(url.getUrlReq().getTask()) + 1);
			}
		}
		if(expiryUrls.size()>0){
			TaskCenter.urlState.removeAll(expiryUrls);
			for(UrlState url : expiryUrls){
				TaskCenter.addUrl(url.getUrlReq());
			}
		}
	}

	/**
	 * 移除已经完成的任务
	 */
	private void stopTasks() {
		for(Task t : TaskCenter.taskCount.keySet()){
			if(TaskCenter.taskCount.get(t)<=0){
				this.tasks.add(t);
			}
		}
		if(tasks.size()>0){
			for (Task t : tasks) {
				// save logs
//				new LogDao().saveLogs(TaskCenter.taskState.get(t));
				
				TaskCenter.taskCount.remove(t);
				TaskCenter.taskState.remove(t);
				logger.info("remove task : "+t.getTname()+" , "+t.getTurl());
			}
		}
		tasks.clear();
	}

	/**
	 * 添加任务
	 */
	private void addTasks() {
		List<UrlRequest> urlReqs = new ArrayList<UrlRequest>();
		List<Task> ts = new TaskDao().getRunnableTasks();
		for (Task task : ts) {
			if (!TaskCenter.taskState.keySet().contains(task)) {
				TaskCenter.taskState.put(task, new ArrayList<UrlState>());
				TaskCenter.taskCount.put(task, 0);
				new TaskDao().updateTaskUpdatetime(task);
				logger.info("run task : " + task.getTname() + " url : "
						+ task.getTurl());
				UrlRequest urlReq = new UrlRequest(task.getTurl(), task);
				urlReqs.add(urlReq);
			}
		}
		if (urlReqs.size() > 0) {
			new TaskDao().updateTaskNextrun(ts); // 更新任务启动时间
			TaskCenter.addUrl(urlReqs);
		}
	}
}
