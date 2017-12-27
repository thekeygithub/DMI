jQuery(function($){
    my_initGrid();
});
/**
初始化表格
CustomersData：要填充的数据
*/
function my_initGrid(){
   window['g'] = $("#maingrid").ligerGrid({
           checkbox: true,
           url:"queryPortalList.action",
           columns: [
           { display: '资源名称', name: 'pname', minWidth: 120 ,width:"25%",isSort:false},
           { display: '资源URL', name: 'url', minWidth: 120 ,width:"25%",isSort:false},
           { display: '备注', name: 'remark', minWidth: 240,width:"45%",isSort:false}
           ],
           pageSize:10,
           root:"listmodal",
           record:"record",
           title:"门户资源列表",
           toolbar: { items: [
           { text: '增加', click: itemclick, icon: 'add' },
           { line: true },
           { text: '修改', click: itemedit, icon: 'modify' },
           { line: true },
           { text: '删除', click: itemdelete, icon: 'delete'}
           ]
           }
       });
   }
   
   /**
     打开添加信息窗口的方法
   */
   function  itemclick(){
    var url = "system/portal/SysPortalAdd.jsp";
	winOpen(url,'添加门户资源',490,300,'添加','取消',function(data){
       	$.ajax({
			url:"sysPortalAdd.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
				if(mm.result == "success"){
					g.addRow(mm.model);
	       			top.$.ligerDialog.success("添加信息成功!");
				}else{
					top.$.ligerDialog.error("添加信息失败!");
				}
       			
			}, 
			error:function (error) {
				top.$.ligerDialog.error("添加信息失败!" + error.status);
		}});
	});
   
   }
   
    /**
     打开修改信息窗口的方法
   */
   function  itemedit(){
      var selected = g.getSelected();
      if (!selected) {  top.my_alert('请选择要操作的数据行!',"warn"); return; }
      var id = (g.getSelectedRow()).id; 
      var url = "system/portal/SysPortalEdit.jsp?id="+id;
		winOpen(url,'修改门户资源',490,300,'修改','取消',function(data){
		   	$.ajax({
				url:"sysPortalUpdate.action", 
				data:data,
				dataType:"json", 
				type:"post",
				success:function (mm) {
					if(mm.result == "success"){
						 g.updateRow(selected,mm.model);
		       			top.$.ligerDialog.success("修改信息成功!");
					}else{
						top.$.ligerDialog.error("修改信息失败!");
					}
		      			
				}, 
				error:function (error) {
					top.$.ligerDialog.error("修改信息失败!" + error.status);
				}
			});
		});
 	}
   
   
   /**
    删除信息的方法
   */
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
        /**
           删除数据库数据
        */
      $.post("sysPortalDelete.action", { ids: idstr},
       function(data){
        g.deleteSelectedRow();
        top.my_alert("删除数据成功!","success");
      });
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