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
						<!-- 检索  -->
						<form action="mtsDBData/listMtsDBDataResult2.do" method="post" name="mtsDBDataForm" id="mtsDBDataForm">
						<input type="hidden" name="DB_DATA_ID" id="DB_DATA_ID" value="${DB_DATA_ID}"/>
						<input type="hidden" name="BATCH_NUM" id="BATCH_NUM" value="${BATCH_NUM}"/>
						<input type="hidden" name="STATUS" id="STATUS" value="1"/>
						<input type="hidden" name="DB_DATA_TYPE" id="DB_DATA_TYPE" value="${DB_DATA_TYPE}"/>
						<%-- <table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
							</tr>
						</table> --%>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center" style="width:200px;">原始数据</th>
									<th class="center">NLP结果</th>
									<th class="center">西医名称</th>
									<th class="center">西医主码</th>
									<th class="center">西医附码</th>
									<th class="center">中医名称</th>
									<th class="center">中医编码</th>
									<th class="center">手术名称</th>
									<th class="center">手术编码</th>
									<th class="center">术语类型</th>
									<th class="center">怀疑诊断</th>
									<th class="center">特殊字符处理</th>
									<th class="center">无法标化-类型</th>
									<th class="center">标化状态</th>
								</tr>
							</thead>
													
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty listMtsDBdataDetail}">
									<c:forEach items="${listMtsDBdataDetail}" var="pd" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1+ pcount}</td>
											<td class="center">${pd.parametersStr}</td>
											<td class="center">${pd.NLP_RESULT}</td>
											<td class="center">${pd.XY_STANDARD_WORD}</td>
											<td class="center">${pd.XY_STANDARD_MAIN_CODE}</td>
											<td class="center">${pd.XY_STANDARD_ATTACH_CODE}</td>
											<td class="center">${pd.ZY_STANDARD_WORD}</td>
											<td class="center">${pd.ZY_STANDARD_MAIN_CODE}</td>
											<td class="center">${pd.SS_STANDARD_WORD}</td>
											<td class="center">${pd.SS_STANDARD_MAIN_CODE}</td>
											<td class="center">
												<c:if test="${pd.TERMINOLOGY_TYPE=='0'}">
													<span>西医</span>
												</c:if>
												<c:if test="${pd.TERMINOLOGY_TYPE=='1'}">
													<span>中医</span>
												</c:if>
												<c:if test="${pd.TERMINOLOGY_TYPE=='2'}">
													<span>手术</span>
												</c:if>
												
											</td>
											<td class="center">
												<c:if test="${pd.DOUBT_DIAG != '1'}">
													<span>-</span>
												</c:if>
												<c:if test="${pd.DOUBT_DIAG=='1'}">
													<span>Y</span>
												</c:if>
											</td>
											<td class="center">
												<c:if test="${pd.SPECIAL_CHARACTERS != '1'}">
													<span>-</span>
												</c:if>
												<c:if test="${pd.SPECIAL_CHARACTERS=='1'}">
													<span>Y</span>
												</c:if>
											</td>
											<td class="center">
												<c:if test="${pd.CAN_NOT_STANDARD_TYPE != '1'}">
													<span>-</span>
												</c:if>
												<c:if test="${pd.CAN_NOT_STANDARD_TYPE=='1'}">
													<span>Y</span>
												</c:if>
											</td>
											<td class="center">
												<c:if test="${pd.STATUS=='0'}">
													<span style='color:red;'>未匹配</span>
												</c:if>
												<c:if test="${pd.STATUS=='1'}">
													<span style='color:green;'>完全匹配</span>
												</c:if>
												<c:if test="${pd.STATUS=='2'}">
													<span style='color:red;'>无法标化</span>
												</c:if>
												<c:if test="${pd.STATUS=='3'}">
													<span style='color:red;'>人工标化</span>
												</c:if>
												<c:if test="${pd.STATUS=='4'}">
													<span style='color:red;'>AI处理</span>
												</c:if>
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="15" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						
					</form>
					<div class="page-header position-relative">
					<table style="width:100%;">
					
						<tr>
							<c:if test="${STATUS == 1 }">
								<td style="vertical-align:top;">
									<a class="btn btn-mini btn-success" onclick="exportExcel('${DB_DATA_ID}','${BATCH_NUM}');">导出结果</a>
								</td>
							</c:if>
							<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
						</tr>
					</table>
					</div>
	
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

