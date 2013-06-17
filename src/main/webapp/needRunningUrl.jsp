<%@page import="com.zhangwoo.spider.server.*,java.util.*,com.zhangwoo.spider.po.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<title></title>
</head>
<body>
<table width="100%" border=1>
<%
Queue<UrlRequest> urls  = (Queue<UrlRequest>)TaskCenter.taskQueue;
for(UrlRequest url : urls) {%>
	<tr>
		<td width="50%" align="center"><%=url.getUrl() %></td>
		<td align="center"><%=(url.getTask().getTname()+"###"+url.getTask().getTurl()) %></td>
	</tr>
<%} %>
</table>
</body>
</html>