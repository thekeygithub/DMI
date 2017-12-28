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
								<form action="mtsToolkit/${msg}.do" name="mtsToolrelAddForm" id="mtsToolrelAddForm" method="post">
									
									<input type="hidden" name="TOOL_REL_ID" id="TOOL_REL_ID" value="${pd.TOOL_REL_ID}"/>
									<input type="hidden" name="TOOLKIT_ID" id="TOOLKIT_ID" value="${pd.TOOLKIT_ID}"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:90px;text-align: right;padding-top: 13px;">工具:</td>
											<%-- <td><input type="text" name="TOOL_NAME" id="TOOL_NAME" value="${pd.TOOL_NAME }" maxlength="32" placeholder="这里输入工具信息" title="工具信息" style="width:98%;"/></td> --%>
											<td>
													<input type="hidden" name="TOOL_ID" id="TOOL_ID" value="${pd.TOOL_ID }"  >
													<input type="text" readonly="readonly" name="TOOL_NAME" id="TOOL_NAME"  value="${pd.TOOL_NAME }"  maxlength="32" title="工具信息" style="width:78%;"/>
													<a class="btn btn-mini btn-success" onclick="selectTool();">选择工具</a>
											</td>
										</tr>
										
										<tr>
											<td style="width:90px;text-align: right;padding-top: 13px;">工具顺序:</td>
											<td><input type="text" name="TOOL_ORDER" id="TOOL_ORDER" value="${pd.TOOL_ORDER }" maxlength="32" placeholder="这里输入工具顺序" title="工具顺序"  style="width:98%;"/></td>
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
	
	//选择工具
	function selectTool(){
		 top.jzts();
		 var diag = new top.Dialog();
		 diag.Drag=true;
		 diag.Title ="工具信息";
		 diag.URL = '<%=path%>/mtsToolkit/selectTool.do';
		 diag.Width = 620;
		 diag.Height = 450;
		 diag.CancelEvent = function(){ //关闭事件
			 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				 if('${page.currentPage}' == '0'){
					 top.jzts();
					 setTimeout("self.location=self.location",100);
				 }else{
					 nextPage(${page.currentPage});
				 }
			}
		 	var toolinfo=diag.innerFrame.contentWindow.document.getElementById('TOOL_INFO').value;
			//document.getElementById('EXP_ONE_ROLE').value=;
			diag.close();
			if(toolinfo==null||toolinfo==""){
			}else{
				$("#TOOL_ID").val(toolinfo.split("###")[0]);
				$("#TOOL_NAME").val(toolinfo.split("###")[1]);
			}
		 };
		 diag.show();
	}
	//保存
	function save(){		
		var msg = '${msg}';
		var OPERATE = '';
		var TOOL_REL_ID = '${pd.TOOL_REL_ID}';
		var TOOLKIT_ID='${pd.TOOLKIT_ID}';
		var TOOL_ID = $("#TOOL_ID").val();
		var TOOL_ORDER = $("#TOOL_ORDER").val();
		if(msg == 'saveToolrel'){
			OPERATE = 'Add';
		}else if(msg == 'editToolrel'){
			OPERATE = 'Edit';
		}
		
		var obj1 = document.getElementById('TOOL_NAME');
		var options = obj1.options;
		if(obj1.value==""){
			$("#TOOL_NAME").tips({
				side:3,
	            msg:'请选择工具',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TOOL_NAME").focus();
			return false;
		}
		var obj2 = document.getElementById('TOOL_ORDER');
		var options2 = obj2.options;
		if(obj2.value==""){
			$("#TOOL_ORDER").tips({
				side:3,
	            msg:'请输入顺序',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TOOL_ORDER").focus();
			return false;
		}
		var result = 'true';
		$.ajax({
			url : '<%=path%>/mtsToolkit/verifyMtsToolrel.do',  
			async: false,
	        type : 'post',  
	        data : {"OPERATE" : OPERATE, "TOOL_REL_ID" : TOOL_REL_ID , "TOOLKIT_ID" : TOOLKIT_ID , "TOOL_ID" : TOOL_ID , "TOOL_ORDER" : TOOL_ORDER  },  
	        dataType : 'json',  
	        success : function (data) {  
	           if(data.result == 'success'){
	        	    $("#mtsToolrelAddForm").submit();
		       		$("#zhongxin").hide();
		       		$("#zhongxin2").show();   
	           }else{
	        	   $("#TOOL_NAME").tips({
						side:3,
			            msg:data.result,
			            bg:'#AE81FF',
			            time:3
			       });
					$("#TOOL_NAME").focus();
					return false;
	       		
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