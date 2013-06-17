<%@page import="com.zhangwoo.spider.server.TaskCenter"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<title></title>
</head>
<body>
<table width="100%" border=1>
	<tr>
		<td width="50%" align="center">当前运行任务数量:</td>
		<td align="center"><a href="runningTask.jsp"><%=TaskCenter.taskState.keySet().size()%></a></td>
	</tr>
</table>
</body>
</html>