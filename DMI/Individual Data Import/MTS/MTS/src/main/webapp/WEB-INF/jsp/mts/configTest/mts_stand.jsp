<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
								<form action="mtsVisitType/${msg}.do" name="mtsVisitTypeAddForm" id="mtsVisitTypeAddForm" method="post">
									
									<div id="zhongxin" style="padding-top: 13px;">
									<table style="width:95%;">
										<tr>
											<td  style="padding-top: 20px;width:80px">批次:</td>
											<td  style="padding-top: 20px;">
												<input type="text" style="width:100%" name="PT_ID" id="PT_ID" value="${pd.PT_ID}"/>
											</td>
										</tr>
										<tr>
											<td  style="padding-top: 20px;">区域:</td>
											<td  style="padding-top: 20px;">
											<select  name="AREA" id="AREA" style="vertical-align:top;" style="width:98%;" >
												<option value="" >请选择区域</option>
												<c:forEach items="${areaList}" var="mtsArea" varStatus="vs">
													<c:if test="${mtsArea.AREA_CODE != null }">
													<option value="${mtsArea.AREA_CODE }" >${mtsArea.AREA_NAME }</option>
													</c:if>
												</c:forEach>
											</select>
											</td>
										</tr>
										<tr>
										<td  style="padding-top: 20px;">聚类:</td>
										<td  style="padding-top: 20px;">
											<select  name="classcode" id="classcode" onchange="changeClass(this.value)" style="vertical-align:top;" style="width:98%;" >
												<option value="" >请选择聚类</option>
												<c:forEach items="${dataClassList}" var="mtsDataClass">
													<c:if test="${mtsDataClass.DATA_CLASS_CODE != null }">
													    <option value="${mtsDataClass.DATA_CLASS_CODE }" >${mtsDataClass.DATA_CLASS_NAME }</option>
													</c:if>
												</c:forEach>
											</select>
										</td>
										</tr>
										<tr>
										<td style="text-align: center;padding-top: 10px;" colspan="3">
											<a class="btn btn-mini btn-primary" onclick="save();">确认标化</a>											
										</td>
									</tr>
									</table>
									</div>
									<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>
<script type="text/javascript">
	$(top.hangge());
	$(document).ready(function(){
		if($("#user_id").val()!=""){
			$("#loginname").attr("readonly","readonly");
			$("#loginname").css("color","gray");
		}
	});
	
	
	
	//保存
	function save(){
		
		var pt_id=document.getElementById("PT_ID").value;
		var AREA=document.getElementById("AREA").value;
		var classcode=document.getElementById("classcode").value;
		
		$.ajax({ 
			type: "post", 
			data : { "pt_id" : pt_id,"AREA":AREA ,"classcode":classcode} ,  
			url: '<%=basePath%>mtsConfigTest/standard.do', 
			cache:false, 
			async:true, 
			success: function(xmlobj){ 
				
			 } 
		});
	}
	

	
	$(function() {
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
				if(which == 2) 
					$('#form-field-select-4').addClass('tag-input-style');
				 else 
					$('#form-field-select-4').removeClass('tag-input-style');
			});
		}
	});
</script>
</html>