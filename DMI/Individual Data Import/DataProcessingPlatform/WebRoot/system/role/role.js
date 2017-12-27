jQuery(function($){
    my_initGrid();
});
/**
初始化表格
CustomersData：要填充的数据
*/
function my_initGrid(){
	window['g']=$("#maingrid").ligerGrid({
		checkbox: true,
		url:"roleQueryList.action",
		columns: [
			{ display: '角色名', name: 'rolename', minWidth: 100 ,width:"30%",isSort:true},
			{ display: '备注', name: 'remark', minWidth: 100,width:"40%" }
		],
		pageSize:10,
		rownumbers:true,
		root:"listmodel",
		record:"record",
		title:"角色管理",
		width: '100%',
		height: '100%',
		heightDiff:-8,
		frozenCheckbox:false,
		frozenRownumbers:false,
		toolbar: getBtnReourceByUrl()
		//{ items: [
		//	{ text: '增加', click: itemclick, icon: 'add' },
		//	{ line: true },
		//	{ text: '修改', click: itemedit, icon: 'modify' },
		//	{ line: true },
		//	{ text: '删除', click: itemdelete, icon: 'delete'},
		//	{ line: true },
		//	{ text: '操作权限',click: itemqx2, icon: 'role'},
		//	{ line: true },
		//	{ text: '按钮权限',click: itemqx3, icon: 'settings'}
		//]}
	});
}
	
	/*
	 * 查询岗位下的用户
	 */
	function itemquery(){
		var selected = g.getSelected();
		if (!selected) { top.my_alert('请选择角色',"warn"); return; }
		if (g.getSelecteds().length>1) { top.my_alert('请选择唯一的角色',"warn"); return; }
		var id = (g.getSelectedRow()).id;
		var rolename = (g.getSelectedRow()).rolename;
		var url = "system/role/roleHaveUserList.jsp?id="+id;
		winOpen1(url,rolename+'角色下拥有用户列表',900,600,"关闭页面");
	}
	function winOpen1(url,title,width,height,button2){
		window.top.$.ligerDialog.open({
			width: width, height: height, url: url, title: title, buttons: [{
				text: button2, onclick: function (item, dialog) {
					dialog.close();
				}
			}]
	     });
	}
	/**
	  打开添加信息窗口的方法
	*/
	function  itemclick(){
		var url = "system/role/RoleAdd.jsp";
		winOpen(url,'添加角色',490,300,'添加','取消',function(data){
       	$.ajax({
			url:"roleAdd.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
				if(mm.result == "success"){
					g.addRow(mm.model);
	       			top.$.ligerDialog.success("添加角色信息成功!");
				}else{
					top.$.ligerDialog.error("添加角色信息失败!");
				}
			}, 
			error:function (error) {
				top.$.ligerDialog.error("添加角色信息失败!" + error.status);
		}});
	});
	}
   
    /**
     打开修改信息窗口的方法
   */
   function  itemedit(){
      var selected = g.getSelected();
      if (!selected) { top.my_alert('请选择数据行',"warn"); return; }
      var id = (g.getSelectedRow()).id; 
      var url = "system/role/RoleEdit.jsp?id="+id;
      winOpen(url,'修改角色信息',490,300,'保存','取消',function(data){
       	$.ajax({
			url:"roleEdit.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
				if(mm.result == "success"){
					 var selected = g.getSelected();
      				 g.updateRow(selected,mm.model);
	       			 top.$.ligerDialog.success("修改角色信息成功!");
				}else{
					top.$.ligerDialog.error("修改角色信息失败!");
				}
			}, 
			error:function (error) {
				top.$.ligerDialog.error("添加角色失败!" + error.status);
			}
			});
		});
   }
   
   
   /**
    删除信息的方法
   */
   function  itemdelete(){
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
        if(QueryIsDelete("role",idstr)=="false"){
			top.$.ligerDialog.error("删除数据失败，已分配人员的角色不能删除！");
		}else{
	        /**
	           删除数据库数据
	        */
	      $.post("roleDel.action", { ids: idstr},
	       function(data){
	       		if(data.result == "success"){
			      	g.deleteSelectedRow();
			        top.$.ligerDialog.success("删除数据成功!");
		        }else{
		        	top.$.ligerDialog.error("删除数据失敗!");
		        }
	      },"json");
      }
     }
     });
   }
   /**
     打开权限窗口的方法
   */
   function  itemqx(){
      var selected = g.getSelected();
      if (!selected) { alert('请选择行'); return; }
      var id = (g.getSelectedRow()).id; 
      window.top.my_openwindow("roleqx","system/role/RoleQX.jsp?id="+id,600,400,"修改权限");
   }
   
   /**
     打开权限窗口的方法
   */
   function  itemqx2(){
      var selected = g.getSelected();
      if (!selected) { top.my_alert('请选择角色！',"warn"); return; }
      var id = (g.getSelectedRow()).id; 
      window.top.$.ligerDialog.open({
                    width: 800, height: 500, url: 'system/role/RoleOfAU.jsp?role_id=' + id, title: "授权", buttons: [
                        {
                            text: '保存', onclick: function (item, dialog) {
                                dialog.frame.f_save();
                            }
                        },
                        {
                            text: '关闭', onclick: function (item, dialog) {
                                dialog.close();
                            }
                        }
                    ]
                });
   }
   
   /**
     打开权限窗口的方法
   */
   function  itemqx3(){
      var selected = g.getSelected();
      if (!selected) { top.my_alert('请选择角色！',"warn"); return; }
      var id = (g.getSelectedRow()).id; 
      window.top.$.ligerDialog.open({
                    width: 800, height: 400, url: 'system/role/RoleBtnQX.jsp?role_id=' + id, title: "非菜单页面按钮授权", buttons: [
                        {
                            text: '保存', onclick: function (item, dialog) {
                                dialog.frame.f_save();
                            }
                        },
                        {
                            text: '关闭', onclick: function (item, dialog) {
                                dialog.close();
                            }
                        }
                    ]
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