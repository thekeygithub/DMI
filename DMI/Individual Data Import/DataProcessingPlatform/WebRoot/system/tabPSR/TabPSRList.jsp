<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath %>"/>
    <title>职务、岗位、角色tab页面</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" />
    <script type="text/javascript" src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script type="text/javascript" src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script src="<%=basePath  %>system/tabPSR/TabPSRList.js" type="text/javascript"></script>
  	<script type="text/javascript">
  	</script>
  	<style type="text/css">
		.liger-button{
			margin: 2px 8px;
			float: left;
			height:20px;
			line-height: 20px;
			font-size: 12px;
		}
		.l-layout-left{
			overflow: auto !important;
		}
  		.l-tree{
  			width:230px !important;
  		}
  	</style>
</head>
<body style="padding: 0px;overflow:hidden;">
    <form id="form1">
        <div id="layout" style="margin-top: -1px; margin-left: -1px">
            <div position="left" title="组织架构">
            	<div class="btndiv">
            		<div id="toolbar"></div>
            	</div>
                <div id="treediv" style="width: 250px; height: 100%; margin: -1px; float: left; border: 1px solid #ccc; overflow: auto;">
                    <ul id="tree"></ul>
                </div>
            </div>
            <div position="center">
	            <div id="tab1" style="width'100%';height:100%;overflow:hidden; border:1px solid #A3C0E8; ">
	            	<div title="用户管理"  tabid="userTab" id="userTab" class="my-tab-div">
		            	<iframe frameborder="0" id="userframe" src=""></iframe>
		            </div>
		            <div title="职务管理"  tabid="postTab" id="postTab" class="my-tab-div">
		            	<iframe frameborder="0" id="postframe" src=""></iframe>
		            </div>
		            <div title="岗位管理"  tabid="stationTab" id="stationTab" class="my-tab-div">
		            	<iframe frameborder="0" id="stationframe" src=""></iframe>
		            </div>
		            <div title="角色管理"  tabid="roleTab" id="roleTab" class="my-tab-div">
		            	<iframe frameborder="0" id="roleframe" src=""></iframe>
		            </div>
		            <div title="所属区域"  tabid="areaTab" id="areaTab" class="my-tab-div">
		            	<iframe frameborder="0" id="areaframe" src=""></iframe>
		            </div>
	        	</div>
            </div>
        </div>
    </form>
    <input type="hidden" value="" id="cdid"/>
</body>
</html>
