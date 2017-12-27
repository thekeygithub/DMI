<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String id=request.getParameter("id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>所属区域信息修改</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/messages_cn.js" type="text/javascript"></script>
    <script type="text/javascript">
		/**
	   	获取单个的信息
	   */
	  function  getMess(mid){
	     $.ajax({
			url:"areaGetMess.action", 
			data:"id="+mid, 
			dataType:"json", 
			type:"post",
			success:function (mm) {
				liger.get("form2").setData({
					areaname:mm.areaname,
					remark:mm.remark
				});
			}, 
			error:function (error) {
				alert("获取单个信息失败****" + error.status);
		}});
	  } 
    </script>
    <script type="text/javascript">
		var form;
        $(function (){
        	form = $("#form2").ligerForm({
                inputWidth: 170, 
                labelWidth: 90, 
                space: 40,
				validate : true,
                fields: [ 
                	{ name: "id", type: "hidden"},
                    { label: "所属区域名称",name: "areaname", newline: true, type: "text",attr:{disabled:false}, validate: { required: true,maxlength: 100 } },
                    { label: "所属区域描述",name: "remark", newline: true, type: "textarea",width:300 , validate: {maxlength:200} }
                ]
            });
            form.setData({
				id: "<%=id %>"
			});
            getMess("<%=id %>");
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
</html>