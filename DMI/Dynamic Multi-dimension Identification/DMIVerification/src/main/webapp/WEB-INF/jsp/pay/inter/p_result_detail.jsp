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
										<td width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('interDetail');">接口数据详情</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('userDetail');">用户信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center' bgcolor="#F0F0F0">
											<a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('resultDetail');">
												<font size="2" color="#7B7B7B"><b>计算结果信息</b></font>
											</a>
										</td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('fundDetail');">基金分段信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('feeDetail');">费用汇总信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('overDetail');">超限明细信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('billDetail');">结算单据信息</a></td>
									</tr>
								</table>
							
								<table id="table_report" class="table table-striped table-bordered table-hover" width="100%">
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">费用总额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.total_fee}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">自费总额(非医保)</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.self_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
								
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">药品乙类自负</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.self_neg}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">医保费用</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.med_fee}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">转院自费</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.tran_fee}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">起付线</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.level_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">个人自费现金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.self_cash_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">个人自负现金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.neg_cash_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">合计现金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.cash_total}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">合计报销金额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.return_fee}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">历年帐户支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.i_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">当年帐户支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.o_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">职保统筹基金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.whole_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">补充保险支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.add_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">公务员补助支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.ser_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">单位补助支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.com_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">医疗救助支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.salv_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">离休基金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.retire_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">二乙基金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.fund_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">劳模医疗补助支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.model_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">民政救助支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.civil_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">优抚救助支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.priv_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">残联基金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.dpf_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">计生基金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.plan_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">工伤基金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.hurt_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">生育基金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.proc_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">结算后当年个帐余额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.i_balance}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">结算后历年个帐余额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.o_balance}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">合保统筹基金支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.insu_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">合保大病救助支付</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.insu_salv_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">慈善基金</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.charity}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">共济账户支出</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${resultModel.mutual}" pattern="0.00" maxFractionDigits="2"/></td>
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