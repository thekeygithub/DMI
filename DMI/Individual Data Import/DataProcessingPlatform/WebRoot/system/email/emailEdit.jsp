<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>邮箱配置管理</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>  
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.5.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all-edit.js" ></script>
    <script  type="text/javascript"  src="emailEdit.js"></script>
    <style type="text/css">
        body{ font-size:12px;}
        .l-table-edit {}
        .l-table-edit-td{ padding:4px;}
        .l-button-submit,.l-button-test{width:80px; float:left; margin-left:10px; padding-bottom:2px;}
        .l-verify-tip{ left:230px; top:120px;}
    </style>         	
</head>

<body style="padding:10px"> 
	<form id="email">
	<table cellpadding="0" cellspacing="0" class="l-table-edit" >
            <tr>
                <td align="right" class="l-table-edit-td">邮箱类型设置：</td>
                <td align="left" class="l-table-edit-td" style="width:160px" >
                	<input name="id" type="hidden" id="id"/>
                	<input id="mailServerHost" name="mailServerHost" type="text"  size="30" value="smtp.139.com"/>
                	
                </td>
                <td align="left" width="20px;"></td>
                <td align="right" class="l-table-edit-td">邮箱服务器端口号：</td>
                <td align="left" class="l-table-edit-td" style="width:160px">
                    <input name="mailServerPort" type="text" id="mailServerPort" size="30"/>
                </td>
                <td align="left" width="20px;"></td>
             </tr>
             <tr>
                <td align="right" class="l-table-edit-td">发件人邮箱地址：</td>
                <td align="left" class="l-table-edit-td" style="width:160px">
                    <input name="fromAddress" type="text" id="fromAddress"size="30" />
                </td>
                <td align="left" width="20px;"></td>                
                <td align="right" class="l-table-edit-td">是否开启邮箱验证：</td>
                <td align="left" class="l-table-edit-td" style="width:160px" >
                	 <input id="validate_1" type="radio" name="validate" value="1" />
                	 <label for="validate_1">是</label> 
                	 <input id="validate_0" type="radio" name="validate" value="0"/>
                	 <label for="validate_0">否</label>
                </td>
                <td align="left" width="20px;"></td>                
            </tr>    
            <tr>
                <td align="right" class="l-table-edit-td">邮箱用户名：</td>
                <td align="left" class="l-table-edit-td" style="width:160px">
                    <input name="userName" type="text" id="userName" size="30"/>
                </td>
                <td align="left" width="20px;"></td>            
                <td align="right" class="l-table-edit-td">邮箱密码：</td>
                <td align="left" class="l-table-edit-td" style="width:160px" >
                	<input name="emailPwd" type="password" id="emailPwd" size="30"/>
                </td>
                <td align="left" width="20px;"></td>
           </tr>
           <tr>
                <td align="right" class="l-table-edit-td">邮箱确认密码：</td>
                <td align="left" class="l-table-edit-td" style="width:160px">
                    <input name="reEmailPwd" type="password" id="reEmailPwd" size="30"/>
                </td>
                <td align="left" width="20px;"></td>
            </tr>             
        </table>
        <br/>
        <input type="button" value="编辑" class="l-button l-button-test" onclick="f_editEmail();"/>
        <input type="button" value="保存" class="l-button l-button-submit" onclick="f_testEmail();"/>
        <!--<input type="button" value="测试" class="l-button l-button-test" onclick="f_testEmail();"/> -->
    </form> 
</body>
</html>
