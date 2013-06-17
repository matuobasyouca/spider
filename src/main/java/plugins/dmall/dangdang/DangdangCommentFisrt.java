package plugins.dmall.dangdang;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.meiya.common.string.StringUtil;
import com.zhangwoo.spider.client.process.AnalyserTemplate;
import com.zhangwoo.spider.po.Conversation;
import com.zhangwoo.spider.po.UrlRequest;

public class DangdangCommentFisrt implements AnalyserTemplate {

	public boolean isMatchTemplate(UrlRequest urlReq, String html) {
		return urlReq.getUrl().toLowerCase().indexOf(
				"http://product.dangdang.com/product.aspx?product_id") >= 0;
	}

	public List<Conversation> findConversations(UrlRequest urlReq, String html,
			Document docHtml) {
		return null;
	}

	public List<UrlRequest> findLinks(UrlRequest urlReq, String html,
			Document docHtml) {
		List<UrlRequest> links = new ArrayList<UrlRequest>();
		links.add(new UrlRequest(
				"http://product.dangdang.com/comment/main.php?product_id="+StringUtil.match(urlReq.getUrl(), "product_id=(\\d+)")[1]+"&page=1&filtertype=1&type=part",
				StringUtil.match(html, "<title>(.*)</title>")[1],
				urlReq.getTask()));
		return links;
	}
}
