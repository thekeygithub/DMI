var treemanager;
var menu;
var actionNodeID;
var actionNodeType;
jQuery(function($){
 	$("#layout1").ligerLayout({ leftWidth: 250, allowLeftCollapse: true,  height: '100%',heightDiff:-1,space:1});
 	 menu = $.ligerMenu({ top: 100, left: 100, width: 120, items:
		[
			{ text: '添加菜单', click: itemmenu, icon: 'add' },
			{ line: true },
			{ text: '修改菜单', click: itemmenu, icon: 'modify' },
			{ line: true },
			{ text: '删除菜单', click: itemmenu, icon: 'delete' }
		]
	});
    $("#tree1").ligerTree({
                onSelect: onSelect,
                parentIDFieldName :'pid',
                idFieldName: 'id',
                checkbox: false,
                itemopen: false,
				onContextmenu: function (node, e){ 
					actionNodeID = node.data.id;
					actionNodeType = node.data.url;
					menu.show({ top: e.pageY, left: e.pageX });
					return false;
		 		}
          });
     treemanager = $("#tree1").ligerGetTreeManager();     
     function onSelect(note) {
     	  var pageName = note.data.url;
     	  $("#pagename").val(pageName);
     	  $("#menuid").val(note.data.id);
     	  $("#menuname").val(note.data.text);
          //加载数据
          var manager = $("#maingrid").ligerGetGridManager();
          var url = "btnResourceQueryList.action?menuid="+note.data.id+"&rmd=" + new Date().getTime();
          manager.set("url",url);
      	}
     my_initTree();
   	 my_initGrid();
});

/*
初始化树形
*/
function my_initTree(){
	$.ajax({
			url:"topMenuQueryTree.action", 
			dataType:"json", 
			type:"post",
			success:function (data) {
				data.push({ id:'QT', text: '其他' ,pid:'0',icon:'images/Icon/33.png',url:""});
				var managerTree = $("#tree1").ligerGetTreeManager();
				managerTree.setData(data);
			}, 
			error:function (error) {
				alert("获取按钮资源失败！" + error.status);
			}
		});
}

