package com.zhangwoo.spider.po;

public class TaskState extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UrlRequest urlReq;
	private String runinfo; // 执行当前URL的主机信息，一般是内网+外网IP
	private String beginTime;
	private String endTime;
	private int pageLength; // 内容长度

	public UrlRequest getUrlReq() {
		return urlReq;
	}

	public void setUrlReq(UrlRequest urlReq) {
		this.urlReq = urlReq;
	}

	public String getRuninfo() {
		return runinfo;
	}

	public void setRuninfo(String runinfo) {
		this.runinfo = runinfo;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getPageLength() {
		return pageLength;
	}

	public void setPageLength(int pageLength) {
		this.pageLength = pageLength;
	}

}
