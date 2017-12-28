<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ts" uri="/rights"  %>
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
<%@ include file="../../index/top.jsp"%>
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
						<form action="pay/${msg }.do" name="payRuleForm" id="payRuleForm" method="post">
							<input type="hidden" name="ID" id="id" value="${pd.ID }"/>
							<div id="zhongxin" style="padding-top: 13px;">
							<table id="table_report" class="table table-striped table-bordered table-hover">
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">类别:</td>
									<td>
										<select class="chosen-select form-control" name="CHECK_TYPE" id="CHECK_TYPE" data-placeholder="校验类别" style="width:98%;" >
											<c:if test="${not empty checkTypeSet}">
													<c:forEach items="${checkTypeSet}" var="hosp" varStatus="vs">
														<option value="${hosp}" <c:if test="${pd.CHECK_TYPE==hosp}">selected</c:if> >${hosp}</option>
													</c:forEach>
											</c:if>
										</select>
									<%-- <input type="text" name="CHECK_TYPE" id="type" value="${pd.CHECK_TYPE }"  placeholder="输入类别" title="用户类别" style="width:98%;" />
									 --%>
									</td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">名称:</td>
									<td><input onclick="recovercss(this)" type="text" name="CHECK_NAME" id="CHECK_NAME"  value="${pd.CHECK_NAME }" placeholder="输入规则名称" title="规则名称" style="width:98%;" maxlength="50" /></td>
								</tr>
								<tr>
									<td style="width:79px;text-align: right;padding-top: 13px;">数值:</td>
									<%-- <td><input type="text" name="CHECK_VALUE" id="code" value="${pd.CHECK_VALUE }"  placeholder="输入数值" title="数值" style="width:98%;" maxlength="255"/></td> --%>
									<td>
									<input type="text" name="CHECK_VALUE" id="CHECK_VALUE" value="${pd.CHECK_VALUE }" style="display: none;" />
									<input onclick="recovercss(this)" type="text" name="tmpcheckvalue" id="maxlength" value="${pd.CHECK_VALUE }"  placeholder="输入大于0的整数" title="输入数值" style="width:98%;display: none;" maxlength="255"/>
									<input onclick="recovercss(this)" type="text" name="tmpcheckvalue" id="dateformat" value="${pd.CHECK_VALUE }"  placeholder="格式如：yyyy-MM-dd HH:mm:ss" title="输入数值" style="width:98%;display: none;" maxlength="255"/>
									<input onclick="recovercss(this)" type="text" name="tmpcheckvalue" id="notnull" value="${pd.CHECK_VALUE }"  placeholder="默认为1且不可修改" title="输入数值" style="width:98%;display: none;" maxlength="255"/>
									<input onclick="recovercss(this)" type="text" name="tmpcheckvalue" id="fixedvalue" value="${pd.CHECK_VALUE }"  placeholder="用“;”分隔各值" title="输入数值" style="width:98%;display: none;" maxlength="255"/>
									<select class="chosen-select form-control" name="tmpcheckvalue" id="paramtype" data-placeholder="选择参数类型" style="width:98%;display: none" >
										<option value="Integer" <c:if test="${pd.CHECK_VALUE=='Integer'}">selected</c:if> >Integer</option>
										<option value="String" <c:if test="${pd.CHECK_VALUE=='String'}">selected</c:if> >String</option>
										<option value="Double" <c:if test="${pd.CHECK_VALUE=='Double'}">selected</c:if> >Double</option>
										<option value="Long" <c:if test="${pd.CHECK_VALUE=='Long'}">selected</c:if> >Long</option>
									</select>
									</td>
								</tr>
								<tr>
									<td class="center" colspan="6">
										<ts:rights code="pay/savePay">
											<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
										</ts:rights>
									
										<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
									</td>
								</tr>
							</table>
							</div>
							<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
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
	<%@ include file="../../index/foot.jsp"%>
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
		//参数类型选择
		$("#"+$("#CHECK_TYPE").val()).show();
		$("#CHECK_TYPE").change(function (){
			var paramType = $(this).val();
			$("[name='tmpcheckvalue']").each(function(){
				$(this).val("");
				recovercss(this);
			    $(this).hide();
			  });
			$("#paramtype").val("Integer");
			$("#"+paramType).show();
		});
	});
// 	function len(){
// 		var pattern = /(1,2,3,4,5)/,
// 		str = '';
// 		console.log(pattern.test(str));
// 	}
	function setcss(obj){
		$(obj).css("border-color","red");
		$(obj).css("color","red");
	}
	function recovercss(obj){
		$(obj).css("border-color","#d5d5d5");
		$(obj).css("color","#858585");
	}
	//保存
	function save(){
		var ptype = $("#CHECK_TYPE").val();
		var pname = $("#CHECK_NAME").val();
		var pvalue = $("#"+ptype).val();
		if(!pname){
			setcss($("#CHECK_NAME"));
			return;
		}
		if(!pvalue){
			setcss($("#"+ptype));
			return;
		}
		var r = /^[0-9]*[1-9][0-9]*$/;//正整数 
		if(ptype=="maxlength" && !r.test(pvalue)){
			setcss($("#"+ptype));
			return;
		} 
		$("#CHECK_VALUE").val(pvalue);
		$("#payRuleForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
</script>
</html>