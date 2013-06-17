<%@page import="com.meiya.common.simpledao.BaseDao"%>
<%@page import="com.zhangwoo.spider.server.TaskCenter"%>
<%@page import="java.util.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<title></title>
</head>
<body>
<form action=""></form>
<table width="100%" border=1>
<%
String taskid=request.getParameter("taskid");
if(taskid!=null&&taskid.length()>0){
	BaseDao dao=new BaseDao();
	List<Object[]> res = dao.query("select Id,tid,tname,turl,updatetime,runable,nextstart,sleeptime from task where tid = '"+taskid+"'");
	if(res != null)
	for(Object[] r : res){
		out.print("<tr>");
		for(Object o : r)
			out.print("<td>"+o+"</td>");
		out.print("<td>修改</td>");
		out.print("</tr>");
	}

}
%>
</table>
</body>
</html>