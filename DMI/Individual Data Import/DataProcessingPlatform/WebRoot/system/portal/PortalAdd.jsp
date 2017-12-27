<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Portal页面</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script type="text/javascript">
        $(function (){
        	$("#form1").ligerForm();
        	my_getURL("");
        	my_getWidth("32");
       		my_getHeight("208");
        });
        
        function my_getWidth(initVale){
        	var m = $("#width").ligerComboBox({
			    width : 280,
			    data:[{"id":"32","text":"33%"},{"id":"48","text":"50%"},{"id":"64","text":"66%"},]
			}); 
			m.setValue(initVale);
        }
        
       function my_getHeight(initVale){
        	var m = $("#height").ligerComboBox({
			    width : 280,
			    data:[{"id":"208","text":"50%"},{"id":"420","text":"100%"}]
			}); 
			m.setValue(initVale);
        }
        
		/*
		   门户资源获取
		*/
		function my_getURL(mkey){
			var m = $("#url").ligerComboBox({
				width : 280,
				url:"querySelectPortal.action",
				onSelected: function (newvalue){
					if(newvalue != ""){
					 	$("#title").val(liger.get("url").getText());
					}else if(newvalue === ""){
						$("#title").val("");
					}
				}
				}); 
			m.setValue(mkey);
		}
     
	    /*
	    添加模块
	    */
	 function f_add() {
	 	var $width = liger.get("width").getValue() + "%";
	 	var $height = liger.get("height").getValue();
	 	var $rowIndex = $("#rowIndex").val();
	 	var $columnIndex = $("#columnIndex").val();
	 	var $title = $("#title").val();
	 	var $URL = liger.get("url").getValue();
	 	var json = {"width":$width,"height":$height,"rowIndex":$rowIndex,"columnIndex":$columnIndex,"title":$title,"url":$URL};
	 	if(checkData()){
	 		return json;
	 	}else{
	 		return null;
	 	}
	 	return  null;
	}  
	
	function checkData(){//验证数据
		var $width = liger.get("width").getValue();
	 	var $height = liger.get("height").getValue();
	 	var $rowIndex = $("#rowIndex").val();
	 	var $columnIndex = $("#columnIndex").val();
	 	var $title = $("#title").val();
	 	var $URL = liger.get("url").getValue();
	 	
	 	if($URL == null || $URL == ""){
			$.ligerDialog.warn("请选择资源！");
			return false;
		}
		
		if($title == null || $title == ""){
			$.ligerDialog.warn("标题不能为空！");
			return false;
		}
		if($width == null || $width == ""){
			$.ligerDialog.warn("请选择模块宽度！");
			return false;
		}
		if($height == null || $height == ""){
			$.ligerDialog.warn("请选择模块高度！");
			return false;
		}
		return true;
	}
    </script>
    <style type="text/css">
        body{ font-size:14px;}
        .td_tit,.td_cont{ padding:2px;height:27px}
    </style>
</head>
<body>  
	<form action="" id="form1" name="form1">
		<input type="hidden" id="rowIndex" value="0" name="model.rowIndex"/>
	     <table width="99%">
	          <tr>
	          <td align="right" class="td_tit">选择资源：</td>
	          <td class="td_cont">
	          <input ltype="select" id="url" value="" name="model.url"/>
	          </td>
	         </tr>
	          <tr>
	          <td align="right" class="td_tit">标题：</td>
	          <td class="td_cont"><input type="text" id="title" value="" name="model.title" ligerui='{width:280}' /></td>
	         </tr>
	         <tr>
	          <td align="right" class="td_tit">宽度(%)：</td>
	          <td class="td_cont"><input ltype="select" id="width"/></td>
	         </tr>
	         <tr>
	          <td align="right" class="td_tit">高度：</td>
	          <td class="td_cont"><input ltype="select" id="height"/></td>
	         </tr>
	         <tr>
	          <td align="right" class="td_tit">第几列：</td>
	          <td class="td_cont"><input type="text" id="columnIndex" value="" name="model.columnIndex"  ltype='spinner' ligerui="{type:'int',isNegative:false,width:280}" /></td>
	         </tr>
	      </table>
	</form>
</body>
</html>
