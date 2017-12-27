/*--------
	 打开附件添加页面
-----------------*/
function  openFileUploadPage(id,tablename){
   	var rmd = new Date().getTime();
    window.top.$.ligerDialog.open(
	{ title: '附件上传', name:'winselector'+rmd,width: 400, height: 200, url: 'bussiness/callcenter/util/FileUpload.jsp?itemid='+id+'&itemtype=checkonsite&rmd='+rmd, 
		buttons: 
			[{ text: '上传', onclick: 
				function(item,dialog){
					var fn = dialog.frame.ajaxFileUpload || dialog.frame.window.ajaxFileUpload;
					var data = fn(function(data){
						dialog.close();
						loadDatas(id,tablename);
					});
				} 
			},
			{ text: '取消', onclick: 
				function(item,dialog){
					dialog.close();
				}  
			}]
	});
}
/**
附件上传成功后回调函数
itemid id
itemtype 表名
*/
function loadDatas(itemid,itemtype){
	$.ajax({
		url:'attQueryByItemId.action',
		data:"model.itemid="+itemid+"&model.itemtype="+itemtype,
		type:"post",
		dataType:'json',
		success:function(msg){
			$("#filename").empty();
			var mypath = $("#mypath").val();
			$.each(msg,function(index,item){
				var url = "filedownload.action?filename="+item.filename+"&truefilename="+item.originalfilename;;
				var oclick="attDelete('"+item.id+"','"+itemid+"','"+itemtype+"');";
				var bder="0";
				var hurl="javascript:void(0)";
				var str="<a href="+url+">"+item.originalfilename+"</a>  <a href="+hurl+" onclick="+oclick+"  border="+bder+" ><img   border="+bder+" title=\"删除\" src=\""+mypath+"/images/delete.png\"/></a>;&nbsp;";
				$("#filename").append(str);
			});
		},
		error:function(error){
			alert(error.status);
		}
	});
}
	
/**
附件删除函数
*/
function attDelete(id,itemid,itemtype){
	$.ligerDialog.confirm("确定删除该附件?删除数据后不可恢复！", "提示", function (ok) {
		if (ok) {
			$.ajax({
				url:'attDelete.action',
				data:'ids='+id,
				dataType:'json',
				success:function(){
					loadDatas(itemid,itemtype);
				},
				error:function(error){
					window.top.$.ligerDialog.error(error.status);
				}
			});
		}
	});
}
/**
 * 下载附件
 * @param itemid 需要查找附件的id
 * @param itemtype 表名称
 * @param spanid 表格id
 * @return
 */
function loadFileList(itemid,itemtype,spanid){
	$.ajax({
		url:'attQueryByItemId.action',
		data:"model.itemid="+itemid+"&model.itemtype="+itemtype,
		type:"post",
		dataType:'json',
		success:function(msg){
			$("#"+spanid).empty();
			var mypath = "<%=path %>";
			$.each(msg,function(index,item){
				var url = "filedownload.action?filename="+item.filename+"&truefilename="+item.originalfilename;;
				var str="<a href="+url+">"+item.originalfilename+"</a>;&nbsp;";
				$("#"+spanid).append(str);
			});
		},
		error:function(error){
			alert(error.status);
		}
	});
}