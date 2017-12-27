<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String userid = (String)request.getSession().getAttribute("userid");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>日志列表页面</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>js/dateformat.js"></script> 
    <script  type="text/javascript"  src="./syslogList.js"></script> 
    <script type="text/javascript"> 
		jQuery(function($){
			$("#starttime").ligerDateEditor({ showTime: false, labelWidth: 100 });
			$("#endtime").ligerDateEditor({ showTime: false, labelWidth: 100 });
			$("#formsearch").ligerForm();
			getMess();
		 });
		/**
		  是否具有最大权限
		 */
		function  getMess(){
			$.ajax({
				url:"queryConfigSuperUserID.action", 
				async:false,
				type:"post",
				success:function (data) {
					var userid = "<%=userid %>";
					if(userid==data){
						$("#del").css("display","block");
					}
				}, 
				error:function (error) {
					top.my_alert("获取信息失败！" + error.status);
				}
			});
		} 
    </script>
  	
</head>
<body>  
	<div id="toolbar" class="searchDiv">
	<form action="" method="post" name="formsearch" id="formsearch">
		<table>
			<tr>
				<td class="ser_tit">
					<label>开始时间:</label>
				</td>
				<td class="ser_cont" valign="middle">
					<input id="starttime" name="starttime" value=""  />
				</td>
				<td class="ser_tit">
					<label>结束时间:</label>
				</td>
				<td class="ser_cont">
					<input id="endtime" name="endtime" value=""  />
				</td>
				<td class="ser_tit">
					<label>用户姓名:</label>
				</td>
				<td class="ser_cont">
					<input id="username" type="text" value=""/>
				</td>
				<td class="ser_cont">   
					<input type="button" class="btn" id="datasearch" value="查询" />
					<input type="button" class="btn" id="dataclear" value="清空" />
				</td>
				<td class="ser_cont">
					<input type="button" id="del" value="删除" class="btn" onclick="itemdelete();" style="display: none"/>
				</td>
			</tr>
		</table>
		</form>
	</div>
    <div id="maingrid">
    </div>
</body>
</html>
