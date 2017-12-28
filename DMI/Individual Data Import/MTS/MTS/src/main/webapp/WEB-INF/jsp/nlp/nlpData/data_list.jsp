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
<body class="no-skin" >

	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
						
						<!-- 检索  -->
						<form action="nlpData/listdatas.do" method="post" name="nlpDataForm" id="nlpDataForm">
						<table style="margin-top:5px;">
							<tr>
								<td style="padding-left:2px;">								
								<select  name="NLPTYPE" id="NLPTYPE"   style="width:120px;" >
											<option value="" >术语类型选择</option>											
											<c:forEach items="${termList}" var="terms">											  
											  <c:if test="${pd.NLPTYPE == null }">
													<option value="${terms }" >${terms }</option>
												</c:if>
												<c:if test="${pd.NLPTYPE != null }">
												    <option value="${terms}" <c:if test="${terms == pd.NLPTYPE }">selected</c:if>>${terms }</option>
												</c:if>											
											</c:forEach>
								  </select>
								</td>
								<td style="padding-left:2px;">
								  <select  name="CLASSCODE" id="CLASSCODE"  style="width:100px;" >
											<option value="" >选择聚类查询</option>									
											<c:forEach items="${dataClassList}" var="dataClass">
												<c:if test="${pd.CLASSCODE == null }">
													<option value="${dataClass.DATA_CLASS_CODE }" >${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
												<c:if test="${pd.CLASSCODE != null }">
												    <option value="${dataClass.DATA_CLASS_CODE }" <c:if test="${dataClass.DATA_CLASS_CODE == pd.CLASSCODE }">selected</c:if>>${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
											</c:forEach>
								  </select>
								</td>
								
								<td style="padding-left:2px;">								
						
								  <select  name="AREAID" id="AREAID"  style="width:100px;" >
											<option value="" >区域选择</option>	
											<c:forEach items="${areaList}" var="areas">
											    <c:if test="${pd.AREAID != null }">
													<option value="${areas.AREA_CODE }" <c:if test="${areas.AREA_CODE == pd.AREAID }">selected</c:if>>${areas.AREA_NAME }</option>
												</c:if>
												<c:if test="${pd.AREAID == null }">
													<option value="${areas.AREA_CODE }" >${areas.AREA_NAME }</option>
												</c:if>
											</c:forEach>
									</select>
								</td>		
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="搜索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								<td style="vertical-align:top;padding-left:2px;"><a id="txt" name="txt" class="btn btn-light btn-xs" onclick="toTXT();" title="导出到TXT"><i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a></td>
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="fromExcel();" title="从EXCEL导入"><i id="nav-search-icon" class="ace-icon fa fa-cloud-upload bigger-110 nav-search-icon blue"></i></a></td>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">术语名称</th>
									<th class="center">术语类型</th>		
									<th class="center">聚类</th>
									<th class="center">区域</th>							
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty dataList}">
									<c:forEach items="${dataList}" var="nlpData" varStatus="vs">
												
										<tr>
											<td class='center' style="width: 30px;">
												<label><input type='checkbox' name='ids' value="${nlpData.NLPID }"  class="ace"/><span class="lbl"></span></label>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class="center">${nlpData.NLPNAME }</td>
											<td class="center">${nlpData.NLPTYPE }</td>
											<td class="center">${nlpData.DATA_CLASS_NAME }</td>
											<td class="center">${nlpData.AREA_NAME }</td>
											
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
								<a title="批量删除" class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
							</td>
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

function getBS(){
	
	  
	   var txt = jQuery('#txt');	   
	    
	    jQuery.ajax({  
	        url : '<%=basePath%>nlpData/getComplemt.do',  
	        type : 'post',
	      
	        success : function (opts) {  		        
	          
	 		   jQuery('#typecode').html(""); 
	 		   var itemsType=opts.listDataType;
		 		 
		 		
		 		 if(opts=="1")  {			         	 		  
		 		 
		         } else{
		        	 
		         	} 
		         }
	        
	        
	        });  
}
//查询
function searchs(){
	top.jzts();
	$("#nlpDataForm").submit();
}




//导出TXT
function toTXT(){	
	
	bootbox.confirm("确定要导出到TXT文本吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=basePath%>nlpData/nlpData_writeFile.do";
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
	
	
	
}


//打开上传excel页面
function fromExcel(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="EXCEL 导入到数据库";
	 diag.URL = '<%=path%>/nlpData/goUploadExcel.do';
	 diag.Width = 300;
	 diag.Height = 150;
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
						url: '<%=basePath%>nlpData/deleteAllU.do?tm='+new Date().getTime(),
				    	data: {NLPIDS:str},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							 $.each(data.list, function(i, list){
									nextPage(${page.currentPage});
							 });
						}
					});
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
