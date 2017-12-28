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
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center' bgcolor="#F0F0F0">
											<a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('userDetail');">
												<font size="2" color="#7B7B7B"><b>用户信息</b></font>
											</a>
										</td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('resultDetail');">计算结果信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('fundDetail');">基金分段信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('feeDetail');">费用汇总信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('overDetail');">超限明细信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('billDetail');">结算单据信息</a></td>
									</tr>
								</table>
							
								<table id="table_report" class="table table-striped table-bordered table-hover" width="100%">
									<!--
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">业务接口编号</td>
										<td align="left" width="35%" bgcolor="white">${userModel.api_type}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">请求流水号</td>
										<td align="left" width="35%" bgcolor="white">${userModel.req_no}</td>
									</tr>
									-->
								
									<tr>
										<!--
										<td align="center" width="15%" bgcolor="#FCFCFC">时间戳</td>
										<td align="left" width="35%" bgcolor="white">${userModel.time_stamp}</td>
										-->
										<td align="center" width="15%" bgcolor="#FCFCFC">卡号</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.med_card}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">身份证</td>
										<td align="left" width="35%" bgcolor="white">${userModel.id_card}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">市民卡号</td>
										<td align="left" width="35%" bgcolor="white">${userModel.card_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">识别码</td>
										<td align="left" width="35%" bgcolor="white">${userModel.ic_card}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">用户姓名</td>
										<td align="left" width="35%" bgcolor="white">${userModel.name}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">卡状态</td>
										<td align="left" width="35%" bgcolor="white">${userModel.card_stat_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">银行卡信息</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.bank_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">读卡方式</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.read_type}</td>
									</tr>
									
									<!--
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">医院名称</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.hosp_name}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">卡号</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.med_card}</td>
									</tr>
									-->
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">个人社保编号</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.insu_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">性别</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.sex_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">民族</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.nation}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">出生日期</td>
										<td align="left" width="35%" bgcolor="white">
											${insuModel.birth}
<%-- 											<fmt:formatDate value="${insuModel.birth}" pattern="yyyy-MM-dd"/> --%>
										</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">单位性质</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.com}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">单位名称/家庭地址</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.name_addr}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">地区编码</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.area_code}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">地区名称</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.area_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">医保待遇（政策）类别，结算依据</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.med_name}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">荣誉类别</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.honor_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">低保类别</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.low_name}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">优抚级别</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.priv_rank_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">特殊病标志</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${insuModel.spec_flag == '1'}">中心有特殊病登记</c:if>
											<c:if test="${insuModel.spec_flag == '0'}">没有登记</c:if>
										</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">特殊病编码</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.spec_code}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">当年帐户余额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.i_balance}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">历年帐户余额</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.o_balance}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">当年住院医保累计</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.out_total}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">当年门诊医保累计</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.in_total}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">当年规定病医保累计</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.dis_total}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">当年累计列入统筹基数</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.whole}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">当年统筹基金支付累计</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.whole_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">当年补充保险支付累计</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.insu_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">当年公务员补助支付累计</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.ser_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">当年企事业补助支付累计</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.com_pay}" pattern="0.00" maxFractionDigits="2"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">当年专项基金支付累计</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatNumber type="number" value="${insuModel.spec_pay}" pattern="0.00" maxFractionDigits="2"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">当年住院次数</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.in_count}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">工伤认定部位</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.part}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">医疗小险种</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.little_insu_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">交易状态</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${insuModel.deal_stat == '0'}">成功</c:if>
											<c:if test="${insuModel.deal_stat != '0'}">失败</c:if>
										</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">错误信息</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.error}
<%-- 											<c:if test="${insuModel.error == '0'}">不写或写卡成功</c:if> --%>
<%-- 											<c:if test="${insuModel.error != '0'}">写卡错误</c:if> --%>
										</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">写社会保障卡结果</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${insuModel.card_res == '0'}">不写或写卡成功</c:if>
											<c:if test="${insuModel.card_res != '0'}">写卡错误</c:if>
										</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">扣银行卡结果</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${insuModel.bank_res == '0'}">不扣或扣成功</c:if>
											<c:if test="${insuModel.bank_res != '0'}">失败</c:if>
										</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">更新后IC卡数据</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.ic_data}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">医疗身份验证结果</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.med_res}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">工伤身份验证结果</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.hurt_res}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">生育身份验证结果</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.proc_res}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">市民卡社会保障卡号</td>
										<td align="left" width="35%" bgcolor="white">${insuModel.card_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">&nbsp;</td>
										<td align="left" width="35%" bgcolor="white">&nbsp;</td>
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