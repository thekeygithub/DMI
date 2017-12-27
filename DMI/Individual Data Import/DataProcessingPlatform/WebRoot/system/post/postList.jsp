<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath %>"/>
    <title>职务列表页面</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" />
    <script type="text/javascript" src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script type="text/javascript" src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script> 
  	<script type="text/javascript">
		$(function () {
			window['g'] = $("#maingrid").ligerGrid({
				checkbox:true,
				url: "postQueryList.action",
				columns: [
				{ display: '职务名称', name: 'postname', minWidth:30 ,width:"30%",isSort:true},
				{ display: '职务排序', name: 'ordernum', minWidth:30 ,width:"15%",isSort:true},
				{ display: '备注', name: 'remark', minWidth: 30 ,width:"50%",isSort:true}
				],
				rownumbers:true,
				pageSize:10,
				width: '100%',
                height: '100%',
				root:"listmodal",
				record:"record",
				alternatingRow:true,
				frozenCheckbox:false,
				heightDiff:-8,
				title:"职务信息",
				toolbar: { 
					items: [
						{ text: '增加', click: itemclick, icon: 'add' },
						{ line: true },
						{ text: '修改', click: itemedit, icon: 'modify' },
						{ line: true },
						{ text: '删除', click: itemdelete, icon: 'delete'}
					]
				}
			});

			$("#serchform").ligerForm();
			$("#searchBtn").click(function(){
				var name = $("#name").val();
				g.setOptions({newPage:1});
				g.setOptions({
				       parms: [
				       		{ name: 'name', value: name }
				      		]
				});
				g.loadData();
			});
			$("#clearBtn").click(function(){
				$("#name").val("");
				g.setOptions({newPage:1});
				g.setOptions({
				       parms: [
				       		{ name: 'name', value: "" }
				      		]
						 });
				g.loadData();
			});
		});
		
		
		function onSelect(note) {
            var url = "postQueryList.action?cdid="+note.data.id;
            g.set('url',url);
        }
		/**
		  打开添加信息窗口的方法
		*/
		function  itemclick(){
			var url = "system/post/postAdd.jsp";
			winOpen(url,'添加职务信息',490,300,'添加','取消',function(data){
				$.ajax({
				url:"postAdd.action", 
				data:data, 
				async:false,
				dataType:"json", 
				type:"post",
				success:function (mm) {
					if("error"==mm.result){
	       				top.my_alert("添加职务信息失败!");
	       			}else{
	       				g.addRow(mm);
	       				top.my_alert("添加职务信息成功！");
	       			}
				}, 
				error:function (error) {
					top.my_alert("添加职务信息失败！" + error.status);
				}
			});
			});
		}
	   
		/**
		  打开修改信息窗口的方法
		*/
		function  itemedit(){
			var selected = g.getSelected();
			if (!selected) { window.top.my_alert('请选择行',"warn"); return; }
			var id = (g.getSelectedRow()).id; 
			var url = "system/post/postEdit.jsp?id="+id;
			winOpen(url,'修改职务信息',490,300,'保存','取消',function(data){
				$.ajax({
				url:"postUpdate.action", 
				data:data, 
				async:false,
				dataType:"json", 
				type:"post",
				success:function (mm) {
					if("error"==mm.result){
	       				top.my_alert("修改职务信息失败!");
	       			}else{
	       				g.updateRow(selected,mm);
	       				top.my_alert("修改职务信息成功！");
	       			}
				}, 
				error:function (error) {
					alert("修改职务信息失败！" + error.status);
			}});
			});
		}
		
		/**
		 删除信息的方法
		*/
		function  itemdelete(){
			var selected = g.getSelected();
			if (!selected) {  top.my_alert('请选择要删除的数据行!',"warn"); return; }
			window.top.$.ligerDialog.confirm("确定删除选择的数据", "提示", function (ok) {
				if (ok) {
					var selecteds = g.getSelecteds();
					var idstr="";//所有选择行的id
					for(var i=0;i<selecteds.length;i++){
						idstr = idstr + selecteds[i].id;
						if(i!=(selecteds.length-1)){
							idstr = idstr + ",";
						}
					}
					if(QueryIsDelete("post",idstr)=="false"){
						top.my_alert("删除数据失败，已分配人员的职务不能删除！","error");
					}else{
						g.deleteSelectedRow();
							  /**
							      删除数据库数据
							    */
						$.post("postDel.action", { ids: idstr},
							function(){
							       top.my_alert("删除数据成功!","success");
							});
					}
				}
			});
		}
		/**
			是否可以删除信息
		*/
		function  QueryIsDelete(type,id){
		   	var dataPost = {"type":type,"ids":id};
		   	var dataMM;
			$.ajax({
				url:"QueryIsDelete.action", 
				data:dataPost, 
				async:false,
				dataType:"json", 
				type:"post",
				success:function (mm) {
					dataMM = mm; 
				}, 
				error:function (error) {
					dataMM = false;
				}
			});
			return dataMM;
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
</head>
<body style="padding: 0px;overflow:hidden;">
	<div id="toolbar" class="searchDiv">
   		<form id="serchform">
    		<table>
    			<tr>
    				<td class="ser_cont"><input type="text" id="name" value=""/></td>
    				<td class="ser_cont"><input type="button" id="searchBtn" value="查询" class="btn"/></td>
    				<td class="ser_cont"><input type="button" id="clearBtn" value="清空" class="btn"/></td>
    			</tr>
    		</table>
   		</form>
   	</div>
    <div id="maingrid" style="margin-top: -1px; margin-left: -1px"></div>
</body>
</html>
