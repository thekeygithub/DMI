<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String pid = request.getParameter("pid");//部门id
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath %>"/>
    <title>部门信息添加</title>
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
                inputWidth: 170, 
                labelWidth: 90, 
                space: 40,
				validate : true,
                fields: [ 
                	{ name: "type", type: "hidden"},
                    { label: "部门名称",name: "corpname", newline: true, type: "text", validate: { required: true,maxlength: 100 } },
                    { label: "部门编码",name: "epid", newline: true, type: "text", validate: { required: true,maxlength: 100 } },
                    { label: "上级单位", name: "pid", newline: true, type: "select", validate: { required: true}, 
						editor: {
							width : 180, 
							selectBoxWidth: 200,
							selectBoxHeight: 200, 
							valueField: 'id',
							treeLeafOnly: false,
							tree: { 
								url:"cropDeptTreeQuery.action", 
								ajaxType:'post',
								idFieldName: 'id',
								parentIDFieldName: 'pid',
								checkbox: false
							}
						}
					},
					{ label: "部门排序", name: "ordernum", newline: true, type: "number", validate: { required: true, maxlength: 3,messages:{maxlength:'最大不能超过三位数字！'} } },
                    { label: "部门描述",name: "remark", newline: true, type: "textarea",width:300 , validate: { maxlength:200} }
                ]
            });
            form.setData({type:2});//部门添加页面type代表部门
            if("null"!="<%=pid %>"&&"0"!="<%=pid %>"){
				form.setData({
					pid: "<%=pid %>"
				});
			}
        });
        
        /**提交验证*/
        function f_validate() { 
            if (form.valid()) {
            //alert(liger.toJSON(form.getData()));
              return form.getData();
            }else {
                form.showInvalid();
            }
            return null;
        }
	</script>
    <style type="text/css">
        body{ 
	         margin: 0;
	         padding: 0;
        }
        .liger-button {
        	float:left;
        	margin-left:20px;
       	}
    </style>
</head>
<body style="padding:10px">   
	<form id="form2"></form> 
</body>
