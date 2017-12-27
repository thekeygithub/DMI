<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath %>"/>
    <title>角色列表页面</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" />
    <script type="text/javascript" src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script type="text/javascript" src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script> 
    <script type="text/javascript" src="<%=basePath  %>system/role/role.js" ></script> 
    <script type="text/javascript" src="<%=basePath %>js/btnQuery.js"></script>
</head>
<body style="padding: 0px;overflow:hidden;">
    <div id="maingrid" style="margin-top: -1px; margin-left: -1px"></div>
</body>
</html>
