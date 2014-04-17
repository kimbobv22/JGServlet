<%@page import="com.jg.util.JGServletUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello world!</title>
</head>
<body>
	<h1>Welcome to JGServlet</h1>
	<p>
		<a href="<%=JGServletUtils.getServerURL(request) %>sub/another?srvID=another">click here to forward another page</a>
	</p>
</body>
</html>