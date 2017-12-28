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
<style type="text/css" >
	.overlay {
	    position: absolute;
	    top: 0px;
	    left: 0px;
	    z-index: 10001;
	    display:none;
	    filter:alpha(opacity=60);
	    background-color: #777;
	    opacity: 0.5;
	    -moz-opacity: 0.5;
	}
</style>
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
						<form action="mtsCorpus/listMtsCorpusViewPage.do" method="post" name="corpusAssertForm" id="corpusAssertForm">
						<input type='hidden' name="accsessType" id="accsessType_" value="3"/>
						<table style="margin-top:5px;">
							<tr>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="START_DATE" id="START_DATE_"  value="${pd.START_DATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始日期"/></td>
								<td style="padding-left:10px;"><input class="span10 date-picker" name="END_DATE" id="END_DATE_"  value="${pd.END_DATE}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束日期"/></td>
								<td style="padding-left:10px;">
									  <select  name="TYPE" id="TYPE_"  style="width:120px;" >
												<option value="" >选择语料来源</option>									
												<c:forEach items="${typeList}" var="type">
													<c:if test="${pd.TYPE == null }">
														<option value="${type.CODE}" >${type.NAME}</option>
													</c:if>
													<c:if test="${pd.TYPE != null }">
													    <option value="${type.CODE}" <c:if test="${type.CODE == pd.TYPE }">selected</c:if>>${type.NAME}</option>
													</c:if>
												</c:forEach>
									  </select>
								</td>
								<td style="vertical-align:top;padding-left:12px;"><a class="btn btn-light btn-xs" style="border-bottom-width: 2px; border-top-width: 1px; padding-top: 4px; margin-top: 2px; padding-bottom: 4px;" onclick="javascript:searchDate();" title="根据时间或类型检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
								</td> 
								<!-- 按语料模糊查询 -->
								<td style="padding-left:12px;">
									<div class="nav-search" >
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="ff" type="text" name="ORIG_CORPUS" id="origCorpus_" value="${pd.ORIG_CORPUS}" placeholder="按语料模糊查询" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								<td style="vertical-align:top;padding-left:12px;"><a class="btn btn-light btn-xs" style="border-bottom-width: 2px; border-top-width: 1px; padding-top: 4px; margin-top: 2px; padding-bottom: 4px;" onclick="searchDetail();"   title="按语料检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								<!-- 按实体名称查询 -->
								<td style="padding-left:12px;">
									<div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="entityName_"  type="text"  name="ENTITY_NAME" value="${pd.ENTITY_NAME}" placeholder="按实体名称精确查询" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								<td style="vertical-align:top;padding-left:12px;"><a style="border-bottom-width: 2px; border-top-width: 1px; padding-top: 4px; margin-top: 2px; padding-bottom: 4px;" class="btn btn-light btn-xs" onclick="searchEntity();" title="按实体名称检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a>
								</td> 
								<td style="vertical-align:top;padding-left:12px;" ><a class="btn btn-sm btn-info" onclick="gReload();" style="border-top-width: 1px; border-bottom-width: 2px;margin-top: 2px;">重置</a></td>
				 				<!-- <td><a class="btn btn-mini btn-success" onclick="importAllMtsCorpus();"
											title="导出语料">导入所有MTS原始语料</a></td> -->
											<td style="vertical-align:top;padding-left:12px;" ><a class="btn btn-mini btn-success" style="border-top-width: 3px; border-bottom-width: 2px; margin-top: 2px; padding-top: 3px; padding-bottom: 3px;"  onclick="javascript:importExcel();"
											title="从XML导入语料">导入语料</a></td>
									<td style="vertical-align:top;padding-left:12px;" ><a class="btn btn-mini btn-success" style="border-top-width: 3px; border-bottom-width: 2px; margin-top: 2px; padding-top: 3px; padding-bottom: 3px;" onclick="javascript:exportExcel();"
											title="导出Excel">导出Excel</a></td> 
									<td style="vertical-align:top;padding-left:12px;" ><a class="btn btn-mini btn-success" style="border-top-width: 3px; border-bottom-width: 2px; margin-top: 2px; padding-top: 3px; padding-bottom: 3px;" onclick="javascript:traningNLPCorpus();"
											title="导出Excel">语料训练</a></td> 
							</tr>
						</table>
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center" width="350px">原始语料</th>
									<th class="center" width="550px">标注实体</th>
									<th class="center">生成时间</th>
									<th class="center">语料来源</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty listMtsCorpus}">
									<c:forEach items="${listMtsCorpus}" var="mtsCorpus" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${mtsCorpus.P_HTML}</td>
											<td class="center">${mtsCorpus.ENTITY_NAME}</td>
											<td class="center"><fmt:formatDate value="${mtsCorpus.EDIT_DATE}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
											<td class="center">${mtsCorpus.TYPE ==1?'人工标注':'AI更新'}</td>
											<td class="center"><a class="btn btn-mini btn-primary"  onclick="assertEntity('${mtsCorpus.ID}','${mtsCorpus.ORIG_CORPUS}');" style="margin-left: 20px;">语料维护</a></td>
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
						<!-- 实体结果 -->
						<!-- <div id="entityResultDiv_">
							<div style="padding-left: 200px; ">
								<h5>实体结果：</h5>
							</div>
							<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;margin-left: 300px;margin-bottom: 20px;max-width: 100%;width: 50%;">
								<thead>
									<tr>
										<th width="45px">&nbsp;</th>
										<th class="center" width="45px">序号</th>
										<th class="center">实体名称</th>
										<th class="center">实体类别</th>
									</tr>
								</thead>
								<tbody id ="entityResult_">
								</tbody>
							</table>
						</div>
						<div class="center" style="margin-right: 240px;">
								<a class="btn btn-mini btn-success" onclick="javascript:addEntity();" style="margin-left: 20px;">添加实体</a>
								<a class="btn btn-mini btn-primary"  onclick="updateEntity();" style="margin-left: 20px;">修改实体</a>
								<a class="btn btn-mini btn-danger" onclick="javascript:delEntity();"  style="margin-left: 20px;">删除实体</a>
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
	<!-- 遮罩层 -->
	<div id="overlay" class="overlay"></div>
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
function importExcel(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="导入EXCEL原始语料";
	 diag.URL = '<%=path%>/mtsCorpus/goUploadExcel.do';
	 diag.Width = 300;
	 diag.Height = 150;
	 diag.CancelEvent = function(){ //关闭事件
		 if('${page.currentPage}' == '0'){
			 top.jzts();
			 setTimeout("self.location.reload()",100);
		 }else{ 
			 nextPage(${page.currentPage});
		 }
		/*  if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 alert(11111);
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
//导出excel
function exportExcel(){
	/* var listSize = ${listSize}
	if(listSize < 1){
		alert("列表无内容，请查询列表后，进行导出操作");
		return;
	} */
	$("#corpusAssertForm").attr("action",'<%=basePath%>mtsCorpus/exportCorpusExcel.do');
	$("#corpusAssertForm").submit();
}

function gReload(){
	$("#START_DATE_").val("");
	$("#END_DATE_").val("");
	$("#TYPE_").val("");
	$("#origCorpus_").val("");
	$("#entityName_").val("");
}

//检索
function searchDetail(){
	if($("#origCorpus_").val() == ""){
		$("#origCorpus_").tips({
			side:2,
            msg:'请输入语料关键字',
            bg:'#AE81FF',
            time:2
        });
		return;
	}
	$("#entityName_").val("");
	top.jzts();
	$("#accsessType_").val("1")
	$("#corpusAssertForm").submit();
}

//检索
function searchEntity(){
	if($("#entityName_").val() == ""){
		$("#entityName_").tips({
			side:2,
            msg:'请输入实体名称',
            bg:'#AE81FF',
            time:2
        });
		return;
	}
	$("#origCorpus_").val("");
	top.jzts();
	$("#accsessType_").val("2")
	$("#corpusAssertForm").submit();
}

//检索
function searchDate(){
	
	$("#entityName_").val("");
	$("#origCorpus_").val("");
	top.jzts();
	$("#accsessType_").val("3")
	$("#corpusAssertForm").submit();
}

function importAllMtsCorpus(){
	window.location.href='<%=basePath%>mtsCorpus/importAllMtsCorpus.do';
}
//语料训练

function showOverlay() {
    // 遮罩层宽高分别为页面内容的宽高
    $('.overlay').css({'height':$(document).height(),'width':$(document).width()});
    $('.overlay').show();
}

function traningNLPCorpus(){
	   //遮罩
	   showOverlay();
	   alert("训练中....");
	   var  TYPE =  $('#TYPE_').val(); 
	   var  START_DATE = $('#START_DATE_').val();
	   var  END_DATE = $('#END_DATE_').val();
	   $("#zhongxin").hide();
	   $("#zhongxin2").show();
	   jQuery.ajax({  
	        url : '<%=basePath%>mtsCorpus/traningNLPCorpus.do',  
	        type : 'POST',  
	        data : { "TYPE" : TYPE ,"START_DATE":START_DATE, "END_DATE":END_DATE} ,  
	        dataType : 'text',  
	        success : function (msg) {  
	        	$('.overlay').hide();
	        	if("training success" == msg){
	        		alert("训练成功");
	        	}else if("training fail" == msg){
	        		alert("由于系统原因，训练失败");
	        	}else if("upload fail" == msg){
	        		alert("由于系统原因，上传语料失败");
	        	}else if("busy" == msg){
	        		alert("有语料正在训练，请稍候再次尝试");
	        	}else{
	        		alert("未知错误");
	        	}
	        }
	    });  
	
	
}

//新增实体
function assertEntity(detailId,origCorpus){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="语料维护";
	 diag.URL = '<%=path%>/mtsCorpus/goUpdateEntity.do?detailId='+detailId+'&origCorpus='+origCorpus;
	 diag.Width = 800;
	 diag.Height = 700;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location=self.location",100);
			 }else{
				 //修改实体后，直接跳回首页，因为后台列表按时间排序，修改后，时间更新到最新，会在第一页显示
				 nextPage('1');
			 }
		}
		diag.close();
	 };
	 diag.show();
}


