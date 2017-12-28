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
						<form action="mtsStandardize/standardizeDataPage.do" method="post" name="standardizeForm" id="standardizeForm">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
									<span class="input-icon">
										就诊ID:
										<input class="nav-search-input" autocomplete="off" id="visitId" type="text" name="visitId" value="${pd.visitId }" placeholder="这里输入关键词" />
									</span>
									</div>
								</td>
								<td style="padding-left:2px;">
									<div class="nav-search">
									<span class="input-icon">
										就诊类型:
										<select class="chosen-select form-control" name="visitType" id="visitType" data-placeholder="请选择就诊类型" style="vertical-align:top;width: 120px;">
										<option value="nbcs" selected>内部测试</option>
										<c:forEach items="${listMtsVisit }" var="mtsVisitType">
											<option value="${mtsVisitType.FLAG }" >${mtsVisitType.DESCRIPTION }</option> 
										</c:forEach>
									  	</select>
									</span>
									</div>
								</td>
								<td style="padding-left:2px;">
									<div class="nav-search">
									<span class="input-icon">
										数据来源:
										<select class="chosen-select form-control" name="dataSource" id="dataSource" data-placeholder="请选择数据来源" style="vertical-align:top;width: 120px;">
										<option value=""></option>
										<option value="nbcs" selected>内部测试</option>
										<c:forEach items="${listMtsDataSource }" var="mtsDataSource">
											<option value="${mtsDataSource.FLAG }" >${mtsDataSource.DESCRIPTION }</option> 
										</c:forEach>
									  	</select>
									</span>
									</div>
								</td>
								<td style="padding-left:2px;">
									<div class="nav-search">
									<span class="input-icon">
										聚类:
										<select class="chosen-select form-control" name="dataType" id="dataType" data-placeholder="请选择聚类" style="vertical-align:top;width: 120px;">
										<option value=""></option>
										<option value="02" selected>诊断</option>
									  	</select>
									</span>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									参数：
									<div>
										<!-- <textarea cols="125" rows="5" name="parameters" id="parameters" onfocus="getMouse(event);"/>
										</textarea> -->
										<textarea rows="5" cols="125" name="parameters" id="parameters" placeholder="这里输入参数" title="参数"  style="width:98%;">${pd.parameters}</textarea>
									</div>
								</td>
								<td style="vertical-align:bottom;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
							</tr>
							
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">标准化参数</th>
									<th class="center">标准化结果集</th>
									<th class="center">批次号</th>
									<th class="center">标化标识</th>
									<th class="center">生成时间</th>
									<th class="center">状态</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty listStanData}">
									<c:forEach items="${listStanData}" var="mtsQuestion" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${mtsQuestion.PARAMETERS}</td>
											<td class="center">${mtsQuestion.RESULT}</td>
											<td class="center">${mtsQuestion.BATCH_NUM}</td>
											<c:if test="${mtsQuestion.DATA_CLASS == 05 }">
												<td class="center"><span style="color: green">按词</span></td>
											</c:if>
											<c:if test="${mtsQuestion.DATA_CLASS == 09 }">
												<td class="center"><span style="color: green">按码</span></td>
											</c:if>
											<td class="center"><fmt:formatDate value="${mtsQuestion.OPERATE_TIME}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
											<c:if test="${mtsQuestion.STATUS == 1 }">
												<td class="center"><span style="color: green">成功</span></td>
											</c:if>
											<c:if test="${mtsQuestion.STATUS == 0 }">
												<td class="center"><span style="color: red">失败</span></td>
											</c:if>
										</tr>
									</c:forEach>
										<tr>
											<td colspan="10" class="center">您无权查看</td>
										</tr>
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
							<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
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
	if($("#visitId").val()==""){
		$("#visitId").tips({
			side:3,
            msg:'请输入就诊ID',
            bg:'#AE81FF',
            time:2
        });
		$("#visitId").focus();
		return false;
	}
	if($("#parameters").val()==""){
		$("#parameters").tips({
			side:3,
            msg:'请输入参数',
            bg:'#AE81FF',
            time:2
        });
		$("#parameters").focus();
		return false;
	}
	top.jzts();
	$("#standardizeForm").submit();
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
	 diag.URL = '<%=path%>/mtsDict/goAddDict.do';
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
function editMtsDict(DID){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="修改规则";
	 diag.URL = '<%=path%>/mtsDict/goEditDict.do?DID='+DID;
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

//导出excel
function toExcel(){
	var keywords = $("#nav-search-input").val();
	var lastLoginStart = $("#lastLoginStart").val();
	var lastLoginEnd = $("#lastLoginEnd").val();
	var ROLE_ID = $("#role_id").val();
	window.location.href='<%=basePath%>user/excel.do?keywords='+keywords+'&lastLoginStart='+lastLoginStart+'&lastLoginEnd='+lastLoginEnd+'&ROLE_ID='+ROLE_ID;
}

//打开上传excel页面
function fromExcel(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="EXCEL 导入到数据库";
	 diag.URL = '<%=path%>/user/goUploadExcel.do';
	 diag.Width = 300;
	 diag.Height = 150;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location.reload()",100);
			 }else{
				 nextPage('${page.currentPage}');
			 }
		}
		diag.close();
	 };
	 diag.show();
}	

//查看用户
function viewUser(USERNAME){
	if('admin' == USERNAME){
		bootbox.dialog({
			message: "<span class='bigger-110'>不能查看admin用户!</span>",
			buttons: 			
			{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
		});
		return;
	}
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="资料";
	 diag.URL = '<%=path%>/user/view.do?USERNAME='+USERNAME;
	 diag.Width = 469;
	 diag.Height = 380;
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}

function getMouse(event){
	var e = event.srcElement;
    var r =e.createTextRange();
    r.moveStart('character',e.value.length);
    r.collapse(true);
    r.select();
}
</script>
</html>
