var treemanager;
var menu;
var actionNodeID;
var actionNodeType;
var form;
$(function () {
	$("#layout").ligerLayout({ leftWidth: 250, allowLeftResize: false, allowLeftCollapse: true, space: 2 });
	menu = $.ligerMenu({ top: 100, left: 100, width: 120, items:
		[
			{ text: '添加部门', click: itemclick, icon: 'add' },
			{ line: true },
			{ text: '修改部门', click: itemclick, icon: 'modify' },
			{ line: true },
			{ text: '删除', click: itemclick, icon: 'delete' }
		]
	});
	$("#tree").ligerTree({
		url: "cropDeptTreeQuery.action",
		onSelect: onSelect,
		idFieldName: 'id',
		parentIDFieldName: 'pid',
		checkbox: false,
		itemopen: false,
		onContextmenu: function (node, e){ 
			actionNodeID = node.data.id;
			actionNodeType = node.data.url;
			menu.show({ top: e.pageY, left: e.pageX });
			return false;
 		}
	});
	treemanager = $("#tree").ligerGetTreeManager();
	$("#tab1").ligerTab({
		contextmenu:false,
		onBeforeSelectTabItem:function(tabid){ 
			var note = treemanager.getSelected(); 
			if(note == null){
				$("#cdid").val("");
				if(tabid == "userTab"){
					var userurl =  "system/user/treeTabList.jsp";
					if($("#userframe").attr("src") != userurl){
						$("#userframe").attr("src",userurl);
					}
				}
			}else{
				if(tabid == "userTab"){
					var userurl =  "system/user/treeTabList.jsp?cdid="+note.data.id;
					if($("#userframe").attr("src") != userurl){
						$("#userframe").attr("src",userurl);
					}
				}
			}
			if(tabid == "postTab"){
				var posturl = "system/post/postList.jsp";
				if($("#postframe").attr("src") != posturl){
					$("#postframe").attr("src",posturl);
				}
			}else if(tabid == "stationTab"){
				var stationurl = "system/station/stationList.jsp";
				if($("#stationframe").attr("src") != stationurl){
					$("#stationframe").attr("src",stationurl);
				}
			}else if(tabid == "roleTab"){
				var roleurl = "system/role/RoleList.jsp";
				if($("#roleframe").attr("src") != roleurl){
					$("#roleframe").attr("src",roleurl);
				}
			}else if(tabid == "areaTab"){
				var areaurl = "system/area/areaList.jsp";
				if($("#areaframe").attr("src") != areaurl){
					$("#areaframe").attr("src",areaurl);
				}
			}
		},
		onAfterSelectTabItem:function(tabid){ 
		
		}
	});
	var divHeight = $("#tab1").height() - 28;
	$(".my-tab-div").css("height",divHeight);
	$("#userframe").attr("src","system/user/treeTabList.jsp");
	$("#toolbar").ligerToolBar({ items:[
		{ text: '添加公司', click: addCompany, icon: 'add' },
		{ text: '修改公司', click: modifyCompany, icon: 'modify' }
	]});
});

/*
单位信息删除
*/
function deleteCompany(id,type){
	var mangerTree = $("#tree").ligerGetTreeManager();
	var obj = mangerTree.getDataByID(id);
	if(obj.children){
		top.$.ligerDialog.error("含有下级不能删除！");
	}else{
		window.top.$.ligerDialog.confirm("确定删除选择的数据?数据删除后不可恢复！", "提示", function (ok) {
			if (ok) {
				var deleteAction = "corpDel.action";
				$.post(deleteAction, { ids: id},
				function(mm){
					mangerTree.reload();
					top.$.ligerDialog.success("删除数据成功！");
				},"json");
			}
		});
	}
}
//树选中事件
function onSelect(note) {
	var cdid = note.data.id;
	var tabmanager = liger.get("tab1");
	var tabid = tabmanager.getSelectedTabItemID();
	tabmanager.selectTabItem(tabid);
}
//树右键选择事件
function itemclick(item, i){
	if("add"==item.icon){
		var url = "system/dept/CorpAdd.jsp?pid="+actionNodeID;
		winOpen(url,"添加部门信息",500,300,"添加","取消",function(data){
			$.ajax({
				url:"corpAdd.action", 
				data:data, 
				dataType:"json", 
				type:"post",
				success:function (mm) {
					treemanager.reload();;
					top.$.ligerDialog.success("添加部门信息成功！");
				}, 
				error:function (error) {
					top.my_alert("添加部门信息失败！" + error.status,"error");
				}
			});
		});
	}else if("modify"==item.icon){
		if("1"==actionNodeType){//代表修改公司
			top.$.ligerDialog.warn("请选择部门进行修改！");
		}else{
			var url = "system/dept/CorpEdit.jsp?id="+actionNodeID;
			winOpen(url,"修改部门信息",500,300,"修改","取消",function(data){
				$.ajax({
					url:"corpEdit.action", 
					data:data, 
					dataType:"json", 
					type:"post",
					success:function (mm) {
						treemanager.reload();;
						top.$.ligerDialog.success("修改部门信息成功！");
					}, 
					error:function (error) {
						top.my_alert("修改部门信息失败！" + error.status,"error");
					}
				});
			});
		}
	}else if("delete"==item.icon){
		deleteCompany(actionNodeID,actionNodeType);
	}
}
function addCompany(){
	var mangerTree = $("#tree").ligerGetTreeManager();
	var url = "";
	var note = mangerTree.getSelected();
	if(note){
		url = "system/corp/CorpAdd.jsp?pid="+note.data.id;
	}else{
		url = "system/corp/CorpAdd.jsp?pid=0";
	}
	winOpen(url,"添加公司信息",500,300,"添加","取消",function(data){
		$.ajax({
			url:"corpAdd.action", 
			data:data, 
			dataType:"json", 
			type:"post",
			success:function (mm) {
				treemanager.reload();;
				top.$.ligerDialog.success("添加信息成功！");
			}, 
			error:function (error) {
				top.my_alert("添加公司信息失败！" + error.status,"error");
			}
		});
	});
}
 function modifyCompany(){
	var mangerTree = $("#tree").ligerGetTreeManager();
	var url = "";
	var note = mangerTree.getSelected();
	if(note){
		if(note.data.url != "1"){
			top.$.ligerDialog.warn("请选择公司进行修改信息！");
			return false;
		}
		url = "system/corp/CorpEdit.jsp?id="+note.data.id+"&pid="+note.data.pid;
		winOpen(url,"修改公司信息",500,300,"修改","取消",function(data){
			$.ajax({
				url:"corpEdit.action", 
				data:data, 
				dataType:"json", 
				type:"post",
				success:function (mm) {
					 var mangerTree1 = $("#tree").ligerGetTreeManager();
			         var node1 = mangerTree.getSelected();
					 var nodes = { id:mm.id, text: data.mm ,pid:mm.pid,url:"0"};            
					 mangerTree1.update(node1.target, nodes);
			       	 top.$.ligerDialog.success("修改信息成功！");
				}, 
				error:function (error) {
					 top.$.ligerDialog.success("修改信息失败！" + error.status);
				}
			});
		});
	}else{
		top.$.ligerDialog.warn("请选择公司！");
	}
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