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
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDataset.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDatasetUI.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGServlet/js/JGService.min.js"></script>
<script type="text/javascript">
JGService.requestURL("main","main");
$(function(){
	var dataset_ = JGDS("dataset","testData");
	dataset_.addRow();
	dataset_.setColumnValues({
		col1 : "test"
		,col2 : "test1"
		,col3 : "test2"
		,col4 : "test3"
	},0,true);
	
	$("#testView").JGDatasetUI();
});

function getQueryByParameters(){
	var dataset_ = JGDS("dataset","testData");
	var col1_ = dataset_.getColumnValue("col1",0);
	var col2_ = dataset_.getColumnValue("col2",0);
	var col3_ = dataset_.getColumnValue("col3",0);
	var col4_ = dataset_.getColumnValue("col4",0);
	
	JGService.ajax("main",{
		data : {
			srvID : "getQueryByParameters"
			,col1 : col1_
			,col2 : col2_
			,col3 : col3_
			,col4 : col4_
		},success : function(result_){
			if(result_.result === 0){
				$("#queryResult").text(result_.message);
			}
		}
	});
}
function getQueryByDataset(){
	var dataset_ = JGDS("dataset","testData");
	JGService.ajax("main",{
		data : {
			srvID : "getQueryByDataset"
			,condData : dataset_.toJSONString(true)
		},success : function(result_){
			if(result_.result === 0){
				$("#queryResult").text(result_.message);
			}
		}
	});
}

</script>
<title>JGServlet</title>
</head>
<body>
	<h1>Test Linkage for JGDBConnection</h1>
	<p>
		<table border="1">
			<thead>
				<tr>
					<th>COL1</th>
					<th>COL2</th>
					<th>COL3</th>
					<th>COL4</th>
				</tr>
			</thead>
			<tbody jg-dataset="testData" id="testView">
				<tr>
					<td><input type="text" jg-column="col1"></td>
					<td><input type="text" jg-column="col2"></td>
					<td><input type="text" jg-column="col3"></td>
					<td><input type="text" jg-column="col4"></td>
				</tr>
			</tbody>
		</table>
		<a href="javascript:getQueryByParameters()">get query by parameters</a><br>
		<a href="javascript:getQueryByDataset()">get query by dataset</a>
	</p>
	<p>
		<span>result</span><br>
		<pre id="queryResult" class="query-result"></pre>
	</p>
	<p>
		<a href="https://github.com/kimbobv22/JGDBConnection" target="_blank">more information for JGDBConnection</a>
	</p>
</body>
</html>