/*打开首页*/
function openIndex(){
	window.location.href= "/MDEPlatform/pages_new/index.html";
}
/*确认框*/
function alertShow(con,isState){
	if(isState=="confirm"){
		$(".ok").css("width","50%").attr("onclick","userSignOut()");
		$(".cancel").css("display","block");
		$(".alertHead strong").text(con);
		$(".blackBox").fadeIn();
	}else {
		$(".ok").css("width","100%").removeAttr("onclick");
		$(".cancel").css("display","none");
		$(".alertHead strong").text(con);
		$(".blackBox").fadeIn();
	}
	
}
/*确认框*/
function alertShowNew(con,isState){
	if(isState=="confirm"){
		$(".ok").css("width","50%").attr("onclick","userSignOut()");
		$(".cancel").css("display","block");
		$(".alertHead strong").text(con);
		$(".blackBox").fadeIn();
	}else {
		$(".ok").css("width","100%").removeAttr("onclick");
		$(".cancel").css("display","none");
		$(".alertHead strong").html(con);
		$(".blackBox").fadeIn();
	}
	
}
function closeAlert(){
	$(".blackBox").fadeOut();
}
//function navIsShow(){
//	$(".sub-nav").stop(true,true).show();
//	//$(".whiteBg").show();
//	setTimeout(initSubNav,400);
//	
//}
//function initSubNav(){
//	$(".sub-nav ol").css("display","none");
//	$(".sub-nav>li>a").css({
//		"color":"#ffffff"
//	});
//	$(".sub-nav>li>a").parent().css("background","#4899eb");
//	$(".sub-nav>li>a").children("i").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
//}
function openSubNav(){
	
	$(this).next("ol").stop(true,true).slideToggle();
	if($(this).children("i").hasClass("fa-plus-square-o")){
		$(this).children("i").removeClass("fa-plus-square-o").addClass("fa-minus-square-o");
		$(this).parent().css("background","#5F6A72");
		$(this).css("color","#fff");
	}else {
		$(this).children("i").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
		$(this).parent().css("background","#5F6A72");
		$(this).css("color","#fff");
	}
	
	
	$(this).parent().siblings().css("background","#5F6A72");
	$(this).parent().siblings().children().css("color","#fff");
	$(this).parent().siblings().children().next("ol").stop(true,true).slideUp();
	$(this).parent().siblings().children().children("i").removeClass("fa-minus-square-o").addClass("fa-plus-square-o");
}
/*注销*/
function userSignOut(){
	document.location.replace("/MDEPlatform/login.html");
	$.ajax({
		url:"/MDEPlatform/userSignOut.action",
		dataType:"json",
		type:"post",
	    async:false,
	    success:function(data){
	    	//console.info(data);
	    }
	});
}
//html取值
function getvalue(name){
    var str=window.location.search;  
    		
    if (str.indexOf("&")==-1){        
        var pos_start=str.indexOf(name)+name.length+1;
        return str.substring(pos_start);
        
    }else{
    	var ss=str.split("&");
    	for(var i in ss){
    		if(ss[i].indexOf(name)!=-1){
    			 var start=ss[i].indexOf(name)+name.length+1;
    			 return ss[i].substring(start);
    		}
    	}
    }
}
//文件上传
function changeVal(self){
	if($(self).val()==""){
		$(".modal-body span").css("color","#333").text("点击添加上传文件");
	}else{
		var filename=$(self).val();
		var realnames=filename.split("\\");
		var realname=realnames[realnames.length-1];
		$(".modal-body span").css("color","#333").text("文件名称:"+realname);
		var id=$(self).attr("id")+"input";
		$("#"+id).val(realname);
	}
	
}
var filePath2= "";
function uploadFile() {
	var ipd=$("#tjwj").attr("data-ot");
	if($(ipd).val()==""){
		$(".modal-body span").css("color","#FF0000").text("请上传文件再提交");
	}else{		
		var id = ipd+"input";
		$(id).show();
		//filePath2=filePath2+","+$(ipd).val();
		$("#subdownload").prop("disabled",true);
		$("#myModal .sk-spinner").css("display","block");
		setTimeout(function(){
			$("#myModal .sk-spinner").hide();
			$(ipd).val("");
			$(".fa-check-circle-o").show(100);
			delay();
		},200);
		
	}
	
}
function delay(){
	setTimeout(function(){
		$('#myModal').modal('hide');
		$(".modal-body span").css("color","#333").text("点击添加上传文件");
		$(".fa-check-circle-o").hide();
	},500)
}
function titleName(t,ipt){
	$("#scwjflag").val(ipt);
	$(".modal-title").text($(t).text());
	$(".table").hide();
	$(".table.ui-jqgrid-htable").show();
	$(".table.ui-jqgrid-btable").show();
	$(".jqGrid_wrapper").hide();
	$("input[type='file']").hide();
//	if($(ipt).val()==""){
//		$(".modal-body span").css("color","#333").text("点击添加上传文件");
//	}else{
//		if($(".modal-body span").text()=="点击添加其他上传文件"){
//			$(".modal-body span").css("color","#333").text("点击添加其他上传文件");
//		}else {
//			$(".modal-body span").css("color","#333").text("文件路径:"+$(ipt).val());
//		}		
//	}
	$(ipt).show();
	$(".modal-footer .btn-primary").attr("value",ipt);
	$('.textCheck input').iCheck('uncheck');
	$("textarea").val("").attr("disabled","disabled")
	$("#tjwj").attr("data-ot",ipt);
}
//toast
function toastSelf(con,pos){
	$(pos).stop(true,true).fadeIn(200).text(con);
	setTimeout(function(){
		$(pos).stop(true,true).fadeOut(200);
	},2000);
}
//toggle 
function toggleContent(t){	
	var _display = $(t).parent().next().css("display");
	if(_display=="none"){
		$(t).text("收起");
		$(t).parent().next().stop(true,true).slideDown();
	}else {
		$(t).text("展开");
		$(t).parent().next().stop(true,true).slideUp();
	}
}
$(document).ready(function() {
	$('.tap3').on('click', function (){
		if($(this).html()=="收起"){
			$(this).html("展开");
			$(this).parents("div.mumbercont").height(32);
		}else{
			$(this).html("收起");
			$(this).parents("div.mumbercont").height($(window).height()-20); 
		}
		$(this).parent().next(".content").slideToggle('fast');
		 
	});
});