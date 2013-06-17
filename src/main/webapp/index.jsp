<%@page import="com.zhangwoo.spider.server.TaskCenter"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<title>Spider Main</title>
</head>
<body>
<table width="100%" border=1>
	<tr>
		<td width="50%" align="center"><a href="managerTask.jsp">任务管理</a></td>
		<td align="center"></td>
	</tr>
	<tr>
		<td width="50%" align="center">当前运行任务数量:</td>
		<td align="center"><a href="runningTask.jsp"><%=TaskCenter.taskState.keySet().size()%></a></td>
	</tr>
	<tr>
		<td width="50%" align="center">待运行URL数量:</td>
		<td align="center"><a href="needRunningUrl.jsp"><%=TaskCenter.taskQueue.size()%></a></td>
	</tr>
	<tr>
		<td width="50%" align="center">当前运行URL:</td>
		<td align="center"><a href="runningUrl.jsp"><%=TaskCenter.urlState.size()%></a></td>
	</tr>
	<tr>
		<td width="50%" align="center">已经运行任务情况:</td>
		<td align="center"><a href="taskState.jsp"><%=TaskCenter.taskState.keySet().size()%></a></td>
	</tr>
</table>
</body>
</html>