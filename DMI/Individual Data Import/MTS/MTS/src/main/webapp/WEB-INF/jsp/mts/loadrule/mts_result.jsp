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
								<form action="loadRule/${msg }.do" name="loadRuleAddForm" id="loadRuleAddForm" method="post">
									<input type="hidden" name="keys" id="keys" />
									<input type="hidden" name="values" id="values" />
									<input type="hidden" name="rid" id="rid" value="${pd.ruleid }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td>
											入参：
											<div>
												<textarea cols="100" rows="5" name="param1" id="paramK" onfocus="getMouse(event);"/>
												
														
												</textarea>
												<input type="button" onclick="javascript:getResult();" value="提交"/>
												<input type="button" onclick="javascript:toImport();" value="导入"/>
											</div>
											</td>
										
										</tr>
										<tr>
											<td>
											结果：
											<div>
												<span id="result_" style="color: green"> 
												</span>
											</div>
											</td>
										</tr>
										<!-- <tr>
											<td style="text-align: center;" colspan="10">
												<a class="btn btn-mini btn-primary" onclick="getResult();">保存</a>
												<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											</td>
										</tr> -->
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
	
	function getMouse(event){
		var e = event.srcElement;
	    var r =e.createTextRange();
	    r.moveStart('character',e.value.length);
	    r.collapse(true);
	    r.select();
	}
	
	
	function toImport() {  
		   var paramK = $.trim(jQuery('#paramK').val()); 
		   jQuery.ajax({  
		        url : '<%=basePath%>mtsDBData/standardizeDBData.do',  
		        type : 'post',  
		        data : { "toBeStandardStr" : paramK } ,  
		        dataType : 'text',  
		        success : function (opts) {
		           //jQuery('#result_').html(opts); 
		        }
		    });  
		}

	
	//异步请求查询字典列表的方法并返回json数组 
	function getResult() {  
		   var paramK = $.trim(jQuery('#paramK').val()); 
		   jQuery.ajax({  
		        url : '<%=basePath%>loadRule/getMtsResult.do',  
		        type : 'post',  
		        data : { "toBeStandardStr" : paramK } ,  
		        dataType : 'text',  
		        success : function (opts) {
		           jQuery('#result_').html(opts); 
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
		if($("#typecode").val()==""){
			$("#typecode").tips({
				side:3,
	            msg:'请选择标化类型',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#typecode").focus();
			return false;
		}
		var val = $("#keyrule_").find("option:selected").val();
		if(val  == ""){
			$("#keyrule_").tips({
				side:3,
	            msg:'请选择KEY生成规则',
	            bg:'#AE81FF',
	            time:3
	        });
			//$("#keyrule").focus();
			return false;
		}
		
		var obj1 = document.getElementById('fhznr1');
		var options1 = obj1.options;
		if(options1.length==0){
			$("#fhznr1").tips({
				side:3,
	            msg:'匹配内容不能为空',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#fhznr1").focus();
			return false;
		}
		var ppnrvalue="@"+val;
		
		var fhzvalue="";
		for(var i=0;i<options1.length;i++){
			fhzvalue=fhzvalue+"@"+options1[i].value;
		}
		document.getElementById("keys").value=ppnrvalue;
		document.getElementById("values").value=fhzvalue;
		$("#loadRuleAddForm").submit();
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