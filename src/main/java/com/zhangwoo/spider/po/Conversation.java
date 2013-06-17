package com.zhangwoo.spider.po;

import com.meiya.common.encrypt.MD5Builder;

public class Conversation extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//======================== 持久化属性 ==============================
	private String title;
	private String author;
	private String publishTime;
	private String updateTime;
	private String mainLink; // 指向主题的链接（多个回复的该属性相同）
	private String selfLink; // 自身链接
	private String content;
	private String mainLinkMd5;
	private String selfLinkMd5;
	private String isTopic;
	private String tid;
	//======================== 业务临时属性 ==============================
	private String stopByXpath; // 丢弃链接的XPATH
	private String stopByExp; // 丢弃链接的正则表达式
	private String runable = "0"; //该链接是否需要运行
	private String saveable = "1"; //该实体是否保存

	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	public String getSaveable() {
		return saveable;
	}
	public void setSaveable(String saveable) {
		this.saveable = saveable;
	}
	public String getStopByXpath() {
		return stopByXpath;
	}
	public void setStopByXpath(String stopByXpath) {
		this.stopByXpath = stopByXpath;
	}
	public String getStopByExp() {
		return stopByExp;
	}
	public void setStopByExp(String stopByExp) {
		this.stopByExp = stopByExp;
	}
	public String getRunable() {
		return runable;
	}
	public void setRunable(String runable) {
		this.runable = runable;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getMainLink() {
		return mainLink;
	}
	public void setMainLink(String mainLink) {
		this.mainLink = mainLink;
		this.mainLinkMd5 =MD5Builder.getMD5String(mainLink);
	}
	public String getSelfLink() {
		return selfLink;
	}
	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
		this.selfLinkMd5 =MD5Builder.getMD5String(selfLink);
	}
	public String getMainLinkMd5() {
		return mainLinkMd5;
	}
	public void setMainLinkMd5(String mainLinkMd5) {
		this.mainLinkMd5 = mainLinkMd5;
	}
	public String getSelfLinkMd5() {
		return selfLinkMd5;
	}
	public void setSelfLinkMd5(String selfLinkMd5) {
		this.selfLinkMd5 = selfLinkMd5;
	}
	public String getIsTopic() {
		return isTopic;
	}
	public void setIsTopic(String isTopic) {
		this.isTopic = isTopic;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}
