<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../../../system/index/top.jsp"%>
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
						
							<form action="<%=path%>/record/totalDetail.do?SUPER_ID=${pd.SUPER_ID}" name="form2" id="form2" method="post">
							
								<div id="zhongxin" style="padding-top: 13px;">
									
									<!-- 列表  -->
									<table id="simple-table" width="100%" class="table table-striped table-bordered table-hover" style="margin-top:5px;">
										<thead>
											<tr>
												<th width="5%" class="center">序号</th>
												<th width="14%" class="center">机构名称</th>
												<th width="14%" class="center">医院名称</th>
												<th width="14%" class="center">结算日期</th>
												<th width="9%" class="center">费用总额</th>
												<th width="9%" class="center">自费总额(非医保)</th>
												<th width="9%" class="center">药品乙类自负</th>
												<th width="9%" class="center">医保费用</th>
												<th width="9%" class="center">合计报销金额</th>
												<th width="8%" class="center">操作</th>
											</tr>
										</thead>
										
										<tbody>
										<!-- 开始循环 -->	
											<c:choose>
												<c:when test="${not empty varList}">
													<c:forEach items="${varList}" var="totalRecord" varStatus="vs">
														<tr>
															<td width="5%" class='center'>${vs.index+1}</td>
															<td width="14%" class="center">${totalRecord.GROUP_NAME}</td>
															<td width="14%" class="center">${totalRecord.hosp_name}</td>
															
															<td width="14%" class="center">
																<fmt:formatDate value="${totalRecord.FINAL_DATE}" pattern="yyyy-MM-dd HH:mm:ss"/>
															</td>
															<td width="9%" class="center">
																<fmt:formatNumber type="number" value="${totalRecord.TOTAL_FEE}" pattern="0.00" maxFractionDigits="2"/>
															</td>
															<td width="9%" class="center">
																<fmt:formatNumber type="number" value="${totalRecord.SELF_PAY}" pattern="0.00" maxFractionDigits="2"/>
															</td>
															<td width="9%" class="center">
																<fmt:formatNumber type="number" value="${totalRecord.SELF_NEG}" pattern="0.00" maxFractionDigits="2"/>
															</td>
															<td width="9%" class="center">
																<fmt:formatNumber type="number" value="${totalRecord.MED_FEE}" pattern="0.00" maxFractionDigits="2"/>
															</td>
															<td width="9%" class="center">
																<fmt:formatNumber type="number" value="${totalRecord.RETURN_FEE}" pattern="0.00" maxFractionDigits="2"/>
															</td>
															
															<td width="8%" class="center">
																<div class="btn-group">
																	<ts:rights code="record/totalSituation2">
																		<a class="btn btn-xs btn-purple" title="查看详情" onclick="querySituation('${totalRecord.ID}');">查看详情</a>
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
									
									<div style="text-align: center;">
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">关&nbsp;&nbsp;闭</a>
									</div>
									
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
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../../system/index/foot.jsp"%>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- inline scripts related to this page -->
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
</body>

<script type="text/javascript">
	$(top.hangge());
	
	$(document).ready(function(){
		
	});
	
	function querySituation(id){
		top.jzts();
		var diag = new top.Dialog();
		diag.Drag = true;
		diag.Title = '查看详情';
		diag.URL = '<%=path%>/record/totalSituation2.do?id='+id;
		diag.Width = 900;
		diag.Height = 500;
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
	}
</script>

</html>