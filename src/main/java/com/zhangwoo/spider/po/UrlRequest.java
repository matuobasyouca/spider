package com.zhangwoo.spider.po;

import java.util.HashMap;
import java.util.Map;

public class UrlRequest extends Entity {

	public UrlRequest() {
		super();
	}

	public UrlRequest(String url, Task task) {
		this.url = url;
		this.task = task;
	}

	public UrlRequest(String url, String title, Task task) {
		this.url = url;
		this.title = title;
		this.task = task;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * URL地址
	 */
	private String url;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 请求发送方式 Get/Post
	 */
	private String method = "get";

	/**
	 * 需要注入的头请求
	 */
	private Map<String, String> header = new HashMap<String, String>();

	/**
	 * 发送POST请求附带的参数
	 */
	private Map<String, String> postParam = new HashMap<String, String>();

	/**
	 * 当前运行URL归属任务
	 */
	private Task task;

	private int delays = 0; // 当前URL需要的延迟数。单位为秒

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDelays() {
		return delays;
	}

	public void setDelays(int delays) {
		this.delays = delays;
	}

	public Map<String, String> getPostParam() {
		return postParam;
	}

	public void setPostParam(Map<String, String> postParam) {
		this.postParam = postParam;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url.trim();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method.trim().toLowerCase();
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof UrlRequest)
			return this.url.equals(((UrlRequest) obj).url);
		else
			return this.url.equals(((UrlState) obj).getUrlReq().url);
	}

	@Override
	public int hashCode() {
		return this.url.hashCode();
	}

}
