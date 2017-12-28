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
	<%@ include file="../../../system/index/top.jsp"%>
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
							
							<form action="" name="form1" id="form1" method="post">
							
								<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover" width="100%">
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">数据类型</td>
											<td align="left" width="35%" bgcolor="white">
												<c:if test="${varModel.data_type == '1'}">总额对账</c:if>
												<c:if test="${varModel.data_type == '2'}">明细对账</c:if>
											</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">业务接口编号</td>
											<td align="left" width="35%" bgcolor="white">${varModel.api_type}</td>
										</tr>
									
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">请求流水号</td>
											<td align="left" width="35%" bgcolor="white">${varModel.req_no}</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">时间戳</td>
											<td align="left" width="35%" bgcolor="white">${varModel.time_stamp}</td>
										</tr>
										
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">机构名称</td>
											<td align="left" width="35%" bgcolor="white">${varModel.group_name}</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">医院名称</td>
											<td align="left" width="35%" bgcolor="white">${varModel.hos_name}</td>
										</tr>
										
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">结算时间</td>
											<td align="left" width="35%" bgcolor="white">
												<fmt:formatDate value="${varModel.final_date}" pattern="yyyy-MM-dd HH:mm:ss"/>
											</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">支付总数</td>
											<td align="left" width="35%" bgcolor="white">${varModel.pay_num}</td>
										</tr>
										
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">确认交易成功记录数</td>
											<td align="left" width="35%" bgcolor="white">${varModel.confirm_num}</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">退费记录数</td>
											<td align="left" width="35%" bgcolor="white">${varModel.return_num}</td>
										</tr>
										
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">费用总额</td>
											<td align="left" width="35%" bgcolor="white">
												<fmt:formatNumber type="number" value="${varModel.total_fee}" pattern="0.00" maxFractionDigits="2"/>
											</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">自费总额(非医保)</td>
											<td align="left" width="35%" bgcolor="white">
												<fmt:formatNumber type="number" value="${varModel.self_pay}" pattern="0.00" maxFractionDigits="2"/>
											</td>
										</tr>
										
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">药品乙类自负</td>
											<td align="left" width="35%" bgcolor="white">
												<fmt:formatNumber type="number" value="${varModel.self_neg}" pattern="0.00" maxFractionDigits="2"/>
											</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">医保费用</td>
											<td align="left" width="35%" bgcolor="white">
												<fmt:formatNumber type="number" value="${varModel.med_fee}" pattern="0.00" maxFractionDigits="2"/>
											</td>
										</tr>
										
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">合计报销金额</td>
											<td align="left" width="35%" bgcolor="white">
												<fmt:formatNumber type="number" value="${varModel.return_fee}" pattern="0.00" maxFractionDigits="2"/>
											</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">合计现金支付</td>
											<td align="left" width="35%" bgcolor="white">
												<fmt:formatNumber type="number" value="${varModel.cash_total}" pattern="0.00" maxFractionDigits="2"/>
											</td>
										</tr>
										
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">对账类型</td>
											<td align="left" width="35%" bgcolor="white">
												<c:if test="${varModel.check_type == '0'}">门诊和住院</c:if>
												<c:if test="${varModel.check_type == '1'}">门诊</c:if>
												<c:if test="${varModel.check_type == '2'}">住院</c:if>
											</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">医院交易流水号</td>
											<td align="left" width="35%" bgcolor="white">${varModel.visit_no}</td>
										</tr>
										
										<tr>
											<td align="center" width="15%" bgcolor="#FCFCFC">结算流水号</td>
											<td align="left" width="35%" bgcolor="white">${varModel.final_no}</td>
											
											<td align="center" width="15%" bgcolor="#FCFCFC">数据时间</td>
											<td align="left" width="35%" bgcolor="white">
												<fmt:formatDate value="${varModel.data_date}" pattern="yyyy-MM-dd HH:mm:ss"/>
											</td>
										</tr>
									</table>
								</div>
							
								<div style="text-align: center;">
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">关&nbsp;&nbsp;闭</a>
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
	<%@ include file="../../../system/index/foot.jsp"%>
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
</script>

</html>