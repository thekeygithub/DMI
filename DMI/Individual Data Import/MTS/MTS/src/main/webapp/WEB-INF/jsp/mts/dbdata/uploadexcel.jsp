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

<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>


<link href="static/area/css/city-picker.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	
</script>
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
							<form action="#" name="Form" id="Form" method="post" enctype="multipart/form-data">
								<div id="zhongxin">
								<table style="width:95%;" >
									<tr>
										<td  style="padding-top: 20px;width:80px">批次名称:</td>
										<td  style="padding-top: 20px;">
											<input type="text" style="width:100%"name="EXPORT_NAME" id="EXPORT_NAME" />
										</td>
									</tr>
									<tr>
										<td  style="padding-top: 20px;">数据来源:</td>
										<td  style="padding-top: 20px;">
										<select  name="DATA_SOURCE_CODE" id="DATA_SOURCE_CODE" style="vertical-align:top;" style="width:98%;" >
											<option value="" >请选择数据来源</option>
											<c:forEach items="${listDataSource}" var="mtsDataSource" varStatus="vs">
												<c:if test="${mtsDataSource.FLAG != null }">
												<option value="${mtsDataSource.FLAG }" >${mtsDataSource.DESCRIPTION }</option>
												</c:if>
											</c:forEach>
										</select>
										</td>
									</tr>
									<tr>
										<td  style="padding-top: 20px;">标准化库:</td>
										<td  style="padding-top: 20px;">
										<select  name="AREA" id="AREA" style="vertical-align:top;" style="width:98%;" >
											<option value="" >请选择标准化库</option>
											<c:forEach items="${listMtsArea}" var="mtsArea" varStatus="vs">
												<c:if test="${mtsArea.AREA_CODE != null }">
												<option value="${mtsArea.AREA_CODE }" >${mtsArea.AREA_NAME }</option>
												</c:if>
											</c:forEach>
										</select>
										</td>
									</tr>
									<tr>
										<td  style="padding-top: 20px;">使用聚类:</td>
										<td  style="padding-top: 20px;">
											<select  name="classcode" id="classcode" onchange="changeClass(this.value)" style="vertical-align:top;" style="width:98%;" >
												<option value="" >请选择聚类</option>
												<c:forEach items="${listDataClass}" var="mtsDataClass">
													<c:if test="${mtsDataClass.DATA_CLASS_CODE != null }">
													    <option value="${mtsDataClass.DATA_CLASS_CODE }" >${mtsDataClass.DATA_CLASS_NAME }</option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr style="display: none;">
										<td  style="padding-top: 20px;">使用标化:</td>
										<td  style="padding-top: 20px;">
											<select  name="typecode" id="typecode"  style="vertical-align:top;" style="width:98%;" >
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
										<td  style="padding-top: 20px;">批次号:</td>
										<td  style="padding-top: 20px;">
										${batchNum}
										<input type="hidden" name="BATCH_NUM" id="BATCH_NUM" value="${batchNum}"/>
										
										<input type="hidden" name="DB_DATA_TYPE" id="DB_DATA_TYPE" value="${DB_DATA_TYPE}"/>
										</td>
									</tr>
									<tr>
										<td  style="padding-top: 20px;">文件类别:</td>
										<td  style="padding-top: 20px;">
											<select  name="fileType" id="fileType"  style="vertical-align:top;" style="width:98%;" >
												<option value="0" selected>EXCEL(适用于数据量在5万条以下)</option>	
												<option value="1" >TXT(适用于数据量在5万条以上)</option>	
											</select>
										</td>
									</tr>
									<tr>
										<td style="padding-top: 20px;" colspan="2"><input type="file" id="excel" name="excel" style="width:50px;" onchange="fileType(this)" /></td>
									</tr>
									<tr>
										<td style="text-align: center;padding-top: 10px;" colspan="3">
											<a class="btn btn-mini btn-primary" onclick="save();">导入</a>
											<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
										</td>
									</tr>
								</table>
								</div>
								<div id="zhongxin2" class="center" style="display:none"><br/><img src="static/images/jzx.gif" /><br/><h4 class="lighter block green"></h4></div>
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
	<script src="static/area/js/jquery.js"></script>
	<script src="static/area/js/bootstrap.js"></script>
	<script src="static/area/js/city-picker.data.js"></script>
	<script src="static/area/js/city-picker.js"></script>
	<script src="static/area/js/main.js"></script>
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 上传控件 -->
	<script src="static/ace/js/ace/elements.fileinput.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		<!--改变导入框的大小-->
		/* var container = window.parent.document.getElementById("_Container_0");
		$(container).css("width", "500px");
		$(container).css("height", "400px") */
		
		$(top.hangge());
		$(function() {
			//上传
			$('#excel').ace_file_input({
				no_file:'请选择导入数据文件...',
				btn_choose:'选择',
				btn_change:'更改',
				droppable:false,
				onchange:null,
				thumbnail:false, //| true | large
				whitelist:'xls|xlsx|txt',
				blacklist:'gif|png|jpg|jpeg'
				//onchange:''
			});
		});
		//保存
		function save(){
			if($("#EXPORT_NAME").val()==""){
				$("#EXPORT_NAME").tips({
					side:3,
		            msg:'请输入批次名称',
		            bg:'#AE81FF',
		            time:3
		        });
				$("#EXPORT_NAME").focus();
				return false;
			}
			if($("#DATA_SOURCE_CODE").val()==""){
				$("#DATA_SOURCE_CODE").tips({
					side:3,
		            msg:'请选择数据来源',
		            bg:'#AE81FF',
		            time:3
		        });
				$("#DATA_SOURCE_CODE").focus();
				return false;
			}
			if($("#AREA").val()==""){
				$("#AREA").tips({
					side:3,
		            msg:'请选择标准化库',
		            bg:'#AE81FF',
		            time:3
		        });
				$("#AREA").focus();
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
			/* if($("#typecode").val()==""){
				$("#typecode").tips({
					side:3,
		            msg:'请选择标化',
		            bg:'#AE81FF',
		            time:3
		        });
				$("#typecode").focus();
				return false;
			} */
			if($("#excel").val()=="" || document.getElementById("excel").files[0] =='请选择正确格式的数据文件'){
				$("#excel").tips({
					side:3,
		            msg:'请选择文件',
		            bg:'#AE81FF',
		            time:3
		        });
				return false;
			}
			var DB_DATA_TYPE = $("#DB_DATA_TYPE").val();
			if(DB_DATA_TYPE == 0){
				document.Form.action="mtsDBData/readExcel.do";
			}else if(DB_DATA_TYPE == 1){
				document.Form.action="mtsDBData/readExcelNew.do";
			}
			
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		function fileType(obj){
			var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		    if(fileType != '.xls' && fileType != '.xlsx' && fileType != '.txt'){
		    	$("#excel").tips({
					side:3,
		            msg:'请上传正确的文件格式',
		            bg:'#AE81FF',
		            time:3
		        });
		    	$("#excel").val('');
		    	document.getElementById("excel").files[0] = '请选择正确格式的文件';
		    }
		}
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
					             ppnr2Value.append("<option value = '"+item.MATCHCODE+"'>"+item.DNAME+"</option>");				            
					            }
					       } else { 
					    	   ppnr2Value.empty();  
					    	  
					       } 
				 		
				 		 if(itemsType!=null)
					      {
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
					             fhznr2Value.append("<option value = '"+item.MATCHCODE+"'>"+item.DNAME+"</option>");
					            }
					       } else { 				    	   
					    	   fhznr2Value.empty();   
					       } 
				       }  
			        
			        
			        });  
			}

	</script>
</body>
</html>