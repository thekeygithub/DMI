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
								<form action="mtsArea/${msg}.do" name="mtsAreaAddForm" id="mtsAreaAddForm" method="post">
									<input type="hidden" name="AREA_ID" id="AREA_ID" value="${pd.AREA_ID}"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">区域名称:</td>
											<td><input type="text" name="AREA_NAME" id="AREA_NAME" value="${pd.AREA_NAME }" maxlength="32" placeholder="这里输入区域名称" title="区域名称" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">区域编码:</td>
											<td><input type="text" name="AREA_CODE" id="AREA_CODE" value="${pd.AREA_CODE }" maxlength="32" placeholder="这里输入区域编码" title="区域编码" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">备注:</td>
											<td><input type="text" name="REMARK" id="REMARK" value="${pd.REMARK }" maxlength="32" placeholder="这里输入备注" title="备注"  style="width:98%;"/></td>
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
	
	//异步请求查询字典列表的方法并返回json数组 
	function changeKeyClass(v) {  
		   jQuery('#classcode').val(v); 
		   var keyrule = jQuery('#keyrule'); 
		   jQuery.ajax({  
		        url : '<%=basePath%>matchRule/selKeyDict.do',  
		        type : 'post',  
		        data : { "classcode" : v } ,  
		        dataType : 'json',  
		        success : function (opts) {  
		 		   jQuery('#keyrule').html("<option value=''>请选择KEY生成规则</option>");
		 		   var items=opts.list; 
			 		  if(items!=null){
				         for(var i =0;i<items.length;i++)
				            {
				             var item=items[i];
				             if('${pd.LOADCODE}'=="'"+item.LOADCODE+"'"){
				            	 keyrule.append("<option value=\""+item.LOADCODE+"\" selected>"+item.DNAME+"</option>");
				             }else{
				            	 keyrule.append("<option value=\""+item.LOADCODE+"\" >"+item.DNAME+"</option>");
				             }
				            }
				       } else { 
				    	   keyrule.empty();   
				    	   //fhznr2Value.empty();   
				       } 
			       }  
		        });  
		}

	
	//异步请求查询字典列表的方法并返回json数组 
	function changeValueClass(v) {  
		   jQuery('#classcode').val(v); 
		   var fhznr2Value = jQuery('#fhznr2'); 
		   jQuery.ajax({  
		        url : '<%=basePath%>matchRule/selValueDict.do',  
		        type : 'post',  
		        data : { "classcode" : v } ,  
		        dataType : 'json',  
		        success : function (opts) {  
		        
		           jQuery('#fhznr2').html("");  
		 		   jQuery('#fhznr1').html("");  
		 		   var items=opts.list; 
			 		  if(items!=null){
				         for(var i =0;i<items.length;i++)
				            {
				             var item=items[i];
				             fhznr2Value.append("<option value = '"+item.LOADCODE+"'>"+item.DNAME+"</option>");
				            }
				       } else { 
				    	   fhznr2Value.empty();   
				       } 
			       }  
		        });  
		}
	
	
	
	
	//匹配内容加入
	function ppnradd(){
		var leftOption=$('#ppnr2 option:selected');			
		var rightOption=$('#ppnr1 option');		
		var right=$('#ppnr1')		
	 	addOptionList(leftOption,rightOption,right);
	}
	//匹配内容移除
	function ppnrdel(){
		$('#ppnr1').find('option:selected').remove();
	}
	//返回值内容加入
	function fhznradd(){
		var leftOption=$('#fhznr2 option:selected');
		var rightOption=$('#fhznr1 option');
		var right=$('#fhznr1')
	 	addOptionList(leftOption,rightOption,right);
	}
	//返回值内容移除
	function fhznrdel(){
		$('#fhznr1').find('option:selected').remove();
	}
	
	function addOptionList(leftOption,rightOption,right){
	
  		for(var i=0;i<leftOption.length;i++){
  			var leftText=leftOption.eq(i).text();  			
  			var leftValue=leftOption.eq(i).val();
  			
	  	for(var h=0;h<rightOption.length;h++){
	  		if(rightOption[h].text==leftText){
	  			return;
	  		}
	  	}
		var option=$('<option value='+leftValue+'>'+leftText+'</option>');
		right.append(option);
		}
  	}
	
	//保存
	function save(){		
		var msg = '${msg}';
		var OPERATE = '';
		var AREA_ID = '${pd.AREA_ID}';
		if(msg == 'saveArea'){
			OPERATE = 'Add';
		}else if(msg == 'editArea'){
			OPERATE = 'Edit';
		}
		
		var obj1 = document.getElementById('AREA_NAME');
		var options = obj1.options;
		if(obj1.value==""){
			$("#AREA_NAME").tips({
				side:3,
	            msg:'请输入区域名称',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#AREA_NAME").focus();
			return false;
		}
		var result = 'true';
		$.ajax({
			url : '<%=path%>/mtsArea/verifyMtsArea.do',  
			async: false,
	        type : 'post',  
	        data : {"AREA_NAME" : obj1.value ,"OPERATE" : OPERATE, "AREA_ID" : AREA_ID },  
	        dataType : 'json',  
	        success : function (data) {  
	           if(data.result == 'false'){
	        	   $("#AREA_NAME").tips({
						side:3,
			            msg:'已存在此区域名称',
			            bg:'#AE81FF',
			            time:3
			       });
					$("#AREA_NAME").focus();
					result =  "false";
	           }
		    }  
	     });  
		if(result == 'false'){
			return false;
		}
		var obj = document.getElementById('AREA_CODE');
		var options = obj.options;
		if(obj.value==""){
			$("#AREA_CODE").tips({
				side:3,
	            msg:'请输入区域编码',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#AREA_CODE").focus();
			return false;
		}
		$.ajax({
			url : '<%=path%>/mtsArea/verifyMtsArea.do',  
			async: false,
	        type : 'post',  
	        data : {"AREA_CODE" : obj.value ,"OPERATE" : OPERATE, "AREA_ID" : AREA_ID },  
	        dataType : 'json',  
	        success : function (data) {  
	        	if(data.result == 'false'){
	        	   $("#AREA_CODE").tips({
						side:3,
			            msg:'已存在此区域编码',
			            bg:'#AE81FF',
			            time:3
			       });
					$("#AREA_CODE").focus();
					result =  "false";
	           }
		    }  
	     });  
		if(result == 'false'){
			return false;
		}
		$("#mtsAreaAddForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
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