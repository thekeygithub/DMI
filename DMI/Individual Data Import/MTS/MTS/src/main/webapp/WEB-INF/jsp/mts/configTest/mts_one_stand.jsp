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
								
						<!-- 检索  -->
						<form action="mtsOneStand/standOne.do" method="post" name="standOneForm" id="standOneForm">
						<table style="margin-top:5px;">
							<tr>
								<td style="padding-left:2px;">								
						
								  <select  name="AREA_ID" id="AREA_ID"  style="width:100px;" >
											<option value="" >选择区域</option>	
											<c:forEach items="${areaList}" var="areas">											    
													<option value="${areas.AREA_CODE }" >${areas.AREA_NAME }</option>												
											</c:forEach>
									</select>
								</td>
								<td style="padding-left:2px;">
								  <select  name="classcode" id="classcode" onchange="changeClass(this.value)" style="vertical-align:top;" style="width:98%;" >
											<option value="" >选择聚类</option>											
											<c:forEach items="${dataClassList}" var="dataClass">												
													<option value="${dataClass.DATA_CLASS_CODE }" >${dataClass.DATA_CLASS_NAME }</option>												
												
											</c:forEach>
											</select>
								</td>						
								
								
								<td>
									<select  name="typecode" id="typecode"  style="vertical-align:top;" style="width:98%;" >
											<option value="" >选择标化类型</option>	
											<c:forEach items="${dataTypeList}" var="dataType">											    
													<option value="${dataType.MEM_DATA_CODE }" >${dataType.DATA_TYPE_NAME }</option>												
											</c:forEach>
										</select>
                                  </td>								
								
								<td> 											
										<select  name="appMethod" id="appMethod"  style="vertical-align:top;" style="width:98%;" >
											<option value="" >选择匹配程序</option>	
											<c:forEach items="${softList}" var="softls">											    
													<option value="${softls.SOFT_EN_NAME }" >${softls.SOFT_CN_NAME }</option>												
											</c:forEach>
											</select>
								</td>
								<td>
												匹配内容:<input type="text" name="key" id="key" />
								</td>
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="执行标化"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								
							</tr>
						</table>
						<!-- 检索  -->
									
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>																	
									<th class="center">原数据</th>
									<th class="center">NLP切词结果</th>
									<th class="center">NLP切词标化结果</th>
									<th class="center">特殊处理结果</th>
									<th class="center">直接匹配结果</th>									
									<th class="center">特殊处理匹配标识</th>
									<th class="center">怀疑标识</th>
									<th class="center">标化结果</th>										
								</tr>
							</thead>
													
							<tbody>
								
								<tr>									
									<td class='center' style="width: 50px;">${pd.souceData }</td>
									<td class='center' style="width: 50px;">${pd.NLP }</td>
									<td class='center' style="width: 50px;">${pd.NlpValue }</td>
									<td class='center' style="width: 50px;">${pd.TSBH }</td>
									<td class='center' style="width: 100px;">${pd.result }</td>											
									<td class="center">${pd.SPEC}</td>
									<td class="center">${pd.doubt}</td>											
									<td class="center">${pd.status}</td>										
									
								</tr>
									
									
							</tbody>
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
	
	
	//查询
	function searchs(){
		top.jzts();
		$("#standOneForm").submit();
	}
	
	
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