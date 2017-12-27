<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String id=request.getParameter("id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>门户资源信息添加</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/messages_cn.js" type="text/javascript"></script>
    <script type="text/javascript">
		var form;
		$(function (){
			form = $("#form2").ligerForm({
				inputWidth: 190, 
				labelWidth: 90, 
				space: 40,
				validate : true,
				fields: [ 
					{ label: "资源名称", name: "pname", newline: true, type: "text", validate: { required: true,maxlength: 50 } },
					{ label: "资源URL", name: "url", newline: true, type: "text", validate: { required: true,maxlength: 200}},
					{ label: "资源描述", name: "remark", newline: true,width:300,type: "textarea", validate: { required: false,maxlength: 200 } }
				]
			});
			getMess("<%=id %>");
		});
		
		/**提交验证*/
		function f_validate() { 
		    if (form.valid()) {
		    	return addInfo();
		    }else {
		        form.showInvalid();
		    }
		    return null;
		}
		
		function addInfo() {
			var data = form.getData();
			var mid  = "<%=id %>";//id
			var pname  = data.pname;//名称
			var url  = data.url;//名称
			var remark  = $("#remark").val();//备注
			var infoData = {"model.id":mid,"model.pname":pname,"model.url":url,"model.remark":remark};
			return infoData;
		}  
		function  getMess(id){//获取单个信息
		    $.ajax({
			url:"sysPortalQueryById.action", 
			data:"id="+id, 
			dataType:"json", 
			type:"post",
			success:function (data) {
				var mm = data.model;
				form.setData({
							id: mm.id,
			                pname: mm.pname,
			                url : mm.url,
			                remark : mm.remark
			            });
			}, 
			error:function (error) {
				top.$.ligerDialog.error("获取单个信息失败****" + error.status);
			}
			});
		 }   
	</script>
    <style type="text/css">
        body{ font-size:14px;}
    </style>
</head>
<body style="padding:10px">   
	<form id="form2"></form> 
</body>
</html>
