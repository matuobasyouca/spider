package com.zhangwoo.spider.client.process;

import java.util.List;

import org.w3c.dom.Document;

import com.zhangwoo.spider.po.Conversation;
import com.zhangwoo.spider.po.UrlRequest;

public interface AnalyserTemplate {

	/**
	 * 第一步，判断是否为可用模板
	 * 
	 * @param url
	 * @param html
	 * @return
	 */
	public boolean isMatchTemplate(UrlRequest urlReq, String html);

	/**
	 * 解析该页面中可用实体
	 * 
	 * @param url
	 * @param html
	 * @param docHtml
	 * @return
	 */
	public List<Conversation> findConversations(UrlRequest urlReq, String html,
			Document docHtml);

	/**
	 * 获取页面中可用链接
	 * 
	 * @param url
	 * @param html
	 * @param docHtml
	 * @return
	 */
	public List<UrlRequest> findLinks(UrlRequest urlReq, String html,
			Document docHtml);

}
