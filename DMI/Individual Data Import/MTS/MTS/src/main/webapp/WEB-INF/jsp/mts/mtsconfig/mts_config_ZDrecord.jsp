<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
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
						<form action="mtsConfigDR/listMtsRecord.do" method="post" name="mtsZDrecordForm" id="mtsZDrecordForm">
						<input type="hidden" name="TITEL_EN" id="TITEL_EN" value="${pd.TITEL_EN}"/>
						<input type="hidden" name="PT_ID" id="PT_ID" value="${pd.PT_ID}"/>
						<input type="hidden" name="TYPE" id="TYPE" value="${pd.TYPE}"/>
						<input type="hidden" name="AREA_CODE" id="AREA_CODE" value="${pd.AREA_CODE}"/>
						<input type="hidden" name="CLASS_CODE" id="CLASS_CODE" value="${pd.CLASS_CODE}"/>
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="SNAME" value="${pd.SNAME }" placeholder="原术语名称" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								<td style="padding-left:2px;">									
								  <select  name="WFBH" id="WFBH"  style="width:150px;" >
									<option value=""  >无法干预类型 </option>									
									<c:forEach items="${listWFBH}" var="wfbhlist">
										<c:if test="${pd.WFBH == null }">
											<option value="${wfbhlist.TYPE_NAME }" >${wfbhlist.TYPE_NAME }</option>
										</c:if>
										<c:if test="${pd.WFBH != null }">
										    <option value="${wfbhlist.TYPE_NAME }" <c:if test="${wfbhlist.TYPE_NAME == pd.WFBH }">selected</c:if>>${wfbhlist.TYPE_NAME }</option>
										</c:if>
									</c:forEach>
								  </select>
								</td>	
								<td style="padding-left:2px;">									
								  <select  name="NLP_STATUS" id="NLP_STATUS"  style="width:150px;" >
									<option value="" >匹配标识 </option>										
											<option value="1"  <c:if test="${pd.NLP_STATUS == '1' }">selected</c:if>>标化成功</option>
											<option value="0" <c:if test="${pd.NLP_STATUS == '0' }">selected</c:if>>未标化</option>
											<option value="2" <c:if test="${pd.NLP_STATUS == '2' }">selected</c:if>>无法标化</option>
											<option value="3" <c:if test="${pd.NLP_STATUS == '3' }">selected</c:if>>人工切词</option>
											<option value="4" <c:if test="${pd.NLP_STATUS == '4' }">selected</c:if>>人工标化</option>
										
								  </select>
								</td>								
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<c:choose>
									  <c:when test="${not empty listZDtitle}">
									    <c:forEach items="${listZDtitle}" var="pd" >
											<th class="center" onclick="seq('${pd.TITEL_EN}');" >${pd.TITEL_NAME}</th>
										</c:forEach>
									  </c:when>
									 </c:choose>
									 <th class="center">操作</th>
								</tr>
							</thead>
													
							<tbody >
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty listZDrecord}">
									<c:forEach items="${listZDrecord}" var="pd" >
										<tr>									

											<td class="center">${pd.SOURCETEXT}</td>
											<td class="center">${pd.SOURCEDATA}</td>
											<td class="center">${pd.NLP_SNAME}</td>
											<td class="center">${pd.XY_NAME}</td>
											<td class="center">${pd.XY_CODE12}</td>										
											<td class="center">${pd.WSXY_NAME}</td>
											<td class="center">${pd.WSXY_CODE12}</td>											
											<td class="center">${pd.ZY_NAME}</td>
											<td class="center">${pd.ZY_CODE1}</td>
											<td class="center">${pd.WSZY_NAME}</td>
											<td class="center">${pd.WSZY_CODE1}</td>
											<td class="center">${pd.SS_NAME}</td>
											<td class="center">${pd.SS_CODE1}</td>
											<td class="center">${pd.WSSS_NAME}</td>
											<td class="center">${pd.WSSS_CODE1}</td>
											<td class="center" >${pd.SPEC }</td>
											<td class="center" >${pd.DOUB }</td>
											<td class="center">${pd.WFBH_NAME}</td>
											
											<td class="center" >
												<c:if test="${pd.NLP_STATUS == 1 }">
															<span>标化成功</span>
												</c:if>
												<c:if test="${pd.NLP_STATUS == 0 }">
															<span >未标化</span>
												</c:if>
												<c:if test="${pd.NLP_STATUS == 2 }">
															<span >无法标化</span>
												</c:if>
												<c:if test="${pd.NLP_STATUS == 3 }">
															<span >人工切词</span>
												</c:if>
												<c:if test="${pd.NLP_STATUS == 4 }">
															<span >人工标化</span>
												</c:if>
											</td>
																																										
											<td class="center">
												<div class="hidden-sm hidden-xs btn-group">
												  <c:if test="${pd.NLP_STATUS != 3 }">
													<a class="btn btn-mini btn-purple" onclick="manualStandardize('${pd.T_ID}','${pd.NLP_SNAME}');">
														<i class="icon-pencil"></i>人工标化
													</a>
													</c:if>													
													<a class="btn btn-mini btn-purple" onclick="manualNLP('${pd.T_ID}');">
														<i class="icon-pencil"></i>NLP切分
													</a>													
													<c:if test="${pd.NLP_STATUS == 3 }">
														<a class="btn btn-mini btn-purple" onclick="manualNLP('${pd.T_ID}');">
															<i class="icon-pencil"></i>执行标化
														</a>
													</c:if>
												</div>
											</td>
											
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="11" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						
					<div class="page-header position-relative">
					<table style="width:100%;">
						<tr>							
							<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
						</tr>
					</table>
					</div>
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

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	</body>

