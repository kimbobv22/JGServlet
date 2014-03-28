<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDataset.2.0.0.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGDataset/js/JGDatasetUI.2.0.0.min.js"></script>
<script type="text/javascript" src="http://kimbobv22.github.io/JGServlet/js/JGService.2.0.0.min.js"></script>
<script type="text/javascript">
JGService.requestURL("main","main");

$(function(){
	$("#testView").JGDatasetUI();
	
	JGService.ajax("main",{
		data : {
			srvID : "ajaxTest"
		},success: function(result_){
			JGDS("dataset", "testData", result_.message);
		}
	});
});
</script>
<title>JGServlet</title>
</head>
<body>
	<h1>Welcome to JGServlet</h1>
	<p>
		Example for linkage JGDataset<br>
	</p>
	<p>
		<table border="1">
			<thead>
				<tr>
					<th>COL1</th>
					<th>COL2</th>
					<th>COL3</th>
				</tr>
			</thead>
			<tbody jg-dataset="testData" id="testView">
				<tr>
					<td jg-column="col1"></td>
					<td jg-column="col2"></td>
					<td jg-column="col3"></td>
				</tr>
			</tbody>
		</table>
	</p>
</body>
</html>