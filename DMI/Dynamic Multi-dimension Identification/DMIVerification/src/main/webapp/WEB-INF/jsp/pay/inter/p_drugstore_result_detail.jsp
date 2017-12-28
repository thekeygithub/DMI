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
										<td width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('drugstoreInterDetail');">接口数据详情</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('drugstoreDrugDetail');">药品信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center' bgcolor="#F0F0F0">
											<a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('drugstoreResultDetail');">
												<font size="2" color="#7B7B7B"><b>计算结果信息</b></font>
											</a>
										</td>
									</tr>
								</table>
							
								<table id="table_report" class="table table-striped table-bordered table-hover" width="100%">
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">收据张数</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.receipt_count}" pattern="0" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">大额补助支付金额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.lar_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
								
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">统筹金支付金额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.plan_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">公务员补助支付金额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.ser_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">个人帐户支付金额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.self_account_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">个人现金支付金额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.self_cash_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">结算前帐户余额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.b_balance}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">结算后帐户余额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.a_balance}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">预结算提示信息</td>
										<td align="left" width="35%" bgcolor="white">${resultModel.info}</td>
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