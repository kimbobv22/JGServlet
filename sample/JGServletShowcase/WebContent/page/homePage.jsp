<%@page import="com.jg.util.JGServletUtils"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	String serverURL_ = JGServletUtils.getServerURL(request);
%>
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
<script type="text/javascript" src="http://code.jquery.com/jquery-2.1.1.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDataset.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDatasetUI.min.js"></script>
<script type="text/javascript" src="<%=serverURL_%>/page/JGService.3.0.0.js"></script>
<script type="text/javascript">
console.log(JGService.requestURL("main","<%=serverURL_%>"));
console.log(JGService.requestURL("main",{
	parameters : {param1 : "param123"},
	mappingData : {map1 : "map123"},
	//appendPath : "test/teste/test"
}));
function doPost(){
	JGService.postService("main",{
		appendPath : "anyword/anyword1/test/doPost",
		parameters : {number : $("#number").val()}
	});
}
function doAjax(){
	JGService.ajax("main",{
		appendPath : "anyword/anyword1/test/doAjax"
	},{
		success : function(data_){
			console.log(JSON.stringify(data_));
		}
	});
}
function doMultipart(){
	JGService.sendMultipart("main",{
		appendPath : "anyword/anyword1/test/doMultipart",
		parameters : {
			param1 : "testData"
		}
	},{
		success : function(data_){
			console.log(JSON.stringify(data_));
		}
	});
}
function getDataset(){
	JGService.ajax("main",{
		appendPath : "anyword/anyword1/test/getDataset"
	},{
		success : function(data_){
			var dataset_ = JGDS("dataset","sampleData");
			dataset_.applyJSON(data_.message);
		}
	});
}
function storeDataset(){
	var dataset_ = JGDS("dataset","sampleData");
	
	JGService.ajax("main",{
		appendPath : "anyword/anyword1/test/storeDataset"
	},{
		data : {
			dataset : dataset_.toJSONString()
		}
	});
}
$(document).ready(function(){
	$("#datasetSampleView").JGDatasetUI();
	getDataset();
});
</script>
<title>JGServlet</title>
</head>
<body>
	<h1>Hello World :)</h1>
	<p>
		<%=request.getAttribute("testAttribute") %><br/>
		VDData&nbsp;&nbsp;: <%=request.getAttribute("VDData") %><br/>
		
		<input type="text" value="0" id="number"><a href="javascript:doPost();">post service</a><br/>
		<a href="javascript:doAjax();">ajax request</a><br/>
		<a href="javascript:doMultipart();">send multipart</a><br/>
	</p>
	<br/>
	<h2>JGDataset Transaction Sample</h2>
	<table border="1" style="width: 100%;">
		<thead>
			<tr>
				<th>col1</th>
				<th>col2</th>
			</tr>
		</thead>
		<tbody jg-dataset="sampleData" id="datasetSampleView">
			<tr>
				<td><input type="text" jg-column="col1"></td>
				<td><input type="text" jg-column="col2"></td>
			</tr>
		</tbody>
	</table>
	<a href="javascript:getDataset();">get dataset</a> / <a href="javascript:storeDataset();">send dataset</a>
	
</body>
</html>