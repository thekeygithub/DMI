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
											<a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('interDetail');">
												<font size="2" color="#7B7B7B"><b>接口数据详情</b></font>
											</a>
										</td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('userDetail');">用户信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('resultDetail');">计算结果信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('fundDetail');">基金分段信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('feeDetail');">费用汇总信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('overDetail');">超限明细信息</a></td>
										<td style="border-left:1px solid;border-left-color:#dddddd;" width="14%" class='center'><a href="javascript:void(0);" style="text-decoration:none" onclick="queryData('billDetail');">结算单据信息</a></td>
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
										
										<td align="center" width="15%" bgcolor="#FCFCFC">现金支付方式</td>
										<td align="left" width="35%" bgcolor="white">${varModel.pay_type_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">时间戳</td>
										<td align="left" width="35%" bgcolor="white">${varModel.time_stamp}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">银行卡信息</td>
										<td align="left" width="35%" bgcolor="white">${varModel.bank_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">医院名称</td>
										<td align="left" width="35%" bgcolor="white">${varModel.hosp_name}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">业务类型</td>
										<td align="left" width="35%" bgcolor="white">${varModel.busi_type_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">医院交易流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.visit_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">疾病编号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.dis_code}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">登记编号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.reg_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">病种审批号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.appr_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">疾病名称</td>
										<td align="left" width="35%" bgcolor="white">${varModel.dis_name}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">疾病描述</td>
										<td align="left" width="35%" bgcolor="white">${varModel.dis_desc}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">本次结算单据张数</td>
										<td align="left" width="35%" bgcolor="white">${varModel.bill_num}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">交易状态</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.deal_stat == '0'}">成功</c:if>
											<c:if test="${varModel.deal_stat != '0'}">失败</c:if>
										</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">错误信息</td>
										<td align="left" width="35%" bgcolor="white">${varModel.error}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">写社会保障卡结果</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.card_res == '0'}">不写或写卡成功</c:if>
											<c:if test="${varModel.card_res != '0'}">写卡错误</c:if>
										</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">扣银行卡结果</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.bank_res == '0'}">不扣或扣成功</c:if>
											<c:if test="${varModel.bank_res != '0'}">失败</c:if>
										</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">更新后IC卡数据</td>
										<td align="left" width="35%" bgcolor="white">${varModel.ic_data}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">超限提示标记</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.over_flag == '1'}">有提示信息</c:if>
											<c:if test="${varModel.over_flag != '1'}">无提示信息</c:if>
										</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">规定病种标志</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.spec_flag == '0'}">非特种病结算</c:if>
											<c:if test="${varModel.spec_flag == '1'}">特种病结算</c:if>
										</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">结算时间</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatDate value="${varModel.createdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">结算流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.final_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">要作废的结算交易号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.abo_deal_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">经办人</td>
										<td align="left" width="35%" bgcolor="white">${varModel.operator}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">是否重复退费</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.is_repet == '0'}">正常退费</c:if>
											<c:if test="${varModel.is_repet == '1'}">结算单已经被退</c:if>
										</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">退费交易流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.return_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">退费结算日期</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatDate value="${varModel.return_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">住院登记交易号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.reg_deal_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">本次结算明细条数</td>
										<td align="left" width="35%" bgcolor="white">${varModel.final_num}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">住院结算交易交流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.in_deal_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">住院登记流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.in_reg_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">明细序号列表</td>
										<td align="left" width="35%" bgcolor="white">${varModel.detail_list}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">未上传成功的明细序号列表</td>
										<td align="left" width="35%" bgcolor="white">${varModel.fail_detail_list}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">待查询用户交易类型号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.search_type_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">待查询的用户交易流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.search_deal_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">享受状态</td>
										<td align="left" width="35%" bgcolor="white">${varModel.have_stat_name}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">用户交易是否成功</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.is_ok == '0'}">成功</c:if>
											<c:if test="${varModel.is_ok == '-1'}">失败</c:if>
										</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">交易时间</td>
										<td align="left" width="35%" bgcolor="white"><fmt:formatDate value="${varModel.deal_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">交易结算流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.deal_final_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">交易处于阶段</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.deal_step == '0'}">已结算</c:if>
											<c:if test="${varModel.deal_step == '1'}">已确认</c:if>
										</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">交易类型</td>
										<td align="left" width="35%" bgcolor="white">${varModel.deal_type}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">医保交易流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.med_deal_no}</td>
									</tr>
									
									<tr>
										<td align="center" width="15%" bgcolor="#FCFCFC">交易流水号</td>
										<td align="left" width="35%" bgcolor="white">${varModel.deal_no}</td>
										
										<td align="center" width="15%" bgcolor="#FCFCFC">掌医事务结果</td>
										<td align="left" width="35%" bgcolor="white">
											<c:if test="${varModel.hos_res == '0'}">成功</c:if>
											<c:if test="${varModel.hos_res == '-1'}">失败</c:if>
										</td>
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