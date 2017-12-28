<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
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
							<form action="mtsCorpus/editAssertMtsCorpus.do" id="mtsCorpusAssertEditForm"
								method="post">
								<input type='hidden' id="origContent_" name="origContent" value="" /> 
								<input type='hidden' id="jsonContent_" name="jsonContent" /> 
								<input type='hidden' id="corpusDetailId_" name="corpusDetailId" value="${corpusDetailId}" />
								<div id="zhongxin" style="padding-top: 13px;">
									<div id="contentDiv" contentEditable="false" style="height:100px;overflow-y:scroll;border: 1px solid;font-size:15px">
									</div>
									<div>
										<table style="width: 100%; border: 1px solid;">
										<a class="btn btn-mini btn-primary" style="margin-top:5px;margin-bottom: 5px" id="butAdd">添加实体</a>&nbsp;&nbsp;&nbsp;&nbsp;
											<thead>
												<th>行号</th>
												<th>实体</th>
												<th>实体类型</th>
												<th>不确定</th>
												<th>操作</th>
											</thead>
											<tbody id="tab">
												<c:if test="${not empty listEntity}"> 
													<c:forEach items="${listEntity}" var="entity" varStatus="i">
															<tr id="${i.index}" align='left'>
																	<input type='hidden' id='startTextOff${i.index}' value="${entity.START_TEXT_OFF }"/>
																	<input type='hidden' id='endTextOff${i.index}' value="${entity.END_TEXT_OFF }"/>
																	<input type='hidden' id='startHtmlOff${i.index}' value="${entity.START_HTML_OFF }"/>
																	<input type='hidden' id='endHtmlOff${i.index}' value="${entity.END_HTML_OFF }"/>
																	<input type='hidden' id='origCorpus${i.index}' value="${entity.ORIG_CORPUS}"/>
																	<input type='hidden' id='pnum${i.index}' value="${entity.SPAN_NUM}"/>
																	<input type='hidden' id='color${i.index}' value="${entity.COLOR}"/>
																	<td>${i.index+1}</td>
																	<td id='entity${i.index}'><a style='cursor:pointer' onclick='javascript:locateIndex("${entity.SPAN_NUM}");'>${entity.ENTITY_NAME}【${entity.START_TEXT_OFF}-${entity.END_TEXT_OFF}】</a></td>
																	<td id='sel${i.index}'>
																		<select onchange="changeColor(this,'${entity.SPAN_NUM}');" id="select_${i.index}">
																			<options>
																				<option value=''>请选择</option>
																				<c:forEach items='${termList}' var='term'>
																					<option style='color:${term.COLOR}' value='${term.TERM_CN_NAME}' <c:if test='${term.TERM_CN_NAME eq entity.ENTITY_TYPE_NAME}'>selected</c:if>>${term.TERM_CN_NAME}</option>
																				</c:forEach>
																			</options>
																		</select>
																	</td>
																	<td ><input type='checkbox' id='isNotConfirm${i.index}' onclick="checkBox(this,'${entity.SPAN_NUM}');"></td>
																	<td><a href='javascript:void(0);' onclick='javascript: deltr("${i.index}","${entity.SPAN_NUM}")'>删除实体</a></td>
															</tr>
													  </c:forEach>
												</c:if>
											</tbody>
										</table>
									</div>
									<div style="padding-top: 13px;">
											<a class="btn btn-mini btn-primary" onclick="javascript:save();">保存</a>
											<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
									</div>
								</div>
								<div id="zhongxin2" class="center" style="display: none">
									<img src="static/images/jiazai.gif" /><br />
									<h4 class="lighter block green"></h4>
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
	//取消遮罩层
	$(top.hangge());
	String.prototype.replaceAll = function(s1, s2) {
		return this.replace(new RegExp(s1, "gm"), s2);
	}
	
	var container = window.parent.document.getElementById("_Container_0");
	/* $(container).css("width", "1200px");
	$(container).css("height", "750px"); */
	
	var clearContent;
	var spanStrs = new Array();
	Array.prototype.remove = function(val) {
		 var index = this.indexOf(val);
		 if (index > -1) {
		 	this.splice(index, 1);
		 }
	};
	function contains(arr, obj) {
		  var i = arr.length;
		  while (i--) {
		    if (arr[i] === obj) {
		      return true;
		    }
		  }
		  return false;
	}
	var dataMap = new Map();
	//原始语料内容
	$(document).ready(function() {
						//文件内容
						$("#contentDiv").html('${mtsCorpus.NEW_CONTENT}');
						//恢复span ID 值 列表
						$("#contentDiv span").each(function(i){
							spanStrs.push($(this).attr("id"));
						});
						//增加<tr/>
						$("#butAdd").click(function() {
											//***********************获取选中内容，并取消选中状态***********************
											//var selRange = getRange();
											var sel, selRange;
											if (window.getSelection) {
												// IE9 and non-IE
												sel = window.getSelection();
												/* alert("selection开始的位置==" + sel.anchorOffset + "=========" + "结束的位置=="
														+ sel.focusOffset); */
												//alert("sel选中的内容==="+sel);
												if (sel.getRangeAt && sel.rangeCount) {
													var _len = $("#tab tr").length;
													selRange = sel.getRangeAt(0);
													//var startNode = document.getElementsByTagName("br").item(4);
													//selRange.setStart(startNode, 0);
													/* alert("selection开始的位置==" + sel.anchorOffset + "=========" + "结束的位置=="
															+"===="+$.trim($(selRange.commonAncestorContainer).text())+"xxxxx"); */
													var range = new String(selRange);
													var offMap = getTextOff(selRange, range,_len);
													var startTextOff = offMap['startTextOff'];
													var endTextOff = offMap['endTextOff'];
													var origCorpus = offMap['origCorpus'];
													var pnum = offMap['pnum'];
													//alert("====开始位置==="+startTextOff+"===结束位置==="+endTextOff);
													//===========start 计算位置 start ========
													//======end 计算位置 end =======
													if (range != null && range != '') {
														$("#tab").append("<tr id="+_len+" align='left'>"
																	//行号
																	+ "<input type='hidden' id='startTextOff"+_len+"' value='"+startTextOff+"'/>"
																	+ "<input type='hidden' id='endTextOff"+_len+"' value='"+endTextOff+"'/>"
																	+ "<input type='hidden' id='startHtmlOff"+_len+"'/>"
																	+ "<input type='hidden' id='endHtmlOff"+_len+"' />"
																	+ "<input type='hidden' id='origCorpus"+_len+"' value='"+origCorpus+"'/>"
																	+ "<input type='hidden' id='pnum"+_len+"' value='"+pnum+"'/>"
																	+ "<td>"
																	+ (_len + 1)
																	+ "</td>"
																	//实体
																	+ "<td id='entity"+_len+"'><a style='cursor:pointer' onclick='javascript:locateIndex(\""+pnum+"\");'>"+range+"【"+startTextOff+"-"+endTextOff+"】"+"</a></td>"
																	//选择框
																	+ "<td id='sel"+_len+"' >"
																		+ "<select onchange=\"changeColor(this,'"+pnum+"');\" id='select_"+ _len+"'>"
																			+ "<options><option value=''>请选择</option>"
																				+"<c:forEach items='${termList}' var='term'>"
																					+"<option style='color:${term.COLOR}' value='${term.TERM_CN_NAME}'>${term.TERM_CN_NAME}</option>"
																				+"</c:forEach>"
																			+"</options>"
																		+"</select>"
																	+"</td>"
																	+ "<td ><input type='checkbox' id='isNotConfirm"+ _len
																	+ "' onclick=\"checkBox(this,'"+pnum+"');\"></td>"
																	+ "<td><a href='javascript:void(0);' onclick=\"javascript:deltr("+_len+",'"+pnum+"');\">删除实体</a></td>"
																	+ "</tr>");
														//直接确定位置
													} else {//if判断结束
														alert("请选择要标注的实体");
														return;
													}
												} else {
													alert("此浏览器不兼容标注工具")
												}
												//解除选中状态
												sel.removeAllRanges();
											} else if (document.selection && document.selection.type != "Control") {
												// IE < 9
												document.selection.createRange().pasteHTML(html);
											}
										});
					});

	
	
	
	
	
	/*
    针对整个文档结构 ，获取选中内容 起始位置
  return  map
 */
 
