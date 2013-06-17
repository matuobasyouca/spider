<%@page import="com.zhangwoo.spider.server.*,java.util.*,com.zhangwoo.spider.po.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<title></title>
</head>
<body>
<table width="100%" border=1>
<%
Map<Task, List<UrlState>> urls  = (Map<Task, List<UrlState>>)TaskCenter.taskState;
for(Task url : urls.keySet()) {%>
	<tr>
		<td width="50%" align="center"><%=url.getTname()+"###"+url.getTurl() %></td>
		<td align="center"><table width="100%" border=1>
		<tr>
				<td>IP</td>
				<td>URL</td>
				<td>Btime</td>
				<td>Etime</td>
				<td>PageSize</td>
				<td>ConvSize</td>
				<td>UrlSize</td>
			</tr>
		<% for(UrlState state : urls.get(url)){%>
			<tr>
				<td><%=state.getRuninfo() %></td>
				<td><%=state.getUrlReq().getUrl() %></td>
				<td><%=state.getBeginTime() %></td>
				<td><%=state.getEndTime() %></td>
				<td><%=state.getPageLength() %></td>
				<td><%=state.getConvSize() %></td>
				<td><%=state.getUrlSize() %></td>
			</tr>
			<%
		}
		%></table></td>
	</tr>
<%} %>
</table>
</body>
</html>