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
		url:"deptQueryList.action",
		columns: [
		{ display: '部门名称', name: 'corpname', width:"30%",isSort:true},
		{ display: '部门编码', name: 'epid', width:"30%",isSort:true},
		{ display: '备注', name: 'remark',width:"35%" }
		],
		pageSize:10,
		root:"listmodal",
		sortname:"corpname",
		record:"record",
		tree: { 
			columnName: 'corpname', 
			idField: 'id',
			parentIDField: 'pid'
		},
	 	rownumbers:true,
	 	title:"部门列表",
	 	onSuccess:function(data){
			
		},
		toolbar: { items: [
   			{ text: '增加', click: itemclick, icon: 'add' },
  			{ line: true },
  			{ text: '修改', click: itemedit, icon: 'modify' },
  			{ line: true },
  			{ text: '删除', click: itemdelete, icon: 'delete'},
  		]}
       });
   }
   
   /**
     打开添加信息窗口的方法
   */
   function  itemclick(){
      var url = "system/dept/CorpAdd.jsp?pid=0";
      winOpen(url,"添加部门信息",500,300,'添加','取消',function(data){
	       	$.ajax({
				url:"corpAdd.action", 
				data:data, 
				dataType:"json", 
				type:"post",
				success:function (mm) {
	       			if("error"==mm.result){
	       				top.my_alert("添加部门信息失败！");
	       			}else{
	       				g.addRow(mm);
						top.$.ligerDialog.success("添加部门信息成功！");
	       			}
				}, 
				error:function (error) {
					top.my_alert("添加部门信息失败！" + error.status,"error");
				}
			});
 		});
   
   }
   
    /**
     打开修改信息窗口的方法
   */
   function  itemedit(){
		var selected = g.getSelected();
		if (!selected) { top.my_alert('请选择数据行',"warn"); return; }
		var id = (g.getSelectedRow()).id; 
		var url = "system/dept/CorpEdit.jsp?id="+id;
		winOpen(url,"修改部门信息",500,300,'修改','取消',function(data){
	     	$.ajax({
				url:"corpEdit.action", 
				data:data, 
				dataType:"json", 
				type:"post",
				success:function (mm) {
		     		if("error"==mm.result){
	       				top.my_alert("修改部门信息失败！");
	       			}else{
	       				g.updateRow(selected,mm);
						top.$.ligerDialog.success("修改部门信息成功！");
	       			}
				}, 
				error:function (error) {
					top.$.ligerDialog.error("修改部门信息失败！");
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
				/**是否含有未选中的子菜单*/
				$.ajax({
					url:"ifDelCorp.action?ids="+idstr, 
					type:"post",
					success:function (mm) {
			     		if("error"==mm){
		       				top.my_alert("所选部门中有未选中的子部门，请重新勾选！");
		       			}else{
		       				$.post("corpDel.action", { ids: idstr},
   								function(datamm){
   									g.deleteSelectedRow();
       								top.$.ligerDialog.success("删除数据成功！");
   				       		});
		       			}
					}, 
					error:function (error) {
						top.$.ligerDialog.error("删除部门信息失败！");
					}
				});
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