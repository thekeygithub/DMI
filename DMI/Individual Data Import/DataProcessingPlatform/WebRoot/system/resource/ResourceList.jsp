<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath%>"/>
    <title>菜单、按钮管理页面</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>system/resource/resourcelist.js"></script> 
    <style type="text/css">
    .l-layout-left{
    			overflow: auto !important;
    			}
    </style>
</head>
<body style="padding: 0px;overflow:hidden;">
    <form id="form1">
        <div id="layout1" style="margin-top: -1px; margin-left: -1px">
            <div position="left" title="菜单列表">
                <div id="treediv" style="width: 250px; height: 100%; margin: -1px; float: left; border: 0px solid #ccc; overflow: auto;">
                    <ul id="tree1" style="width:250px !important;"></ul>
                </div>
            </div>
            <div position="center" title="按钮资源列表">
                <div id="maingrid" style="margin-top: -1px; margin-left: -1px"></div>
            </div>
        </div>
    <input type="hidden" id="pagename" value=""/>
    <input type="hidden" id="menuid" value=""/>
    <input type="hidden" id="menuname" value=""/>
    </form>
</body>

</html>
