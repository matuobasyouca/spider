package com.zhangwoo.spider.client.process;

import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.meiya.common.DateUtil;
import com.meiya.common.XmlUtil;
import com.meiya.common.string.StringUtil;
import com.zhangwoo.spider.po.Conversation;
import com.zhangwoo.spider.po.UrlRequest;

@SuppressWarnings("restriction")
public class XmlAnalyserTemplate implements AnalyserTemplate {
	XPath xpath = XPathFactory.newInstance().newXPath();
	ScriptEngineManager sem = new ScriptEngineManager();
	ScriptEngine engine = sem.getEngineByName("javascript");

	Node templateNode = null;
	UrlRequest urlReq = null;

	Logger logger = Logger.getLogger(this.getClass().getName());

	public XmlAnalyserTemplate(Node templateNode,UrlRequest urlReq) {
		this.templateNode = templateNode;
		this.urlReq = urlReq;
	}

	public boolean isMatchTemplate(UrlRequest urlReq, String html) {
		try {
			String include = xpath.evaluate("RULE/INDEXOF/text()",
					this.templateNode);
			String regexp = xpath.evaluate("RULE/REGEXP/text()",
					this.templateNode);
			String code = xpath.evaluate("RULE/CODE/text()", this.templateNode);

			// 三种方式任意一种不匹配则认定不适用，直接否决当前模版
			if (!StringUtil.isEmpty(include)) {
				if (urlReq.getUrl().indexOf(include) == -1)
					return false;
			}
			if (!StringUtil.isEmpty(regexp)) {
				if (StringUtil.matchWeak(urlReq.getUrl(), regexp) == null)
					return false;
			}
			if (!StringUtil.isEmpty(code)) {
				engine.eval(code);
				Invocable jsInvoke = (Invocable) engine;
				if (jsInvoke
						.invokeFunction("ismatch",
								new Object[] { urlReq.getUrl(), html })
						.toString().trim().equals("0"))
					return false; // js 代码 返回1 是,0否
			}
		} catch (Exception e) {
			logger.error(
					"isMatchTemplate domain " + urlReq.getUrl()
							+ " error ,  template xml "
							+ XmlUtil.printNodeXml(templateNode), e);
		}
		return true;
	}

	public List<Conversation> findConversations(UrlRequest urlReq, String html,
			Document docHtml) {
		try {
			Node analyserNode = (Node) xpath.evaluate("ANALYSER",
					this.templateNode, XPathConstants.NODE);
			if (analyserNode != null) {
				String anSave = xpath.evaluate("@save", analyserNode);
				String anXpath = xpath.evaluate("@xpath", analyserNode);
				String anRegexp = xpath.evaluate("@regexp", analyserNode);

				Conversation convsXpath = getXpaths(analyserNode);
				Conversation convsRegExp = getRegExps(analyserNode);
				List<Conversation> convsResults = new ArrayList<Conversation>();

				// reg(可能json) 与 xpath(html标签) 走完全不同的路线
				if (!StringUtil.isEmpty(anXpath)
						&& StringUtil.isEmpty(anRegexp)) { // 纯粹XPATH，给出结果一定是NodeList
					NodeList cons = (NodeList) xpath.evaluate(anXpath, docHtml,
							XPathConstants.NODESET);
					Conversation conTemp = new Conversation();
					for (int consi = 0; consi < cons.getLength(); consi++) {
						conTemp = analyserConversation(convsXpath, convsRegExp,
								cons.item(consi), null);
						conTemp.setSaveable(anSave);
						if(StringUtil.isEmpty(conTemp.getSelfLink())){
							conTemp.setSelfLink(urlReq.getUrl());
						}
						conTemp.setTid(urlReq.getTask().getTid());
						convsResults.add(conTemp);
					}
				} else if (!StringUtil.isEmpty(anRegexp)) {
					// List<String[]> cons=StringUtil.matchAll(anRegexp, html);
				}

				return convsResults;
			}
		} catch (Exception e) {
			logger.error(
					"isMatchTemplate domain " + urlReq.getUrl()
							+ " error ,  template xml "
							+ XmlUtil.printNodeXml(templateNode), e);
		}
		return null;
	}

	public List<UrlRequest> findLinks(UrlRequest urlReq, String html,
			Document docHtml) {
		List<UrlRequest> links = new ArrayList<UrlRequest>();
		try {
			UrlRequest urlTemp = null;

			NodeList linksNode = (NodeList) xpath.evaluate("LINKS/LINK",
					this.templateNode, XPathConstants.NODESET);
			if (linksNode != null && linksNode.getLength() > 0) {
				for (int linki = 0; linki < linksNode.getLength(); linki++) {
					String linkRegExp = xpath.evaluate("@regexp",
							linksNode.item(linki));
					String linkStopRegExp = xpath.evaluate("@stopregexp",
							linksNode.item(linki));
					String linkStopXpath = xpath.evaluate("@stopxpath",
							linksNode.item(linki));

					List<String[]> linkUrls = StringUtil.matchAll(html, linkRegExp);
					List<String[]> linkStops = null;
					if(!StringUtil.isEmpty(linkStopRegExp))
						linkStops = StringUtil.matchAll(html, linkStopRegExp);
					else if(!StringUtil.isEmpty(linkStopXpath)){
						linkStops=new ArrayList<String[]>();
						linkStops.add(new String[]{"",xpath.evaluate(linkStopXpath, docHtml).trim()});
					}

					for (int i = 0; i < linkUrls.size(); i++) {
						String linkUrl = linkUrls.get(i)[1];
						String linkStop = null;
						if(linkStops!=null&&linkStops.size()>0){
							if(i>linkStops.size()-1)
								linkStop=linkStops.get(linkStops.size()-1)[1];
							else
								linkStop=linkStops.get(i)[1];
						}
						if (!StringUtil.isEmpty(linkStop)
								&& urlReq.getTask() != null
								&& !StringUtil.isEmpty(urlReq.getTask()
										.getUpdatetime())) {
							if (DateUtil.diffDate(DateUtil.SECOND, DateUtil
									.stringToDate(urlReq.getTask()
											.getUpdatetime()), DateUtil
									.stringToDate(linkStop)) < 0) {
								continue;
							}
						}
						if (StringUtil.isEmpty(linkUrl))
							continue;
						urlTemp = (UrlRequest) urlReq.clone();
						urlTemp.setHeader(null);
						urlTemp.setUrl(SpiderThread.formatUrl(linkUrl, this.urlReq));
						links.add(urlTemp);
					}
				}
			}
		} catch (Exception e) {
			logger.error(
					"isMatchTemplate domain " + urlReq.getUrl()
							+ " error ,  template xml "
							+ XmlUtil.printNodeXml(templateNode), e);
		}
		return links;
	}
	

