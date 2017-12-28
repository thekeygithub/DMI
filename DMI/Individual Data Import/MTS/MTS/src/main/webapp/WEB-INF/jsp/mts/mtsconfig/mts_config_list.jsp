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
						<form action="mtsConfigDR/configFirst.do" method="post" name="mtsDBDataForm" id="mtsDBDataForm">
						<table style="margin-top:5px;">
							<tr>
								<td style="padding-left:2px;">
									
								  <select  name="FLAG" id="FLAG"  style="width:150px;" >
									<option value="" >选择数据来源</option>									
									<c:forEach items="${listDataSource}" var="DATA_SOURCE">
										<c:if test="${pd.FLAG == null }">
											<option value="${DATA_SOURCE.FLAG }" ></option>
										</c:if>
										<c:if test="${pd.FLAG != null }">
										    <option value="${DATA_SOURCE.FLAG }" <c:if test="${DATA_SOURCE.FLAG == pd.FLAG }">selected</c:if>>${DATA_SOURCE.DESCRIPTION }</option>
										</c:if>
									</c:forEach>
								  </select>
								</td>								
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
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
									<th class="center">数据来源</th>
									<th class="center">批次名称</th>
									<th class="center">批次编号</th>
									<th class="center">标准化库</th>	
									<th class="center">数据类型</th>	
									<th class="center">导入时间</th>
									<th class="center">状态</th>
									<th class="center">操作</th>
								</tr>
							</thead>
													
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty listConfigFirst}">
									<c:forEach items="${listConfigFirst}" var="pd" varStatus="vs">
										<tr>
											
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${pd.DESCRIPTION}</td>
											<td class="center">${pd.PT_NAME}</td>
											<td class="center">${pd.PT_ID}</td>
											<td class="center">${pd.AREA_NAME}</td>
											<td class="center">${pd.DATA_CLASS_NAME}</td>
											<td class="center">${pd.CRE_DATE}</td>
											<td class="center" style="width:100px;">
											<c:if test="${pd.STAU == 1 }">
														<span style="color: green">已标化</span>
											</c:if>
											<c:if test="${pd.STAU == 0 }">
														<span style="color: red">未标化</span>
											</c:if>
											
											<%--<c:if test="${pd.STAU == 2 }">
														<span style="color: red">标化中</span>
												</c:if> --%>
											</td>											
										
											<td class="center">
												<div class="hidden-sm hidden-xs btn-group">
													<a class="btn btn-mini btn-purple" onclick="standData('${pd.PT_ID}','${pd.AREA_CODE }','${pd.DATA_CLASS_CODE }');">
														<i class="icon-pencil"></i>标准化
													</a>
													<a class="btn btn-mini btn-success" onclick="viewResult('${pd.PT_ID}','${pd.AREA_CODE }','${pd.DATA_CLASS_CODE }');">
														<i class="icon-pencil"></i>详细
													</a>													
													<a class="btn btn-xs btn-danger" onclick="delZDData('${pd.PT_ID}');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
													<%-- <a class="btn btn-mini btn-success" onclick="showP('${pd.DB_DATA_ID}_status','${pd.BATCH_NUM}','${pd.STATUS}');">
														<i class="icon-pencil"></i>显示
													</a> --%>
												</div>
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="11" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						
					<div class="page-header position-relative">
					<table style="width:100%;">
						<tr>
							<td style="vertical-align:top;">
								<a class="btn btn-mini btn-success" onclick="importExcel(${pd.DB_DATA_TYPE});">导入数据</a>
								<a class="btn btn-mini btn-success" onclick="downloadTemplate();">下载模板</a>
							</td>
							<!-- <td style="vertical-align:top;">
								<a class="btn btn-mini btn-success" onclick="downloadTemplate();">下载模板</a>
							</td> -->
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


function showP(DB_DATA_ID,BATCH_NUM){
	var toolbarDivContent = '<div class="progress progress-striped active"><div class="progress-bar progress-bar-success" style="width:80%;text-align:center;" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"><span style="color:yellow;font-weight: bold;text-align:center;">41%</span></div></div>';
	$("#"+DB_DATA_ID).html(toolbarDivContent);
}

