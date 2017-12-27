<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>数据字典添加</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/messages_cn.js" type="text/javascript"></script>
    <script src="<%=basePath  %>js/valform.js" type="text/javascript"></script>
    <script type="text/javascript">
        var form;
        $(function (){
        	form = $("#form2").ligerForm({
                inputWidth: 170, 
                labelWidth: 90, 
                space: 40,
				validate : true,
                fields: [ 
                    { label: "类别", name: "dtype", newline: true, type: "text", validate: { required: true},editor: { onChangeValue: letter }  },
                    { label: "代码", name: "dkey", newline: true, type: "text", validate: { required: true}},
                    { label: "值", name: "dvalue", newline: true, type: "text", validate: { required: true,maxlength: 32} },
                    { label: "备注", name: "remark", newline: true, type: "textarea" , validate: { maxlength: 64 }}
                ]
            }); 
        });
		function letter(){
			var data = form.getData();
			var str = /^[A-Z]+$/;
			if (!str.test(data.dtype)){
				//top.my_alert("类别必须由大写字母组成！");
				form.showFieldError("dtype","类别必须由大写字母组成！");
				$("[name=dtype]").val("");
			}
		}
        function f_validate(){ 
			if(form.valid()){
				return form.getData();
			}else{
			    form.showInvalid();
			}
		}
    </script>
    <style type="text/css">
        body{ font-size:14px;}
        .liger-button {
        	float:left;margin-left:20px;
       	}
    </style>
</head>
<body style="padding:10px">   
	<form id="form2"></form> 
</body>
</html>
