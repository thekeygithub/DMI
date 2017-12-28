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
								<form action="matchRule/${msg }.do" name="matchRuleAddForm" id="matchRuleAddForm" method="post">								
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">区域:</td>
											<td>
											<select  name="area" id="area"  style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>	
											<c:forEach items="${areaList}" var="areas">
											   <option value="${areas.AREA_CODE }" >${areas.AREA_NAME }</option>												
											</c:forEach>
											</select>
                                            </td>
										</tr>
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">聚类:</td>
											<td id="juese">
											<select  name="classcode" id="classcode" onchange="changeClass(this.value)" style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>	
											<c:forEach items="${dataClassList}" var="dataClass">											
													<option value="${dataClass.DATA_CLASS_CODE }"  <c:if test="${dataClass.DATA_CLASS_CODE == '02' }">selected</c:if>>${dataClass.DATA_CLASS_NAME }</option>												
											</c:forEach>
											</select>
											</td>
										</tr>
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">标化类型:</td>
											<td>
											<select  name="typecode" id="typecode"  style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>	
											<c:forEach items="${dataTypeList}" var="dataType">											   
													<option value="${dataType.MEM_DATA_CODE }" >${dataType.DATA_TYPE_NAME }</option>												
											</c:forEach>
											</select>
                                            </td>
										</tr>
										
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">匹配内容:</td>
											<td>
												<input type="text" name="key" id="key" />
											</td>
											
										</tr>
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">返回结果:</td>
											<td>
												<input type="text" name="value" id="value" style="width:98%;"/>
											</td>
											
										</tr>									
										<tr>
											<td style="text-align: center;" colspan="10">
												<a class="btn btn-mini btn-primary" onclick="findOne();">单条查询</a>												
												<a class="btn btn-mini btn-primary" onclick="findMore();">数量查询</a>
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
	function changeClass(v) {  
		   
		  
		   jQuery('#classcode').val(v); 	
		   var typecode = jQuery('#typecode');		   
		    
		    jQuery.ajax({  
		        url : '<%=basePath%>redisFind/selType.do',  
		        type : 'post',  
		        data : { "classcode" : v } ,  
		        dataType : 'json',  
		        success : function (opts) {  		        
		 		   jQuery('#typecode').html(""); 		 		 
		 		   var itemsType=opts.listDataType;
			 		
			 		
			 		 if(itemsType!=null)
				      {	
			 			typecode.append("<option value = ''> </option>");
				         for(var i =0;i<itemsType.length;i++)
				            {
				             var item=itemsType[i];
				             typecode.append("<option value = '"+item.MEM_DATA_CODE+"'>"+item.DATA_TYPE_NAME+"</option>");				            
				            }
				       } else { 
				    	   typecode.empty();  
				    	  
				       } 			 		  
			 		 
			       }  
		        
		        
		        });  
		}

	//异步请求查询方法并返回 
	function findOne() {    
		  
		if($("#area").val()==""){
			$("#area").tips({
				side:3,
	            msg:'请选择区域',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#area").focus();
			return false;
		}
		
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
		
		if($("#key").val()==""){
			$("#key").tips({
				side:3,
	            msg:'请填写匹配内容',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#key").focus();
			return false;
		}
		   
		   var areaid = jQuery('#area').val();
		   var css = jQuery('#classcode').val();
		   var bhlx = jQuery('#typecode').val();
		   var inkey = jQuery('#key').val();		  
		    
		    jQuery.ajax({  
		        url : '<%=basePath%>redisFind/findOne.do',  
		        type : 'post',  
		        data : { "areaid" : areaid,"css":css,"bhlx" : bhlx,"inkey":inkey } ,  
		        dataType : 'json',
		        success : function (opts) {  	        	
		        	
		        	document.getElementById("value").value=opts.one;
		 		   
			       }  
		        
		        
		        });  
		}

	//异步请求查询方法并返回 
	function findMore() {  
		   
		if($("#area").val()==""){
			$("#area").tips({
				side:3,
	            msg:'请选择区域',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#area").focus();
			return false;
		}
		
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
		   
		   var areaid = jQuery('#area').val();
		   var css = jQuery('#classcode').val();
		   var bhlx = jQuery('#typecode').val();		  	  
		    
		    jQuery.ajax({  
		        url : '<%=basePath%>redisFind/findMore.do',  
		        type : 'post',  
		        data : { "areaid" : areaid,"css":css,"bhlx" : bhlx } ,  
		        dataType : 'json',  
		        success : function (opts) {          
		        	
		        	document.getElementById("value").value=opts;
		 		   
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
				if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
				 else $('#form-field-select-4').removeClass('tag-input-style');
			});
		}
	});
</script>
</html>