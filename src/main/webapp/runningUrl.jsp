<%@page import="com.zhangwoo.spider.server.*,java.util.*,com.zhangwoo.spider.po.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<title></title>
</head>
<body>
<table width="100%" border=1>
<%
List<UrlState> urls  = (List<UrlState>)TaskCenter.urlState;
for(UrlState url : urls) {%>
	<tr>
		<td width="50%" align="center"><%=url.getUrlReq().getUrl() %></td>
		<td align="center"><%=(url.getUrlReq().getTask().getTname()+"###"+url.getUrlReq().getTask().getTurl()) %></td>
	</tr>
<%} %>
</table>
</body>
</html>