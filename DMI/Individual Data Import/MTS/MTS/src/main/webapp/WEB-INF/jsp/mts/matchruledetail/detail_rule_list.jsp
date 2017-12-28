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
						<form action="detailRule/listDetailRules.do" method="post" name="detailRuleForm" id="detailRuleForm">
						<table style="margin-top:5px;">
							<tr>
								
								<td style="padding-left:2px;">
								  <select  name="DATA_CLASS_CODE" id="DATA_CLASS_CODE"  style="width:100px;" >
											<option value="" >选择聚类查询</option>									
											<c:forEach items="${dataClassList}" var="dataClass">
												<c:if test="${pd.DATA_CLASS_CODE == null }">
													<option value="${dataClass.DATA_CLASS_CODE }" >${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
												<c:if test="${pd.DATA_CLASS_CODE != null }">
												    <option value="${dataClass.DATA_CLASS_CODE }" <c:if test="${dataClass.DATA_CLASS_CODE == pd.DATA_CLASS_CODE }">selected</c:if>>${dataClass.DATA_CLASS_NAME }</option>
												</c:if>
											</c:forEach>
								  </select>
								</td>
								
								<td style="padding-left:2px;">								
						
								  <select  name="AREA_ID" id="AREA_ID"  style="width:100px;" >
											<option value="" >区域选择</option>	
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
								
								<td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="searchs();"  title="搜索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
							<thead>
								<tr>
									<!-- <th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th> -->
									<th class="center" style="width:50px;">序号</th>
									<th class="center" style="width:50px;">流程ID</th>
									<th class="center">区域</th>
									<th class="center">聚类</th>
									<th class="center">标化类型</th>
									<th class="center">流程入口</th>									
									<th class="center">成功跳转</th>
									<th class="center">失败跳转</th>
									<th class="center">问题单</th>	
									<th class="center">匹配内容</th>
									<th class="center">返回值处理</th>
									<th class="center">使用标识</th>	
									<th class="center">触发条件</th>
									<th class="center">拼接key</th>	
									<th class="center">匹配程序</th>										
									<th class="center">操作</th>
								</tr>
							</thead>
													
							<tbody>
								
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty detailRuleList}">
									
									<c:forEach items="${detailRuleList}" var="detailRule" varStatus="vs">
												
										<tr>
											<%-- <td class='center' style="width: 30px;">
												<label><input type='checkbox' name='ids' value="${detailRule.MEM_ID }"  class="ace"/><span class="lbl"></span></label>
											</td> --%>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class='center' style="width: 50px;">${detailRule.MEM_ID }</td>
											<td class='center' style="width: 50px;">${detailRule.AREA_NAME }</td>
											<td class='center' style="width: 50px;">${detailRule.DATA_CLASS_NAME }</td>
											<td class='center' style="width: 100px;">${detailRule.DATA_TYPE_NAME }</td>
											<td class='center' style="width: 50px;">
											<c:if test="${detailRule.ORDERBY == '1'  }">是</c:if>											
											</td>
											<td class="center">${detailRule.SUCESSTO}</td>
											<td class="center">${detailRule.FALLTO}</td>
											<td class="center">
											<c:if test="${detailRule.QUESTION == '1'  }">是</c:if>
											<c:if test="${detailRule.QUESTION == '0' }">否</c:if>
											</td>
											<td class="center">
											<c:if test="${detailRule.NOC == '05'  }">诊断词</c:if>
											<c:if test="${detailRule.NOC == '09'  }">诊断码</c:if>
											<c:if test="${detailRule.NOC == '30'  }">手术词</c:if>
											<c:if test="${detailRule.NOC == '28'  }">诊疗词</c:if>
											<c:if test="${detailRule.NOC == '19'  }">药品通用名</c:if>
											<c:if test="${detailRule.NOC == '21'  }">药品产品名</c:if>
											<c:if test="${detailRule.NOC == '23'  }">drug剂型</c:if>
											<c:if test="${detailRule.NOC == '03'  }">药品规格</c:if>
											<c:if test="${detailRule.NOC == '17'  }">生产企业</c:if>
											<c:if test="${detailRule.NOC == 'ALL' }">全匹配</c:if>
											</td>
											<td class="center">
											<c:if test="${detailRule.MARK == '1'  }">叠加</c:if>
											<c:if test="${detailRule.MARK == '0' }">覆盖</c:if>
											</td>
											<td class="center">
											<c:if test="${detailRule.FLAG == '1'  }">匹配流程用</c:if>
											<c:if test="${detailRule.FLAG == '0' }">联合字段用</c:if>
											</td>
											<td class="center">${detailRule.IFNEXT}</td>
											<td class="center">
											<c:if test="${detailRule.PINKEY == '1'  }">是</c:if>
											<c:if test="${detailRule.PINKEY == '0' }">否</c:if>											
											</td>
											<td class="center">${detailRule.SOFT_CN_NAME}</td>
											
											<td class="center">
												
											
												
												<div class="hidden-sm hidden-xs btn-group">													
													
													<a class="btn btn-xs btn-success" title="编辑" onclick="editDRule('${detailRule.MEM_ID}');">
														<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
													</a>
													
													
													<a class="btn btn-xs btn-danger" onclick="delDRule('${detailRule.MEM_ID }','${detailRule.DATA_TYPE_NAME }');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
													
												</div>
												<div class="hidden-md hidden-lg">
													<div class="inline pos-rel">
														<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
															<i class="ace-icon fa fa-cog icon-only bigger-110"></i>
														</button>
														<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
															
															
															<li>
																<a style="cursor:pointer;" onclick="editDRule('${detailRule.MEM_ID}');" class="tooltip-success" data-rel="tooltip" title="修改">
																	<span class="green">
																		<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																	</span>
																</a>
															</li>
															
															<li>
																<a style="cursor:pointer;" onclick="delDRule('${detailRule.MEM_ID }','${detailRule.DATA_TYPE_NAME }');" class="tooltip-error" data-rel="tooltip" title="删除">
																	<span class="red">
																		<i class="ace-icon fa fa-trash-o bigger-120"></i>
																	</span>
																</a>
															</li>
															
														</ul>
													</div>
												</div>
											</td>
										</tr>
									
									</c:forEach>
									
									
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="15" class="center">没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						
					<div class="page-header position-relative">
					<table style="width:100%;">
						<tr>
							<td style="vertical-align:top;">								
								<a class="btn btn-mini btn-success" onclick="add();">新增</a>
								
								<!-- 							
								<c:if test="${QX.del == 1 }">
								<a title="批量删除" class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" ><i class='ace-icon fa fa-trash-o bigger-120'></i></a>
								</c:if>
								 -->
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

//查询
function searchs(){
	top.jzts();
	$("#detailRuleForm").submit();
}

//删除
function delDRule(memid,msg){
	
	bootbox.confirm("确定要删除["+msg+"]的匹配吗?", function(result) {
		if(result) {
			top.jzts();
			var url = "<%=basePath%>detailRule/deleteDR.do?memid="+memid;
			$.get(url,function(data){
				nextPage(${page.currentPage});
			});
		};
	});
}

//新增
function add(){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="新增";
	 diag.URL = '<%=path%>/detailRule/goAddDR.do';
	 diag.Width = 469;
	 diag.Height = 510;
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
function editDRule(memid){
	 top.jzts();
	 var diag = new top.Dialog();
	 diag.Drag=true;
	 diag.Title ="修改";
	 diag.URL = '<%=path%>/detailRule/goEditDR.do?memid='+memid;
	 diag.Width = 469;
	 diag.Height = 510;
	 diag.CancelEvent = function(){ //关闭事件
		 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
			nextPage(${page.currentPage});
		}
		diag.close();
	 };
	 diag.show();
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
