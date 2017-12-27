<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String roleid = request.getParameter("role_id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <base href="<%=basePath%>"/>
    
    <title>角色权限</title>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<link  rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link  rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>   
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/core/base.js" ></script>  
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/plugins/ligerTree.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/plugins/ligerGrid.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/plugins/ligerLayout.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/plugins/ligerDialog.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/plugins/ligerDrag.js" ></script> 
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/json2.js" ></script> 
  	<script type="text/javascript">


        var manager = "";
        var treemanager;
        var sarr = [];
        $(function () {
            $("#layout1").ligerLayout({ leftWidth: 150, allowLeftResize: false, allowLeftCollapse: true, space: 2 });
            $("#tree1").ligerTree({
                url: 'topMenuQueryAll.action',
                onSelect: onSelect,
                idFieldName: 'id',
                textFieldName: 'title',
                iconFieldName: 'image',
                checkbox: false,
                itemopen: false
            });

            treemanager = $("#tree1").ligerGetTreeManager();

   		  manager =  $("#maingrid4").ligerGrid({
	            	checkbox: true,
	            	selectRowButtonOnly:true,
	            	rowHeight:24,
	            	async:false,
	                url: "",
	                columns: [
	                	{ display: '菜单名称', name: 'title', width: "30%" },
	                    { display: '按钮', name: 'btnStr', width: "60%",
		                    render:function(item){
		                   		 if(item.flag == "1"){
		                    		sarr.push(item);
		                    	}
		                    	return f_btnRender(item.btnStr,item.id);
	                    }
	                    }
	                ],
	                rowid: "id",
	                frozenCheckbox:false,
	                root:"list",
	                width: '99%', 
	                height: '100%',
	                usePager: false,
	                isScroll:false,
	                tree: { 
	                	columnName: 'title', 
	                	idField: 'id',
	                    parentIDField: 'pid'
	                 },
	                heightDiff: -3,
	                onAfterShowData:function(){
			            for(var i=0;i<sarr.length;i++){
			              manager.select(sarr[i]);
			            }
	                }
	            });
            

            var manager = $("#maingrid4").ligerGetGridManager();
            
            $("tbody> :checkbox").attr("checked", false);

        });

        function onSelect(note) {
        	sarr = [];
            //加载数据
            var manager = $("#maingrid4").ligerGetGridManager();
            var r_id = "<%=roleid %>";
            var url = "menuListForAu.action?tid=" + note.data.id + '&roleid=' + r_id;
            manager.set("url",url);
             //manager.loadData(true);
             $.ajax({//按钮权限获取
               	url:"roleBtnQueryByMenuId.action",
				data:"roleid="+r_id+"&menuSort="+note.data.id,
                type: 'post',
                success: function (data) {
                 	var m = eval(data);
                 	$.each(m,function(index,item){
                 		if(item != ""){
                 			$("#"+item).attr("checked","checked");
                 		}
                 	});
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $.ligerDialog.error("按钮权限获取出错！");
                }
               });
        	}

        function f_save() {
            var notes = treemanager.getSelected();
            if (notes != null && notes != undefined) {
                var app = notes.data.id;
                var r_id = "<%=roleid %>";
                var manager = $("#maingrid4").ligerGetGridManager();
                  var selecteds = manager.getSelecteds();
			      var idstr="";//所有选择行的id
			      for(var i=0;i<selecteds.length;i++){
			         idstr = idstr + selecteds[i].id;
			         if(i!=(selecteds.length-1)){
			         idstr = idstr + ",";
			         }
			         }
                f_saving();
                $.ajax({
                	url:"roleMenuModify.action",
					data:"ch="+idstr+"&roleId="+r_id+"&sort="+app,
					async:false,
                    type: 'post',
                    success: function (data) {
                       
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                       $.ligerDialog.closeWaitting();
                       top.$.ligerDialog.error("修改权限出错！");
                    }
                });
                var btnIDStr = "";
               $(".btncheck:checked").each(function(index,item){
               		btnIDStr += item.value;
               		btnIDStr += ",";
               });
               
               $.ajax({
	               	url:"roleBtnModify.action",
					data:"ch="+btnIDStr+"&roleid="+r_id+"&menuSort="+app,
	                success: function (data) {
	                   $.ligerDialog.closeWaitting();
                       top.$.ligerDialog.confirm("是否继续编辑", "保存成功", function (ok) {
		                    if (!ok) {
		                       window.frameElement.dialog.close();
		                    }
               		 	});
	                },
	                error: function (XMLHttpRequest, textStatus, errorThrown) {
	               		$.ligerDialog.closeWaitting();
	                    $.ligerDialog.error("按钮权限修改出错！");
	                }
              	 });
           	 }
            else {
                top.$.ligerDialog.error("请选择系统目录！");
            }

        }
  
       
        function f_saving() {
            $.ligerDialog.waitting("正在保存中...");
        }

		
		function f_btnRender(btnStr,menuid){
			var tempStr = "";
			if(btnStr != null && btnStr != ""){
				var btnArr = btnStr.split(",");
				for(var i=0;i<btnArr.length;i++){
					if(btnArr[i]!=""){
						var tempArr = btnArr[i].split(";");
						tempStr += "<input type=\"checkbox\" class='btncheck' id ='"+tempArr[0]+"' value='"+tempArr[0]+"' name='"+menuid+"'/>&nbsp;<label>"+tempArr[1]+"</label>";
					}
				}
			}
			return tempStr;
		}
	
    </script>
    <style type="text/css">
    	.btncheck,.btncheck1{
    	        vertical-align:middle;
    			margin-left: 10px;
    		}
    	 label{
    	        vertical-align:middle;
    		}
    	.btnTable{
    	 		width: "99%";
    	 		font-size:12px;
    	 		border-collapse: collapse;
    	 		border:1px #BED5F3 solid;
    	  }
    	 .btnTable th{
    	 		width:150px;
    	 		height:22px;
    	 		text-align: center;
    	  }
    	  .btnTable td{
    	  		border: 1px solid #B1CDE3;  
    	 		text-align: left;
    	 		height:22px;
    	 		padding: 5px;
    	  }
    	  .l-layout-center .l-layout-content{
    			overflow: auto !important;
    			}
    </style>
  </head>
  
  <body style="padding: 0px;overflow:hidden;">
    <div id="layout1" style="margin-top: -1px; margin-left: -1px">
        <div position="left" title="系统目录">
            <div id="treediv1" style="width: 250px; height: 100%; margin: -1px; float: left; border: 1px solid #ccc; overflow: auto;">
                <ul id="tree1"></ul>
            </div>
        </div>
        <div position="center">
            <div id="maingrid4"></div>
        </div>
    </div>
  </body>
</html>
