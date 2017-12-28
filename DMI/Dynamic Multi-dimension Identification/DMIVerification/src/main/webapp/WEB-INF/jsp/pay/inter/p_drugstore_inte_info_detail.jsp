<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
							
							<form action="" name="interForm" id="interForm" method="post">
							
							<input type="hidden" name="interId" id="interId" value="${pd.interId}">
							<input type="hidden" name="userId" id="userId" value="${pd.userId}">
							
							<div id="zhongxin" style="padding-top: 13px;">
								<table width="100%" style="border: 1px solid #dddddd; padding-bottom: 10px; margin-bottom:10px;">
									<tr height="30">
										<td width="14%" class='center' bgcolor="#F0F0F0">
											<a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('drugstoreInterDetail');">
												<font size="2" color="#7B7B7B"><b>接口数据详情</b></font>
											</a>
										</td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('drugstoreDrugDetail');">药品信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('drugstoreResultDetail');">计算结果信息</a></td>
									</tr>
								</table>
							
								<table id="table_report" class="table table-striped table-bordered table-hover" width="100%">
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">业务接口编号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.api_type}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">数据类型</td>
										<td align="left" width="35%" bgcolor="white">${varModel.data_type_name}</td>
									</tr>
								
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">机构名称</td>
										<td align="left" width="35%" bgcolor="white">${varModel.group_name}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">附加消息</td>
										<td align="left" width="35%" bgcolor="white">${varModel.add_info}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">请求流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.req_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">时间戳</td>
										<td align="left" width="35%" bgcolor="white">${varModel.time_stamp}</td>
									</tr>
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">药店名称</td>
										<td align="left" width="35%" bgcolor="white">${varModel.service_name}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">业务类型</td>
										<td align="left" width="35%" bgcolor="white">${varModel.busi_type_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">IC卡号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.ic_card}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">工号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.work_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">个人编号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.pers_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">开单医生</td>
										<td align="left" width="35%" bgcolor="white">${varModel.bill_doc}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">收款员编号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.cashier_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">收款员姓名</td>
										<td align="left" width="35%" bgcolor="white">${varModel.cashier_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">就诊科室</td>
										<td align="left" width="35%" bgcolor="white">${varModel.medical_dept}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">诊断疾病名称</td>
										<td align="left" width="35%" bgcolor="white">${varModel.medical_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">药店收据类型</td>
										<td align="left" width="35%" bgcolor="white">${varModel.receipt_type}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">调用状态</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.deal_stat == '0'}">成功</c:if>
											<c:if test="${varModel.deal_stat != '0'}">失败</c:if>
										</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">调用错误编码</td>
										<td align="left" width="35%" bgcolor="white">${varModel.deal_error_code}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">调用错误信息</td>
										<td align="left" width="35%" bgcolor="white">${varModel.deal_error_info}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">服务返回的错误信息</td>
										<td align="left" width="35%" bgcolor="white">${varModel.service_error}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">医保流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.medicare_no}</td>
									</tr>
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">药店收据号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.receipt_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">药店退费收据号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.refund_no}</td>
									</tr>
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">药店新收据号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.new_no}</td>
									</tr>
								</table>
							</div>
							
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
		
	});
	
	function queryData(item){
		interForm.action = "<%=basePath%>/interInfo/"+item+".do";
		interForm.submit();
	}
</script>

</html>