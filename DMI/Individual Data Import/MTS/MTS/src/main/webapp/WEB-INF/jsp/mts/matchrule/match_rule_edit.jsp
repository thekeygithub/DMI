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
									<input type="hidden" name="keys" id="keys" />
									<input type="hidden" name="values" id="values" />
									<input type="hidden" name="rid" id="rid" value="${pd.ruleid }"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">区域:</td>
											<td>
											<select  name="area" id="area"  style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>	
											<c:forEach items="${areaList}" var="areas">
											    <c:if test="${pd.AREA_ID != null }">
													<option value="${areas.AREA_CODE }" <c:if test="${areas.AREA_CODE == pd.AREA_ID }">selected</c:if>>${areas.AREA_NAME }</option>
												</c:if>
												<c:if test="${pd.AREA_ID == null }">
													<option value="${areas.AREA_CODE }" >${areas.AREA_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
                                            </td>
										</tr>
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">聚类:</td>
											<td id="juese">
											<select  name="classcode" id="classcode" onchange="changeClass(this.value)" style="vertical-align:top;" style="width:98%;" >
											
											<c:forEach items="${dataClassList}" var="dataClass">
												<c:if test="${pd.CLASS_CODE == null }">
													<option value="${dataClass.DATA_CLASS_CODE }" <c:if test="${dataClass.DATA_CLASS_CODE == '02' }">selected</c:if>>${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
												<c:if test="${pd.CLASS_CODE != null }">
												    <option value="${dataClass.DATA_CLASS_CODE }" <c:if test="${dataClass.DATA_CLASS_CODE == pd.CLASS_CODE }">selected</c:if>>${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
											</td>
										</tr>
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">标化类型:</td>
											<td>
											<select  name="typecode" id="typecode" onchange="changeType(this.value)" style="vertical-align:top;" style="width:98%;" >
											<option value="" ></option>	
											<c:forEach items="${dataTypeList}" var="dataType">
											    <c:if test="${pd.TYPE_CODE != null }">
													<option value="${dataType.MEM_DATA_CODE }" <c:if test="${dataType.MEM_DATA_CODE == pd.TYPE_CODE }">selected</c:if>>${dataType.DATA_TYPE_NAME }</option>
												</c:if>
												<c:if test="${pd.TYPE_CODE == null }">
													<option value="${dataType.MEM_DATA_CODE }" >${dataType.DATA_TYPE_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
                                            </td>
										</tr>
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">匹配内容:</td>
											<td>											  
											    
									     	<select id="ppnr1" name="ppnr1" multiple="multiple"  style="float:left;width:125px;height:150px;">
									     		<c:forEach items="${keyList}" var="keylist">
									     		   <c:forEach items="${mtsDictKeyList}" var="mtskeyDict">	
									     		     <c:if test="${mtskeyDict.MATCHCODE == keylist }">										     			 
														 <option value="${mtskeyDict.MATCHCODE }" title="${mtskeyDict.DNAME }">${mtskeyDict.DNAME }</option>
													 </c:if>
												   </c:forEach>
												</c:forEach>
									     	</select>
												
											    <div style="float:left;padding-top: 25px;padding-left: 5px;padding-right: 5px;">
											    <input  type="button" onclick="ppnradd();" value="加入"/>
											    <br><br>
											    <input type="button" onclick="ppnrdel();"value="移除"/></div>
                                                
                                                    <select id="ppnr2" name="ppnr2" multiple="multiple"  style="float:left;width:125px;height:150px;">
                                                    	<c:forEach items="${mtsDictKeyList}" var="mtskeyDict">
															<option value="${mtskeyDict.MATCHCODE }" title="${mtskeyDict.DNAME }">${mtskeyDict.DNAME }</option>
														</c:forEach>
													</select>
												
                                              
                                            </td>
										</tr>
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">返回值内容:</td>
											<td>
												
												<select id="fhznr1" name="fhznr1" multiple="multiple"  style="float:left;width:125px;height:150px;">
													<c:forEach items="${valueList}" var="valuelist">
											     		   <c:forEach items="${mtsDictValueList}" var="mtsValDict">	
											     		     <c:if test="${mtsValDict.MATCHCODE == valuelist }">										     			 
																 <option value="${mtsValDict.MATCHCODE }" title="${mtsValDict.DNAME }">${mtsValDict.DNAME }</option>
															 </c:if>
														   </c:forEach>
													</c:forEach>
												</select>
											    <div style="float:left;padding-top: 25px;padding-left: 5px;padding-right: 5px;">
											    <input  type="button" onclick="fhznradd();" value="加入"/>
											    <br><br>
											    <input type="button" onclick="fhznrdel();" value="移除"/></div>
                                                
                                                	<select id="fhznr2" name="fhznr2" multiple="multiple"  style="float:left;width:125px;height:150px;">
														  <c:forEach items="${mtsDictValueList}" var="mtsValDict">
															<option value="${mtsValDict.MATCHCODE }" title="${mtsValDict.DNAME }" >${mtsValDict.DNAME }</option>
														</c:forEach>
													</select>
												
											</td>
										</tr>
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">是否需要切词:</td>
											<td>										
											<c:if test="${(pd.IF_NLP != null &&pd.IF_NLP=='1') || pd.IF_NLP == null}">
												<input type="radio" name="ifnlp" id="ifnlp" checked="checked" value="1"/>是 &nbsp;&nbsp; <input type="radio" name="ifnlp" id="ifnlp" value="0"/>否</td>
											</c:if>
											<c:if test="${pd.IF_NLP != null &&pd.IF_NLP=='0'}">
												<input type="radio" name="ifnlp" id="ifnlp" value="1"/>是 &nbsp;&nbsp; <input type="radio" name="ifnlp" id="ifnlp" checked="checked" value="0"/>否</td>
											</c:if>
										</tr>
										
										<tr>
											<td style="width:60px;text-align: right;padding-top: 13px;">标化入参:</td>
											<td>										
											<c:if test="${(pd.STANDARDE != null &&pd.STANDARDE=='1') || pd.STANDARDE == null}">
												<input type="radio" name="stand" id="stand" checked="checked" value="1"/>是 &nbsp;&nbsp; <input type="radio" name="stand" id="stand" value="0"/>否</td>
											</c:if>
											<c:if test="${pd.STANDARDE != null &&pd.STANDARDE=='0'}">
												<input type="radio" name="stand" id="stand" value="1"/>是 &nbsp;&nbsp; <input type="radio" name="stand" id="stand" checked="checked" value="0"/>否</td>
											</c:if>
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
		   var ppnr2Value = jQuery('#ppnr2'); 
		   var fhznr2Value = jQuery('#fhznr2'); 
		   var typecode = jQuery('#typecode');
		   
		   
		    
		    jQuery.ajax({  
		        url : '<%=basePath%>matchRule/selDict.do',  
		        type : 'post',  
		        data : { "classcode" : v } ,  
		        dataType : 'json',  
		        success : function (opts) {  
		        
		           jQuery('#fhznr2').html("");  
		 		   jQuery('#ppnr2').html("");
		 		   jQuery('#fhznr1').html("");  
		 		   jQuery('#ppnr1').html(""); 
		 		   jQuery('#typecode').html(""); 
		 		   var itemsK=opts.listK; 
		 		   var itemsV=opts.listV;
		 		   var itemsType=opts.listDataType;
			 		  if(itemsK!=null)
				      {
				         for(var i =0;i<itemsK.length;i++)
				            {
				             var item=itemsK[i];
				             ppnr2Value.append("<option value = '"+item.MATCHCODE+"' title = '"+item.DNAME+"' >"+item.DNAME+"</option>");				            
				            }
				       } else { 
				    	   ppnr2Value.empty();  
				    	  
				       } 
			 		
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
			 		  
			 		 if(itemsV!=null)
				      {
				         for(var i =0;i<itemsV.length;i++)
				            {
				             var item=itemsV[i];				             
				             fhznr2Value.append("<option value = '"+item.MATCHCODE+"' title = '"+item.DNAME+"'>"+item.DNAME+"</option>");
				            }
				       } else { 				    	   
				    	   fhznr2Value.empty();   
				       } 
			       }  
		        
		        
		        });  
		}

	//异步请求查询字典列表的方法并返回json数组 
	function changeType(v) {  
		   
		  
		   jQuery('#typecode').val(v); 
		   var ppnr2Value = jQuery('#ppnr2'); 
		   var fhznr2Value = jQuery('#fhznr2'); 
		   var classcode = jQuery('#classcode').val();				  
		   
		    
		    jQuery.ajax({  
		        url : '<%=basePath%>matchRule/selDict.do',  
		        type : 'post',  
		        data : { "classcode" : classcode,"typecode":v } ,  
		        dataType : 'json',  
		        success : function (opts) {  
		        
		           jQuery('#fhznr2').html("");  
		 		   jQuery('#ppnr2').html("");
		 		   jQuery('#fhznr1').html("");  
		 		   jQuery('#ppnr1').html(""); 
		 	
		 		   var itemsK=opts.listK; 
		 		   var itemsV=opts.listV;
		 	
			 		  if(itemsK!=null)
				      {
				         for(var i =0;i<itemsK.length;i++)
				            {
				             var item=itemsK[i];
				             ppnr2Value.append("<option value = '"+item.MATCHCODE+"' title = '"+item.DNAME+"' >"+item.DNAME+"</option>");				            
				            }
				       } else { 
				    	   ppnr2Value.empty();  
				    	  
				       } 
			 		
			 		 
			 		  
			 		 if(itemsV!=null)
				      {
				         for(var i =0;i<itemsV.length;i++)
				            {
				             var item=itemsV[i];				             
				             fhznr2Value.append("<option value = '"+item.MATCHCODE+"' title = '"+item.DNAME+"'>"+item.DNAME+"</option>");
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
  			var n=0;
		  	for(var h=0;h<rightOption.length;h++){
	
				var zn=leftOption.length-1;				
		  		if(rightOption[h].text==leftText  ){ n=1;break;}
		  	}
		  if(n==0){
			  var option=$('<option value='+leftValue+' title='+leftText+'>'+leftText+'</option>'); 
			  right.append(option);
		  }		
		
		}
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
		var obj = document.getElementById('ppnr1');
		var options = obj.options;
		
		if(options.length==0){
			$("#ppnr1").tips({
				side:3,
	            msg:'匹配内容不能为空',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#ppnr1").focus();
			return false;
		}
			
		
		
		var obj1 = document.getElementById('fhznr1');
		var options1 = obj1.options;
		if(options1.length==0){
			$("#fhznr1").tips({
				side:3,
	            msg:'返回值内容不能为空',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#fhznr1").focus();
			return false;
		}
		var ppnrvalue="";
		for(var i=0;i<options.length;i++){
			if(i==options.length-1){
				ppnrvalue=ppnrvalue+obj.options[i].value;	
			}else{
				ppnrvalue=ppnrvalue+obj.options[i].value+"#" ;	
			}
		}
		var fhzvalue="";
		for(var i=0;i<options1.length;i++){
			if(i==options1.length-1){
				fhzvalue=fhzvalue+obj1.options[i].value;	
			}else{
				fhzvalue=fhzvalue+obj1.options[i].value+"@#&" ;	
			}
		}
		
		document.getElementById("values").value=fhzvalue;
		document.getElementById("keys").value=ppnrvalue;
		
				
		$("#matchRuleAddForm").submit();
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