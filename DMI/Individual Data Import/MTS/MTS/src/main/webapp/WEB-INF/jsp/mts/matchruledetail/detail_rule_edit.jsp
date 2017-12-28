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
<body class="no-skin" >
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
								<form action="detailRule/${msg }.do" name="detailRuleAddForm" id="detailRuleAddForm" method="post">
									<input type="hidden" name="memid" id="memid" value="${mred.MEM_ID }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">区域:</td>
											<td>
											<select  name="area" id="area" onchange="changeSF(this.value)" style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>	
											<c:forEach items="${areaList}" var="areas">
											    <c:if test="${mred.AREA_ID != null }">
													<option value="${areas.AREA_CODE }" <c:if test="${areas.AREA_CODE == mred.AREA_ID }">selected</c:if>>${areas.AREA_NAME }</option>
												</c:if>
												<c:if test="${mred.AREA_ID == null }">
													<option value="${areas.AREA_CODE }" >${areas.AREA_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
                                            </td>
										</tr>
										
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">聚类:</td>
											<td id="juese">
											<select  name="classcode" id="classcode" onchange="changeClass(this.value)" style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>											
											<c:forEach items="${dataClassList}" var="dataClass">
												<c:if test="${mred.DATA_CLASS_CODE == null }">
													<option value="${dataClass.DATA_CLASS_CODE }" >${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
												<c:if test="${mred.DATA_CLASS_CODE != null }">
												    <option value="${dataClass.DATA_CLASS_CODE }" <c:if test="${dataClass.DATA_CLASS_CODE == mred.DATA_CLASS_CODE }">selected</c:if>>${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
											</td>
										</tr>
										
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">标化类型:</td>
											<td>
											<select  name="typecode" id="typecode"  style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>	
											<c:forEach items="${dataTypeList}" var="dataType">
											    <c:if test="${mred.MEM_DATA_CODE != null }">
													<option value="${dataType.MEM_DATA_CODE }" <c:if test="${dataType.MEM_DATA_CODE == mred.MEM_DATA_CODE }">selected</c:if>>${dataType.DATA_TYPE_NAME }</option>
												</c:if>
												<c:if test="${mred.MEM_DATA_CODE == null }">
													<option value="${dataType.MEM_DATA_CODE }" >${dataType.DATA_TYPE_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
                                            </td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">流程入口:</td>											
											<td><c:if test="${(mred.ORDERBY != null && mred.ORDERBY=='1') || mred.ORDERBY == null}">
												<input type="radio" name="orderby" id="orderby" checked="checked" value="1"/>是&nbsp;&nbsp; <input type="radio" name="orderby" id="orderby" value="0"/>否</td>
											</c:if>
											<c:if test="${mred.ORDERBY != null && mred.ORDERBY=='0'}">
												<input type="radio" name="orderby" id="orderby" value="1"/>是 &nbsp;&nbsp; <input type="radio" name="orderby" id="orderby" checked="checked" value="0"/>否</td>
											</c:if>
											</td>
										</tr>
										<tr>
											<td style="width:90px;text-align: right;padding-top: 13px;">成功跳转:</td>
											<td>
												<select  name="sucessto" id="sucessto"  style="vertical-align:top;" style="width:98%;" >
											     <option value="" > </option>												 
												<c:forEach items="${sFList}" var="listsf">
											    <c:if test="${mred.SUCCESS_REDIRECT_TO != null }">
													<option value="${listsf.MEM_ID }" <c:if test="${listsf.MEM_ID == mred.SUCCESS_REDIRECT_TO }">selected</c:if>>${listsf.MEM_NAME }</option>
												</c:if>
												<c:if test="${mred.SUCCESS_REDIRECT_TO == null }">
													<option value="${listsf.MEM_ID }" >${listsf.MEM_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
											</td>
										</tr>
										<tr>
											<td style="width:90px;text-align: right;padding-top: 13px;">失败跳转:</td>
											<td>
												<select  name="fallto" id="fallto"  style="vertical-align:top;" style="width:98%;" >
											    <option value="" ></option>
												
												<c:forEach items="${sFList}" var="listsf">
											    <c:if test="${mred.FAILURE_REDIRECT_TO != null }">
													<option value="${listsf.MEM_ID  }" <c:if test="${listsf.MEM_ID  == mred.FAILURE_REDIRECT_TO }">selected</c:if>>${listsf.MEM_NAME }</option>
												</c:if>
												<c:if test="${mred.FAILURE_REDIRECT_TO == null }">
													<option value="${listsf.MEM_ID  }" >${listsf.MEM_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">失败后加入AI问题单:</td>
											<td><c:if test="${(mred.QUESTION != null && mred.QUESTION=='1') || mred.QUESTION == null}">
												<input type="radio" name="ques" id="ques" checked="checked" value="1"/>是 &nbsp;&nbsp; <input type="radio" name="ques" id="ques" value="0"/>否</td>
											</c:if>
											<c:if test="${mred.QUESTION != null && mred.QUESTION=='0'}">
												<input type="radio" name="ques" id="ques" value="1"/>是 &nbsp;&nbsp; <input type="radio" name="ques" id="ques" checked="checked" value="0"/>否</td>
											</c:if>
											</td>
										</tr>	
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">匹配内容:</td>
											<td>
											<select  name="noc" id="noc"  style="vertical-align:top;" style="width:98%;" >
												<option value="" ></option>	
												
											    <c:if test="${mred.NOC != null }">
													<option value="05" <c:if test="${'05' == mred.NOC }">selected</c:if>>诊断词</option>
													<option value="09" <c:if test="${'09' == mred.NOC }">selected</c:if>>诊断码</option>
													<option value="30" <c:if test="${'30' == mred.NOC }">selected</c:if>>手术词</option>
													<option value="28" <c:if test="${'28' == mred.NOC }">selected</c:if>>诊疗词</option>
													<option value="19" <c:if test="${'19' == mred.NOC }">selected</c:if>>药品通用名</option>
													<option value="21" <c:if test="${'21' == mred.NOC }">selected</c:if>>药品产品名</option>
													<option value="23" <c:if test="${'23' == mred.NOC }">selected</c:if>>drug剂型</option>
													<option value="03" <c:if test="${'03' == mred.NOC }">selected</c:if>>药品规格</option>
													<option value="17" <c:if test="${'17' == mred.NOC }">selected</c:if>>生产企业</option>
													<option value="ALL" <c:if test="${'ALL' == mred.NOC }">selected</c:if>>全匹配</option>
												</c:if>
												<c:if test="${mred.NOC == null }">
													<option value="05" >诊断词</option>
													<option value="09" >诊断码</option>
													<option value="30" >手术词</option>
													<option value="28" >诊疗词</option>
													<option value="19" >药品通用名</option>
													<option value="21" >药品产品名</option>
													<option value="23" >drug剂型</option>
													<option value="03" >药品规格</option>
													<option value="17" >生产企业</option>
													<option value="ALL" >全匹配</option>
												</c:if>
											
											</select>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">返回值处理:</td>
											<td><c:if test="${(mred.MARK != null && mred.MARK=='1') || mred.MARK == null}">
												<input type="radio" name="mark" id="mark" checked="checked" value="1"/>叠加&nbsp;&nbsp; <input type="radio" name="mark" id="mark" value="0"/>覆盖</td>
											</c:if>
											<c:if test="${mred.MARK != null && mred.MARK=='0'}">
												<input type="radio" name="mark" id="mark" value="1"/>叠加 &nbsp;&nbsp; <input type="radio" name="mark" id="mark" checked="checked" value="0"/>覆盖</td>
											</c:if>
											</td>
										</tr>
										<tr>
											<td style="width:100px;text-align: right;padding-top: 13px;">使用标识:</td>
											<td>
											<select  name="flag" id="flag" onchange="changeFlag(this.value)" style="vertical-align:top;" style="width:98%;" >
											
											    <c:if test="${mred.FLAG != null }">
													<option value="1" <c:if test="${mred.FLAG =='1' }">selected</c:if>>匹配流程用</option>
													<option value="0" <c:if test="${mred.FLAG =='0' }">selected</c:if>>联合字段用</option>
												</c:if>
												<c:if test="${mred.FLAG == null }">
													<option value="1" selected>匹配流程用</option>
													<option value="0" >联合字段用</option>
												</c:if>	
											</select>
											</td>
										</tr>
										<c:if test="${mred.FLAG != null && mred.FLAG =='0' }">
										<tr name="cfbs" id="cfbs"  >
											<td style="width:100px;text-align: right;padding-top: 13px;">触发标识:</td>
											<td>
											<select  name="ifnext" id="ifnext"  style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>											
											<c:forEach items="${dataTypeList}" var="dataType">
											    
												<option value="${dataType.MEM_DATA_CODE }" <c:if test="${dataType.MEM_DATA_CODE == mred.IFNEXT }">selected</c:if>>${dataType.DATA_TYPE_NAME }</option>
												
											</c:forEach>
											</select>
											</td>
										</tr>
										</c:if>
										<c:if test="${mred.FLAG == null || (mred.FLAG != null && mred.FLAG =='1') }">
										<tr name="cfbs" id="cfbs" style="display:none;" >
											<td  style="width:100px;text-align: right;padding-top: 13px;">触发标识:</td>
											<td >
											<select  name="ifnext" id="ifnext"  style="vertical-align:top;" style="width:98%;" >											
											<option value="" ></option>
											<c:forEach items="${dataTypeList}" var="dataType">
											    <option value="${dataType.MEM_DATA_CODE }" >${dataType.DATA_TYPE_NAME }</option>
												
											</c:forEach>
											</select>
											</td>
										</tr>
										</c:if>	
										
										<c:if test="${mred.FLAG != null && mred.FLAG =='0' }">
										<tr name="pkbs" id="pkbs">
											<td style="width:100px;text-align: right;padding-top: 13px;">拼key标识:</td>
											<td>
											<select  name="pinkey" id="pinkey"  style="vertical-align:top;" style="width:98%;" >
											
											    <option value="1" <c:if test="${mred.PINKEY =='1' }">selected</c:if>>是</option>
												<option value="0" <c:if test="${mred.PINKEY =='0' }">selected</c:if>>否</option>										
												
											</select>
											</td>
										</tr>
										</c:if>
										<c:if test="${mred.FLAG == null || (mred.FLAG != null && mred.FLAG =='1') }">
										<tr name="pkbs" id="pkbs" style="display:none;">
											<td style="width:100px;text-align: right;padding-top: 13px;">拼key标识:</td>
											<td>
											<select  name="pinkey" id="pinkey"  style="vertical-align:top;" style="width:98%;" >
											 
													<option value="1" selected>是</option>
													<option value="0" >否</option>
												
											</select>
											</td>
										</tr>
										</c:if>
										
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">匹配程序:</td>
											<td> 											
											<select  name="appMethod" id="appMethod"  style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>	
											<c:forEach items="${softList}" var="softls">
											    <c:if test="${mred.APPLY_METHOD != null }">
													<option value="${softls.SOFT_EN_NAME }" <c:if test="${softls.SOFT_EN_NAME == mred.APPLY_METHOD }">selected</c:if>>${softls.SOFT_CN_NAME }</option>
												</c:if>
												<c:if test="${mred.APPLY_METHOD == null }">
													<option value="${softls.SOFT_EN_NAME }" >${softls.SOFT_CN_NAME }</option>
												</c:if>
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
	
	//控制触发标识的显隐 
	function changeFlag(v) {  
	
		  if(v=="0"){
			 document.getElementById("cfbs").style.display="";
			 document.getElementById("pkbs").style.display="";
		
			
		  }else{
			  document.getElementById("cfbs").style.display="none"; 
			  document.getElementById("pkbs").style.display="none"; 
		  }
		  
		   
	}
	
	//异步请求根据聚类返回相应标化类型 
	function changeClass(v) {  
		   
		  
		   jQuery('#classcode').val(v); 
		   var typecode = jQuery('#typecode');
		   var ifnext=jQuery('#ifnext');
		   var area= document.getElementById("area").value;	
		   changeSF(area);
		    
		    jQuery.ajax({  
		        url : '<%=basePath%>detailRule/selType.do',  
		        type : 'post',  
		        data : { "classcode" : v } ,  
		        dataType : 'json',  
		        success : function (opts) {  		        
		          
		 		   jQuery('#typecode').html(""); 
		 		   jQuery('#ifnext').html(""); 
		 		   var itemsType=opts.listDataType;
			 		 
			 		
			 		 if(itemsType!=null)
				      {	
			 			 typecode.append("<option value = ''> </option>");
			 			 ifnext.append("<option value = ''> </option>");
				         for(var i =0;i<itemsType.length;i++)
				            {
				             var item=itemsType[i];
				             typecode.append("<option value = '"+item.MEM_DATA_CODE+"'>"+item.DATA_TYPE_NAME+"</option>");
				             ifnext.append("<option value = '"+item.MEM_DATA_CODE+"'>"+item.DATA_TYPE_NAME+"</option>");
				            }
				       } else { 
				    	   typecode.empty();  
				    	   ifnext.empty();  
				    	  
				       } 
			 		  
			 		 
			       }  
		        
		        
		        });  
		}

	//异步请求根据区域返回相应匹配流程列表
	function changeSF(v) {  
		   
		  
		jQuery('#area').val(v); 
		   var sucessto = jQuery('#sucessto');
		   var fallto = jQuery('#fallto');
		   var memid= document.getElementById("memid").value;		   
		   var flag=document.getElementById("flag").value;
		   var classcode=document.getElementById("classcode").value;
		    
		    jQuery.ajax({  
		        url : '<%=basePath%>detailRule/selDetail.do',  
		        type : 'post',  
		        data : { "AREA_ID" : v,"MEMID":memid,"FLAG":flag,"classcode" :classcode} ,  
		        dataType : 'json',  
		        success : function (opts) {  		        
		          
		 		   jQuery('#sucessto').html(""); 
		 		   jQuery('#fallto').html(""); 
		 		   var itemsType=opts.listDetail;
			 		 
			 		
			 		 if(itemsType!=null)
				      {
			 			 sucessto.append("<option value = ''> </option>");
			             fallto.append("<option value = ''> </option>");	
				         for(var i =0;i<itemsType.length;i++)
				            {
				        	 
				             var item=itemsType[i];
				             sucessto.append("<option value = '"+item.MEM_ID+"'>"+item.MEM_NAME+"</option>");
				             fallto.append("<option value = '"+item.MEM_ID+"'>"+item.MEM_NAME+"</option>");	
				            }
				       } else { 
				    	   sucessto.empty(); 
				    	   fallto.empty(); 
				    	  
				       } 
			 		  
			 		 
			       }  
		        
		        
		        });  
		}
		
	//保存
	function save(){		
		
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
		
		if($("#classcode").val()==""){
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
		
		
		if($("#noc").val()==""){
			$("#noc").tips({
				side:3,
	            msg:'请选择匹配类型（词or码）',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#noc").focus();
			return false;
		}	
		
		if($("#appMethod").val()==""){
			$("#appMethod").tips({
				side:3,
	            msg:'请选择匹配程序',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#appMethod").focus();
			return false;
		}		
		
				
		$("#detailRuleAddForm").submit();
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
				if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
				 else $('#form-field-select-4').removeClass('tag-input-style');
			});
		}
	});
</script>
</html>