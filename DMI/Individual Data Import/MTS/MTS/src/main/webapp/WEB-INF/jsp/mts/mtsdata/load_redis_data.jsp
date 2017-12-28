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
								<form action="mtsData/loadRedisData.do" name="Form" id="Form" method="post">
									<input type="hidden" name="keys" id="keys" />
									<input type="hidden" name="values" id="values" />
									<input type="hidden" name="rid" id="rid" value="${pd.ruleid }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">区域:</td>
											<td>
												<select  name="area" id="area_" style="vertical-align:top;" style="width:98%;" onchange="changeArea(this.value);">
														<option value="">请选择区域</option>
														<c:forEach items="${areaList}" var="areas">
																<option value="${areas.AREA_CODE }" >${areas.AREA_NAME }</option>
														</c:forEach>
												</select>
                                            </td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">聚类:</td>
											<td id="juese">
											<select  name="classcode" id="classcode" onchange="changeClass(this.value);" style="vertical-align:top;" style="width:98%;" >
											<c:if test="${pd.CLASS_CODE == null }">
													<option value="" >请选择聚类 </option>
											</c:if>
											<c:forEach items="${dataClassList}" var="dataClass">
												<c:if test="${pd.CLASS_CODE == null }">
													<option value="${dataClass.DATA_CLASS_ID }">${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
												<c:if test="${pd.CLASS_CODE != null }">
												    <option value="${dataClass.DATA_CLASS_ID }" <c:if test="${dataClass.DATA_CLASS_CODE == pd.CLASS_CODE }">selected</c:if>>${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
											</td>
										</tr>
										
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">标化类型:</td>
											<td>
											<select  name="typecode" id="typecode"  style="vertical-align:top;" style="width:98%;" onchange="changeType(this.value);">
											
											<c:if test="${pd.TYPE_CODE == null }">
													<option value="" >请选择标化类型</option>
											</c:if>
											<c:forEach items="${dataTypeList}" var="dataType">
											    <c:if test="${pd.TYPE_CODE != null }">
													<option value="${dataType.DATA_TYPE_ID }" <c:if test="${dataType.MEM_DATA_CODE == pd.TYPE_CODE }">selected</c:if>>${dataType.DATA_TYPE_NAME }</option>
												</c:if>
												<c:if test="${pd.TYPE_CODE == null }">
													<option value="${dataType.DATA_TYPE_ID }" >${dataType.DATA_TYPE_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
                                            </td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">批次号:</td>
											<td>
											<select  name="batchNo" id="batchNo_"  style="vertical-align:top;" style="width:98%;" >
											
											<c:if test="${pd.BATCH_NO == null }">
													<option value="" >请选择批次号</option>
												</c:if>
											<c:forEach items="${batchNoList}" var="pd">
												<option value="${pd.BATCH_NO}">${pd.BATCH_NO}</option>
											</c:forEach>
											</select>
                                            </td>
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
	function changeClass(v) {  
		   jQuery('#classcode').val(v); 
		   var areaId =  jQuery('#area_').val(); 
		   var typecode = $('#typecode');
		   var batchno =  $('#batchNo_');
		   jQuery.ajax({  
		        url : '<%=basePath%>mtsData/selClassLoadDict.do',  
		        type : 'post',  
		        data : { "DATA_CLASS_ID" : v ,"AREA_ID":areaId} ,  
		        dataType : 'json',  
		        success : function (opts) {  
		        	//标化类型
		        	 typecode.html("<option value=''>请选择标化类型</option>"); 
		 		   	 var itemsType=opts.listDataType;
			 		 if(itemsType){
				         for(var i =0;i<itemsType.length;i++){
				             var item=itemsType[i];
				             typecode.append("<option value = '"+item.DATA_TYPE_ID+"'>"+item.DATA_TYPE_NAME+"</option>");				            
				         }
				     } else { 
				    	  
				     } 
			 		 //批次号
		 		     var batchList=opts.batchList;
		 		 	 batchno.html("<option value = ''>请选择批次号</option>");	
			 		 if(batchList!=null){
				         	for(var i =0;i<batchList.length;i++) {
					             var item=batchList[i];
					             batchno.append("<option value = '"+item.BATCH_NO+"'>"+item.BATCH_NO+"</option>");				            
				            }
				     } else { 
				    	 
				     } 
		        }
		    });  
		}
	
	
	//异步请求查询字典列表的方法并返回json数组 
	function changeArea(v) {  
		   jQuery('#area_').val(v); 
		   var batchno = jQuery('#batchNo_');
		   var dataTypeId = jQuery('#typecode').val(); 
		   var dataClassId = jQuery('#classcode').val(); 
		   jQuery.ajax({  
		        url : '<%=basePath%>mtsData/selLoadBatch.do',  
		        type : 'post',  
		        data : { "AREA_ID":v , "DATA_TYPE_ID":dataTypeId , "DATA_CLASS_ID":dataClassId} ,  
		        dataType : 'json',  
		        success : function (opts) {  
		 		     jQuery('#batchNo_').html(""); 
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
	
	
	//异步请求查询字典列表的方法并返回json数组 
	function changeType(dataTypeId) {  
		   jQuery('#typecode').val(dataTypeId); 
		   var areaId =  jQuery('#area_').val(); 
		   var batchno = jQuery('#batchNo_');
		   var dataClassId = jQuery('#classcode').val(); 
		   jQuery.ajax({  
		        url : '<%=basePath%>mtsData/selLoadBatch.do',  
		        type : 'post',  
		        data : { "AREA_ID" : areaId ,"DATA_TYPE_ID":dataTypeId, "DATA_CLASS_ID":dataClassId} ,  
		        dataType : 'json',  
		        success : function (opts) {  
		        	batchno.html("<option value = ''>请选择批次号</option>");	
		 		    var batchList=opts.batchList;
			 		 if(batchList!=null){
			 			 
				         for(var i =0;i<batchList.length;i++) {
				             var item=batchList[i];
				             batchno.append("<option value = '"+item.BATCH_NO+"'>"+item.BATCH_NO+"</option>");				            
				         }
				     } else { 
				    	 batchno.empty();  
				     } 
		        }
		    });  
		}

	
	
	//保存
	function save(){
		if($("#area_").val()==""){
			$("#area_").tips({
				side:3,
	            msg:'请选择区域',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#area_").focus();
			return false;
		}
		
		/* if($("#classcode").val()==""){
			$("#classcode").tips({
				side:3,
	            msg:'请选择聚类',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#classcode").focus();
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
		
		if($("#batchNo_").val()==""){
			$("#batchNo_").tips({
				side:3,
	            msg:'请选择批次号',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#batchNo_").focus();
			return false;
		} */
		$("#Form").submit();
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