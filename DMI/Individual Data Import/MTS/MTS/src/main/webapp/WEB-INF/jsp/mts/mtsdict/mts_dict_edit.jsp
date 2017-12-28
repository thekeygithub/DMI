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
								<form action="mtsDict/${msg}.do" name="mtsDictAddForm" id="mtsDictAddForm" method="post">
									<input type="hidden" name="DID" id="DID" value="${pd.DID}"/>
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">数据字典名称:</td>
											<td><input type="text" name="DNAME" id="DNAME_" value="${pd.DNAME}"  maxlength="32" placeholder="这里输入数据字典名称" title="数据字典名称" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">加载规则码:</td>
											<td><input type="text" name="LOADCODE" id="LOADCODE_" value="${pd.LOADCODE }" maxlength="32" placeholder="这里输入加载规则码" title="加载规则码" style="width:98%;"/></td>
										</tr>
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">匹配规则码:</td>
											<td><input type="text" name="MATCHCODE" id="MATCHCODE_" value="${pd.MATCHCODE }" maxlength="32" placeholder="这里输入匹配规则码" title="匹配规则码"  style="width:98%;"/></td>
										</tr>	
										<!-- 聚类 -->
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">聚类:</td>
											<td id="juese">
											<select  name="DATACLASS" id="DATACLASS_" onchange="changeClass(this.value);" style="vertical-align:top;" style="width:98%;" >
											<c:if test="${pd.DATACLASS == null }">
													<option value="" >请选择聚类 </option>
											</c:if>
											<c:forEach items="${dataClassList}" var="dataClass">
												<c:if test="${pd.DATACLASS == null }">
													<option value="${dataClass.DATA_CLASS_CODE }">${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
												<c:if test="${pd.DATACLASS != null }">
												    <option value="${dataClass.DATA_CLASS_CODE }" <c:if test="${dataClass.DATA_CLASS_CODE == pd.DATACLASS }">selected</c:if>>${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
											</c:forEach>
											</select>
											</td>
										</tr>
										
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">标化类型:</td>
											<td>
											<select  name="DATATYPE" id="DATATYPE_"  style="vertical-align:top;" style="width:98%;" >
												<c:if test="${pd.DATATYPE == null }">
														<option value="" >请选择标化类型</option>
												</c:if>
												<c:forEach items="${dataTypeList}" var="dataType">
												    <c:if test="${pd.DATATYPE != null }">
														<option value="${dataType.MEM_DATA_CODE }" <c:if test="${dataType.MEM_DATA_CODE == pd.DATATYPE }">selected</c:if>>${dataType.DATA_TYPE_NAME }</option>
													</c:if>
													<c:if test="${pd.DATATYPE == null }">
														<option value="${dataType.MEM_DATA_CODE }" >${dataType.DATA_TYPE_NAME }</option>
													</c:if>
												</c:forEach>
											</select>
                                            </td>
										</tr>
										<!-- 聚类 -->
										
										<!-- 字典类型 -->
										<tr>
											<td style="width:79px;text-align: right;padding-top: 13px;">字典类型:</td>
											<td id="juese">
												<select  name="TYPE" id="TYPE_" style="vertical-align:top;" style="width:98%;" >
													<option value="">请选择字典类型</option>
												    <option value="KEYRULE" <c:if test="${pd.TYPE eq 'KEYRULE' }">selected</c:if>>KEY规则</option>
												    <option value="VALUERULE" <c:if test="${pd.TYPE eq 'VALUERULE' }">selected</c:if>>VALUE规则</option>
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
	
	
	function validateName(val){
		jQuery.ajax({  
	        url : '<%=basePath%>mtsDict/selByName.do',  
	        type : 'post',  
	        data : { "name" : val } ,  
	        dataType : 'text',  
	        success : function (opts) {  
	        	  if(opts == "1"){
	        		  alert("存在");
	        	  }else{
	        		  alert("不存在");
	        	  }
	 		      /* jQuery('#DATATYPE_').html(""); 
	 		      var itemsType=opts.listDataType;
		 		  if(itemsType!=null){
			         for(var i =0;i<itemsType.length;i++)
			            {
			             var item=itemsType[i];
			             typecode.append("<option value = '"+item.MEM_DATA_CODE+"'>"+item.DATA_TYPE_NAME+"</option>");				            
			            }
			       } else { 
			    	   typecode.empty();  
			       }  */
	        }
	     });  
	}
	
	//异步请求查询字典列表的方法并返回json数组 
	function changeClass(v) {  
		   jQuery('#DATACLASS_').val(v); 
		   var typecode = jQuery('#DATATYPE_');
		   jQuery.ajax({  
		        url : '<%=basePath%>matchRule/selDict.do',  
		        type : 'post',  
		        data : { "classcode" : v } ,  
		        dataType : 'json',  
		        success : function (opts) {  
		 		      jQuery('#DATATYPE_').html(""); 
		 		      var itemsType=opts.listDataType;
			 		  if(itemsType!=null){
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
		if($("#DNAME_").val()==""){
			$("#DNAME_").tips({
				side:3,
	            msg:'请输入数据字典名称',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#DNAME_").focus();
			return false;
		}
		if($("#LOADCODE_").val()==""){
			$("#LOADCODE_").tips({
				side:3,
	            msg:'请输入加载规则码',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#LOADCODE_").focus();
			return false;
		}
		if($("#MATCHCODE_").val()==""){
			$("#MATCHCODE_").tips({
				side:3,
	            msg:'请输入匹配规则码',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#MATCHCODE_").focus();
			return false;
		}
		if($("#DATACLASS_").val()==""){
			$("#DATACLASS_").tips({
				side:3,
	            msg:'请选择聚类类型',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#DATACLASS_").focus();
			return false;
		}
		if($("#DATATYPE_").val()==""){
			$("#DATATYPE_").tips({
				side:3,
	            msg:'请选择标化类型',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#DATATYPE_").focus();
			return false;
		}
		
		if($("#TYPE_").val()==""){
			$("#TYPE_").tips({
				side:3,
	            msg:'请选择字典类型',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TYPE_").focus();
			return false;
		}
		
		$("#mtsDictAddForm").submit();
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