	private Conversation getRegExps(Node analyserNode)
			throws XPathExpressionException {
		Conversation convsRegExp = new Conversation();
		convsRegExp.setAuthor(xpath.evaluate("AUTHOR/@regexp", analyserNode));
		convsRegExp.setContent(xpath.evaluate("CONTENT/@regexp", analyserNode));
		convsRegExp.setMainLink(xpath
				.evaluate("MAINLINK/@regexp", analyserNode));
		convsRegExp.setPublishTime(xpath.evaluate("PUBLISHTIME/@regexp",
				analyserNode));
		convsRegExp.setSelfLink(xpath
				.evaluate("SELFLINK/@regexp", analyserNode));
		convsRegExp.setTitle(xpath.evaluate("TITLE/@regexp", analyserNode));
		convsRegExp.setUpdateTime(xpath.evaluate("UPDATETIME/@regexp",
				analyserNode));
		convsRegExp.setIsTopic(xpath.evaluate("ISTOPIC/@regexp", analyserNode));
		convsRegExp.setStopByExp(xpath.evaluate("SELFLINK/@stopByExp",
				analyserNode));
		return convsRegExp;
	}

	private Conversation getXpaths(Node analyserNode)
			throws XPathExpressionException {
		Conversation convsXpath = new Conversation();
		convsXpath.setAuthor(xpath.evaluate("AUTHOR/@xpath", analyserNode).trim());
		convsXpath.setContent(xpath.evaluate("CONTENT/@xpath", analyserNode).trim());
		convsXpath.setMainLink(xpath.evaluate("MAINLINK/@xpath", analyserNode).trim());
		convsXpath.setPublishTime(xpath.evaluate("PUBLISHTIME/@xpath",
				analyserNode).trim());
		convsXpath.setSelfLink(xpath.evaluate("SELFLINK/@xpath", analyserNode).trim());
		convsXpath.setTitle(xpath.evaluate("TITLE/@xpath", analyserNode).trim());
		convsXpath.setUpdateTime(xpath.evaluate("UPDATETIME/@xpath",
				analyserNode).trim());
		convsXpath.setIsTopic(xpath.evaluate("ISTOPIC/@xpath", analyserNode).trim());
		convsXpath.setStopByXpath(xpath.evaluate("SELFLINK/@stopByXpath",
				analyserNode).trim());
		convsXpath
				.setRunable(xpath.evaluate("SELFLINK/@runable", analyserNode).trim());
		return convsXpath;
	}

	/**
	 * 根据模版的配置，
	 * 
	 * @param analyserNode
	 * @return
	 * @throws XPathExpressionException
	 */
	private Conversation analyserConversation(Conversation convsXpath,
			Conversation convsRegExp, Node node, String str)
			throws XPathExpressionException {
		Conversation resutl = new Conversation();

		if (node == null) {

		} else if (StringUtil.isEmpty(str)) {
			if (!StringUtil.isEmpty(convsXpath.getAuthor()))
				resutl.setAuthor(xpath.evaluate(convsXpath.getAuthor(), node).trim());
			if (!StringUtil.isEmpty(convsXpath.getContent()))
				resutl.setContent(xpath.evaluate(convsXpath.getContent(), node).trim());
			if (!StringUtil.isEmpty(convsXpath.getIsTopic()))
				resutl.setIsTopic(xpath.evaluate(convsXpath.getIsTopic(), node).trim());
			if (!StringUtil.isEmpty(convsXpath.getMainLink()))
				resutl.setMainLink(xpath.evaluate(convsXpath.getMainLink(),
						node).trim());
			if (!StringUtil.isEmpty(convsXpath.getPublishTime()))
				resutl.setPublishTime(xpath.evaluate(
						convsXpath.getPublishTime(), node).trim());
			if (!StringUtil.isEmpty(convsXpath.getRunable()))
				resutl.setRunable(xpath.evaluate(convsXpath.getRunable(), node).trim());
			if (!StringUtil.isEmpty(convsXpath.getSelfLink()))
				resutl.setSelfLink(SpiderThread.formatUrl(xpath.evaluate(convsXpath.getSelfLink(),
						node).trim(),this.urlReq));
			if (!StringUtil.isEmpty(convsXpath.getTitle()))
				resutl.setTitle(xpath.evaluate(convsXpath.getTitle(), node).trim());
			if (!StringUtil.isEmpty(convsXpath.getUpdateTime()))
				resutl.setUpdateTime(xpath.evaluate(convsXpath.getUpdateTime(),
						node).trim());
			if (!StringUtil.isEmpty(convsXpath.getRunable()))
				resutl.setRunable(xpath.evaluate(convsXpath.getRunable(), node).trim());
		}

		return resutl;
	}
}
