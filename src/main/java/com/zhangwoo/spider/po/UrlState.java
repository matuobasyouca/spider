package com.zhangwoo.spider.po;

public class UrlState extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String runinfo; // 执行当前URL的主机信息，一般是内网+外网IP
	private String beginTime;
	private String endTime;
	private int pageLength; // 内容长度
	private int urlSize; // url数量
	private int convSize; // con数量
	private UrlRequest urlReq; // url请求

	public UrlState(int pageLength, int urlSize, int convSize, UrlRequest urlReq) {
		super();
		this.pageLength = pageLength;
		this.urlSize = urlSize;
		this.convSize = convSize;
		this.urlReq = urlReq;
	}

	public int getUrlSize() {
		return urlSize;
	}

	public void setUrlSize(int urlSize) {
		this.urlSize = urlSize;
	}

	public int getConvSize() {
		return convSize;
	}

	public void setConvSize(int convSize) {
		this.convSize = convSize;
	}

	public UrlState(UrlRequest urlReq) {
		this.urlReq = urlReq;
	}

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

	@Override
	public boolean equals(Object obj) {
		return this.urlReq.equals(((UrlState) obj).getUrlReq());
	}

	@Override
	public int hashCode() {
		return this.urlReq.getUrl().hashCode();
	}

}
