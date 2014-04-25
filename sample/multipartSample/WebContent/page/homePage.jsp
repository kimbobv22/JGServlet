<%@page import="com.jg.main.JGMainConfig"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.query-result{
	border: 1px solid gray;
	min-height: 50px;
}
</style>
<script src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDataset.2.0.0.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDatasetUI.2.0.0.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGServlet/js/JGService.2.0.0.min.js"></script>
<script type="text/javascript">
JGService.requestURL("main","main");
function doUpload(){
	JGService.sendMultipart("main",{
		srvID : "mulipartUpload"
		,testParameter : "test parameter"
	},{
		mutiple : true
		,success : function(result_){
			console.log(result_);
		}
	});
}
</script>
<title>JGServlet</title>
</head>
<body>
	<h1>JGServlet Multipart</h1>
	<p>
		upload root path : <%=JGMainConfig.sharedConfig().getFileRootPath() %><br>
		upload reject pattern : <%=JGMainConfig.sharedConfig().getFileRejectRegexp() %><br>
		upload image compression : <%=JGMainConfig.sharedConfig().isCompressionImage() %><br>
		upload image compression regexp: <%=JGMainConfig.sharedConfig().getFileImageCompressionRegexp() %><br>
	</p>
	<p>
		<a href="javascript:doUpload();">upload file</a>
	</p>
</body>
</html>