<script type="text/javascript">
$(top.hangge());


function showP(DB_DATA_ID,BATCH_NUM){
	//alert($("#"+DB_DATA_ID).text());
	var toolbarDivContent = '<div class="progress progress-striped active"><div class="progress-bar progress-bar-success" style="width:80%;text-align:center;" role="progressbar" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"><span style="color:yellow;font-weight: bold;text-align:center;">41%</span></div></div>';
	//alert(DB_DATA_ID);
	//alert(toolbarDivContent);
	$("#"+DB_DATA_ID).html(toolbarDivContent);
}

//页面进度刷新定时器
//$(function(){ 
//	var t1 = window.setInterval(updateStatus,10000000); 
//});

function updateStatus(){
	var DB_DATA_ID_STRING = '';
	$("div[name='status_flag']").each(function(){
		var DB_DATA_ID_STR = this.id;
		if(DB_DATA_ID_STR.indexOf("_") != -1){
			var DB_DATA_ID = DB_DATA_ID_STR.substring(0,DB_DATA_ID_STR.indexOf("_"))
			if(DB_DATA_ID_STRING == ''){
				DB_DATA_ID_STRING = DB_DATA_ID;
			}else{
				DB_DATA_ID_STRING = DB_DATA_ID_STRING + "@" + DB_DATA_ID;
			}
		}
	});
	if(DB_DATA_ID_STRING != ''){
		$.ajax({ 
			type: "post", 
			url: "<%=basePath%>mtsDBData/updateStatus.do?DB_DATA_ID_STRING="+DB_DATA_ID_STRING, 
			cache:false, 
			async:false, 
			success: function(result){ 
				if(result != ''){
					var jsondata = JSON.parse(result);
					var db_data = DB_DATA_ID_STRING.split('@');
					for(var j = 0;j < db_data.length;j++){
						var resId = db_data[j];
						var status = jsondata[resId]['status'];
						var percentage = jsondata[resId]['percentage'];
						var statusDivContent = "";
						if(status == '0'){
							statusDivContent = '<span style="color: red">未标化</span>';
						}else if(status == '1'){
							statusDivContent = '<span style="color: green">已标化</span>';
						}else if(status == '2'){
							if(percentage == '100'){
								statusDivContent = '<span style="color: green">已标化</span>';
							}else{
								statusDivContent = "<div class='progress progress-striped active'><div class='progress-bar progress-bar-success' style='width:"+percentage+"%;text-align:center;' role='progressbar' aria-valuenow='"+percentage+"' aria-valuemin='0' aria-valuemax='100'><span style='color:red;font-weight: bold;text-align:center;'>"+percentage+"%</span></div></div>";
							}
						}
						$("#"+resId+"_status").html(statusDivContent);
					}
				}
			} 
		});
	}
}
//筛选
function searchs(){	
	top.jzts();
	$("#mtsZDrecordForm").submit();
}

