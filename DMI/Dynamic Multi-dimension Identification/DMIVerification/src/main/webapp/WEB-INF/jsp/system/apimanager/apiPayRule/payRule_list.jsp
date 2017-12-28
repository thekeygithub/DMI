<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
						<!-- 检索  -->
						<form action="pay/payrule.do" method="post" name="payRuleForm" id="payRuleForm">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
									<span class="input-icon">
										<input class="nav-search-input" autocomplete="off" id="nav-search-input" type="text" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词" />
										<i class="ace-icon fa fa-search nav-search-icon"></i>
									</span>
									</div>
								</td>
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								
							</tr>
						</table>
						<!-- 检索  -->
						<table id="simple-table" class="table table-condensed table-striped table-bordered table-hover" style="margin-top:5px;<c:if test="${not empty payRuleList}">table-layout:fixed</c:if>">
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
<!-- 									<th class="center">业务ID</th> -->
									<th class="center">名称</th>
									<th class="center">类别</th>
									<th class="center">数值</th>
									<th class="center">操作</th>
								</tr>
							</thead>
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty payRuleList}">
									<c:forEach items="${payRuleList}" var="inter" varStatus="vs">
										<tr>
											<td class='center' style="width: 30px;">
												<label><input type='checkbox' name='ids' value="${inter.ID }"  class="ace"/><span class="lbl"></span></label>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
<%-- 											<td class="center">${inter.IN_ID }</td> --%>
											<td class="center" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;" title="${inter.CHECK_VALUE }">${inter.CHECK_NAME }</td>
											<td class="center">${inter.CHECK_TYPE }</td>
											<td class="center" style="overflow:hidden;white-space:nowrap;text-overflow:ellipsis;" title="${inter.CHECK_VALUE }">${inter.CHECK_VALUE }</td>
											<td class="center">
												<div class="btn-group">
													<ts:rights code="pay/goEdit">
														<a class="btn btn-xs btn-success" title="编辑" onclick="editInterface('${inter.ID}');">
															<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
														</a>
													</ts:rights>
													<ts:rights code="pay/delete">
														<a class="btn btn-xs btn-danger" onclick="delInterface('${inter.ID }');">
															<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
														</a>
													</ts:rights>
												</div>
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="100" class="center" >没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
						<table style="width:100%;">
							<tr>
								<td style="vertical-align:top;">
									<ts:rights code="pay/goAdd">
										<a class="btn btn-mini btn-success" onclick="add();">新增</a>
									</ts:rights>
									<ts:rights code="pay/deleteAll">
										<a title="批量删除" class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
									</ts:rights>
								</td>
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
	<%@ include file="../../index/foot.jsp"%>
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
		
		$(function() {
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
		
		//检索
		function searchs(){
			top.jzts();
			$("#payRuleForm").submit();
		}

		
		//新增
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="规则信息";
			 diag.URL = '<%=path%>/pay/goAdd.do';
			 diag.Width = 600;
			 diag.Height = 415;
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
		
		//修改
		function editInterface(id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="规则信息";
			 diag.URL = '<%=path%>/pay/goEdit.do?ID='+id;
			 diag.Width = 600;
			 diag.Height = 415;
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 nextPage(${page.currentPage});
				}
				diag.close();
			 };
			 diag.show();
		}
		
		//删除
		function delInterface(id){
			var url = "<%=basePath%>pay/checkINID.do?IN_IDS="+id;
			$.get(url,function(data){
				var tip = "";
				if("success" == data.result){
					tip ="<font color='#EEEE00' size='4'><b>提示：</b></font><b>确定要删除此数据吗?</b>";
				}else{
					tip ="<font color='#FF0000' size='4'><b>警告：</b></font><b>"+data.result+"</b>";
				}
			bootbox.confirm(tip, function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>pay/deletePay.do?ID="+id;
					$.get(url,function(data){
							if("success" == data.result){
								nextPage(${page.currentPage});
							}else if("failed" == data.result){
								top.hangge();
								bootbox.dialog({
									message: "<span class='bigger-110'>删除失败!</span>",
									buttons: 			
									{
										"button" :
										{
											"label" : "确定",
											"className" : "btn-sm btn-success"
										}
									}
								});
							}
					});
				}
			});
			});
		}
		
		//批量操作
		function makeAll(msg){
			var str = '';
			for(var i=0;i < document.getElementsByName('ids').length;i++)
			{
				if(document.getElementsByName('ids')[i].checked){
					if(str=='') str += document.getElementsByName('ids')[i].value;
					else str += ',' + document.getElementsByName('ids')[i].value;
				}
			}
			var url = "<%=basePath%>pay/checkINID.do?IN_IDS="+str;
			$.get(url,function(data){
				var tip = "";
				if("success" == data.result){
					tip ="<font color='#EEEE00' size='4'><b>提示：</b></font><b>确定要删除此数据吗?</b>";
				}else{
					tip ="<font color='#FF0000' size='4'><b>警告：</b></font><b>"+data.result+"</b>";
				}
			bootbox.confirm(tip, function(result) {
				if(result) {
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
								url: '<%=basePath%>pay/deleteAll.do?tm='+new Date().getTime(),
						    	data: {IN_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											nextPage(${page.currentPage});
									 });
								}
							});
						}
						
					}
				}
			});
			});
		}	

		</script>
</html>
