<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDataset.2.0.1.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGServlet/js/JGService.2.1.0.min.js"></script>
<script type="text/javascript">
JGService.requestURL("main","main");
function doAjax(){
	JGService.ajax("main",{
		data : {
			srvID : "ajaxTest"
		},success: function(result_){
			$("#labelResult").text(result_.message);
		}
	});
}
</script>
<title>JGServlet</title>
</head>
<body>
	<h1>Welcome to JGServlet</h1>
	<p>
		Example for Ajax<br>
		<a href="javascript:doAjax();">ajax test</a>
	</p>
	<p>
		result : <span id="labelResult"></span>
	</p>
</body>
</html>