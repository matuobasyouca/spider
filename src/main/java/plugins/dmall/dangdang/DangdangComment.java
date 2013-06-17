package plugins.dmall.dangdang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.meiya.common.DateUtil;
import com.meiya.common.FileUtil;
import com.meiya.common.XmlUtil;
import com.meiya.common.string.StringUtil;
import com.zhangwoo.spider.client.process.AnalyserTemplate;
import com.zhangwoo.spider.po.Conversation;
import com.zhangwoo.spider.po.UrlRequest;

public class DangdangComment implements AnalyserTemplate {
	XPath xpath = XPathFactory.newInstance().newXPath();
	Logger logger = Logger.getLogger(this.getClass().getName());

	public boolean isMatchTemplate(UrlRequest urlReq, String html) {
		return urlReq.getUrl().indexOf(
				"http://product.dangdang.com/comment/main.php?product_id=") >= 0;
	}

	public List<Conversation> findConversations(UrlRequest urlReq, String html,
			Document docHtml) {
		List<Conversation> convsResults = new ArrayList<Conversation>();
		try {
			NodeList cons = (NodeList) xpath.evaluate("//DIV[@class='text clearfix']", docHtml,XPathConstants.NODESET);
			for (int i = 0; i < cons.getLength(); i++) {
				Conversation result = new Conversation();
				result.setTitle(urlReq.getTitle());
				result.setContent(xpath.evaluate("DIV[@class='title_top']/text()", cons.item(i))+xpath.evaluate("DIV[@class='s_cont']/text()", cons.item(i)));
				result.setSelfLink(xpath.evaluate("DIV[@class='title_top']/A/@href", cons.item(i)));
				result.setPublishTime(StringUtil.match(xpath.evaluate("DIV[@class='title clearfix']/SPAN[@class='level']/text()", cons.item(i)),"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")[0]);
				convsResults.add(result);
			}
		} catch (XPathExpressionException e) {
			logger.error("findConversations error",e);
		}
		return convsResults;
	}

	public List<UrlRequest> findLinks(UrlRequest urlReq, String html,
			Document docHtml) {
		List<UrlRequest> links = new ArrayList<UrlRequest>();
		List<String[]> dts = StringUtil.matchAll(html, "发表于(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
		if (dts==null||dts.size()==0||DateUtil.diffDate(DateUtil.SECOND,
				DateUtil.stringToDate(urlReq.getTask()
						.getUpdatetime()), DateUtil
						.stringToDate(dts.get(dts.size()-1)[1])) < 0) {
			return links;
		}
		
		int nowPage=Integer.valueOf(StringUtil.match(urlReq.getUrl(),"page=(\\d+)")[1]);
		++nowPage;
		links.add(new UrlRequest(
				"http://product.dangdang.com/comment/main.php?product_id"+urlReq.getUrl().substring(urlReq.getUrl().indexOf("="))+"&page="+nowPage+"&filtertype=1&type=part",
				urlReq.getTitle(),
				urlReq.getTask()));
		return links;
	}
	public static void main(String[] args) {
		try {
			String h=FileUtil.readFile("d://t", "utf-8");
			Document docHtml=XmlUtil.formatToDoc(h);
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			System.out.println(xpath.evaluate("(//DIV[@class='cat_llx'])[last()]/DIV[@class='fl']", docHtml).trim());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