var locateIndex = function(pnum){
	document.getElementById(pnum).scrollIntoView();
}
	

function getTextOff(selRange, range,_len) {
	var colorSpan = document.createElement("span");
	//计算span的长度
	selRange.surroundContents(colorSpan);
	var $p = $(colorSpan).closest("p");
	/**
	 *   计算位置
	 */
	var $div = $("<div></div>");
	$div.html($p.html());
 	for (var i = 0; i < spanStrs.length; i++) {
 		var $tempSpan = $div.find("span[id='" + spanStrs[i] + "']");
 		$tempSpan.after($tempSpan.text());
 		$tempSpan.remove();
	}
 	//alert("span长度===="+spanStrs.length);
	
	//alert("======删除之后的div串====="+$div.html());
	//alert("spanStrs长度===="+spanStrs.length);
	//alert("XXXXXXXX======"+$p.text());
	//
	//alert($clearContent);
	//去掉所有的span标签，只留当前的span标签
	var clearContent = new String($div.html());
	//文字TEXT开始位置
	//alert("纯净大串==="+clearContent);
	var startTextOff = clearContent.indexOf("<span");
	//文字TEXT结束位置
	var endTextOff = startTextOff + range.length;
	var pid =  $p.attr("id");
	colorSpan.id = pid +"_"+startTextOff+"_"+endTextOff;
	/*=========================START确定选中内容位置START===================*/
	/* var startTextOff = selRange.startOffset;
	var endTextOff = selRange.endOffset;
	var origCorpus = $.trim($(selRange.commonAncestorContainer).text()); */
	var offMap = {};
	offMap['startTextOff'] = startTextOff;
	offMap['endTextOff'] = endTextOff;
	/*=========================END确定选中内容位置END===================*/
	/*=========================START建立colorSpan占位START===================*/
	/*=========================END建立colorSpan占位END===================*/
	/*==================START存储选中内容 与  原始语料的对应关系START===================*/
	//原始语料_开始位置_结束位置 做 value
	
	//原始语料 做 key
	var mapKey = pid+"_"+$p.text();
	var mapValue = range+"_"+startTextOff+"_"+endTextOff;
	//在数组中不重复
	if (startTextOff != -1) {
		/**
		*  新增代码
		*/
		//以原始语料做 key
		//假设可以找出与当前选中的内容相同的文字内容
		if(dataMap.get(mapKey)){
			//并且当前选中内容的原始语料 与 与之前选中过的标注内容的原始语料相同
			if(dataMap.get(mapKey) == mapValue && contains(spanStrs,colorSpan.id)){
				alert("您选中的文字已被标注过，请继续标注其他内容或在实体列表内对此实体进行修改");
				return;
			}else{
				//相同原始语料,相同标注内容、相同索引位置 除外,已引入pid解决
				//原始语料做key，所有在当前语料中选中的内容   当做value，中间用"#"分割，
				var mapV = dataMap.get(mapKey)+"#"+mapValue;
				dataMap.put(mapKey,mapV);
			} 
		}else{
			dataMap.put(mapKey,mapValue);
		}
		//存储原始语料
		offMap["origCorpus"] = $p.text();
		//记录span的id值 ，供编辑使用
		offMap["pnum"] = colorSpan.id;
		//把id加入 数组
		spanStrs.push(colorSpan.id); 
		//alert(dataMap.get(mapKey));
		/*=========================END存储选中内容 与  原始语料的对应关系END===================*/
	} else {
		alert("程序出错，选中内容开始索引为-1");
		return;
	}
	return offMap;
}

