package com.zhangwoo.spider.po;

import com.meiya.common.encrypt.MD5Builder;

public class Task extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tid;
	private String tname;
	private String turl;
	private String updatetime;
	private String runable;
	private String nextstart;
	private String sleeptime;

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public String getTurl() {
		return turl;
	}

	public void setTurl(String turl) {
		this.tid=MD5Builder.getMD5String(turl);
		this.turl = turl;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getRunable() {
		return runable;
	}

	public void setRunable(String runable) {
		this.runable = runable;
	}

	public String getNextstart() {
		return nextstart;
	}

	public void setNextstart(String nextstart) {
		this.nextstart = nextstart;
	}

	public String getSleeptime() {
		return sleeptime;
	}

	public void setSleeptime(String sleeptime) {
		this.sleeptime = sleeptime;
	}

	@Override
	public boolean equals(Object obj) {
		return this.turl.equals(((Task)obj).turl);
	}

	@Override
	public int hashCode() {
		return this.turl.hashCode();
	}
	
}
