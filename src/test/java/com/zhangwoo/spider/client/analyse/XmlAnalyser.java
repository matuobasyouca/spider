package com.zhangwoo.spider.client.analyse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.meiya.common.FileUtil;
import com.meiya.common.XmlUtil;
import com.meiya.common.simpledao.BaseDao;
import com.meiya.common.string.StringUtil;
import com.zhangwoo.spider.po.Conversation;

public class XmlAnalyser {

	public static void main2(String[] args) {
		String html = FileUtil.readFile("d:\\t", "utf-8");

		XPath xpath = XPathFactory.newInstance().newXPath();
		Document docHtml = null;
		try {
			docHtml = XmlUtil.formatToDoc(html);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			NodeList cons = (NodeList) xpath.evaluate(
					"//TABLE[@width='349' and @height='210']", docHtml,
					XPathConstants.NODESET);
			for (int i = 0; i < cons.getLength(); i++) {
				Conversation result = new Conversation();
				result.setContent(xpath.evaluate(
						"//TABLE[@width='349' and @height='210']",
								cons.item(i)));
				System.out.println(xpath.evaluate(
						"//TABLE[@width='349' and @height='210']",
								cons.item(i)));
//				result.setSelfLink(xpath.evaluate(
//						"DIV[@class='title_top']/A/@href", cons.item(i)));
//				result.setPublishTime(StringUtil.match(
//						xpath.evaluate(
//								"DIV[@class='title clearfix']/SPAN[@class='level']/text()",
//								cons.item(i)),
//						"\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")[0]);
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String wxsf=FileUtil.readFile("d:\\class.txt","utf-8");
		String[] wxs=wxsf.split("#");
		List<Object[]> parasValues = new ArrayList<Object[]>();
//		for (UrlState u : states) {
//			parasValues
//					.add(new Object[] { u.getRuninfo(), u.getBeginTime(),
//							u.getEndTime(), u.getPageLength(),
//							u.getUrlSize(), u.getConvSize(),
//							u.getUrlReq().getUrl() });
//		}
		int i=0;
		for (String string : wxs) {
			if(StringUtil.isEmpty(string)) continue;
			String[] cols = string.split("\n");
//			System.out.println]+" ").ols.length);
//			for (String string2 : cols) {
////				System.out.println(string2);
////				System.out.println("--------------");
//			}
			++i;
			parasValues
			.add(new Object[] { 
					(cols[0]+" ： ").split("：")[1],
					(cols[1]+" ： ").split("：")[1],
					(cols[2]+" ： ").split("：")[1],
					(cols[3]+" ： ").split("：")[1],
					(cols[4]+" ： ").split("：")[1],
					(cols[5]+" ： ").split("：")[1],
					(cols[6]+" ： ").split("：")[1]
			});
//			System.out.println((cols[0]+" ： ").split("：")[1]);
//			System.out.println((cols[1]+" ： ").split("：")[1]);
//			System.out.println((cols[2]+" ： ").split("：")[1]);
//			System.out.println((cols[3]+" ： ").split("：")[1]);
//			System.out.println((cols[4]+" ： ").split("：")[1]);
//			System.out.println((cols[5]+" ： ").split("：")[1]);
//			System.out.println((cols[6]+" ： ").split("：")[1]);
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		System.out.println(i);
		
//		try {
//			new BaseDao().save("insert weixin_qq (wxname,wx_sno,wx_no,weburl,qq,area,gettime) values (?,?,?,?,?,?,?)", parasValues);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