/**
 *  ==========改变颜色==============
 */
function changeColor(obj, pnum) {
	if (window.getSelection) {
		// IE9 and non-IE
		sel = window.getSelection();
		sel.removeAllRanges();
		var $colorSpan = $("#"+pnum);
		var $tr = $(obj).parent().parent();
		var colorObj = $tr.find("input[id^='color']");
		var len_ = $tr.attr("id");
		var color =  $(obj).find("option:selected").get(0).style.color;
		if (sel.getRangeAt && sel.rangeCount) {
			selRange = sel.getRangeAt(0);
			var range = new String(selRange);
			var _len = $("#tab tr").length;
			//===========start 计算位置 start ========
			//======end 计算位置 end =======
			if (range != null && range != '') {
				alert("请在添加实体后，选择相应的实体类型");
				return;
			} else {
				$colorSpan.css("color",color);
				if(colorObj.prop("outerHTML")){
					colorObj.val(color);
				}else{
					$tr.append("<input type='hidden' id='color"+len_+"' value='"+color+"'/>");
				}
				
			}
		} else {
			$colorSpan.css("color",color);
			if(colorObj.prop("outerHTML")){
				colorObj.val(color);
			}else{
				$tr.append("<input type='hidden' id='color"+len_+"' value='"+color+"'/>");
			}
		}
		if($("#select_"+len_).val() != null && $("#select_"+len_).val() != ""){
			$tr.find("input[id^='isNotConfirm']").removeAttr("checked");
		}
	} else if (document.selection && document.selection.type != "Control") {
		// IE < 9
		document.selection.createRange().pasteHTML(html);
	}
}
	
