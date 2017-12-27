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
		rowHeight:22,
		height:"100%",
		heightDiff:-8,
		url:"menuQueryList.action",
		columns: [
		{ display: '菜单名', name: 'title', width:"40%"},
		{ display: '序号', name: 'ordernum', width:"10%"},
		{ display: '菜单状态', name: 'whetherpublic', width:"10%",
			render: function (item){
	    	if (item.whetherpublic == '01') return '公开页面';
	        return '私有页面';
    	}
		},
		{ display: '描述', name: 'remark', width:"35%"}
		],
		root:"listmodal",
		sortname:"ordernum",
		record:"record",
		rownumbers:false,
		usePager:false,
		title:"系统菜单列表",
		tree: { 
               	columnName: 'title', 
               	idField: 'id',
                parentIDField: 'pid',
                isExpand:false
	           },
        onAfterShowData: function(currentData){
        	itemss();
        },
		toolbar: { items: [
			{ text: '增加', click: itemclick, icon: 'add' },
			{ line: true },
			{ text: '修改', click: itemedit, icon: 'modify' },
			{ line: true },
			{ text: '删除', click: itemdelete, icon: 'delete'},
			{ line: true },
			{ text: '展开全部', click: itemzk, icon: 'communication'},
			{ line: true },
			{ text: '收缩全部', click: itemss, icon: 'communication'}
			]
			}
	});
}


function itemzk(){//展开全部
	g.expandAll();
}

function itemss(){//收缩全部
 	g.collapseAll();
}
   
function  itemclick(){
	var url = "system/menu/MenuAdd.jsp";
	winOpen(url,'添加菜单',490,400,'添加','取消',function(data){
       	$.ajax({
			url:"menuAdd.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
				if(mm.result == "success"){
					g.addRow(mm.modal);
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
   
  
   function  itemedit(){
      var selected = g.getSelected();
      if (!selected) { $.ligerDialog.warn('请选择行'); return; }
      var id = (g.getSelectedRow()).id; 
      var url = "system/menu/MenuEdit.jsp?id="+id;
      winOpen(url,'修改菜单信息',490,400,'保存','取消',function(data){
       	$.ajax({
			url:"menuEdit.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
				if(mm.result == "success"){
					 var selected = g.getSelected();
      				 g.updateRow(selected,mm.modal);
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
   
   function  itemdelete(){
    var selected = g.getSelected();
    if (!selected) {  top.my_alert('请选择要删除的数据行!',"warn"); return; }
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
      $.post("menuQueryHasChildren.action", { ids: idstr},//判断是否含有子菜单
       function(data){
	       if(data.result == "false"){
		          /**
			           删除数据库数据
			        */
			      $.post("menuDel.action", { ids: idstr},
			       function(data){
				       if(data.result == "success"){
					        g.deleteSelectedRow();
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