//排序
function seq(TITEL_EN){	
	top.jzts();	
	document.getElementById('TITEL_EN').value=TITEL_EN;
	$("#mtsZDrecordForm").submit();
}


function manualNLP(T_ID){	
	top.jzts();
	var diag = new top.Dialog();
	
	var TITEL_EN=document.getElementById('TITEL_EN').value;	
	var PT_ID=document.getElementById('PT_ID').value;	
	var TYPE=document.getElementById('TYPE').value;
	var AREA_CODE=document.getElementById('AREA_CODE').value;
	var CLASS_CODE=document.getElementById('CLASS_CODE').value;
	var SNAMEn=document.getElementsByName('SNAME')[0].value;
	var WFBHn=document.getElementById('WFBH').value;
	var NLP_STATUS=document.getElementById('NLP_STATUS').value;
	var SNAME= encodeURI(encodeURI(SNAMEn));
	var WFBH= encodeURI(encodeURI(WFBHn));
	diag.Drag=true;
	diag.URL = '<%=path%>/mtsConfigDR/manuNLP.do?T_ID='+T_ID+'&TITEL_EN='+TITEL_EN +'&PT_ID='+PT_ID +'&TYPE='+TYPE +'&AREA_CODE='+AREA_CODE +'&CLASS_CODE='+CLASS_CODE +'&SNAME='+SNAME +'&WFBH='+WFBH +'&NLP_STATUS='+NLP_STATUS;
	diag.Title ="NLP人工切词页面";
	diag.Width = $(top.window).width();
	diag.Height = $(top.window).height();
	diag.CancelEvent = function(){ //关闭事件
		if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			nextPage(${page.currentPage});
		}
		diag.close();
	};
	diag.show();
	
	
}
function manualStandardize(infoId,NLP_RESULT){
	top.jzts();
	var diag = new top.Dialog();
	diag.Drag=true;
	diag.URL = '<%=path%>/common/diagAll.do?ONTO_TYPE=1&id='+infoId +'&NLP_RESULT='+NLP_RESULT;
	diag.Title ="手动标化页面";
	diag.Width = $(top.window).width();
	diag.Height = $(top.window).height();
	diag.CancelEvent = function(){ //关闭事件
	diag.close();
	};
	diag.show();

}

function transferData(id,NLP_RESULT,res){
	
	/* [{"id":"188160","name":"血液及造血器官疾病和涉及免疫机制的某些疾患(章)","main_code":"D50-D89","add_code":"","onto_type":"1"}
	,{"id":"67513","name":"undefined","main_code":"","add_code":"","onto_type":"8"}
	,{"id":"178609","name":"第十四章  女性生殖器官手术","main_code":"65-71","add_code":"","onto_type":"2"}] */
	
	/* alert("id:"+id);
	alert("res:"+res);
	alert("batchNum"+bathNum); */
	
	var xqo = eval('(' + res + ')');
	
	var XY_NAME = "";
	var XY_CODE1 = "";
	var XY_CODE2 = "";
	var ZY_NAME = "";
	var ZY_CODE1 = "";
	var SS_NAME = "";
	var SS_CODE1 = "";
	
	
	for(var i in xqo){
		var onto_type = xqo[i].onto_type;
		if(onto_type == 1){
			XY_NAME = xqo[i].name;
			XY_CODE1 = xqo[i].main_code;
			XY_CODE2 = xqo[i].add_code;
		}else if(onto_type == 2){
			SS_NAME = xqo[i].name;
			SS_CODE1 = xqo[i].main_code;
		}else if(onto_type == 8){
			ZY_NAME = xqo[i].name;
			ZY_CODE1 = xqo[i].main_code;
		}
	}
	
	var T_ID = id;
	var NLP_SNAME = NLP_RESULT;
	
	
	$.ajax({
		url : '<%=path%>/mtsDBData/editMtsRecordInfo.do',  
		async: false,
        type : 'post',  
        data : {"T_ID" : T_ID ,"NLP_SNAME" : NLP_SNAME , "XY_NAME":XY_NAME ,  "XY_CODE1":XY_CODE1, "XY_CODE2":XY_CODE2 , "ZY_NAME":ZY_NAME , "ZY_CODE1":ZY_CODE1 , "SS_NAME":SS_NAME , "SS_CODE1":SS_CODE1 },  
        dataType : 'json',  
        success : function (data) {  
           if(data.result == 'true'){
        	   $('#mtsDBDataForm').submit();
           }
	    }  
     });
}

