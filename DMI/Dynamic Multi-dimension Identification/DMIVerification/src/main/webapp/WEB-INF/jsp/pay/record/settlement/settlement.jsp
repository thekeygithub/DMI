<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ts" uri="/rights"%>
<%@ taglib uri="/tags/lf" prefix="lf"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html lang="en">

<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css"/>
	<!-- jsp文件头和头部 -->
	<%@ include file="../../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css"/>
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
						
						<!-- 检索  -->
						<form action="settlement/exec.do?flag=1" method="post" name="form1" id="form1">
							<p>注：“开始时间” 应小于等于 “结束时间” ，时间相等时，表示执行当天结算！</p>
							<table style="margin-top:5px;">
								<tr>
									<td style="padding-left:5px;">
										<input class="span10 date-picker" name="final_date_start" id="final_date_start" value="${final_date_start}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="结算开始日期" title="结算开始日期"/>
									</td>
									<td style="padding-left:5px;">
										<input class="span10 date-picker" name="final_date_end" id="final_date_end" value="${final_date_end}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="结算结束日期" title="结算结束日期"/>
									</td>
									<td>
										<select class="chosen-select form-control" name="settlementType" id="settlementType" data-placeholder="对账类型" style="width:130px;">
											<option value=""></option>
											<option value="all" <c:if test="${settlementType=='all'}">selected</c:if>>全部</option>
											<option value="mz" <c:if test="${settlementType=='mz'}">selected</c:if>>门诊</option>
											<option value="zy" <c:if test="${settlementType=='zy'}">selected</c:if>>住院</option>
											<option value="yd" <c:if test="${settlementType=='yd'}">selected</c:if>>药店</option>
										</select>
									</td>
									<td>
										<select class="chosen-select form-control" name="hospCode" id="hospCode" data-placeholder="医院" style="width:130px;">
											<option value=""></option>
											<option value="all" <c:if test="${hospCode=='all'}">selected</c:if>>全部</option>
											<c:if test="${not empty hospList}">
													<c:forEach items="${hospList}" var="hosp" varStatus="vs">
														<option value="${hosp.HOSP_ID}" <c:if test="${hospCode==hosp.HOSP_ID}">selected</c:if> >${hosp.HOSP_NAME}</option>
													</c:forEach>
											</c:if>
										</select>
									</td>
									<td>
										<select class="chosen-select form-control" name="groupId" id="groupId" data-placeholder="机构" style="width:130px;">
											<option value=""></option>
											<option value="all" <c:if test="${groupId=='all'}">selected</c:if>>全部</option>
											<c:if test="${not empty groupList}">
													<c:forEach items="${groupList}" var="group" varStatus="vs">
														<option value="${group.GROUP_ID}" <c:if test="${groupId==group.GROUP_ID}">selected</c:if> >${group.GROUP_NAME}</option>
													</c:forEach>
											</c:if>
										</select>
									</td>
									<td style="vertical-align:top;padding-left:5px;">
										<!-- <a class="btn btn-light btn-xs" onclick="searchs();" title="检索">
											<i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i>
										</a> -->
										<div class="btn-group">
											<a class="btn btn-xs btn-purple" title="执行结算" onclick="searchs();" style="font-size: 15px;">执行结算</a>
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="3" style="color: red;">${msg}</td>
								</tr>
							</table>	
															
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
		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../../system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	
</body>
 	
<script type="text/javascript">
	$(top.hangge());
	
	$(function() {
		//提示框标签
		$("[data-toggle='tooltip']").tooltip();
		//日期框
		$('.date-picker').datepicker({autoclose: true,todayHighlight: true});	
		//下拉框
		if(!ace.vars['touch']) {
			$('.chosen-select').chosen({allow_single_deselect:true}); 
			$(window)
			.off('resize.chosen')
			.on('resize.chosen', function() {
				$('.chosen-select').each(function() {
					 var $this = $(this);
					 $this.next().css({'width': $this.parent().width()});
				});
			}).trigger('resize.chosen');
			$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
				if(event_name != 'sidebar_collapsed') return;
				$('.chosen-select').each(function() {
					 var $this = $(this);
					 $this.next().css({'width': $this.parent().width()});
				});
			});
			$('#chosen-multiple-style .btn').on('click', function(e){
				var target = $(this).find('input[type=radio]');
				var which = parseInt(target.val());
				if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
				 else $('#form-field-select-4').removeClass('tag-input-style');
			});
		}
	});
		
	//检索
	function searchs(){
		top.jzts();
		$("#form1").submit();
	}
	
</script>

</html>