//页面进度刷新定时器
//$(function(){ 
//	var t1 = window.setInterval(updateStatus,10000000); 
//});

function updateStatus(){
	var DB_DATA_ID_STRING = '';
	$("div[name='status_flag']").each(function(){
		var DB_DATA_ID_STR = this.id;
		if(DB_DATA_ID_STR.indexOf("_") != -1){
			var DB_DATA_ID = DB_DATA_ID_STR.substring(0,DB_DATA_ID_STR.indexOf("_"))
			if(DB_DATA_ID_STRING == ''){
				DB_DATA_ID_STRING = DB_DATA_ID;
			}else{
				DB_DATA_ID_STRING = DB_DATA_ID_STRING + "@" + DB_DATA_ID;
			}
		}
	});
	if(DB_DATA_ID_STRING != ''){
		$.ajax({ 
			type: "post", 
			url: "<%=basePath%>mtsDBData/updateStatus.do?DB_DATA_ID_STRING="+DB_DATA_ID_STRING, 
			cache:false, 
			async:false, 
			success: function(result){ 
				if(result != ''){
					var jsondata = JSON.parse(result);
					var db_data = DB_DATA_ID_STRING.split('@');
					for(var j = 0;j < db_data.length;j++){
						var resId = db_data[j];
						var status = jsondata[resId]['status'];
						var percentage = jsondata[resId]['percentage'];
						var statusDivContent = "";
						if(status == '0'){
							statusDivContent = '<span style="color: red">未标化</span>';
						}else if(status == '1'){
							statusDivContent = '<span style="color: green">已标化</span>';
						}else if(status == '2'){
							if(percentage == '100'){
								statusDivContent = '<span style="color: green">已标化</span>';
							}else{
								statusDivContent = "<div class='progress progress-striped active'><div class='progress-bar progress-bar-success' style='width:"+percentage+"%;text-align:center;' role='progressbar' aria-valuenow='"+percentage+"' aria-valuemin='0' aria-valuemax='100'><span style='color:red;font-weight: bold;text-align:center;'>"+percentage+"%</span></div></div>";
							}
						}
						$("#"+resId+"_status").html(statusDivContent);
					}
				}
			} 
		});
	}
}
//检索
function searchs(){	
	top.jzts();
	$("#mtsDBDataForm").submit();
}


function viewResult(PT_ID,AREA_CODE,CLASS_CODE){	
	top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.URL = '<%=path%>/mtsConfigDR/listMtsCongfigDetail.do?PT_ID='+PT_ID+'&AREA_CODE='+AREA_CODE+'&CLASS_CODE='+CLASS_CODE;
	 diag.Title ="标化结果详细";
	 diag.Width = $(top.window).width();
	 diag.Height = $(top.window).height();
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();
}


function downloadTemplate(){
	window.location.href='<%=basePath%>mtsDBData/downloadTemplate.do';
}

//打开上传excel页面
function importExcel(DB_DATA_TYPE){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="导入数据文件EXCEL";
	 diag.URL = '<%=path%>/mtsConfigDR/goUploadExcel.do';
	 diag.Width = 1000;
	 diag.Height = 800;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location.reload()",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}
		diag.close();
	 };
	 diag.show();
}	

function exportExcel(DB_DATA_ID,BATCH_NUM){
	window.location.href='<%=basePath%>mtsDBData/exportExcel.do?DB_DATA_ID='+DB_DATA_ID+'&BATCH_NUM='+BATCH_NUM;
}

function standData(PT_ID,AREA,classcode){
	$.ajax({ 
		type: "post", 
		url: "<%=basePath%>mtsConfigDR/standard.do?pt_id="+PT_ID+"&AREA="+AREA+"&classcode="+classcode, 
		cache:false, 
		async:true, 
		success: function(xmlobj){ 
			
		 } 
	});

}	

//删除
function delZDData(PT_ID){
	bootbox.confirm("确定要删除批次["+PT_ID+"]的数据信息吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=path%>/mtsConfigDR/delZDData.do?PT_ID="+PT_ID;
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
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
