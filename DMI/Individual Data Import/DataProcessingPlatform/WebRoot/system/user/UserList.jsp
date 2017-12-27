<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath %>"/>
    <title>用户列表页面</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" />
    <script type="text/javascript" src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script type="text/javascript" src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script> 
  	<script type="text/javascript">
		var treemanager;
		var manager = "";
		$(function () {
        	$("#layout").ligerLayout({ leftWidth: 250, allowLeftResize: false, allowLeftCollapse: true, space: 2 });
			$("#tree").ligerTree({
                url: "cropDeptTreeQuery.action",
                onSelect: onSelect,
                idFieldName: 'id',
                parentIDFieldName: 'pid',
                checkbox: false,
                itemopen: false
            });
			treemanager = $("#tree").ligerGetTreeManager();
			
			window['g'] = $("#maingrid").ligerGrid({
				url: "UserQueryList.action",
				parms: [{ name: 'showsubdivision', value: $("#chk1").ligerCheckBox().getValue() }],
				checkbox:true,
				frozenCheckbox:false,//放置拖拽表格变形
				columns: [
				{ display: 'ID', name: 'id', align: 'left',minWidth: 10,width: 10,hide:true,isAllowHide:false},
				//{ display: '序号', width: 50, 
					//render: function (rowData, rowindex, value, column, rowid, page, pagesize) { 
					//render: function (page, pagesize) { 
					//	return pagesize + 1; 
					//} 
				//},
				{ display: '登陆名', name: 'loginname', minWidth:30 ,width:100,isSort:true},
				{ display: '用户姓名', name: 'username', minWidth: 30 ,width:100,isSort:true},
				{ display: '性别', name: 'sex', minWidth: 20 ,width:80,
					render: function (item){
				    	if (item.sex == 'M') return '男';
				        return '女';
		        	}
				},
				{ display: '手机', name: 'mobileprivate', minWidth: 110 ,width:100,isSort:true},
				{ display: '邮箱', name: 'emailprivate', minWidth: 120 ,width:200,isSort:true},
				{ display: '备注', name: 'remark', minWidth: 150,width:200 }
				],
				pageSize:10,
				rownumbers:true,
				width: '100%',
                height: '100%',
                heightDiff:-8,
				root:"listmodal",
				record:"record",
				alternatingRow:true,
				toolbar: { 
					items: [
						{ text: '增加', click: itemclick, icon: 'add' },
						{ text: '修改', click: itemedit, icon: 'modify' },
						{ text: '删除', click: itemdelete, icon: 'delete'},
						{ text: '详细', click: itemmess, icon: 'bookpen'}
					]
				}
			});
			
			$("#serchform").ligerForm();
			
			$("#username").keyup(function(event){
				if(event.keyCode == 13){
					var username = $("#username").val();
					var showsubdivision =  $("#chk1").ligerCheckBox().getValue();
					g.setOptions({newPage:1});
					g.setOptions({
						parms: [
							{ name: 'username', value: username },
							{ name: 'showsubdivision', value: showsubdivision }
						]
					});
					g.loadData();
				}
			});
			
			$("#searchBtn").click(function(){
				var username = $("#username").val();
				var showsubdivision =  $("#chk1").ligerCheckBox().getValue();
				g.setOptions({newPage:1});
				g.setOptions({
					parms: [
						{ name: 'username', value: username },
						{ name: 'showsubdivision', value: showsubdivision }
					]
				});
				g.loadData();
			});
			$("#clearBtn").click(function(){
				$("#username").val("");
				var showsubdivision =  $("#chk1").ligerCheckBox().getValue();
				g.setOptions({newPage:1});
				g.setOptions({
					parms: [
						{ name: 'username', value: "" },
						{ name: 'showsubdivision', value: showsubdivision }
					]
				});
				g.loadData();
			});
			
			$("#chk1").change(function () {
				var username = $("#username").val();
				var showsubdivision = $("#chk1").ligerCheckBox().getValue();
				g.setOptions({newPage:1});
				g.setOptions({
					parms: [
						{ name: 'username', value: username },
						{ name: 'showsubdivision', value: showsubdivision }
					]
				});
				g.loadData();
			});
				
		});
		
		function onSelect(note) {
            //alert("选择的是:" + note.data.text +"; id是:"+ note.data.id);
            var url = "UserQueryList.action?corpid="+note.data.id;
            g.set('url',url);
        }
		/**
		  打开添加信息窗口的方法
		*/
		function  itemclick(){
			var note = treemanager.getSelected(); 
			//alert("选择的是:" + note.data.text +"; id是:"+ note.data.id);
			var url = "";
			if(note==null){
				url = "system/user/UserAdd.jsp";
			}else{
				url = "system/user/UserAdd.jsp?corpid="+note.data.id;
			}
			winOpen(url,'添加用户信息',800,500,'添加','取消',function(data){
				$.ajax({
					url:"treeAdduser.action", 
					data:data,
					dataType:"json",
					async:false, 
					type:"post",
					success:function (mm) {
						if("error"==mm.result){
							top.my_alert("添加用户信息失败!");
						}else{
							g.addRow(mm);
							top.my_alert("添加用户信息成功！","success");
						}
					}, 
					error:function (error) {
						top.my_alert("添加用户信息失败！" + error.status);
				}});
			});
		}
	   
		/**
		  打开修改信息窗口的方法
		*/
		function  itemedit(){
			var selected = g.getSelected();
			if (!selected) { top.my_alert('请选择行',"warn"); return; }
			var id = (g.getSelectedRow()).id; 
			var url = "system/user/UserEdit.jsp?id="+id;
			winOpen(url,'修改用户信息',700,420,'修改','取消',function(data){
				$.ajax({
					url:"treeAdduser.action", 
					data:data,
					dataType:"json",
					async:false, 
					type:"post",
					success:function (mm) {
						if("error"==mm.result){
							top.my_alert("修改用户信息失败!");
						}else{
							g.updateRow(selected,mm);
							top.my_alert("修改用户信息成功！","success");
						}
					}, 
					error:function (error) {
						top.my_alert("修改用户信息失败！" + error.status);
				}});
			});
		}
		/**
		  打开详细信息窗口的方法
		*/
		function  itemmess(){
			var selected = g.getSelected();
			if (!selected) { top.my_alert('请选择行',"warn"); return; }
			var id = (g.getSelectedRow()).id; 
			window.top.my_openwindow("useredit","system/user/UserMess.jsp?id="+id,700,480,"用户详细信息");
		}
	   
		/**
		 删除信息的方法
		*/
		function  itemdelete(){
			var selected = g.getSelected();
			if (!selected) {  top.my_alert('请选择要删除的数据行!','warn'); return; }
			window.top.$.ligerDialog.confirm("确定删除选择的数据", "提示", function (ok) {
				if (ok) {
					g.deleteSelectedRow();
					var selecteds = g.getSelecteds();
					var idstr="";//所有选择行的id
					for(var i=0;i<selecteds.length;i++){
						idstr = idstr + selecteds[i].id;
						if(i!=(selecteds.length-1)){
							idstr = idstr + ",";
						}
					}
					/**
					   删除数据库数据
					*/
					$.post("treeuserDel.action", { ids: idstr},
						function(){
							top.my_alert("删除数据成功!","success");
						}
					);
				}
			});
		}
		function winOpen(url,title,width,height,button1,button2,callback){
			window.top.$.ligerDialog.open({
				width: width, height: height, url: url, title: title, buttons: [{
					text: button1, onclick: function (item, dialog) {
						var fn = dialog.frame.f_validate || dialog.frame.window.f_validate;
						var data = fn();
						if(data){
							callback(data);
							dialog.close();
						}
					}
				},{
					text: button2, onclick: function (item, dialog) {
						dialog.close();
					}
				}]
		     });
		}
  	</script>
  	<style type="text/css">
    	.l-layout-left{
			overflow: auto !important;
		}
		.l-tree{
  			width:230px !important;
  		}
    </style>
</head>
<body style="padding: 0px;overflow:hidden;">
        <div id="layout" style="margin-top: -1px; margin-left: -1px">
            <div position="left" title="组织架构">
                <div id="treediv" style="width: 250px; height: 100%; margin: -1px; float: left; border: 1px solid #ccc; overflow: auto;">
                    <ul id="tree"></ul>
                </div>
            </div>
            <div position="center" title="人员信息">
            	<div id="toolbar" class="searchDiv">
            		<form id="serchform" onsubmit="return false;">
	            		<table>
	            			<tr>
	            				<td class="ser_cont"><input type="text" id="username" value=""/></td>
	            				<td class="ser_cont"><input type="button" id="searchBtn" value="查询" class="btn"/></td>
	            				<td class="ser_cont"><input type="button" id="clearBtn" value="清空" class="btn"/></td>
	            				&nbsp;<td><input type="checkbox" name="chk1" id="chk1" checked="checked" /></td><td>是否显示子部门人员</td>
	            			</tr>
	            		</table>
            		</form>
            	</div>
                <div id="maingrid" style="margin-top: -1px; margin-left: -1px"></div>
            </div>
        </div>
</body>
</html>
