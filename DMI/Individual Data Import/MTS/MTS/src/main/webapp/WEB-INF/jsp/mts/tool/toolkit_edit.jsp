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
								<form action="mtsToolkit/${msg}.do" name="mtsToolkitAddForm" id="mtsToolkitAddForm" method="post">
									<input type="hidden" name="TOOLKIT_ID" id="TOOLKIT_ID" value="${pd.TOOLKIT_ID}"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:90px;text-align: right;padding-top: 13px;">工具包名称:</td>
											<td><input type="text" name="TOOLKIT_NAME" id="TOOLKIT_NAME" value="${pd.TOOLKIT_NAME }" maxlength="32" placeholder="这里输入工具包名称" title="工具包名称" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:90px;text-align: right;padding-top: 13px;">备注:</td>
											<td><input type="text" name="COMMENTS" id="COMMENTS" value="${pd.COMMENTS }" maxlength="32" placeholder="这里输入备注" title="备注"  style="width:98%;"/></td>
										</tr>	
										<tr>
											<td style="text-align: center;" colspan="10">
												<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
												<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
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
		var msg = '${msg}';
		var OPERATE = '';
		var TOOLKIT_ID = '${pd.TOOLKIT_ID}';
		if(msg == 'saveToolkit'){
			OPERATE = 'Add';
		}else if(msg == 'editToolkit'){
			OPERATE = 'Edit';
		}
		
		var obj1 = document.getElementById('TOOLKIT_NAME');
		var options = obj1.options;
		if(obj1.value==""){
			$("#TOOLKIT_NAME").tips({
				side:3,
	            msg:'请输入工具包名称',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TOOLKIT_NAME").focus();
			return false;
		}
		var result = 'true';
		$.ajax({
			url : '<%=path%>/mtsToolkit/verifyMtsToolkit.do',  
			async: false,
	        type : 'post',  
	        data : {"TOOLKIT_NAME" : obj1.value ,"OPERATE" : OPERATE, "TOOLKIT_ID" : TOOLKIT_ID },  
	        dataType : 'json',  
	        success : function (data) {  
	           if(data.result == 'false'){
	        	   $("#TOOLKIT_NAME").tips({
						side:3,
			            msg:'已存在此工具包名称',
			            bg:'#AE81FF',
			            time:3
			       });
					$("#TOOLKIT_NAME").focus();
					return false;
	           }else{
	       		$("#mtsToolkitAddForm").submit();
	       		$("#zhongxin").hide();
	       		$("#zhongxin2").show();
	           }
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