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
		url:"configQuery.action",
		columns: [
			{ display: '类别', name: 'dtype', width:"20%",isSort:true},
			{ display: '代码', name: 'dkey',  width:"20%" },
			{ display: '值', name: 'dvalue',  width:"25%" },
			{ display: '备注', name: 'remark',  width:"30%" }
		],
		pageSize:10,
		root:"listmodel",
		record:"record",
		rownumbers:false,
		title:"系统配置",
		toolbar: { items: [
			{ text: '增加', click: itemclick, icon: 'add' },
			{ line: true },
			{ text: '修改', click: itemedit, icon: 'modify' },
			{ line: true },
			{ text: '删除', click: itemdelete, icon: 'delete'},
			{ line: true },
			{ text: '查询', click: itemcx, icon: 'search2'}
			//{ text: '查询', click: itemcx, img: '../../include/LigerUI/skins/icons/search2.gif'}
		]}
	});
}
   
   /**
     打开添加信息窗口的方法
   */
function  itemclick(){
	var url = "system/config/ConfigAdd.jsp";
	winOpen(url,'添加系统配置',450,280,'添加','取消',function(data){
       	$.ajax({
			url:"configAdd.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
       			if("error"==mm.result){
       				top.my_alert("添加系统配置信息失败!");
       			}else{
	       			g.addRow(mm);
	       			top.$.ligerDialog.success("添加系统配置信息成功!","success");
       			}
			}, 
			error:function (error) {
				top.my_alert("添加系统配置信息失败!" + error.status,"error");
		}});
	});
}
   
    /**
     打开修改信息窗口的方法
   */
function  itemedit(){
	var selected = g.getSelected();
	if (!selected) {  top.my_alert('请选择行',"warn"); return; }
	var id = (g.getSelectedRow()).id; 
	var url = "system/config/ConfigEdit.jsp?id="+id;
	winOpen(url,'修改系统配置',450,280,'修改','取消',function(data){
       	$.ajax({
			url:"configEdit.action", 
			data:data,
			dataType:"json", 
			type:"post",
			success:function (mm) {
	       		if("error"==mm.result){
	   				top.my_alert("修改系统配置信息失败!");
	   			}else{
	   				g.updateRow(selected,mm);
       				top.$.ligerDialog.success("修改系统配置信息成功!","提示");
	   			}
			}, 
			error:function (error) {
				top.my_alert("修改系统配置信息失败!" + error.status,"error");
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
			$.ajax({
				url:"configDel.action?ids="+idstr, 
				dataType:"json", 
				type:"post",
				success:function (mm) {
		       		if("error"==mm.result){
		   				top.my_alert("删除系统配置信息失败!");
		   			}else{
		   				top.my_alert("删除系统配置信息成功!");
		   			}
				}, 
				error:function (error) {
					top.my_alert("删除系统配置信息失败!" + error.status);
			}});
	     }
	 });
}
   /**
      查询的方法
   */
function  itemcx(){
	$.ligerDialog.open({ title:"查询",target: $("#searchDict"),allowClose:true,isHidden:true,
		buttons: [{ text: '查询', onclick: function (item, dialog) { 
			var dtype = $("#dtype").val();
			var dvalue = $("#dvalue").val();
			g.setOptions({newPage:1});
			g.setOptions({
				parms: [
					{ name: 'dtype', value: dtype },
					{ name: 'dvalue', value: dvalue }
				]
			});
			g.loadData();
		}
		}]
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