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
						<form action="mtsData/listMtsData.do" method="post" name="mtsDataForm" id="mtsDataForm">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<select  name="AREA_ID" id="area_" style="width:120px;"  onchange="changeArea(this.value);">
											<option value="">请选择区域</option>
											<c:forEach items="${areaList}" var="areas">
													<option value="${areas.AREA_CODE }" <c:if test="${areas.AREA_CODE == pd.AREA_ID}">selected</c:if>>${areas.AREA_NAME }</option>
											</c:forEach>
									</select>
	                            </td>
								<td style="padding-left:2px;">
									  <select  name="BATCH_NO" id="batchNo_"  style="width:120px;" >
												<option value="" >请选择批次号</option>									
												<c:forEach items="${batchList}" var="bat">
													<option value="${bat.BATCH_NO}" <c:if test="${bat.BATCH_NO == pd.BATCH_NO}">selected</c:if>>${bat.BATCH_NO}</option>
												</c:forEach>
									  </select>
								</td>
								
								<td style="padding-left:2px;">
									  <select  name="LOAD_FLAG" id="loadFlag_"  style="width:120px;" >
												<option value="" >加载状态</option>		
												<option value="0" <c:if test="${pd.LOAD_FLAG == 0}">selected</c:if>>未加载</option>							
												<option value="1" <c:if test="${pd.LOAD_FLAG == 1}">selected</c:if>>已加载</option>							
									  </select>
								</td>
								
								<td style="padding-left:2px;">
									  <select  name="DEL_FLAG" id="delFlag_"  style="width:120px;" >
												<option value="" >删除状态</option>									
												<option value="0" <c:if test="${pd.DEL_FLAG == 0}">selected</c:if>>未删除</option>							
												<option value="1" <c:if test="${pd.DEL_FLAG == 1}">selected</c:if>>已删除</option>	
									  </select>
								</td>
								<td style="vertical-align:top;padding-left:8px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="搜索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								<td>
									<a style="margin-left: 15px;margin-top: 2px;" class="btn btn-mini btn-success" onclick="fromExcel();" title="从EXCEL导入">导入本体数据</a>
									<a style="margin-left: 10px;margin-top: 2px;" class="btn btn-mini btn-success" onclick="loadRedisData();" title="加载Redis内存">加载内存本体</a>
									<a style="margin-left: 10px;margin-top: 2px;" class="btn btn-mini btn-success" onclick="reloadRedisData();" title="重载内存本体">重载内存本体</a>
									<a style="margin-left: 10px;margin-top: 2px;" class="btn btn-mini btn-success" onclick="reloadAllRedisData();" title="重载全部内存本体">重载全部内存本体</a>
									<a style="margin-left: 10px;margin-top: 2px;" class="btn btn-mini btn-success" onclick="delByBatch();" title="按批次删除">按批次删除本体</a>
								</td>
							</tr>
						</table>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">数据标准化类型</th>
									<th class="center">原始数据名称</th>
									<th class="center">所属区域</th>
									<th class="center">导入时间</th>
									<th class="center">批次号</th>
									<th class="center">是否删除</th>
									<th class="center">是否加载</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty listMtsData}">
									<c:forEach items="${listMtsData}" var="mtsData" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${mtsData.DATA_TYPE_NAME}</td>
											<td class="center">${mtsData.ORIG_DATA_NAME}</td>
											<td class="center">${mtsData.AREA_NAME}</td>
											<td class="center"><fmt:formatDate value="${mtsData.IMP_DATE}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class="center">${mtsData.BATCH_NO}</td>
											<td class="center">${mtsData.DEL_FLAG ==0?'未删除':'已删除'}</td>
											<td class="center">${mtsData.LOAD_FLAG ==0?'未加载':'已加载'}</td>
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
									<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
								</tr>
							</table>
						</div>
						
						<!-- <div id="">
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
								<thead>
									<tr>
										<th class="center" style="width:50px;">序号</th>
										<th class="center">数据标准化类型</th>
										<th class="center">原始数据名称</th>
										<th class="center">所属区域</th>
										<th class="center">导入时间</th>
										<th class="center">批次号</th>
										<th class="center">是否删除</th>
										<th class="center">是否加载</th>
									</tr>
								</thead>
							</table>
						</div> -->
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


//打开上传excel页面
<%-- function fromExcel(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="EXCEL 导入到数据库";
	 diag.URL = '<%=path%>/mtsData/goUploadExcel.do';
	 diag.Width = 500;
	 diag.Height = 400;
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
} --%>

//异步请求查询字典列表的方法并返回json数组 
function changeArea(v) {  
	   jQuery('#area_').val(v); 
	   var batchno = $('#batchNo_');
	   jQuery.ajax({  
	        url : '<%=basePath%>mtsData/selAllLoadBatch.do',  
	        type : 'post',  
	        data : { "AREA_ID":v } ,  
	        dataType : 'json',  
	        success : function (opts) {  
	 		     var batchList=opts.batchList;
	 		 	 batchno.html("<option value = ''>请选择批次号</option>");	
		 		 if(batchList!=null){
			            for(var i =0;i<batchList.length;i++) {
				             var item=batchList[i];
				             batchno.append("<option value = '"+item.BATCH_NO+"'>"+item.BATCH_NO+"</option>");				            
			            }
			     } else { 
			    	 //batchno.append("<option value = ''>请选择批次号</option>");	
			     } 
	        }
	    });  
	}

//打开上传excel页面
function fromExcel(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="EXCEL 导入到数据库";
	 diag.URL = '<%=path%>/mtsData/goUploadExcel.do';
	 diag.Width = 400;
	 diag.Height = 200;
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

function reloadRedisData(){
	top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="重载内存数据";
	 diag.URL = '<%=path%>/mtsData/goReloadRedisData.do';
	 diag.Width = 500;
	 diag.Height = 400;
	 diag.CancelEvent = function(){ //关闭事件
		 /* if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location.reload()",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		} */
		diag.close();
	 };
	 diag.show();
}

function loadRedisData(){
	top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="加载内存数据";
	 diag.URL = '<%=path%>/mtsData/goLoadRedisData.do';
	 diag.Width = 500;
	 diag.Height = 400;
	 diag.CancelEvent = function(){ //关闭事件
		 /* if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location.reload()",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		} */
		diag.close();
	 };
	 diag.show();
}

function reloadAllRedisData(){
	bootbox.confirm("确定要重载全部内存数据吗?", function(result) {
		$("#zhongxin").hide();
		$("#zhongxin2").show();
		if(result) {
			window.location.href='<%=path%>/mtsData/reloadAllRedisData.do'
		};
	});
	
}

//按批次删除
function delByBatch(){
	top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="按批次删除";
	 diag.URL = '<%=path%>/mtsData/goDelBatchData.do';
	 diag.Width = 500;
	 diag.Height = 400;
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

//查询
function searchs(){
	top.jzts();
	$("#mtsDataForm").submit();
}

//删除
function delUser(userId,msg){
	bootbox.confirm("确定要删除["+msg+"]吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=basePath%>user/deleteU.do?USER_ID="+userId+"&tm="+new Date().getTime();
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
	 diag.URL = '<%=path%>/matchRule/goAddMR.do';
	 diag.Width = 469;
	 diag.Height = 510;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}
		diag.close();
	 };
	 diag.show();
}

//修改
function editUser(user_id){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="资料";
	 diag.URL = '<%=path%>/user/goEditU.do?USER_ID='+user_id;
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
									nextPage(${page.currentPage});
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


function goGetResult(){
	window.location.href="${pageContext.request.contextPath}/loadRule/goGetMtsResult.do";
}
		
</script>
</html>
