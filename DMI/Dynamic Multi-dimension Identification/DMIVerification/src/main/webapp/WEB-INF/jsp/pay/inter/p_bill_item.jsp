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
							
							<form action="interInfo/billItemDetail.do?B_ID=${pd.b_id}" name="interForm" id="interForm" method="post">
							
							<div id="zhongxin" style="padding-top: 13px;">
							
								<!-- 列表  -->
								<table id="simple-table" width="100%" class="table table-striped table-bordered table-hover" style="margin-top:5px;">
									<thead>
										<tr>
											<th width="7%" class="center">药品诊疗类型</th>
											<th width="6%" class="center">医院项目编码</th>
											<th width="6%" class="center">医院项目名称</th>
											<th width="6%" class="center">医院项目单位</th>
											<th width="6%" class="center">医院项目规格</th>
											<th width="6%" class="center">医院项目剂型</th>
											<th width="6%" class="center">处方号</th>
											<th width="6%" class="center">医院项目单价</th>
											<th width="6%" class="center">贴数</th>
											<th width="6%" class="center">数量</th>
											<th width="6%" class="center">用药天数</th>
											<th width="6%" class="center">总金额</th>
											<th width="7%" class="center">超限自费标志</th>
											<th width="6%" class="center">中心编码</th>
											<th width="7%" class="center">医院项目生产厂家</th>
											<th width="7%" class="center">医院项目转换比</th>
										</tr>
									</thead>
									
									<tbody>
									<!-- 开始循环 -->	
										<c:choose>
											<c:when test="${not empty varList}">
												<c:forEach items="${varList}" var="billItemList" varStatus="vs">
													<tr>
														<td width="7%" class="center">${billItemList.type_name}</td>
														<td width="6%" class="center">${billItemList.code}</td>
														<td width="6%" class="center">${billItemList.name}</td>
														<td width="6%" class="center">${billItemList.unit}</td>
														<td width="6%" class="center">${billItemList.spec}</td>
														<td width="6%" class="center">${billItemList.form}</td>
														<td width="6%" class="center">${billItemList.recipe_no}</td>
														<td width="6%" class="center"><fmt:formatNumber type="number" value="${billItemList.price}" pattern="0.00" maxFractionDigits="2"/></td>
														<td width="6%" class="center">${billItemList.p_num}</td>
														<td width="6%" class="center">${billItemList.num}</td>
														<td width="6%" class="center">${billItemList.use_day}</td>
														<td width="6%" class="center"><fmt:formatNumber type="number" value="${billItemList.fee}" pattern="0.00" maxFractionDigits="2"/></td>
														<td width="7%" class="center">
															<c:if test="${billItemList.over_flag == '0'}">正常</c:if>
															<c:if test="${billItemList.over_flag == '1'}">超限自费</c:if>
														</td>
														<td width="6%" class="center">${billItemList.cen_code}</td>
														<td width="7%" class="center">${billItemList.com}</td>
														<td width="7%" class="center">${billItemList.pack}</td>
													</tr>
												</c:forEach>
											</c:when>
										
											<c:otherwise>
												<tr class="main_info">
													<td colspan="100" class="center">没有相关数据</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
								
								<!-- 分页 -->
								<div class="page-header position-relative">
									<table style="width:100%;">
										<tr>
											<td style="vertical-align:top;">
												<div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div>
											</td>
										</tr>
									</table>
								</div>
				
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
</script>

</html>