/**
初始化表格
CustomersData：要填充的数据
*/
function my_initGrid(){
   window['g']=$("#maingrid").ligerGrid({
           checkbox: true,
           width:'100%',
           url:"btnResourceQueryList.action",
           columns: [
           { display: '页面名称', name: 'pagename', minWidth: 120,width:"20%"},
           { display: '资源名称', name: 'resourcename', minWidth: 120,width:"20%"},
           { display: '绑定事件', name: 'btnevent', minWidth: 120,width:"20%" },
           { display: '备注', name: 'remark', minWidth: 140 ,width:"39%"}
           ],
           pageSize:10,
           root:"listmodel",
           sortname:"resourcename",
           frozenCheckbox:false,
           record:"record",
           height:"100%",
           heightDiff:-9,
           toolbar: { items: [
           { text: '增加',id:'add', click: itemclick, icon: 'add' },
           { line: true },
           { text: '修改',id:"edit",click: itemedit, icon: 'modify' },
           { line: true },
           { text: '删除',id:"delete", click: itemdelete, icon: 'delete'}
           ]
           }
       });
       
   }
   
   
   function  itemclick(){
   	 var pagename = $("#pagename").val();
  	 var menuid = $("#menuid").val();
 	 var menuname = encodeURI($("#menuname").val());
   	 var strCon = "menuid="+menuid+"&menuname="+menuname+"&pagename="+pagename+"";
   	 if(menuid != ""){
    	 	var url = "system/resource/ResourceAdd.jsp?"+strCon;
			winOpen(url,'添加按钮资源',550,400,'保存','取消',function(data){
		       	$.ajax({
					url:"btnResourceAdd.action", 
					data:data,
					dataType:"json", 
					type:"post",
					success:function (mm) {
						if(mm.result == "success"){
							g.addRow(mm.model);
							top.$.ligerDialog.success("添加按钮信息成功!");
						}else{
							top.$.ligerDialog.error("添加按钮信息失败!");
						}
		       			
					}, 
					error:function (error) {
						top.$.ligerDialog.error("添加按钮信息失败!" + error.status);
				}});
			});
     }else{
     	 top.$.ligerDialog.warn("请选择菜单");
     }
   }
   
   function  itemedit(){
      var selected = g.getSelected();
      if (!selected) { window.top.my_alert('请选择行',"warn"); return; }
      var id = (g.getSelectedRow()).id; 
      var url = "system/resource/ResourceEdit.jsp?id="+id;
			winOpen(url,'修改按钮资源',550,400,'保存','取消',function(data){
		       	$.ajax({
					url:"btnResourceUpdate.action", 
					data:data,
					dataType:"json", 
					type:"post",
					success:function (mm) {
						if(mm.result == "success"){
							g.updateRow(selected,mm.model);
							top.$.ligerDialog.success("修改按钮信息成功!");
						}else{
							top.$.ligerDialog.error("修改按钮信息失败!");
						}
		       			
					}, 
					error:function (error) {
						top.$.ligerDialog.error("修改按钮信息失败!" + error.status);
				}});
			});
   }
   
	function  itemdelete(){//删除按钮资源信息
		var selected = g.getSelected();
		if (!selected) {  top.$.ligerDialog.warn('请选择要删除的数据行!'); return; }
		window.top.$.ligerDialog.confirm("确定删除选择的数据?数据删除后不可恢复！", "提示", function (ok) {
			if (ok) {
				var selecteds = g.getSelecteds();
				var idstr="";//所有选择行的id
				for(var i=0;i<selecteds.length;i++){
				   idstr = idstr + selecteds[i].id;
				   if(i!=(selecteds.length-1)){
				   idstr = idstr + ",";
				   }
				  }
				var deleteAction = "btnResourceDel.action";
				$.post(deleteAction, { ids: idstr},
				function(data){
					if(data.result == "success"){
					 	g.deleteSelectedRow();
						top.$.ligerDialog.success("删除数据成功！");
					}else{
						top.$.ligerDialog.error("删除数据失败！");
					}
				},"json");
			}
		});
	}
   
  /**
  	树形菜单右键选择事件
  */
  function itemmenu(item,i){
	if("add"==item.icon){
		itemmenuadd(actionNodeID,actionNodeType);
	}else if("modify"==item.icon){
		itemmenuedit(actionNodeID,actionNodeType);
	}else if("delete"==item.icon){
		itemmenudelete(actionNodeID,actionNodeType);
	}
  }
  
  function  itemmenuadd(id,type){
  	if(id == "QT"){
  		window.top.$.ligerDialog.warn("其他下不能添加菜单！");
 		return false;
  	}
	var url = "system/menu/MenuAdd.jsp?pid="+id;
	winOpen(url,'添加菜单',490,400,'添加','取消',function(data){
       	$.ajax({
			url:"menuAdd.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
				if(mm.result == "success"){
					 my_initTree();
	       			top.$.ligerDialog.success("添加菜单信息成功!");
				}else{
					top.$.ligerDialog.error("添加菜单信息失败!");
				}
       			
			}, 
			error:function (error) {
				top.$.ligerDialog.error("添加菜单信息失败!" + error.status);
		}});
	});
}
   
  
   function  itemmenuedit(id,type){
	   if(id == "QT"){
	  		window.top.$.ligerDialog.warn("其他不能修改！");
	 		return false;
	  	}
      var url = "system/menu/MenuEdit.jsp?id="+id;
      winOpen(url,'修改菜单信息',490,400,'保存','取消',function(data){
       	$.ajax({
			url:"menuEdit.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
				if(mm.result == "success"){
					 my_initTree();
	       			 top.$.ligerDialog.success("修改菜单信息成功!");
				}else{
					top.$.ligerDialog.error("修改菜单信息失败!");
				}
       			
			}, 
			error:function (error) {
				top.$.ligerDialog.error("添加菜单信息失败!" + error.status);
		}});
	});
   }
   
   function  itemmenudelete(id,type){
	   if(id == "QT"){
	  		window.top.$.ligerDialog.warn("其他不能进行修改！");
	 		return false;
	  	}
   		window.top.$.ligerDialog.confirm("确定删除选择的数据?数据删除后不可恢复！", "提示", function (ok) {
				if (ok) {
			     $.post("menuQueryHasChildren.action", { ids: id},//判断是否含有子菜单
			       function(data){
				       if(data.result == "false"){
					          /**
						           删除数据库数据
						        */
						      $.post("menuDel.action", { ids: id},
						       function(data){
							       if(data.result == "success"){
								       my_initTree();
								       top.$.ligerDialog.success("删除数据成功!");
							        }else{
							        	top.$.ligerDialog.error("删除数据失败！");
							        }
						      },"json");
				        }else{
				        	top.$.ligerDialog.error("删除数据失败！该菜单含有子菜单");
				        }
			      },"json");
				}
		});
   }

 /**
 打开窗口的方法
 */
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