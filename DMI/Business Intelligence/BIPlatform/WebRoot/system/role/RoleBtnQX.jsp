<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String roleid = request.getParameter("role_id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>"/>
    
    <title>角色权限</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<link  rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link  rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>   
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/core/base.js" ></script>  
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/plugins/ligerDialog.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/plugins/ligerDrag.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/json2.js" ></script> 
  	<script type="text/javascript">
        $(function () {
			getAllButtonQT();
        });
        
        function f_save() {
            btnRoleQXModify();//其他页面的按钮权限
        }
       
        function f_saving() {
            $.ligerDialog.waitting("正在保存中...");
        }

		/**
		  其他页面的对应按钮获取
		*/
		function getAllButtonQT(){
		 	$.ajax({//其他页面的按钮获取
               	url:"btnQueryAll.action",
				data:{"menuSort":"QT"},
				async:false,
				dataType:"json",
                type: 'post',
                success: function (data) {
                	var tempStr = "<table class=\"btnTable\" style=\"\" cellspacing=\"1px\" width='98%'>";
                	 tempStr += "<caption class=\"info_mess\" >非菜单页面的按钮授权管理</caption>"
                 	$.each(data,function(index,item){
                 		if(item.btnStr != null && item.btnStr != ""){
	                 		tempStr += "<tr>"
	                 		tempStr += "<th>"+item.pagename+"</th><td>";
	                 		var btnArr = (item.btnStr).split(",");
							for(var i=0;i<btnArr.length;i++){
								if(btnArr[i]!=""){
									var tempArr = btnArr[i].split("|");
									tempStr += "<input type=\"checkbox\" class='btncheck1' id ='"+tempArr[0]+"' value='"+tempArr[0]+"' name='"+item.pageurl+"' style=\"vertical-align:middle;\"/>&nbsp;<label>"+tempArr[1]+"</label>";
								}
							}
	                 		tempStr += "</td></tr>"
                 		}
                 	});
                 	 tempStr += "</table>"
                 	 $("#btnDiv").html(tempStr);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $.ligerDialog.error("按钮权限获取出错！");
                }
               });
               
            $.ajax({//按钮权限获取
               	url:"roleBtnQueryByMenuId.action",
				data:{"roleid":"<%=roleid %>","menuSort":"QT"},
                type: 'post',
                success: function (data) {
                 	var m = eval(data);
                 	$.each(m,function(index,item){
                 		$("#"+item).attr("checked","checked");
                 	});
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $.ligerDialog.error("按钮权限获取出错！");
                }
               });
		}

		function btnRoleQXModify(){
			  var qt = 'QT';
			  var roleid = "<%=roleid %>";
              var btnIDStr1 = "";
               $(".btncheck1:checked").each(function(index,item){
               		btnIDStr1 += item.value;
               		btnIDStr1 += ",";
               });
              $.ligerDialog.waitting("正在保存中...");
              $.ajax({
               	url:"roleBtnModify.action",
				data:"ch="+btnIDStr1+"&roleid="+roleid+"&menuSort="+qt,
                type: 'post',
                success: function (data) {
                 	$.ligerDialog.closeWaitting();
               	    top.$.ligerDialog.confirm("是否继续编辑", "保存成功", function (ok) {
	                    if (!ok) {
	                       window.frameElement.dialog.close();
	                     }
               		 });
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                	$.ligerDialog.closeWaitting();
                    $.ligerDialog.error("按钮权限修改出错！");
                }
               });
		}
    </script>
    <style type="text/css">
    	.btncheck,.btncheck1{
    	        vertical-align:middle;
    			margin-left: 10px;
    		}
    	label{
    	        vertical-align:middle;
    		}
    	.btnTable{
    	 		width: "99%";
    	 		margin:0 auto;  
    	 		font-size:14px;
    	 		border-collapse: collapse;
    	 		border:1px #BED5F3 solid;
    	  }
    	 .btnTable th{
    	 		width:150px;
    	 		height:22px;
    	 		text-align: center;
    	 		border: 1px solid #B1CDE3; 
    	  }
    	  .btnTable td{
    	  		border: 1px solid #B1CDE3;  
    	 		text-align: left;
    	 		vertical-align:middle;
    	 		font-size:14px;
    	 		height:22px;
    	 		padding: 5px;
    	  }
    	  .info_mess{
    	  		text-align:center;
				height: 40px;
				line-height:40px;
				font-size: 20px;
				font-weight: bolder;
				color: #4f6b72;  
			}
    </style>
  </head>
  
  <body>
      	<div id="btnDiv"></div>
  </body>
</html>