function viewResult(PT_ID){	
	top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.URL = '<%=path%>/mtsConfigDR/listMtsCongfigDetail.do?PT_ID='+PT_ID;
	 diag.Title ="标化结果详细";
	 diag.Width = $(top.window).width();
	 diag.Height = $(top.window).height();
	 diag.CancelEvent = function(){ //关闭事件
		diag.close();
	 };
	 diag.show();

}


function downloadTemplate(){
	window.location.href='<%=basePath%>mtsDBData/downloadTemplate.do';
}

//打开上传excel页面
function importExcel(DB_DATA_TYPE){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="导入数据文件EXCEL";
	 diag.URL = '<%=path%>/mtsDBData/goUploadExcel.do?DB_DATA_TYPE='+DB_DATA_TYPE;
	 diag.Width = 1000;
	 diag.Height = 800;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			 if('${page.currentPage}' == '0'){
				 top.jzts();
				 setTimeout("self.location.reload()",100);
			 }else{
				 nextPage(${page.currentPage});
			 }
		}
		diag.close();
	 };
	 diag.show();
}	





//删除
function delMtsDBData(DB_DATA_ID,msg){
	bootbox.confirm("确定要删除["+msg+"]的数据信息吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=path%>/mtsDBData/delMtsDBData.do?DB_DATA_ID="+DB_DATA_ID;
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}

//批量操作
function makeAll(msg){
	bootbox.confirm(msg, function(result) {
		if(result) {
			var str = '';
			var emstr = '';
			var phones = '';
			var username = '';
			for(var i=0;i < document.getElementsByName('ids').length;i++)
			{
				  if(document.getElementsByName('ids')[i].checked){
				  	if(str=='') str += document.getElementsByName('ids')[i].value;
				  	else str += ',' + document.getElementsByName('ids')[i].value;
				  	
				  	if(emstr=='') emstr += document.getElementsByName('ids')[i].id;
				  	else emstr += ';' + document.getElementsByName('ids')[i].id;
				  	
				  	if(phones=='') phones += document.getElementsByName('ids')[i].alt;
				  	else phones += ';' + document.getElementsByName('ids')[i].alt;
				  	
				  	if(username=='') username += document.getElementsByName('ids')[i].title;
				  	else username += ';' + document.getElementsByName('ids')[i].title;
				  }
			}
			if(str==''){
				bootbox.dialog({
					message: "<span class='bigger-110'>您没有选择任何内容!</span>",
					buttons: 			
					{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
				});
				$("#zcheckbox").tips({
					side:3,
		            msg:'点这里全选',
		            bg:'#AE81FF',
		            time:8
		        });
				
				return;
			}else{
				if(msg == '确定要删除选中的数据吗?'){
					top.jzts();
					$.ajax({
						type: "POST",
						url: '<%=basePath%>user/deleteAllU.do?tm='+new Date().getTime(),
				    	data: {USER_IDS:str},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							 $.each(data.list, function(i, list){
									nextPage('${page.currentPage}');
							 });
						}
					});
				}else if(msg == '确定要给选中的用户发送邮件吗?'){
					sendEmail(emstr);
				}else if(msg == '确定要给选中的用户发送短信吗?'){
					sendSms(phones);
				}else if(msg == '确定要给选中的用户发送站内信吗?'){
					sendFhsms(username);
				}
			}
		}
	});
}

$(function() {
	//日期框
	$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
	
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

	
	//复选框全选控制
	var active_class = 'active';
	$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
		var th_checked = this.checked;//checkbox inside "TH" table header
		$(this).closest('table').find('tbody > tr').each(function(){
			var row = this;
			if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
			else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
		});
	});
});
</script>
</html>
