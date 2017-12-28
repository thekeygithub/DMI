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
							<form action="mtsConfigTest/readExcel.do" name="Form" id="Form" method="post" enctype="multipart/form-data">
								<div id="zhongxin">
								<table style="width:95%;" >
									<tr>
										<td  style="padding-top: 20px;width:80px">批次名称:</td>
										<td  style="padding-top: 20px;">
											<input type="text" style="width:100%"name="PT_NAME" id="PT_NAME" />
										</td>
									</tr>
								
									<tr>
										<td  style="padding-top: 20px;">批次号:</td>
										<td  style="padding-top: 20px;">
										${pd.batchNum}
										<input type="hidden" name="BATCH_NUM" id="BATCH_NUM" value="${pd.batchNum}"/>
										</td>
									</tr>
									<tr>
										<td style="padding-top: 20px;" colspan="2"><input type="file" id="excel" name="excel" style="width:50px;" onchange="fileType(this)" /></td>
									</tr>
									<tr>
										<td style="text-align: center;padding-top: 10px;" colspan="3">
											<a class="btn btn-mini btn-primary" onclick="save();">导入</a>
											<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
											<%-- <a class="btn btn-mini btn-success" onclick="window.location.href='<%=basePath%>/mtsData/downExcel.do'">下载模版</a> --%>
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
				no_file:'请选择导入数据文件EXCEL ...',
				btn_choose:'选择',
				btn_change:'更改',
				droppable:false,
				onchange:null,
				thumbnail:false, //| true | large
				whitelist:'xls|xlsx',
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
			
			if($("#excel").val()=="" || document.getElementById("excel").files[0] =='请选择xls或xlsx格式的数据文件'){
				$("#excel").tips({
					side:3,
		            msg:'请选择文件',
		            bg:'#AE81FF',
		            time:3
		        });
				return false;
			}
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		function fileType(obj){
			var fileType=obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
		    if(fileType != '.xls' && fileType != '.xlsx'){
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
		

	</script>
</body>
</html>