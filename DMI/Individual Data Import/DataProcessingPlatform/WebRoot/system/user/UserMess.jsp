<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String id = request.getParameter("id");
%>

<!DOCTYPE HTML >
<html>
  <head>
    <title>用户详细信息页面</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>  
    <script  type="text/javascript"  src="<%=basePath  %>js/dateformat.js"></script>   
    <script type="text/javascript">
        jQuery(function ($){
            var userid = "<%=id %>";
            getMess(userid);
        });
   /**
     获取单个的信息
  */
  function  getMess(mid){
     $.ajax({
		url:"treeuserGetById.action", 
		data:"id="+mid, 
		dataType:"json", 
	    async:false,
		type:"post",
		success:function (mm) {
	        $("#loginname").html(mm.loginname);
	        $("#username").html(mm.username);
	        $("#sex").html(mm.sex=="M"?'男':'女');
	        $("#birthday").html(getFormatDate(mm.birth));
	        $("#corp").html(mm.corpname);
	        $("#dept").html(mm.corpid);
	        $("#role").html(mm.rolename);
	        $("#station").html(mm.stationname);
	        $("#post").html(mm.postname);
            $("#phonepublic").html(mm.phonepublic);
            $("#mobilepublic").html(mm.mobilepublic);
            $("#phoneprivate").html(mm.phoneprivate);
            $("#mobileprivate").html(mm.mobileprivate);
            $("#emailpublic").html(mm.emailpublic);
            $("#emailprivate").html(mm.emailprivate);
			$("#remark").html(mm.remark);
			$("#area").html(mm.areaname);
			$("#dcid").html(mm.dcidname);
		}, 
		error:function (error) {
			alert("获取单个信息失败****" + error.status);
	}});
  } 
    </script>
  </head>
  
  <body>
	 <table class="table_infos"  width="98%" style="font-size: 14px;">
	   <caption class="info_mess">用户详细信息</caption>
	   <tr>
	    <td align="right" class="td_tit_Report1" width="20%">登录账号:</td>
	    <td align="left"  class="mycla" width="30%"><span id="loginname"></span></td>
	    <td align="right" class="td_tit_Report1" width="20%">用户姓名:</td>
	    <td align="left"  class="mycla" width="30%"><span id="username"></span></td>
	   </tr>
	   <tr>
	    <td align="right" class="td_tit_Report1">用户性别:</td>
	    <td align="left"  class="mycla"><span id="sex"></span></td>
	   <td align="right" class="td_tit_Report1">出生年月:</td>
	    <td align="left" class="mycla"><span id="birthday"></span></td>
	   </tr>
	   <tr>
	    <td align="right" class="td_tit_Report1">所属公司:</td>
	    <td align="left"  class="mycla"><span id="corp" ></span></td>
	    <td align="right" class="td_tit_Report1">所属部门:</td>
	    <td align="left"  class="mycla"><span id="dept" ></span></td>
	   </tr>
	   <tr>
	    <td align="right" class="td_tit_Report1">岗位:</td>
	    <td align="left"  class="mycla"><span id="station"></span><span id="stationid"></span></td>
	    <td align="right" class="td_tit_Report1">用户角色:</td>
	    <td align="left"  class="mycla"><span id="role"></span><span id="roleid"></span></td>
	   </tr>
	    <tr>
	    <td align="right" class="td_tit_Report1">职务:</td>
	    <td align="left"  class="mycla"><span id="post"></span><span id="postid"></span></td>
	    <td align="right" class="td_tit_Report1">所属区域:</td>
	    <td align="left"  class="mycla"><span id="area"></span></td>
	   </tr>
	   <tr>
	    <td align="right" class="td_tit_Report1">所属数据中心:</td>
	    <td colspan="3"><span  id="dcid"></span></td>
	   </tr>
	    <tr>
	    <td align="right" class="td_tit_Report1">公司座机:</td>
	    <td align="left"  class="mycla"><span id="phonepublic"></span></td>
	     <td align="right" class="td_tit_Report1">家庭座机:</td>
	    <td align="left"  class="mycla"><span id="phoneprivate"></span></td>
	   </tr>
	    <tr>
	    <td align="right" class="td_tit_Report1">公司手机:</td>
	    <td align="left"  class="mycla"><span id="mobilepublic"></span></td>
	    <td align="right" class="td_tit_Report1">私人手机:</td>
	    <td align="left"  class="mycla"><span id="mobileprivate"></span></td>
	   </tr>
	   <tr>
	    <td align="right" class="td_tit_Report1">公司邮箱:</td>
	    <td align="left"  class="mycla"><span id="emailpublic"></span></td>
	    <td align="right" class="td_tit_Report1">私人邮箱:</td>
	    <td align="left"  class="mycla"><span id="emailprivate"></span></td>
	    </tr>
	  <tr>
	    <td align="right" class="td_tit_Report1">用户描述:</td>
	    <td colspan="3"><span  id="remark"></span></td>
	   </tr>
	 </table>
  </body>
</html>
