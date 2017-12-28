<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ts" uri="/rights"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html lang="en">

<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css"/>
	<!-- jsp文件头和头部 -->
	<%@ include file="../../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css"/>
	
	<style type="text/css">
  		#bg{ display: none;  position: absolute;  top: 0%;  left: 0%;  width: 100%;  height: 100%;  background-color: black;  z-index:1001;  -moz-opacity: 0.7;  opacity:.70;  filter: alpha(opacity=70);}
  		#show{display: none;  position: absolute;  top: 30%;  left: 30%;  width: 35%;  height: 55px;  padding: 10px;  border: 5px solid #E8E9F7;  background-color: white;  z-index:1002;  overflow: auto;}
	</style>
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
						<form action="interInfo/dataList.do" method="post" name="form1" id="form1">
						
							<table style="margin-top:5px;">
								<tr>
									<td>
										<div class="nav-search">
											<span class="input-icon">
												<input class="nav-search-input" style="width:140px;" autocomplete="off" id="keywords" type="text" name="keywords" value="${keywords}" placeholder="请输入医院或药店名称"/>
												<i class="ace-icon fa fa-search nav-search-icon"></i>
											</span>
										</div>
									</td>
									<td style="padding-left:5px;"> 
										<select onchange="changeType(this)" class="chosen-select form-control" name="type" id="type" data-placeholder="业务类型" style="width:120px;">
											<option value="1" <c:if test="${type==1}">selected</c:if> >医院支付</option>
											<option value="2" <c:if test="${type==2}">selected</c:if> >药店支付</option>
										</select>
									</td>
									<td id="btn_h"style="padding-left:5px;"> 
										<select class="chosen-select form-control" name="businessCode" id="businessCode" data-placeholder="业务接口编号" style="width:120px;">
											<option value=""></option>
											<option value="">全部</option>
											<c:choose>
												<c:when test="${not empty dictList}">
													<c:forEach items="${dictList}" var="dict" varStatus="vs">
														<option value="${dict.D_VALUE}" <c:if test="${pd.businessCode==dict.D_VALUE}">selected</c:if> >${dict.D_VALUE}</option>
													</c:forEach>
												</c:when>
											</c:choose>
										</select>
									</td>
									<td id="btn_d"style="padding-left:5px;"> 
										<select class="chosen-select form-control" name="businessCode_ds" id="businessCode_ds" data-placeholder="业务接口编号" style="width:120px;">
											<option value=""></option>
											<option value="">全部</option>
											<c:choose>
												<c:when test="${not empty dictList_ds}">
													<c:forEach items="${dictList_ds}" var="dict" varStatus="vs">
														<option value="${dict.D_VALUE}" <c:if test="${pd.businessCode_ds==dict.D_VALUE}">selected</c:if> >${dict.D_VALUE}</option>
													</c:forEach>
												</c:when>
											</c:choose>
										</select>
									</td>
									
									<td style="padding-left:5px;"> 
										<select class="chosen-select form-control" name="groupID" id="groupID" data-placeholder="机构名称" style="width:180px;">
											<option value=""></option>
											<option value="">全部</option>
											<c:choose>
												<c:when test="${not empty sysUserList}">
													<c:forEach items="${sysUserList}" var="sysUser">
														<option value="${sysUser.GROUP_ID}" <c:if test="${pd.groupID==sysUser.GROUP_ID}">selected</c:if> >${sysUser.GROUP_NAME}</option>
													</c:forEach>
												</c:when>
											</c:choose>
										</select>
									</td>
								
									<td style="padding-left:5px;">
										<input class="span10 date-picker" name="final_date_start" id="final_date_start" value="${final_date_start}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="结算开始日期" title="结算开始日期"/>
									</td>
									<td style="padding-left:5px;">
										<input class="span10 date-picker" name="final_date_end" id="final_date_end" value="${final_date_end}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:100px;" placeholder="结算结束日期" title="结算结束日期"/>
									</td>
								
									<td style="vertical-align:top;padding-left:5px;">
										<a class="btn btn-light btn-xs" onclick="searchs();" title="检索">
											<i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i>
										</a>
									</td>
									
									<td style="vertical-align:top;padding-left:5px;">
										<ts:rights code="interInfo/exporExcel">
											<a class="btn btn-light btn-xs" onclick="exporExcel();" title="导出到EXCEL">
												<i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i>
											</a>
										</ts:rights>
									</td>
								</tr>
							</table>
						
							<!-- 列表  -->
							<c:choose>
								<c:when test="${type==2 }"><!--药店支付  -->
									<table id="simple-table" width="100%" class="table table-striped table-bordered table-hover" style="margin-top:10px;">
										<thead>
											<tr>
												<th width="5%" class="center">序号</th>
												<th width="10%" class="center">业务接口编号</th>
												<th width="15%" class="center">数据类型</th>
												<th width="15%" class="center">机构名称</th>
												<th width="20%" class="center">药店名称</th>
												<th width="15%" class="center">结算时间</th>
												<th width="10%" class="center">操作</th>
											</tr>
										</thead>
										
										<tbody>
										<!-- 开始循环 -->	
											<c:choose>
												<c:when test="${not empty varList}">
													<c:forEach items="${varList}" var="interList" varStatus="vs">
														<tr>
															<td width="5%" class='center'>${vs.index+1}</td>
															<td width="10%" class="center">${interList.api_type}</td>
															<td width="15%" class="center">${interList.data_type_name}</td>
															<td width="15%" class="center">${interList.group_name}</td>
															<td width="20%" class="center">${interList.hosp_name}</td>
															<td width="15%" class="center"><fmt:formatDate value="${interList.create_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
															<td width="10%" class="center">
																<div class="btn-group">
																	<ts:rights code="col/goRule">
																		<a class="btn btn-xs btn-purple" title="查看明细" onclick="queryDetail('${interList.id}');">查看明细</a>
																	</ts:rights>
																</div>
															</td>
														</tr>
													</c:forEach>
												</c:when>
											
												<c:otherwise>
													<tr class="main_info">
														<td colspan="100" class="center">没有相关数据</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</c:when>
								<c:otherwise><!--默认的医院支付  -->
									<table id="simple-table" width="100%" class="table table-striped table-bordered table-hover" style="margin-top:10px;">
										<thead>
											<tr>
												<th width="5%" class="center">序号</th>
												<th width="10%" class="center">业务接口编号</th>
												<th width="15%" class="center">数据类型</th>
												<th width="10%" class="center">现金支付方式</th>
												<th width="15%" class="center">机构名称</th>
												<th width="20%" class="center">医院名称</th>
												<th width="15%" class="center">结算时间</th>
												<th width="10%" class="center">操作</th>
											</tr>
										</thead>
									
										<tbody>
										<!-- 开始循环 -->	
											<c:choose>
												<c:when test="${not empty varList}">
													<c:forEach items="${varList}" var="interList" varStatus="vs">
														<tr>
															<td width="5%" class='center'>${vs.index+1}</td>
															<td width="10%" class="center">${interList.api_type}</td>
															<td width="15%" class="center">${interList.data_type_name}</td>
															<td width="10%" class="center">${interList.pay_type_name}</td>
															<td width="15%" class="center">${interList.group_name}</td>
															<td width="20%" class="center">${interList.hosp_name}</td>
															<td width="15%" class="center"><fmt:formatDate value="${interList.createdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
															
															<td width="10%" class="center">
																<div class="btn-group">
																	<ts:rights code="col/goRule">
																		<a class="btn btn-xs btn-purple" title="查看明细" onclick="queryDetail('${interList.id}');">查看明细</a>
																	</ts:rights>
																</div>
															</td>
														</tr>
													</c:forEach>
												</c:when>
											
												<c:otherwise>
													<tr class="main_info">
														<td colspan="100" class="center">没有相关数据</td>
													</tr>
												</c:otherwise>
											</c:choose>
										</tbody>
									</table>
								</c:otherwise>
							</c:choose>
							<!-- 分页 -->
							<div class="page-header position-relative">
								<table style="width:100%;">
									<tr>
										<td style="vertical-align:top;">
											<div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div>
										</td>
									</tr>
								</table>
							</div>
							
						</form>
 						
 						<div id="bg"></div>
						<div id="show" style="text-align:center;">
							<font color="red" size="3">正在导出excel，请稍候.....</font>
						</div> 
 						
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
	
	$(function() {
		//提示框标签
		$("[data-toggle='tooltip']").tooltip();
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
		if($("#type").val()==1)//医院支付
		{
			$("#btn_h").show();
			$("#btn_d").hide();
		}
		else//药店支付
		{
			$("#btn_d").show();
			$("#btn_h").hide();
		}
		
	});
	
	function showdiv(){   
		document.getElementById("bg").style.display = "block";
		document.getElementById("show").style.display = "block";
	}
	          
	function hidediv(){
		document.getElementById("bg").style.display = 'none';
		document.getElementById("show").style.display = 'none';
	}
	
	function exporExcel(){//导出数据信息
		var FINAL_DATE_START = document.getElementById("final_date_start").value;
		var FINAL_DATE_END = document.getElementById("final_date_end").value;
		
		var BUSINESSCODE = document.getElementById("businessCode").value;//医院支付的业务接口编号
		var BUSINESSCODE_DS= document.getElementById("businessCode_ds").value;//药店支付的业务接口编号
		
		var GROUPID = document.getElementById("groupID").value;
		var KEYWORDS = document.getElementById("keywords").value;
		KEYWORDS = KEYWORDS.replace(/\s+/g, "");
		//alert("FINAL_DATE_START=====>>>"+FINAL_DATE_START+"\n FINAL_DATE_END======>>>"+FINAL_DATE_END+"\n BUSINESSCODE====>>>"+BUSINESSCODE+"\n GROUPID====>>>"+GROUPID+"\n KEYWORDS====>>>>"+KEYWORDS);
		
		if($("#type").val()==1)//医院支付
		{
			if(FINAL_DATE_START=="" && FINAL_DATE_END=="" && BUSINESSCODE=="" && GROUPID=="" && KEYWORDS==""){
				alert("至少选择一个检索条件！");
				return false;
			}
			showdiv();
			var textVal = "";
			$.ajax({
				type: "POST",
				url: "<%=basePath%>interInfo/exporExcel.do",
				data: "FINAL_DATE_START="+FINAL_DATE_START+"&FINAL_DATE_END="+FINAL_DATE_END+"&BUSINESSCODE="+BUSINESSCODE+"&KEYWORDS="+encodeURI(KEYWORDS)+"&GROUPID="+GROUPID,
				async: false,
				success:function(msg) {
					textVal = msg;
				}
			})
			hidediv();
		}
		else//药店支付
		{
			if(FINAL_DATE_START=="" && FINAL_DATE_END=="" && BUSINESSCODE_DS=="" && GROUPID=="" && KEYWORDS==""){
				alert("至少选择一个检索条件！");
				return false;
			}
			showdiv();
			var textVal = "";
			$.ajax({
				type: "POST",
				url: "<%=basePath%>interInfo/drugstoreExporExcel.do",
				data: "FINAL_DATE_START="+FINAL_DATE_START+"&FINAL_DATE_END="+FINAL_DATE_END+"&BUSINESSCODE="+BUSINESSCODE_DS+"&KEYWORDS="+encodeURI(KEYWORDS)+"&GROUPID="+GROUPID,
				async: false,
				success:function(msg) {
					textVal = msg;
				}
			})
			hidediv();
		}
		
		if(textVal != ""){
			form1.action = "<%=basePath%>interInfo/fileDown.do?fileName="+textVal;
			form1.submit();
			form1.action = "<%=basePath%>interInfo/dataList.do";
		}
		return false;
	}
	
	//检索
	function searchs(){
		top.jzts();
		$("#form1").submit();
	}
	
	function queryDetail(id){
		var type=$("#type").val();
		var url='';
		if(type==1)
			url='<%=path%>/interInfo/interDetail.do?id='+id;
		else
			url='<%=path%>/interInfo/drugstoreInterDetail.do?id='+id;
		top.jzts();
		var diag = new top.Dialog();
		diag.Drag = true;
		diag.Title = '查看明细';
		diag.URL = url;
		diag.Width = 1400;
		diag.Height = 600;
		diag.CancelEvent = function(){ //关闭事件
			if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
				if('${page.currentPage}' == '0'){
					top.jzts();
					setTimeout("self.location=self.location",100);
				}else{
					nextPage(${page.currentPage});
				}
			}
			diag.close();
		};
		diag.show();
		//window.showModalDialog("", null, "dialogWidth=1024;dialogHeight=800");
	}
	
	//变更操作的类型
	function changeType(obj){
		if(obj.value==1)//医院支付
		{
			$("#btn_h").show();
			$("#btn_d").hide();
		}
		else//药店支付
		{
			$("#btn_d").show();
			$("#btn_h").hide();
		}
	}
</script>

</html>