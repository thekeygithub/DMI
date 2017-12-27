<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>区域信息列表页面</title>
	<jsp:include page="/bussiness/sxydidc/common/head.jsp" flush="true"/>  
    <script type="text/javascript">
		$(function($){
			window['g']=$("#maingrid").ligerGrid(queryAreaData(''));  			  
		});
		
			
    	function f_validate(){
    		var selected = g.getSelected();
    		if (!selected) {  top.$.ligerDialog.warn('请选择行'); return; }
			return g.getSelectedRow();
		}		    
    </script>
</head>
<body>  
    <div id="maingrid" style="margin: 5px;"></div>
</body>
</html>
