package com.zhangwoo.spider.client.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.meiya.common.FileUtil;
import com.meiya.common.XmlUtil;
import com.meiya.common.string.StringUtil;
import com.zhangwoo.spider.po.Conversation;
import com.zhangwoo.spider.po.UrlRequest;
import com.zhangwoo.spider.po.UrlState;

/**
 * 本类主要职责为：
 * <ol>
 * <li>从服务端获取请求信息，即获得一个<code>UrlRequest</code></li>
 * <li>发送该请求（此处有可能需要选择线路），获得对应源码</li>
 * <li>读取解析模板配置，形成模板列表</li>
 * <li>执行解析过程，并返回解析产物（包括实体与链接两种）</li>
 * <li>日志记录</li>
 * <ol>
 * 
 * @author cchen
 * 
 */
public class SpiderThread extends Thread {
	XPath xpath = XPathFactory.newInstance().newXPath();
	static List<String> userAgents = new ArrayList<String>();
	private Logger logger = Logger.getLogger(SpiderThread.class.getName());

	static {
		userAgents = new ArrayList<String>();
		userAgents
				.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11");
		userAgents
				.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER");
		userAgents
				.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)");
		userAgents
				.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E) ");
		userAgents
				.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400) ");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E) ");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; 360SE)");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E) ");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
		userAgents
				.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
		userAgents
				.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E) ");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E) ");
		userAgents
				.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E) ");
		userAgents
				.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0");
		userAgents
				.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; SE 2.X MetaSr 1.0) ");
	}

	@Override
	public void run() {
		codeProcess();
	}

	public void codeProcess() {
		// 获得request
		UrlRequest urlReq = ClientMessageCenter.getInstance().urlGet();
		if (urlReq == null || StringUtil.isEmpty(urlReq.getUrl()))
			return;

		// *+* 此处首先获取模板，若无模板则无需浪费时间与流量，直接放弃该链接。
		List<AnalyserTemplate> templates = null;
		try {
			templates = this.findAnalyserTemplate(urlReq);
		} catch (Exception e) {
			logger.error(e);
		}
		if (templates == null || templates.size() == 0)
			return;

		logger.info("running url : " + urlReq.getUrl());

		// 发送request
		String html = null;
		for (int i = 0; i < 3; i++) { // 最多发送3次请求
			try {
				html = sendRequest(urlReq);
				if (!StringUtil.isEmpty(html))
					break;
			} catch (Exception e) {
				logger.error("send request url : " + urlReq.getUrl()
						+ " error! ", e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("thread sleep error! ", e);
			}
		}

		List<Conversation> convsResult = new ArrayList<Conversation>();
		List<UrlRequest> urlReqResult = new ArrayList<UrlRequest>();

		if (!StringUtil.isEmpty(html)) { // html 若无内容  则无需分析
			// 一个个分析模板过程，单个页面可能分析出多组数据
			for (AnalyserTemplate template : templates) {
				if (!template.isMatchTemplate(urlReq, html))
					continue;

				Document docHtml = null;
				try {
					docHtml = XmlUtil.formatToDoc(html);
				} catch (Exception e) {
					logger.error("html fromat to doc error! " + html, e);
				}

				List<Conversation> convsTemp = template.findConversations(
						urlReq, html, docHtml);
				if (convsTemp != null)
					for (Conversation convs : convsTemp) {
						if (convs.getSaveable().equals("1")) {
							convsResult.add(convs);
						}
						if (convs.getRunable().equals("1"))
							urlReqResult.add(new UrlRequest(formatUrl(
									convs.getSelfLink(), urlReq), urlReq
									.getTask()));
					}
				urlReqResult.addAll(template.findLinks(urlReq, html, docHtml));
			}

			if (urlReqResult != null && urlReqResult.size() > 0) {
				ClientMessageCenter.getInstance().urlSend(urlReqResult);
				logger.info("url : " + urlReq.getUrl() + " get next url : "
						+ urlReqResult.size());
			}
			if (convsResult != null && convsResult.size() > 0) {
				ClientMessageCenter.getInstance().resultSend(convsResult);
				logger.info("url : " + urlReq.getUrl() + " get convsResult : "
						+ convsResult.size());
			}
		}
		if ((urlReqResult == null || urlReqResult.size() == 0) && (convsResult == null || convsResult.size() == 0)) {
			logger.info("url : " + urlReq.getUrl() + " get no reuslt!  ");
		}
		ClientMessageCenter.getInstance().stateSend(
				new UrlState(html.length(), urlReqResult.size(), convsResult
						.size(), urlReq));
	}

	public static String formatUrl(String selfUrl, UrlRequest urlReq) {
		if (selfUrl == null)
			return null;

		if (selfUrl.startsWith("http://") || selfUrl.startsWith("https://"))
			return selfUrl;

		String headUrl = null;
		int tmpStop = -1;
		if (selfUrl.startsWith("/")) {
			tmpStop = urlReq.getUrl().indexOf("/", 10);
		} else if (selfUrl.startsWith("?")) {
			// 添加参数的方式，需要移除#以及？等其他符号。
			tmpStop = urlReq.getUrl().indexOf("?");
			if(tmpStop>urlReq.getUrl().indexOf("#")){
				urlReq.getUrl().indexOf("#");
			}
			if(tmpStop<=0){tmpStop=urlReq.getUrl().length();}
		} else {
			tmpStop = urlReq.getUrl().lastIndexOf("/") + 1;
		}
		if (tmpStop == -1)
			tmpStop = urlReq.getUrl().length();
		headUrl = urlReq.getUrl().substring(0, tmpStop);
		selfUrl = headUrl + selfUrl;
		return selfUrl;
	}

	public static String sendRequest(UrlRequest urlReq) throws Exception {
//		Thread.sleep(3000);
		
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpResponse response = null;
		String html = null;

		if (urlReq.getMethod().toLowerCase().endsWith("get")) {
			HttpGet httpGet = new HttpGet(urlReq.getUrl());
			try {
				if (urlReq.getHeader() != null) {
					for (String name : urlReq.getHeader().keySet()) {
						httpGet.addHeader(name, urlReq.getHeader().get(name));
					}
				}
				// 若没有指定User-Agent则使用随机User-Agent
				if (urlReq.getHeader() == null
						|| !urlReq.getHeader().containsKey("User-Agent")) {
					httpGet.addHeader("User-Agent", userAgents.get(new Random()
							.nextInt(userAgents.size())));
				}
				httpGet.addHeader("Connection", "close");

				response = httpclient.execute(httpGet);

				HttpEntity entity = response.getEntity();
				html = FileUtil.readStream(entity.getContent());

				EntityUtils.consume(entity);
			} catch (Exception e) {
				throw e;
			} finally {
				httpGet.releaseConnection();
			}
		} else {
			HttpPost httpPost = new HttpPost(urlReq.getUrl());
			try {
				for (String name : urlReq.getHeader().keySet()) {
					httpPost.addHeader(name, urlReq.getHeader().get(name));
				}

				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("UserName", "cchen"));
				nvps.add(new BasicNameValuePair("password", "123456"));
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));

				response = httpclient.execute(httpPost);

				HttpEntity entity = response.getEntity();
				html = FileUtil.readStream(entity.getContent());

				EntityUtils.consume(entity);
			} catch (Exception e) {
				throw e;
			} finally {
				httpPost.releaseConnection();
			}
		}
		return html;
	}

	private String getUrlDomain(UrlRequest urlReq) {
		return StringUtil.match(urlReq.getUrl(), "http://([^/]*)")[1];
	}

	/**
	 * 构建所有解析模板，来源有两种（Xml配置与硬编码）
	 * 
	 * @param urlRequest
	 * @return
	 * @throws XPathExpressionException
	 * @throws SAXException
	 * @throws IOException
	 */
	private List<AnalyserTemplate> findAnalyserTemplate(UrlRequest urlReq)
			throws XPathExpressionException, IOException, SAXException {
		List<AnalyserTemplate> templates = new ArrayList<AnalyserTemplate>();

		Document doc = XmlUtil.formatToDoc(FileUtil.readStream(
				this.getClass().getResourceAsStream(
						"/plugins/" + getUrlDomain(urlReq) + ".xml"), "utf-8"));

		NodeList nodes = (NodeList) xpath.evaluate("//TEMPLATE", doc,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			// 首先确定是硬编码（存在class属性）还是配置文件
			String classname = xpath.evaluate("@classname", nodes.item(i));

			try {
				if (!StringUtil.isEmpty(classname)) {
					templates.add((AnalyserTemplate) Class.forName(classname)
							.newInstance());
				} else {
					templates
							.add(new XmlAnalyserTemplate(nodes.item(i), urlReq));
				}
			} catch (Exception e) {
				logger.error("analyser template error!  \n node xml is : {}"
						+ XmlUtil.printNodeXml(nodes.item(i)), e);
			}
		}

		return templates;
	}

	public static void main(String[] args) {
		new SpiderThread().start();
//		try {
//			String t=new SpiderThread().sendRequest(new UrlRequest("http://www.5nd.com/5nd_1.htm",null));
//			System.out.println(t);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
