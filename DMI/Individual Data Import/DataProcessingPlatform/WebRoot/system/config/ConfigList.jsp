<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>系统配置</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/main.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.9.1.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script> 
    <script  type="text/javascript"  src="ConfigList.js"></script> 
     <script type="text/javascript">
    	jQuery(function($){
    		$("#searchform").ligerForm();
    	});
    </script>
  	
</head>
<body>  
    <div id="maingrid"></div>
    <div id="searchDict"  style="margin:3px;display: none">
     <form action="" id="searchform">
   		<table id="searchtable">
		   	<tr>
			   	<td class="ser_tit"><label>类别：</label></td>
			   	<td class="ser_cont"><input id="dtype" type="text"  name="dtype" style="width: 180px" /></td>
		   	</tr>
	   	  	<tr>
		   	<td class="ser_tit"><label>值：</label> </td>
		   	<td class="ser_cont">
			   	<input id="dvalue" type="text"  name="dvalue" style="width: 180px" />
	         </td>
		   	</tr>
   		</table>
   	</form>
	</div>
</body>
</html>
