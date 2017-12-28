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
								<div id="zhongxin" style="padding-top: 1px;">
								<form action="mtsToolkit/goConfigTool.do" method="post" name="toolSearchForm" id="toolSearchForm">
									
									<input type="hidden" name="TOOLKIT_ID" id="TOOLKIT_ID" value="${pd.TOOLKIT_ID }"/>
									<input type="hidden" name="TOOLKIT_NAME" id="TOOLKIT_NAME" value="${pd.TOOLKIT_NAME }"/>
									<table style="margin-top:5px;">
										<tr>
											<!--<td>
												<div>
												<span class="input-icon">
													<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词" />
													<i class="ace-icon fa fa-search nav-search-icon"></i>
												</span>
												</div>
											</td>
											<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
										  -->	
										</tr>
									</table>
									<!-- 检索  -->
								
									<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
										<thead>
											<tr>
												<!-- <th class="center" style="width:35px;">
												<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
												</th> -->
												<th class="center" style="width:50px;">序号</th>
												<th class="center">工具顺序</th>
												<th class="center">工具名称</th>
												<th class="center">工具路径</th>
												<th class="center">备注</th>
												<th class="center">操作</th>
											</tr>
										</thead>
																
										<tbody>
											
										<!-- 开始循环 -->	
										<c:choose>
											<c:when test="${not empty toolList}">
												<c:forEach items="${toolList}" var="tool" varStatus="vs">
													<tr>
														<td class='center' style="width: 30px;">${vs.index+1}</td>
														<td class="center">${tool.TOOL_ORDER}</td>
														<td class="center">${tool.TOOL_NAME}</td>
														<td class="center">${tool.TOOL_PATH}</td>
														<td class="center">${tool.COMMENTS }</td>
														<td class="center">
															<a class="btn btn-xs btn-success" title="编辑" onclick="editToolrel('${tool.TOOL_REL_ID}','${pd.TOOLKIT_NAME}','${tool.TOOL_NAME}');">
																<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
															</a>
															<a class="btn btn-xs btn-danger" onclick="delToolrel('${tool.TOOL_REL_ID}','${pd.TOOLKIT_NAME}','${tool.TOOL_NAME}');">
																<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
															</a>
														</td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr class="main_info">
													<td colspan="10" class="center">没有相关数据</td>
												</tr>
											</c:otherwise>
										</c:choose>
										</tbody>
									</table>
										
									<div class="page-header position-relative">
									<table style="width:100%;">
										<tr>
											<td style="vertical-align:top;">
												<a class="btn btn-mini btn-success" onclick="addToolrel('${pd.TOOLKIT_ID}','${pd.TOOLKIT_NAME}');">新增</a>
											</td>
											<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
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
	//检索
	function searchs(){
		top.jzts();
		$("#toolSearchForm").submit();
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
	
	//新增
	function addToolrel(TOOLKIT_ID,TOOLKIT_NAME){
		 top.jzts();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="新增工具包["+TOOLKIT_NAME+"]关联的工具信息";
		 diag.URL = '<%=path%>/mtsToolkit/goAddToolrel.do?TOOLKIT_ID='+TOOLKIT_ID;
		 diag.Width = 469;
		 diag.Height = 180;
		 diag.CancelEvent = function(){ //关闭事件
			 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 if('${page.currentPage}' == '0'){
					 top.jzts();
					 setTimeout("self.location=self.location",100);
				 }else{
					 searchs();
				 }
			}
			diag.close();
		 };
		 diag.show();
	}

	//修改
	function editToolrel(TOOL_REL_ID,TOOLKIT_NAME,TOOL_NAME){
		 top.jzts();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="修改 此工具包关联的工具["+TOOL_NAME+"]信息";
		 diag.URL = '<%=path%>/mtsToolkit/goEditToolrel.do?TOOL_REL_ID='+TOOL_REL_ID;
		 diag.Width = 469;
		 diag.Height = 180;
		 diag.CancelEvent = function(){ //关闭事件
			 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 if('${page.currentPage}' == '0'){
					 top.jzts();
					 setTimeout("self.location=self.location",100);
				 }else{
					 searchs();
				 }
			}
			diag.close();
		 };
		 diag.show();
	}

	//删除
	function delToolrel(TOOL_REL_ID,TOOLKIT_NAME,TOOL_NAME){
		bootbox.confirm("确定要删除此工具包中的["+TOOL_NAME+"]工具信息吗?", function(result) {
			if(result) {
				top.jzts();
	   			var url = "<%=basePath%>mtsToolkit/delToolrel.do?TOOL_REL_ID="+TOOL_REL_ID;
	   			$.get(url,function(data){
	   				if('${page.currentPage}' == '0'){
						 top.jzts();
						 setTimeout("self.location=self.location",100);
					 }else{
						 searchs();
					 }
	   			});
			};
		});
	}

</script>
</html>