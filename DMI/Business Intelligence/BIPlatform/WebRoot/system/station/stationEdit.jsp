<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String id=request.getParameter("id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>岗位信息修改</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/messages_cn.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function (){
	        var mid = "<%=id %>";
	        form = $("#form2").ligerForm({
                inputWidth: 190, 
                labelWidth: 90, 
                space: 40,
				validate : true,
                fields: [
                	{ name: "id", type: "hidden" },
					{ label: "岗位名称", name: "stationname", newline: true, type: "text", validate: { required: true,maxlength: 50 } },
					{ label: "岗位描述", name: "remark", newline: true, type: "textarea",width:300, validate: { maxlength: 200 } }
				]
            }); 
	        getMess(mid);//得到数据字典信息
        });
    	
    	function f_validate() { 
            if (form.valid()) {
              return form.getData();
            }else {
                form.showInvalid();
            }
            return null;
        } 
   
		/**
	        获取单个的信息
		*/
		function  getMess(mid){
			$.ajax({
				url:"stationGetMess.action", 
				data:"id="+mid, 
				dataType:"json", 
				type:"post",
				success:function (mm) {
					form.setData({
						id: mid,
		                stationname: mm.stationname,
		                remark : mm.remark
		            });
				}, 
				error:function (error) {
					alert("获取单个信息失败****" + error.status);
			}});
		}       
        
    </script>
    <style type="text/css">
        body{ 
        	font-size:14px;
        }
        .liger-button {
        	float:left;margin-left:20px;
       	}
    </style>
</head>
<body style="padding:10px">   
	<form id="form2"></form> 
</body>
</html>
