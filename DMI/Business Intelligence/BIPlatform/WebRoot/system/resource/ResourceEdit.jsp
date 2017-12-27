<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String id = request.getParameter("id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath%>"/>
    <title>主资源管理页面</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/messages_cn.js" type="text/javascript"></script>
    <script type="text/javascript">
    function f_validate(){ 
			if (form.valid()){
				return addBtnResource();
			}else{
			    form.showInvalid();
			}
			return null;
		}
		var form;
        $(function (){
        	form = $("#form2").ligerForm({
                inputWidth: 170, 
                labelWidth: 110, 
                space: 40,
				validate : true,
                fields: [ 
                	{ name: "id", type: "hidden"},
                	{ name: "menuid", type: "hidden"},
                    { label: "页面名称", name: "pagename", newline: true, type: "text", validate: { required: true}},
                    { label: "页面URL", name: "pageurl", newline: true, type: "text"},
                    { label: "按钮名称", name: "resourcename", newline: true, type: "text", validate: { required: true}},
                    { label: "按钮ID/icon", name: "resourceid", newline: true, type: "text"},
                    { label: "按钮事件", name: "btnevent", newline: true, type: "text", validate: { required: true }},
                    { label: "按钮排序", name: "btnsort", newline: true, type: "number", validate: { required: true} },
                    { label: "信息描述", name: "remark", newline: true,width:300,type: "textarea", validate: { required: false,maxlength:200} }
                ]
           	 }); 
      
            QueryRes("<%=id %>");//查询页面对应的按钮资源信息
        });
        
        /*
        按钮资源数据
        */
        function addBtnResource(){
       		var rid = form.getData().id;//菜单id
       		var menuid = form.getData().menuid;//菜单id
       		var pagename = form.getData().pagename;//页面名称
       		var pageurl = form.getData().pageurl;//页面url
       		var resourcename = form.getData().resourcename;//按钮名称
       		var resourceid = form.getData().resourceid;//按钮id
       		var btnevent = form.getData().btnevent;//按钮绑定事件
       		var btnsort = form.getData().btnsort;//按钮排序
       		var remark = form.getData().remark;//按钮排序
       		var resourceData = {"model.id":rid,"model.menuid":menuid,"model.pagename":pagename,"model.pageurl":pageurl,
				       		"model.resourcename":resourcename,"model.resourceid":resourceid,
				       		"model.btnevent":btnevent,"model.btnsort":btnsort,"model.remark":remark};
        	return resourceData;
        }
            
	function QueryRes(rid) {//根据id查询资源信息
		$.ajax({
			url:"btnResourceQueryById.action", 
			data:"id="+rid, 
			dataType:"json", 
			type:"post",
			success:function (data) {
				var mm = data.model;
				form.setData({
					id:"<%=id %>",
					pagename:mm.pagename,
					pageurl: mm.pageurl,
					pagename: mm.pagename,
					menuid: mm.menuid,
					btnsort: mm.btnsort,
					resourcename: mm.resourcename,
					resourceid: mm.resourceid,
					btnevent: mm.btnevent,
					remark: mm.remark
				});
			}, 
			error:function (error) {
				  top.$.ligerDialog.error("获取按钮资源失败！" + error.status);
			}
			});
	}
    </script>
    <style type="text/css">
        body{ font-size:14px;}
        .my_td{ padding:4px;height:27px}
    </style>
</head>
<body>  
	<form id="form2"></form> 
	<div style="display:none"></div>
</body>
</html>
