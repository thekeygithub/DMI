<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8" />
	<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
	<link type="text/css" rel="stylesheet" href="plugins/zTree/v3/zTreeStyle.css"/>
	<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.core.min.js"></script>
	<script type="text/javascript" src="plugins/zTree/v3/jquery.ztree.excheck.min.js"></script>
<body>
<div style="width: 100%">

<!-- 	<div style="height: 100%;width:50%; left: 0px" > -->
<table style="width:100%;" border="0">
	<tr>
		<td style="width:50%;" valign="top" bgcolor="#F9F9F9">
			<iframe name="malLoad" id="malLoad" frameborder="0"   scrolling="auto"src="<%=basePath%>api/mapload.do"  style="margin:0 auto;width:100%;height:100%;"></iframe>
		</td>
		<td style="width:50%;" valign="top" >
			<iframe name="apiLoad" id="apiLoad" frameborder="0"  scrolling="auto" src="<%=basePath%>api/apiload.do"  style="margin:0 auto;width:100%;height:100%;"></iframe>
		</td>
	</tr>
</table>
</div>
</body>
		
<script type="text/javascript">
		$(top.hangge());

		$(document).ready(function(){
			var h = $(window).height()-10;
			$("table td").height(h);
		});
	
</script>
</html>

