<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String userid = (String)request.getSession().getAttribute("userid");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath %>"/>
    <title>职务信息添加</title>
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
                    { label: "职务名称",name: "postname", newline: true, type: "text", validate: { required: true,maxlength: 100 } },
					{ label: "所属单位", name: "dept", newline: true, type: "select", validate: { required: false},
						editor: {
							width : 180, 
							selectBoxWidth: 190,
							selectBoxHeight: 190, 
							valueField: 'id',
							treeLeafOnly: false,
							tree: { 
								url:"cropDeptTreeQuery.action", 
								ajaxType:'post',
								idFieldName: 'id',
								parentIDFieldName: 'pid',
								checkbox: false
							},
							onSelected: function(id,value){
								if(''!=id&&''!=value&&"null"!=value){
									$("[name=deptid]").val(id);
								}
							}
						}
					},
					{ name: "deptid", type: "hidden" },
					{ label: "职务排序", name: "ordernum", newline: true, type: "number", validate: { required: true, minlength: 3,maxlength: 3,messages:{maxlength:'最大不能超过三位数字！'} } },
                    { label: "职务描述",name: "remark", newline: true, type: "textarea",width:300 , validate: { maxlength:200} }
                ]
            });
        	form.setVisible(["dept", "deptid"], false);
        	$.ajax({
				url:"queryConfigSuperUserID.action", 
				async:false,
				type:"post",
				success:function (data) {
					var userid = "<%=userid %>";
					if(userid==data){
						form.setVisible(["dept", "deptid"], true);
						form.setFieldValidate("dept", {required: true});
					}
				}, 
				error:function (error) {
					top.my_alert("获取信息失败！" + error.status);
				}
			});
        });
        
        /**提交验证*/
        function f_validate() { 
            if (form.valid()) {
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
