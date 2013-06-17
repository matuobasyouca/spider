package plugins.music.fivend;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.meiya.common.string.StringUtil;
import com.zhangwoo.spider.client.process.AnalyserTemplate;
import com.zhangwoo.spider.po.Conversation;
import com.zhangwoo.spider.po.UrlRequest;

public class FivendMusicDownload implements AnalyserTemplate {

	public boolean isMatchTemplate(UrlRequest urlReq, String html) {
		if(StringUtil.match(urlReq.getUrl(), "www\\.5nd\\.com/mp3/\\d+\\.htm")!=null)
			return true;
		return false;
	}

	public List<Conversation> findConversations(UrlRequest urlReq, String html,
			Document docHtml) {
		List<Conversation> convsResults = new ArrayList<Conversation>();
		
		String[] tmps=StringUtil.match(html, "<a class=\"downlink\" href=\"([^\"]+)\"></a>");
		if(tmps!=null){
			Conversation c=new Conversation();
			c.setSelfLink(StringUtil.match(html, "<a class=\"downlink\" href=\"([^\"]+)\"></a>")[1]);
			tmps=StringUtil.match(html, "<a href=\"/all/\\d+.htm\">(.*?\\-.*?)</a>");
			if(tmps!=null){
				c.setAuthor(tmps[1].split("-")[0]);
				c.setTitle(tmps[1].split("-")[1]);
				convsResults.add(c);
			}
		}
		
		
		return convsResults;
	}

	public List<UrlRequest> findLinks(UrlRequest urlReq, String html,
			Document docHtml) {
		return new ArrayList<UrlRequest>();
	}
}