//保存操作
function save() {
	var corpusMap = new Map();
	var jsondata = {};
	//保存原始语料
	var flag = true;
	var entityArray  = new Array();
	$("#tab tr").each(
			function(i) {
				//原始语料
				//var origCorpus = $(this).find("input[id^='origCorpus']").val();
				var entity = new Object();
				//保存编辑区内容
				//实体名称
				var entityName = $(this).find("td[id^='entity']").text();
				entityName = entityName.substring(0, entityName.lastIndexOf("【"));
				entity['entityName'] = entityName;
				//是否不确定  (不确定 0  打勾)    1 确定 
				var isNotConfirm;
				var ischeck = $(this).find("input[id^='isNotConfirm']").attr("checked");
				var color = $(this).find("input[id^='color']").val();
				var entityTypeId = $(this).find("td[id^='sel']").find("option:selected").val();
				var entityTypeName = $(this).find("td[id^='sel']").find("option:selected").text();
				if (ischeck) {
					isNotConfirm = '0';
					entity['color'] = '#B1B1B1';
					entity['entityTypeId'] = '不确定';
					entity['entityTypeName'] = '不确定';
				} else {
					isNotConfirm = '1';
					entity['color'] = color;
					entity['entityTypeId'] = entityTypeId;
					entity['entityTypeName'] = entityTypeName;
				}
				entity['isNotConfirm'] = isNotConfirm;
				//实体类型
				var entityTypeId = $(this).find("td[id^='sel']").find("option:selected").val();
				var entityTypeName = $(this).find("td[id^='sel']").find("option:selected").text();
				//如果没勾上是否确定类型，并且没选择类型，给出提示
				if (entityTypeId == "" && isNotConfirm == '1') {
					alert("请为实体\"" + entityName + "\"选择实体类型");
					flag = false;
				}
				var startTextOff = $(this).find(
						"input[id^='startTextOff']").val();
				var endTextOff = $(this).find(
						"input[id^='endTextOff']").val();
				//P的序号
				var pnum = $(this).find("input[id^='pnum']").val();
				entity['startTextOff'] = startTextOff;
				entity['endTextOff'] = endTextOff;
				entity['spanNum'] = pnum;
				entityArray.push(entity);
			});
	jsondata['entityList'] = entityArray;
	jsondata['pid'] = $("#contentDiv p").attr("id");
	jsondata['phtml'] = $("#contentDiv").html();
	$("#jsonContent_").val(JSON.stringify(jsondata));
	if (flag == true) {
		$("#mtsCorpusAssertEditForm").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
}

//删除实体
var deltr = function(index,spanid) {
	$("tr[id='" + index + "']").remove();//删除当前行
	var $colorSpan = $("#" + spanid);
	var text = $colorSpan.text();
	$colorSpan.after(text);
	$colorSpan.remove();
	//删除索引后，顺序向上推移
	spanStrs.remove(spanid);
}
	
	function UUID() {
		var s = [];
		var hexDigits = "0123456789abcdef";
		for (var i = 0; i < 36; i++) {
			s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
		}
		s[14] = "4";
		s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);

		s[8] = s[13] = s[18] = s[23] = "-";

		var uuid = s.join("");
		return uuid;
	}

	//点击不确定
	function checkBox(obj,pnum) {
		$(obj).attr("checked", obj.checked);
		var $tr = $(obj).parent().parent();
		var $colorSpan = $("#"+pnum);
		var len_ = $tr.attr("id");
		var color = '#B1B1B1'
		var colorObj = $tr.find("input[id^='color']");
		if(obj.checked){
			$colorSpan.css("color",color);
			$("#select_"+len_).val("");
			if(colorObj.prop("outerHTML")){
				colorObj.val(color);
			}else{
				$tr.append("<input type='hidden' id='color"+len_+"' value='"+color+"'/>");
			}
		}else{
			$colorSpan.css("color",'');
			if(colorObj.prop("outerHTML")){
				colorObj.remove();
			}
		}
	}


	/*
	HTML文档结构   --> 字符串
	 */
	function htmlToString(html) {
		var str = new String(html);
		return str;
	}

	/*
		字符串 --> HTML文档结构
	 */
	function parseDom(arg) {
		var objE = document.createElement("div");
		objE.innerHTML = arg;
		return objE.childNodes;
	};

	$(function() {
		//下拉框
		if (!ace.vars['touch']) {
			$('.chosen-select').chosen({
				allow_single_deselect : true
			});
			$(window).off('resize.chosen').on('resize.chosen', function() {
				$('.chosen-select').each(function() {
					var $this = $(this);
					$this.next().css({
						'width' : $this.parent().width()
					});
				});
			}).trigger('resize.chosen');
			$(document).on('settings.ace.chosen',
					function(e, event_name, event_val) {
						if (event_name != 'sidebar_collapsed')
							return;
						$('.chosen-select').each(function() {
							var $this = $(this);
							$this.next().css({
								'width' : $this.parent().width()
							});
						});
					});
			$('#chosen-multiple-style .btn').on('click', function(e) {
				var target = $(this).find('input[type=radio]');
				var which = parseInt(target.val());
				if (which == 2)
					$('#form-field-select-4').addClass('tag-input-style');
				else
					$('#form-field-select-4').removeClass('tag-input-style');
			});
		}
	});
	
	
	function Map() {  
	    this.elements = new Array();  
	    //获取MAP元素个数  
	    this.size = function() {  
	        return this.elements.length;  
	    };  
	    //判断MAP是否为空  
	    this.isEmpty = function() {  
	        return (this.elements.length < 1);  
	    };  
	    //删除MAP所有元素  
	    this.clear = function() {  
	        this.elements = new Array();  
	    };  
	    //向MAP中增加元素（key, value)   
	    this.put = function(_key, _value) {  
	        this.elements.push( {  
	            key : _key,  
	            value : _value  
	        });  
	    };  
	    //删除指定KEY的元素，成功返回True，失败返回False  
	    this.remove = function(_key) {  
	        var bln = false;  
	        try {  
	            for (i = 0; i < this.elements.length; i++) {  
	                if (this.elements[i].key == _key) {  
	                    this.elements.splice(i, 1);  
	                    return true;  
	                }  
	            }  
	        } catch (e) {  
	            bln = false;  
	        }  
	        return bln;  
	    };  
	    //获取指定KEY的元素值VALUE，失败返回NULL  
	    this.get = function(_key) {  
	        try {  
	            for (i = 0; i < this.elements.length; i++) {  
	                if (this.elements[i].key == _key) {  
	                    return this.elements[i].value;  
	                }  
	            }  
	        } catch (e) {  
	            return null;  
	        }  
	    };  
	    //获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL  
	    this.element = function(_index) {  
	        if (_index < 0  || _index >= this.elements.length ) {   
	            return null;  
	        }  
	        return this.elements[_index];  
	    };  
	    //判断MAP中是否含有指定KEY的元素  
	    this.containsKey = function(_key) {  
	        var bln = false;  
	        try {  
	            for (i = 0; i < this.elements.length; i++) {  
	                if (this.elements[i].key == _key) {  
	                    bln = true;  
	                }  
	            }  
	        } catch (e) {  
	            bln = false;  
	        }  
	        return bln;  
	    };  
	    //判断MAP中是否含有指定VALUE的元素  
	    this.containsValue = function(_value) {  
	        var bln = false;  
	        try {  
	            for (i = 0; i < this.elements.length; i++) {  
	                if (this.elements[i].value == _value) {  
	                    bln = true;  
	                }  
	            }  
	        } catch (e) {  
	            bln = false;  
	        }  
	        return bln;  
	    };  
	    //获取MAP中所有VALUE的数组（ARRAY）  
	    this.values = function() {  
	        var arr = new Array();  
	        for (i = 0; i < this.elements.length; i++) {  
	            arr.push(this.elements[i].value);  
	        }  
	        return arr;  
	    };  
	    //获取MAP中所有KEY的数组（ARRAY）  
	    this.keys = function() {  
	        var arr = new Array();  
	        for (i = 0; i < this.elements.length; i++) {  
	            arr.push(this.elements[i].key);  
	        }  
	        return arr;  
	    };  
	}  
</script>
</html>