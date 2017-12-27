	jQuery(function($){
		window['g']=$("#maingrid").ligerGrid({
		    checkbox: true,
		    url:"stationQueryList.action",
		    columns: [
			    { display: '岗位名称', name: 'stationname', width:"40%",isSort:true},
			    { display: '岗位描述', name: 'remark', width:"50%",isSort:true}
		    ],
		    pageSize:10,
		    rownumbers:true,
		    root:"listmodal",
		    sortname:"stationname",
		    record:"record",
		    width: '100%',
			height: '100%',
			heightDiff:-8,
			title:"岗位管理",
		    toolbar: getBtnReourceByUrl()	
		    //{ items: [
			//    { text: '增加', click: itemclick, icon: 'add' },
			//    { line: true },
			//    { text: '修改', click: itemedit, icon: 'modify' },
			//    { line: true },
			//    { text: '删除', click: itemdelete, icon: 'delete'}
		    //]}
		});
	});
	
	/*
	 * 查询岗位下的用户
	 */
	function itemquery(){
		var selected = g.getSelected();
		if (!selected) { top.my_alert('请选择岗位',"warn"); return; }
		if (g.getSelecteds().length>1) { top.my_alert('请选择唯一的岗位',"warn"); return; }
		var id = (g.getSelectedRow()).id;
		var stationname = (g.getSelectedRow()).stationname;
		var url = "system/station/stationHaveUserList.jsp?id="+id;
		winOpen1(url,stationname+'岗位下拥有用户列表',900,600,"关闭页面");
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
		var url = "system/station/stationAdd.jsp";
		winOpen(url,'添加岗位信息',490,300,'添加','取消',function(data){
			$.ajax({
				url:"stationAdd.action", 
				data:data, 
				dataType:"json", 
				async:false,
				type:"post",
				success:function (mm) {
					if("error"==mm.result){
	       				top.my_alert("添加岗位信息失败!");
	       			}else{
	       				g.addRow(mm);
		       			top.$.ligerDialog.success("添加岗位信息成功!","success");
	       			}
				}, 
				error:function (error) {
					top.my_alert("添加岗位信息失败！" + error.status);
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
		var url = "system/station/stationEdit.jsp?id="+id;
		winOpen(url,'修改岗位信息',490,300,'保存','取消',function(data){
			$.ajax({
				url:"stationUpdate.action", 
				data:data,
				async:false,  
				dataType:"json", 
				type:"post",
				success:function (mm) {
					if("error"==mm.result){
	       				top.my_alert("修改岗位信息失败!");
	       			}else{
	       				g.updateRow(selected,mm);
	       				top.my_alert("修改岗位信息成功！");
	       			}
				}, 
				error:function (error) {
					top.my_alert("修改岗位信息失败！" + error.status);
			}});
      });
   }
   
   
   /**
    删除信息的方法
   */
	function  itemdelete(){
		var selected = g.getSelected();
	    if (!selected) {  window.top.my_alert('请选择要删除的数据行!'); return; }
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
				if(QueryIsDelete("station",idstr)=="false"){
					top.my_alert("删除数据失败，已分配人员的岗位不能删除！","error");
				}else{
					g.deleteSelectedRow();
					/**
					删除数据库数据
					*/
					$.post("stationDel.action", { ids: idstr},
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