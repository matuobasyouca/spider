<%@page import="com.meiya.common.simpledao.BaseDao,com.zhangwoo.spider.server.*,java.util.*,com.zhangwoo.spider.po.*"%>
<%@page import="com.zhangwoo.spider.server.TaskCenter"%>
<%@page import="java.util.*"%>
<%@ page contentType="text/html;charset=utf-8"%>
<html>
<head>
<title></title>
</head>
<body>
<%
	String tname = request.getParameter("tname");
	String turl = request.getParameter("turl");

	if (turl != null && turl.length() > 0 && tname != null
			&& tname.length() > 0){
		Task task=new Task();
		task.setTname(tname);
		task.setTurl(turl);
		new com.zhangwoo.spider.server.dao.TaskDao().saveTask(task);
		response.sendRedirect("managerTask.jsp");
	}
%>
<form action="managerTask.jsp"><font color="red">新增任务&nbsp;&nbsp;&nbsp;&nbsp;</font>
名称:<input name="tname"> url:<input name="turl"> <input
	type="submit"></form>
<table width="100%" border=1>
	<%
		BaseDao dao = new BaseDao();
		List<Object[]> res = dao
				.query("select Id,tid,tname,turl,updatetime,runable,nextstart,sleeptime from task");
		for (Object[] r : res) {
			out.print("<tr>");
			for (Object o : r)
				out.print("<td>" + o + "</td>");
			out.print("<td>修改</td>");
			out.print("</tr>");
		}
	%>
</table>
</body>
</html>