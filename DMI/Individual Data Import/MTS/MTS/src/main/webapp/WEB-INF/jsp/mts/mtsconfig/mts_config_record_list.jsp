<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
							
								<!-- #section:elements.tab.option -->
								<div class="tabbable">
									<ul class="nav nav-tabs padding-12 tab-color-blue background-blue" id="myTab4">
										
										<li >
											<a data-toggle="tab" href="#home4">标准化结果查询</a>
										</li>
										
										<li class="active">
											<a data-toggle="tab" href="#upConfig4">自动标化结果维护</a>
										</li>

										<li>
											<a data-toggle="tab" href="#profile4">问题单维护</a>
										</li>
									</ul>

									<div class="tab-content" style="min-height:500px;" >
										<div id="#upConfig4" class="tab-pane in active" style="min-height:500px;">
											<iframe name="treeFrame" id="treeFrame" width="100%" style="min-height: 500px;" frameborder="0" src="<%=basePath%>mtsConfigDR/listMtsRecord.do?PT_ID=${PT_ID}&TYPE=1&AREA_CODE=${AREA_CODE}&CLASS_CODE=${CLASS_CODE}" ></iframe>
										</div>
										<!--<div id="profile4" class="tab-pane" style="min-height:500px;">
											<iframe name="treeFrame1" id="treeFrame1" width="100%" style="min-height: 500px;" frameborder="0" src="<%=basePath%>mtsQuestion/listMtsQuestionForDBData.do?BATCH_NUM=${BATCH_NUM}" ></iframe>
										</div>-->
									</div>
								</div>
								<!-- /section:elements.tab.option -->
									
							<!-- <ul class="nav nav-tabs" id="myTab2">
								<li class="active">
									<a data-toggle="tab" href="mtsDBData/listMtsDBDataResult.do">标化结果</a>
								</li>
								<li>
									<a data-toggle="tab" href="#profile2">问题单</a>
								</li>
							</ul> -->
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
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
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
	
});
</script>
</html>
