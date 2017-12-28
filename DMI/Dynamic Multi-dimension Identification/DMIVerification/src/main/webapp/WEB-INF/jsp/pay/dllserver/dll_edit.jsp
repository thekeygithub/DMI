<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
						<form action="dllserver/add.do" name="dllserverForm" id="dllserverForm" method="post">
							<input type="hidden" name="ID" id="in_id" value="${pd.ID }"/>
							<div id="zhongxin" style="padding-top: 13px;">
							<table id="table_report" class="table table-striped table-bordered table-hover">
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">服务地址:</td>
									<td><input type="text" name="LOCAL_IP" id="local_ip" value="${pd.LOCAL_IP }" maxlength="50" onblur="len()" placeholder="输入服务器地址" title="服务器地址" style="width:98%;" /></td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">医院名称:</td>
									<td><input type="text" name="HOSP_NAME" id="hosp_name"  value="${pd.HOSP_NAME }" maxlength="50" placeholder="输入医院名称" title="医院名称" style="width:98%;" /></td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">医院编码:</td>
									<td><input type="text" name="HOSP_ID" id="hosp_id" value="${pd.HOSP_ID }" maxlength="50" placeholder="输入医院编号" title="医院编号"style="width:98%;" /></td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">DLL文件名:</td>
									<td><input type="text" name="DLL_ADDRESS" id="dll_address" value="${pd.DLL_ADDRESS }" maxlength="50" placeholder="输入编号" title="编号"style="width:98%;" /></td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">状态:</td>
									<td>
										<input name="status" type="radio" id="on_status"  />启用
										&nbsp;&nbsp;<input name="status" type="radio" id="off_status"  />停用
										<input  name="CURRENT_STATUS" id="current_status" type="hidden" value="" />
									</td>
								</tr>
								<tr>
									<td class="center" colspan="6">
										<ts:rights code="dllserver/add">
											<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
										</ts:rights>
									
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
									</td>
								</tr>
							</table>
							</div>
							<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
						</form>
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content -->
			</div>
		</div>
		<!-- /.main-content -->
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- inline scripts related to this page -->
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>						
<script type="text/javascript">
	$(top.hangge());
	
	$(document).ready(function(){
		//加载当前状态判断
		if("edit" == "${msg}") {
			if("${pd.CURRENT_STATUS}" == "0") {
				$("#off_status").attr("checked","true");
			}
			if("${pd.CURRENT_STATUS}" == "1") {
				$("#on_status").attr("checked","true");
			}
		}else if("add" == "${msg}"){
			$("#on_status").attr("checked","true");
		}

	});

	//保存
	function save(){
			var hospName = $("#hosp_name").val();
			var hospId = $("#hosp_id").val();
			var localIp = $("#local_ip").val();
			var dllAddress = $("#dll_address").val();
			
			if(localIp=="" || $.trim(localIp)==""){
				$("#local_ip").tips({
					side:3,
		            msg:'请输入服务地址',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#hosp_id").focus();
			return false;
			}
			if(hospName=="" || $.trim(hospName)==""){
				$("#hosp_name").tips({
					side:3,
		            msg:'请输入医院名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#hosp_name").focus();
			return false;
			}
			
			if(hospId=="" || $.trim(hospId)==""){
				$("#hosp_id").tips({
					side:3,
		            msg:'请输入医院编码',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#hosp_id").focus();
			return false;
			}
			
			if(dllAddress=="" || $.trim(dllAddress)==""){
				$("#dll_address").tips({
					side:3,
		            msg:'请输入DLL文件名',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#dll_address").focus();
			return false;
			}
					if($("#on_status").is(":checked")){
						$("#current_status").val("1");
					}else if($("#off_status").is(":checked")){
						$("#current_status").val("0");
					}else{
						alert("请选择状态!")
					}
					var Ip = $.trim(localIp);
					var hId =  $.trim(hospId);
					//远程调用清理缓存服务配置
							var remoteUrl = "http://"+ Ip + "/dllservice/deleteConfigCache.do";
							$.ajax({
								url: remoteUrl,
								type: "post",
								data: 	{HOSP_ID: hId},
								dataType: "json",
								//jsonp: "callback",
								//jsonpCallback:"success_jsonpCallback",
								success: function(data){
									if("success" == data.result){
										//alert(hospId+"缓存已清除！");
									}
								},
								error: function(){
									alert("删除缓存失败,请确认远程服务器IP地址是否正确!");
								}
							});
					$("#dllserverForm").submit();
					$("#zhongxin").hide();
					$("#zhongxin2").show();
					//$("#zhongxin").hide();
	}
</script>
</html>