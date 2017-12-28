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
<%@ include file="/WEB-INF/jsp/system/index/top.jsp"%>
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
						<form action="mtsConfigDR/savaNLP.do" name="manuNlpForm" id="manuNlpForm" method="post">							
									<input type="hidden" name="T_ID" id="T_ID" value="${pd.T_ID }"/>								
									<input type="hidden" name="row_index" id="row_index" value="${pd.row_index }"/>
									<input type="hidden" name="RIGHT_DESC" id="RIGHT_DESC" value=""/>
									
									<input type="hidden" name="TITEL_EN" id="TITEL_EN" value="${pd.TITEL_EN}"/>
									<input type="hidden" name="PT_ID" id="PT_ID" value="${pd.PT_ID}"/>
									<input type="hidden" name="TYPE" id="TYPE" value="${pd.TYPE}"/>
									<input type="hidden" name="AREA_CODE" id="AREA_CODE" value="${pd.AREA_CODE}"/>
									<input type="hidden" name="CLASS_CODE" id="CLASS_CODE" value="${pd.CLASS_CODE}"/>
									<input type="hidden" name="SNAME" id="SNAME" value="${pd.SNAME}"/>
									<input type="hidden" name="WFBH" id="WFBH" value="${pd.WFBH}"/>
									<input type="hidden" name="NLP_STATUS" id="NLP_STATUS" value="${pd.NLP_STATUS}"/>									
									<div id="zhongxin" style="padding-top: 13px;">
									<table id="table_report" class="table table-striped table-bordered table-hover">
										<tr>
											<td style="width:110px;text-align: right;padding-top: 13px;">术语名称:</td>
											<td>
												<input type="text" name="SOURCEDATA" id="SOURCEDATA"  value="${SOURCEDATA }"  maxlength="32" title="" style="width:79%;" readonly/>
												
											</td>
										</tr>
										<tr style="height:100px">
											<td style="width:110px;text-align: right;padding-top: 13px;">原始切分结果:</td>
											<td>
												<table id="t_old" class="table table-striped table-bordered table-hover"  style="margin-top:5px;">
													<thead>
														<tr>
															<th class="center" >术语名称【术语类型】</th>																													
														</tr>
													</thead>
													<tbody>
													<!-- 开始循环 -->	
													<c:choose>
														<c:when test="${not empty listManuNLP}">
															<c:forEach items="${listManuNLP}" var="s" varStatus="vs">
																<tr>														
																	<td class="center">${s.NLP_SNAME }</td>
																																
																</tr>
															</c:forEach>
														</c:when>
													</c:choose>													
													</tbody>
												</table>
											</td>
										</tr>
										<tr>
											<td style="width:110px;text-align: right;padding-top: 13px;">审核后NLP结果:</td>
											<td>
													<div class="input_div" id="aaaaaa" style="float:left;margin-right:3px">
														
													</div>
													<a class="btn btn-xs btn-success" onclick="addName('');">+</a>
													<ts:rights code="NLP/saveNLP">
														<a  style="margin-left:5px" id="saveBtn" class="btn btn-light btn-xs" onclick="split_sure(2)" title="保存">
														<i id="nav-search-icon" class="ace-icon fa fa-file-text-o bigger-110 nav-search-icon blue"></i>保存</a>
													</ts:rights>
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
	<div id="aa" style="display: none;">
		<select name='TERM_CN_NAME' id='nlpterm_' style='margin-bottom:5px;margin-right:3px;width: 85px;'>
			<c:forEach items="${listTerm}" var="termTyp" varStatus="vs">
				<option value="${termTyp.TERM_CN_NAME}" >${termTyp.TERM_CN_NAME}</option>
			</c:forEach>
		</select>
	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="/WEB-INF/jsp/system/index/foot.jsp"%>
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
	//保存切分结果 1为切分正确2 为保存
	function split_sure(type){
		
		$("#saveBtn").attr("disabled",true);
		bootbox.confirm("确认要保存当前的切分结果么?", function(result) {
			if(result) {
				
				var T_ID=$("#T_ID").val();//数据ID
				var SOURCEDATA=$("#SOURCEDATA").val();//原始诊断名称
				var RIGHT_DESC=''//正确的描述				
				var TERM_TYPE='';
				
				
				
				var new_name_val='';//新的切分结果		
				
				
				if(type==2){//新的人工切分结果
					var new_term_name='';
					var temp_name=document.getElementsByName('nlpname');					
					for(var j=0;j<temp_name.length;j++){
						var new_td_name_temp=temp_name[j].value.trim();
						var idIndex = temp_name[j].getAttribute("indexV");
						var new_td_term_temp = document.getElementById("nlpterm_"+idIndex).value.trim();						
						if(new_td_name_temp=='')//术语名称为空,此时不会将数据保存
							continue;
						if(new_td_term_temp=='')//术语不为空，类型为空，给出提示信息
						{
							bootbox.dialog({
								message: "<span class='bigger-110'>术语："+new_td_name_temp+",术语类型不能为空!</span>",
								buttons: 			
								{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
							});
							$("#saveBtn").attr("disabled",false);
							return ;
						}
						
						else//校验词的正确性
						{
							var check_include_flag=isInclude(SOURCEDATA,new_td_name_temp);
							if(!check_include_flag) {
								bootbox.dialog({
									message: "<span class='bigger-110'>新的切分词，不是源术语的部分,请重新处理!</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
								});
								$("#saveBtn").attr("disabled",false);
								return ;
							}else{//校验词的重复性
								var check_double=isIncludeDouble(new_name_val,new_td_name_temp);
								if(check_double) {
									bootbox.dialog({
										message: "<span class='bigger-110'>新的切分词,包含重复的术语,请重新处理!</span>",
										buttons: 			
										{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
									});
									$("#saveBtn").attr("disabled",false);
									return ;
								}
							}
						}
						
						
						if(new_name_val==''){
							new_name_val=new_td_name_temp;
							if(new_td_term_temp!='')
								new_term_name=new_td_name_temp+"【"+new_td_term_temp+"】";
						}else{
							new_name_val=new_name_val+"#q&q#"+new_td_name_temp;
							if(new_td_term_temp!='')
								new_term_name=new_term_name+"#q&q#"+new_td_name_temp+"【"+new_td_term_temp+"】";
						}
					}
					//设置新值										
					RIGHT_DESC=new_term_name;					
				}
				
				if(RIGHT_DESC==''){
					$("#sureBtn").attr("disabled",false);
					$("#saveBtn").attr("disabled",false);
					alert("术语类型或术语名称为空，不能保存！");
					return;
				}
				
				document.getElementById('RIGHT_DESC').value=RIGHT_DESC;				
				$("#manuNlpForm").submit();
				$("#zhongxin").hide();
				$("#zhongxin2").show();
				
			}else{
				
				$("#saveBtn").attr("disabled",false);
			}
		});	
	}
	//判断两个字符串长度是否一致 
	function isEqual(str1,str2){
		var tefu = "#(（）)+,，.。;；？?!！、“'‘\"/| \t\n，，．。；；？？！！、＂”＇’＼＼＂“／、｜｜　　";
		var  tes  = new Array();
		tes=tefu.split("");
		for(i=0;i<tes.length;i++){
			str1=str1.replaceAll("["+tes[i]+"]", "");
			str2=str2.replaceAll("["+tes[i]+"]", "");
		}
		//由于根据空格拆分的  所以空格没剔除掉 再replace下 
		str1=str1.replaceAll("[ ]", "");
		str2=str2.replaceAll("[ ]", "");
		var lenstr1 = $.trim(str1).length;
		var lenstr2 = $.trim(str2).length;
		if(lenstr1==lenstr2){
			return true;
		}
		return false;
	}
	//判断字符串是否在另一个字符串中
	function isInclude(str1,str2){
		var tefu = "#(（）)+,，.。;；？?!！、“'‘\"/| \t\n，，．。；；？？！！、＂”＇’＼＼＂“／、｜｜　　";
		var  tes  = new Array();
		tes=tefu.split("");
		for(i=0;i<tes.length;i++){
			str1=str1.replaceAll("["+tes[i]+"]", "");
			str2=str2.replaceAll("["+tes[i]+"]", "");
		}
		//由于根据空格拆分的  所以空格没剔除掉 再replace下 
		str1=str1.replaceAll("[ ]", "");
		str2=str2.replaceAll("[ ]", "");
		var ishave = str1.indexOf(str2);
		if(ishave != -1){
			return true;
		}
		return false;
	}
	//判断字符串是否包含重复的值
	function isIncludeDouble(new_name_val,new_td_name_temp){
		var vals = new_name_val.split("#q&q#");
		for(var i=0;i<vals.length;i++){
			if(vals[i]==new_td_name_temp)
				return true;
		}
		return false;
	}
	
	String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {  
	    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {  
	        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);  
	    } else {  
	        return this.replace(reallyDo, replaceWith);  
	    }  
	}
	$(function() {
		//下拉框
		if(!ace.vars['touch']) {
			$('.chosen-select').chosen({allow_single_deselect:true}); 
			
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
		if($("#P_ID").val()!=""){
			$("#TASK_TYPE_ID").attr("disabled","disabled");
			$("#TASK_TYPE_ID").css("color","gray");
			
			$("#TASK_TYPE_CHILD_ID").attr("disabled","disabled");
			$("#TASK_TYPE_CHILD_ID").css("color","gray");
			$("#TASK_TYPE_ID").tips({
				side:3,
	            msg:'任务类型不可更改',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#TASK_TYPE_CHILD_ID").tips({
				side:3,
	            msg:'任务子类型不可更改',
	            bg:'#AE81FF',
	            time:3
	        });
			
		}
	});
	
	var num=document.getElementsByName('nlpname').length;
	//增加文本框
	function addName(val){
		var divHtml = $("#aa").html();
		var vals='';
		var termname = '';
		var termtype='';
		if(val!=''){
			vals = val.split(":");
			if(vals.length==1)
				termname=vals[0];
			else
				termname=vals[1];
				termtype=vals[0];
		}
		$("#aaaaaa").append(divHtml.replace('nlpterm_','nlpterm_'+num));
		$("#aaaaaa").append($("<input style='margin-bottom:5px;margin-right:3px;' indexV='"+num+"' class='canremove' type='text' id='nlpname_"+num+"' value='"+termname+"'  name='nlpname' maxlength='32' style='margin-right:3px' /><input type='hidden' class='canremove' id='nlpcode_"+num+"' name='nlpcode' value='UNM'/><a class='canremove' class='btn btn-xs btn-danger' id='a_"+num+"' onclick='cancelAdd("+num+");'>X</a>"
		));
		$("#nlpterm_"+num).val(termtype);
		num=num+1;
	}
	//删除增加的框框
	function cancelAdd(num){
		$("#nlpname_"+num).remove();
		$("#nlpterm_"+num).remove();
		$("#nlpcode_"+num).remove();
		$("#a_"+num).remove();
	}

	//还原或人工切分执行的数据恢复为原来的结果
	function reback(){
		var t_old=$("#t_old").find("tr");
		//将原来存在的先删除
		delAllOld();
		num=0;
		for(var i=1;i<t_old.length-1;i++){
			var line_val=t_old.eq(i).find("td").eq(0).text();//术语名称
			var line_type=t_old.eq(i).find("td").eq(1).text();//术语类型
			addName(line_type+":"+line_val);
		}
	}
	//将原来存在的先删除
	function delAllOld(){
		var nameSelect = $('input[name="nlpname"]');
		for(var int=0;int<nameSelect.size();int++){
			var idIndex = nameSelect[int].getAttribute("indexV");
			$("#nlpterm_"+idIndex).remove();
		}
		$(".canremove").remove();
	}
	
	//任务提交
	function submitRs(){
		var flag = $("#nextShow").prop("checked");//是否选择下一条
		var haveRs=$("#haveRs").val();
		if(haveRs==0){
			bootbox.dialog({
				message: "<span class='bigger-110'>还未包含处理信息，请处理后再提交!</span>",
				buttons: 			
				{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
			});
			return;
		}else{
			//提交当前的任务信息，且判断下一条是否选择
			bootbox.confirm("确认要提交当前任务的处理结果吗?", function(result) {
				if(result) {
					//将按钮失效
					$("#sub").attr("disabled", true);
					var TASK_ID=$("#TASK_ID").val();
					$.ajax({
						type: "POST",
						url: '<%=basePath%>taskQuery/submitTaskAll.do?tm='+new Date().getTime(),
				    	data: {TASK_IDS:TASK_ID,STEP:2},//NLP审核只有二审
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							$("#sub").attr("disabled", false);
							if(data.result=="success"){
								//刷新当前的页面	self.location = path+'/taskQuery/toCtl.do?task_id='+$("#TASK_ID").val()+"&task_stat=2";
								if(flag){
									var searchForm ;
									if($("#ctrl_flag").val()==1){//待处理页入口
										searchForm=parent.frames[0].$("iframe[src='taskQuery/listTask4Ctrl.do']")[0].contentWindow.document.taskQueryForm;
										//显示下一条
										self.location = path+'/taskQuery/toCtl.do?nextShow=1&ctrl_flag=1&'+ $(searchForm).serialize();
									}else{//任务查询页面入口
										searchForm=parent.frames[0].$("iframe[src='taskQuery/listTask.do']")[0].contentWindow.document.taskQueryForm
										//显示下一条
										self.location = path+'/taskQuery/toCtl.do?nextShow=1&'+ $(searchForm).serialize();
									}
								}else{
									//document.getElementById("zhongxin").style.display = 'none';
									top.Dialog.close();
								}
							}else{
								bootbox.dialog({
									message: "<span class='bigger-110'>"+data.result+"</span>",
									buttons: 			
									{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
								});
							}
						}
					});
				}
			});
		}
	}
	
	
</script>
</html>