//检索
function searchs(){
	top.jzts();
	$("#mtsDBDataForm").submit();
}

//删除
function delLRule(rule_id){
	bootbox.confirm("确定要删除此加载规则吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=basePath%>loadRule/deleteLR.do?rule_id="+rule_id;
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}

//新增
function add(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="新增";
	 diag.URL = '<%=path%>/mtsVisitType/goAddVisitType.do';
	 diag.Width = 469;
	 diag.Height = 510;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage('${page.currentPage}');
			 }
		}
		diag.close();
	 };
	 diag.show();
}

//修改
function editMtsVisitType(VISIT_TYPE_ID){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="修改就诊类型";
	 diag.URL = '<%=path%>/mtsVisitType/goEditVisitType.do?VISIT_TYPE_ID='+VISIT_TYPE_ID;
	 diag.Width = 469;
	 diag.Height = 510;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			nextPage(${page.currentPage});
		}
		diag.close();
	 };
	 diag.show();
}

//删除
function delMtsVisitType(VISIT_TYPE_ID,msg){
	bootbox.confirm("确定要删除["+msg+"]的就诊类型信息吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=basePath%>mtsVisitType/delMtsVisitType.do?VISIT_TYPE_ID="+VISIT_TYPE_ID;
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}


function exportExcel(DB_DATA_ID,BATCH_NUM){
	window.location.href='<%=basePath%>mtsDBData/exportExcel.do?DB_DATA_ID='+DB_DATA_ID+'&BATCH_NUM='+BATCH_NUM;
}

//批量操作
function makeAll(msg){
	bootbox.confirm(msg, function(result) {
		if(result) {
			var str = '';
			var emstr = '';
			var phones = '';
			var username = '';
			for(var i=0;i < document.getElementsByName('ids').length;i++)
			{
				  if(document.getElementsByName('ids')[i].checked){
				  	if(str=='') str += document.getElementsByName('ids')[i].value;
				  	else str += ',' + document.getElementsByName('ids')[i].value;
				  	
				  	if(emstr=='') emstr += document.getElementsByName('ids')[i].id;
				  	else emstr += ';' + document.getElementsByName('ids')[i].id;
				  	
				  	if(phones=='') phones += document.getElementsByName('ids')[i].alt;
				  	else phones += ';' + document.getElementsByName('ids')[i].alt;
				  	
				  	if(username=='') username += document.getElementsByName('ids')[i].title;
				  	else username += ';' + document.getElementsByName('ids')[i].title;
				  }
			}
			if(str==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				$("#zcheckbox").tips({
					side:3,
		            msg:'点这里全选',
		            bg:'#AE81FF',
		            time:8
		        });
				
				return;
			}else{
				if(msg == '确定要删除选中的数据吗?'){
					top.jzts();
					$.ajax({
						type: "POST",
						url: '<%=basePath%>user/deleteAllU.do?tm='+new Date().getTime(),
				    	data: {USER_IDS:str},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							 $.each(data.list, function(i, list){
									nextPage('${page.currentPage}');
							 });
						}
					});
				}else if(msg == '确定要给选中的用户发送邮件吗?'){
					sendEmail(emstr);
				}else if(msg == '确定要给选中的用户发送短信吗?'){
					sendSms(phones);
				}else if(msg == '确定要给选中的用户发送站内信吗?'){
					sendFhsms(username);
				}
			}
		}
	});
}

$(function() {
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

	
	//复选框全选控制
	var active_class = 'active';
	$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
		var th_checked = this.checked;//checkbox inside "TH" table header
		$(this).closest('table').find('tbody > tr').each(function(){
			var row = this;
			if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
			else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
		});
	});
});
</script>
</html>
