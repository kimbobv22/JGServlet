<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	String welcomeMessage_ = (String)request.getAttribute("message");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello world!</title>
</head>
<body>
	<h1>This is another page</h1>
	<p>
		<%=welcomeMessage_ %>
	</p>
</body>
</html>