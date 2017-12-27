   
/*
通过菜单id
获取btn
*/  
function getBtnReource(){
       var pageid = (window.top.getSelectedTabId()).substring(5);
       var btnItem1  = {};
       var tempItem = [];
       $.ajax({
		url:"btnQueryByUser.action", 
		data:{"pagename":pageid}, 
		async:false,
		dataType:"json", 
		type:"post",
		success:function (data) {
			$.each(data,function(index,mm){
				tempItem.push({text:mm.resourcename,click:eval(mm.btnevent),icon:mm.resourceid});
				if(index < data.length-1){
					tempItem.push({line:true});
				}
			});
		}
		});
	   btnItem1 = {items:tempItem};
       return btnItem1;
}


   
/*
通过页面URL
获取btn
*/  
function getBtnReourceByUrl(){
	   var pagePath = window.document.location.pathname;
       var btnItem1  = {};
       var tempItem = [];
       $.ajax({
		url:"btnQueryByUserPageUrl.action", 
		data:{"pagename":pagePath}, 
		async:false,
		dataType:"json", 
		type:"post",
		success:function (data) {
			$.each(data,function(index,mm){
				tempItem.push({text:mm.resourcename,click:eval(mm.btnevent),icon:mm.resourceid});
				if(index < data.length-1){
					tempItem.push({line:true});
				}
			});
		}
		});
	   btnItem1 = {items:tempItem};
       return btnItem1;
}