function saveEntity(){
	$("#tab").append("<tr id="+_len+" align='left'>"
			//行号
			+ "<input type='hidden' id='startTextOff"+_len+"' value='"+startTextOff+"'/>"
			+ "<input type='hidden' id='endTextOff"+_len+"' value='"+endTextOff+"'/>"
			+ "<input type='hidden' id='startHtmlOff"+_len+"'/>"
			+ "<input type='hidden' id='endHtmlOff"+_len+"' />"
			+ "<input type='hidden' id='origCorpus"+_len+"' value='"+origCorpus+"'/>"
			+ "<input type='hidden' id='pnum"+_len+"' value='"+pnum+"'/>"
			+ "<td>"
			+ (_len + 1)
			+ "</td>"
			//实体
			+ "<td id='entity"+_len+"'><a style='cursor:pointer' onclick='locateIndex(\""+pnum+"\");'>"+range+"【"+startTextOff+"-"+endTextOff+"】"+"</a></td>"
			//选择框
			+ "<td id='sel"+_len+"' >"
				+ "<select onchange=\"changeColor(this,'"+pnum+"');\" id='select_"+ _len+"'>"
					+ "<options><option value=''>请选择</option>"
						+"<c:forEach items='${termList}' var='term'>"
							+"<option style='color:${term.COLOR}' value='${term.TERM_CN_NAME}'>${term.TERM_CN_NAME}</option>"
						+"</c:forEach>"
					+"</options>"
				+"</select>"
			+"</td>"
			+ "<td ><input type='checkbox' id='isNotConfirm"+ _len
			+ "' onclick='checkBox(this);'></td>"
			+ "<td><a href='javascript:void(0);' onclick=\"javascript:deltr("+_len+",'"+pnum+"');\">删除实体</a></td>"
			+ "</tr>");
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

		
</script>
</html>
