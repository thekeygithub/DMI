//清空
function resetinpt(){
	$("#bdate").val("");
	$("#edate").val("");
	$("#bname").val("");
	sel();
}
//查询
function sel(){
	//门诊
	var mzData=getTalbeDate("11");
	$("#table_list_mz").clearGridData();
	$("#table_list_mz").setGridParam({
		datatype:"local",
		data:mzData
	}).trigger("reloadGrid");
	//住院
	var zyData=getTalbeDate("12");
	$("#table_list_zy").clearGridData();
	$("#table_list_zy").setGridParam({
		datatype:"local",
		data:zyData
	}).trigger("reloadGrid");
	
	//结算单
	var jsdData=getTalbeDate("03");
	$("#table_list_jsd").clearGridData();
	$("#table_list_jsd").setGridParam({
		datatype:"local",
		data:jsdData
	}).trigger("reloadGrid");
	
	//app
	var appData=getTalbeDate("05");
	$("#table_list_app").clearGridData();
	$("#table_list_app").setGridParam({
		datatype:"local",
		data:appData
	}).trigger("reloadGrid");
	
	//购药
	var gyData=getTalbeDate("13");
	$("#table_list_gy").clearGridData();
	$("#table_list_gy").setGridParam({
		datatype:"local",
		data:gyData
	}).trigger("reloadGrid");
	
	//血压
	var xyData=getTalbeDate("15");
	$("#table_list_xy").clearGridData();
	$("#table_list_xy").setGridParam({
		datatype:"local",
		data:xyData
	}).trigger("reloadGrid");
	
	//血糖
	var xtData=getTalbeDate("16");
	$("#table_list_xt").clearGridData();
	$("#table_list_xt").setGridParam({
		datatype:"local",
		data:xtData
	}).trigger("reloadGrid");
	
	//健康档案
	var jkdaData=getTalbeDate("17");
	$("#table_list_jkda").clearGridData();
	$("#table_list_jkda").setGridParam({
		datatype:"local",
		data:jkdaData
	}).trigger("reloadGrid");
	
	//健康体检
	var jktjData=getTalbeDate("18");
	$("#table_list_jktj").clearGridData();
	$("#table_list_jktj").setGridParam({
		datatype:"local",
		data:jktjData
	}).trigger("reloadGrid");
	
	//慢病随访
	var mbsfData=getTalbeDate("19");
	$("#table_list_mbsf").clearGridData();
	$("#table_list_mbsf").setGridParam({
		datatype:"local",
		data:mbsfData
	}).trigger("reloadGrid");
	
}
//获取数据
function getTalbeDate(mtype){
	var d;
	var bname=$("#bname").val();//名称
	var bdate=$("#bdate").val();//查询开始时间
	var edate=$("#edate").val();//查询结束时间
	$.ajax({
		url:"tian/findData.action",
		dataType:"json",
		type:"post",
		data:{cardno:getvalue("card_no"),bname:bname,bdate:bdate,edate:edate,ttype:mtype},
		async:false,
		success:function(data){
			d=data;
		}
	})
	
	return d;
}

$(document).ready(function(){
	var start={elem:"#bdate",format:"YYYY/MM/DD hh:mm:ss",istime:true,istoday:false,choose:function(datas){end.min=datas;end.start=datas}};
	var end={elem:"#edate",format:"YYYY/MM/DD hh:mm:ss",istime:true,istoday:false,choose:function(datas){start.max=datas}};
	laydate(start);
	laydate(end);
	var card_no=getvalue("card_no");
	var nl="";
	var xb="";
	var xm="";
	$.ajax({
		url:"tian/findPersonInformation.action",
		dataType:"json",
		type:"post",
		data:{cardno:card_no},
	    async:false,
	    success:function(data){
	    	nl=data["NL"];
	    	xm=data["XM"];
	    	xb=data["XB"];
	    	for(var i in data){
	    		if(i=="SR"){
	    			$("#"+i).attr("title",data[i].substring(0,10)).text(data[i].substring(0,10));
	    		}else{
	    			$("#"+i).attr("title",data[i]).text(data[i]);
	    		}
	    		
	    	}
	    }
	});
	//初始化表格
	allGrid(card_no,xm);
	mzGrid(card_no);
	zyGrid(card_no);
	jsdGrid(card_no);
	appGrid(card_no,xm);
	gyGrid(card_no,xm);
	xyGrid(card_no,xm);
	xtGrid(card_no,xm);
	mbsfGrid(card_no);
	jkdaGrid(card_no);
	jktjGrid(card_no);
	
	//更多个人社会保险信息
	$(".grsbxx").attr("href","insuranceInformation.html?card_no="+card_no+"&name="+$("#XM").text());
	//展开和还原
	$('span.fullscre').toggle(
			function(){
				$(this).html("还原 ");
				$(this).parents('div.mblock').animate({'width':'100%','height':'99.5%',});
				$(this).parents('div.mblock').prevAll().css('display','none');
				$(this).parents('div.mblock').nextAll().css('display','none');
				
				var spanTitle = $(this).parents('h1').find('span').first().text();
				
				var width = $(this).parents('div.mblock').width();
				var height = $(this).parents('div.mblock').height();
				if(spanTitle == "门诊"){
					$("#table_list_mz").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_mz").setGridParam().showCol("dcname").trigger("reloadGrid");//显示列
					$("#table_list_mz").setGridParam().showCol("unit").trigger("reloadGrid");//显示列
					$("#table_list_mz").setGridWidth(width*2);
					$("#table_list_mz").setGridHeight(height*2-100);
				}else if(spanTitle == "住院"){
					$("#table_list_zy").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_zy").setGridParam().showCol("dcname").trigger("reloadGrid");//显示列
					$("#table_list_zy").setGridParam().showCol("unit").trigger("reloadGrid");//显示列
					$("#table_list_zy").setGridWidth(width*2);
					$("#table_list_zy").setGridHeight(height*2-100);
				}else if(spanTitle == "医保结算单"){
					$("#table_list_jsd").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_jsd").setGridParam().showCol("dcname").trigger("reloadGrid");//显示列
					$("#table_list_jsd").setGridWidth(width*2);
				}else if(spanTitle == "APP使用行为"){
					$("#table_list_app").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_app").setGridWidth(width*2);
				}else if(spanTitle == "购药"){
					$("#table_list_gy").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_gy").setGridWidth(width*2);
				}else if(spanTitle == "血压可穿戴"){
					$("#table_list_xy").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_xy").setGridParam().showCol("dcname").trigger("reloadGrid");//显示列
					$("#table_list_xy").setGridParam().showCol("unit").trigger("reloadGrid");//显示列
					$("#table_list_xy").setGridWidth(width*2);
				}else if(spanTitle == "血糖可穿戴"){
					$("#table_list_xt").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_xt").setGridParam().showCol("dcname").trigger("reloadGrid");//显示列
					$("#table_list_xt").setGridParam().showCol("unit").trigger("reloadGrid");//显示列
					$("#table_list_xt").setGridWidth(width*2);
				}else if(spanTitle == "健康档案建档"){
					$("#table_list_jkda").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_jkda").setGridParam().showCol("dcname").trigger("reloadGrid");//显示列
					$("#table_list_jkda").setGridParam().showCol("unit").trigger("reloadGrid");//显示列
					$("#table_list_jkda").setGridWidth(width*2);
					$("#table_list_jkda").setGridHeight(height*2-100);
				}else if(spanTitle == "慢病随访"){
					$("#table_list_mbsf").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_mbsf").setGridParam().showCol("dcname").trigger("reloadGrid");//显示列
					$("#table_list_mbsf").setGridParam().showCol("unit").trigger("reloadGrid");//显示列
					$("#table_list_mbsf").setGridWidth(width*2);
				}else if(spanTitle == "健康体检"){
					$("#table_list_jktj").setGridParam().showCol("dtype").trigger("reloadGrid");//显示列
					$("#table_list_jktj").setGridParam().showCol("dcname").trigger("reloadGrid");//显示列
					$("#table_list_jktj").setGridParam().showCol("unit").trigger("reloadGrid");//显示列
					$("#table_list_jktj").setGridWidth(width*2);
					$("#table_list_jktj").setGridHeight(height*2-100);
				}				
			},
			function(){
				$(this).html("展开");
				var  a=$(this).parents('div.content').children('div.mblock').length;
				if(a==2){
					$(this).parents('div.mblockleft').animate({'width':'49%','height':'97.5%',}); 
				}else{
					$(this).parents('div.mblockleft').animate({'width':'49%','height':'48%',});
				}
				$(this).parents('div.mblockright').animate({'width':'49%','height':'97.5%',});
				$(this).parents('div.mblock').prevAll().css('display','block');
				$(this).parents('div.mblock').nextAll().css('display','block');

				var spanTitle = $(this).parents('h1').find('span').first().text();
				var width = $(this).parents('div.mblock').width();
				var height = $(this).parents('div.mblock').height();
				if(spanTitle == "门诊"){
					$("#table_list_mz").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_mz").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
					$("#table_list_mz").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
					$("#table_list_mz").setGridWidth(width/2-20);
					$("#table_list_mz").setGridHeight(height/2-130);
				}else if(spanTitle == "住院"){
					$("#table_list_zy").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_zy").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
					$("#table_list_zy").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
					$("#table_list_zy").setGridWidth(width/2-20);
					$("#table_list_zy").setGridHeight(height/2-130);
				}else if(spanTitle == "医保结算单"){
					$("#table_list_jsd").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_jsd").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
					$("#table_list_jsd").setGridWidth(width/2-20);
				}else if(spanTitle == "APP使用行为"){
					$("#table_list_app").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_app").setGridWidth(width/2-20);
				}else if(spanTitle == "购药"){
					$("#table_list_gy").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_gy").setGridWidth(width/2-20);
				}else if(spanTitle == "血压可穿戴"){
					$("#table_list_xy").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_xy").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
					$("#table_list_xy").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
					$("#table_list_xy").setGridWidth(width/2-20);
				}else if(spanTitle == "血糖可穿戴"){
					$("#table_list_xt").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_xt").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
					$("#table_list_xt").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
					$("#table_list_xt").setGridWidth(width/2-20);
				}else if(spanTitle == "健康档案建档"){
					$("#table_list_jkda").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_jkda").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
					$("#table_list_jkda").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
					$("#table_list_jkda").setGridWidth(width/2-20);
					$("#table_list_jkda").setGridHeight(height/2-130);
				}else if(spanTitle == "慢病随访"){
					$("#table_list_mbsf").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_mbsf").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
					$("#table_list_mbsf").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
					$("#table_list_mbsf").setGridWidth(width/2-20);
				}else if(spanTitle == "健康体检"){
					$("#table_list_jktj").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
					$("#table_list_jktj").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
					$("#table_list_jktj").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
					$("#table_list_jktj").setGridWidth(width/2-20);
					$("#table_list_jktj").setGridHeight(height/2-130);
				}
			}
		);
});

/***********************************一下是初始化表格*****************************************/
//门诊
function mzGrid(card_no){
	$.jgrid.defaults.styleUI="Bootstrap";
	var mzdata=getTalbeDate("11");
	var ww = $("#table_list_mz").parents('div.mblock').width();
	var hh = $("#table_list_mz").parents('div.mblock').height()-120;
	$("#table_list_mz").jqGrid({
		data:mzdata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","科室","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
		    {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"unit",index:"unit",width:280},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var rdate=rowObject.rdate;
		    	var dataId = $(".btn-warning").attr("value");
		    	var url="medicalInformation.html?h_no="+rowObject.id+"&hos_id="+rowObject.corp_id+"&hos_id="+rowObject.corp_id+"&cardno="+card_no+"&zd="+cell+"&flag="+dataId;
	    		if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
			}}
		],
		pager:"#pager_list_mz",viewrecords:true,hidegrid:false,
	});
	$("#table_list_mz").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_mz").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_mz").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
	$("#table_list_mz").setGridWidth(ww-10);
	$("#table_list_mz").setGridHeight(hh-25);
}
//住院
function zyGrid(card_no){
	$.jgrid.defaults.styleUI="Bootstrap";
	var zydata=getTalbeDate("12");
	var ww = $("#table_list_zy").parents('div.mblock').width();
	var hh = $("#table_list_zy").parents('div.mblock').height()-120;
	$("#table_list_zy").jqGrid({
		data:zydata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","科室","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"unit",index:"unit",width:280},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var rdate=rowObject.rdate;
		    	var dataId = $(".btn-warning").attr("value");
		    	var url="hospitalInfomation.html?h_no="+rowObject.id+"&hos_id="+rowObject.corp_id+"&cardno="+card_no+"&zd="+cell+"&flag="+dataId;
		    	if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
			}}
		],
		pager:"#pager_list_zy",viewrecords:true,hidegrid:false,
	});
	$("#table_list_zy").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_zy").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_zy").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
	$("#table_list_zy").setGridWidth(ww-10);
	$("#table_list_zy").setGridHeight(hh-25);
}
//结算单
function jsdGrid(card_no){
	$.jgrid.defaults.styleUI="Bootstrap";
	var jsddata=getTalbeDate("03");
	var ww = $("#table_list_jsd").parents('div.mblock').width();
	var hh = $("#table_list_jsd").parents('div.mblock').height()-120;
	$("#table_list_jsd").jqGrid({
		data:jsddata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var rdate=rowObject.rdate;
		    	var dataId = $(".btn-warning").attr("value");
		    	if(rowObject.corp_id!=null&&rowObject.corp_id!=""){
		    		var url="../sheets/settlements.html?id="+rowObject.id+"&cardno="+card_no+"&zd="+cell+"&hos_id="+rowObject.corp_id;
	    			if(cell.length>type){
		    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
		    		}
		    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}else{
	    			return "来自其他医院结算单";
	    		}
			}}
		],
		pager:"#pager_list_jsd",viewrecords:true,hidegrid:false,
	});
	$("#table_list_jsd").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_jsd").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_jsd").setGridWidth(ww-10);
	$("#table_list_jsd").setGridHeight(hh-25);
}


//APP使用行为
function appGrid(card_no,xm){
	$.jgrid.defaults.styleUI="Bootstrap";
	var appdata=getTalbeDate("05");
	//console.info(appdata);
	var ww = $("#table_list_app").parents('div.mblock').width();
	var hh = $("#table_list_app").parents('div.mblock').height()-120;
	$("#table_list_app").jqGrid({
		data:appdata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var rdate=rowObject.rdate;
		    	var url="../sheets/appuse.html?id="+rowObject.id+"&xm="+xm;
		    	if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
			}}
		],
		pager:"#pager_list_app",viewrecords:true,hidegrid:false,
	});
	$("#table_list_app").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_app").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_app").setGridWidth(ww-10);
	$("#table_list_app").setGridHeight(hh-25);
}

//购药
function gyGrid(card_no,xm){
	$.jgrid.defaults.styleUI="Bootstrap";
	var gydata=getTalbeDate("13");
	var ww = $("#table_list_gy").parents('div.mblock').width();
	var hh = $("#table_list_gy").parents('div.mblock').height()-120;
	$("#table_list_gy").jqGrid({
		data:gydata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var rdate=rowObject.rdate;
		    	var url="../sheets/gouyao.html?id="+rowObject.id+"&xm="+xm;
		    	if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
			}}
		],
		pager:"#pager_list_gy",viewrecords:true,hidegrid:false,
	});
	$("#table_list_gy").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_gy").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_gy").setGridWidth(ww-10);
	$("#table_list_gy").setGridHeight(hh-25);
}

//血压可穿戴
function xyGrid(card_no,xm){
	$.jgrid.defaults.styleUI="Bootstrap";
	var zydata=getTalbeDate("15");
	var ww = $("#table_list_xy").parents('div.mblock').width();
	var hh = $("#table_list_xy").parents('div.mblock').height()-120;
	$("#table_list_xy").jqGrid({
		data:zydata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","科室","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"unit",index:"unit",width:280},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var url="../sheets/celiangxueya.html?id="+rowObject.id+"&xm="+xm;
		    	if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    	}}
		],
		pager:"#pager_list_xy",viewrecords:true,hidegrid:false,
	});
	$("#table_list_xy").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_xy").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_xy").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
	$("#table_list_xy").setGridWidth(ww-10);
	$("#table_list_xy").setGridHeight(hh-25);
}

//血糖可穿戴
function xtGrid(card_no,xm){
	$.jgrid.defaults.styleUI="Bootstrap";
	var zydata=getTalbeDate("16");
	var ww = $("#table_list_xt").parents('div.mblock').width();
	var hh = $("#table_list_xt").parents('div.mblock').height()-120;
	$("#table_list_xt").jqGrid({
		data:zydata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","科室","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"unit",index:"unit",width:280},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var url="../sheets/celiangxuetang.html?id="+rowObject.id+"&xm="+xm;
		    	if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    	}}
		],
		pager:"#pager_list_xt",viewrecords:true,hidegrid:false,
	});
	$("#table_list_xt").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_xt").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_xt").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
	$("#table_list_xt").setGridWidth(ww-10);
	$("#table_list_xt").setGridHeight(hh-25);
}

//健康档案建档
function jkdaGrid(card_no){
	$.jgrid.defaults.styleUI="Bootstrap";
	var zydata=getTalbeDate("17");
	var ww = $("#table_list_jkda").parents('div.mblock').width();
	var hh = $("#table_list_jkda").parents('div.mblock').height()-120;
	$("#table_list_jkda").jqGrid({
		data:zydata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","科室","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"unit",index:"unit",width:280},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var url="../sheets/healthcover.html?id="+rowObject.id;
		    	if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    	}}
		],
		pager:"#pager_list_jkda",viewrecords:true,hidegrid:false,
	});
	$("#table_list_jkda").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_jkda").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_jkda").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
	$("#table_list_jkda").setGridWidth(ww-10);
	$("#table_list_jkda").setGridHeight(hh-25);
}

//慢病随访
function mbsfGrid(card_no){
	$.jgrid.defaults.styleUI="Bootstrap";
	var zydata=getTalbeDate("19");
	var ww = $("#table_list_mbsf").parents('div.mblock').width();
	var hh = $("#table_list_mbsf").parents('div.mblock').height()-120;
	$("#table_list_mbsf").jqGrid({
		data:zydata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","科室","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"unit",index:"unit",width:280},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var htmlpage="../sheets/tnbsfjl.html?id="+rowObject.id;
	    		if(cell=="高血压"){
	    			htmlpage="../sheets/gxysfjl.html?id="+rowObject.id;
	    		}
	    		if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+htmlpage+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+htmlpage+"')\">查看详情</a>";
	    	}}
		],
		pager:"#pager_list_mbsf",viewrecords:true,hidegrid:false,
	});
	$("#table_list_mbsf").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_mbsf").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_mbsf").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
	$("#table_list_mbsf").setGridWidth(ww-10);
	$("#table_list_mbsf").setGridHeight(hh-25);
}

//健康体检
function jktjGrid(card_no){
	$.jgrid.defaults.styleUI="Bootstrap";
	var zydata=getTalbeDate("18");
	var ww = $("#table_list_jktj").parents('div.mblock').width();
	var hh = $("#table_list_jktj").parents('div.mblock').height()-120;
	$("#table_list_jktj").jqGrid({
		data:zydata,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [5,10,15], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","科室","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"unit",index:"unit",width:280},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var url="../sheets/jktijian.html?cardno="+rowObject.id;
		    	if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    	}}
		],
		pager:"#pager_list_jktj",viewrecords:true,hidegrid:false,
	});
	$("#table_list_jktj").setGridParam().hideCol("dtype").trigger("reloadGrid");//隐藏列
	$("#table_list_jktj").setGridParam().hideCol("dcname").trigger("reloadGrid");//隐藏列
	$("#table_list_jktj").setGridParam().hideCol("unit").trigger("reloadGrid");//隐藏列
	$("#table_list_jktj").setGridWidth(ww-10);
	$("#table_list_jktj").setGridHeight(hh-25);
}
//360度视图
//住院
function allGrid(card_no,xm){
	$.jgrid.defaults.styleUI="Bootstrap";
	var allData=getTalbeDate("00");
	//console.info(allData);
	var ww = $("#table_list_360").parents('div.mblock').width();
	var hh = $("#table_list_360").parents('div.mblock').height();
	$("#table_list_360").jqGrid({
		data:allData,
		datatype:"local",
		mtype: 'POST',
		autowidth:true,
		shrinkToFit:true,
		loadonce:true,
		rownumbers:true,
		sortable:true,
		sortname:"rdate",
		sortorder:"desc",
        rowNum: 10,
        rowList: [10,20,30], 

		colNames:["","","日期","数据源类型","就诊类型","医生","位置","科室","描述"],
		colModel:[
            {name:"id",index:"id",hidden:true, editable:false},
            {name:"corp_id",index:"corp_id",hidden:true, editable:false},
            {name:"rdate",index:"rdate",width:580,formatter:function(c,b,rowObject){
		    	if(rowObject.dtype=="慢病随访"||rowObject.dtype=="医保结算单"||rowObject.dtype=="健康体检"||rowObject.dtype=="健康档案建档"){
		    		return c.substring(0,10);
		    	}else{
		    		return c.substring(0,19);
		    	}
		    }},
		    {name:"mtype",index:"mtype",hidden:true},
		    {name:"dtype",index:"dtype",width:280},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580},
		    {name:"unit",index:"unit",width:280},
		    {name:"detail",index:"detail",width:680,formatter:function(cell,options,rowObject){
		    	var type = 10;
		    	var rdate=rowObject.rdate;
		    	var dataId = $(".btn-warning").attr("value");
		    	var url="";
		    	if(rowObject.dtype=="住院"){
		    		url="hospitalInfomation.html?h_no="+rowObject.id+"&hos_id="+rowObject.corp_id+"&cardno="+card_no+"&zd="+cell+"&flag="+dataId;
		    		
		    	}else if(rowObject.dtype=="门诊"){
                    url="medicalInformation.html?h_no="+rowObject.id+"&hos_id="+rowObject.corp_id+"&hos_id="+rowObject.corp_id+"&cardno="+card_no+"&zd="+cell+"&flag="+dataId;		    		
		   
		    	}else if(rowObject.dtype=="购药"){
		    		url="../sheets/gouyao.html?id="+rowObject.id+"&xm="+xm;
		   
		    	}else if(rowObject.dtype=="APP使用行为"){
		    		url="../sheets/appuse.html?id="+rowObject.id+"&xm="+xm;
		    	}else if(rowObject.dtype=="血压(可穿戴)"){
		    		url="../sheets/celiangxueya.html?id="+rowObject.id+"&xm="+xm;
		    	
		    	}else if(rowObject.dtype=="血糖(可穿戴)"){
		    		url="../sheets/celiangxuetang.html?id="+rowObject.id+"&xm="+xm;
		    		
		    	}else if(rowObject.dtype=="健康档案建档"){
		    		url="../sheets/healthcover.html?id="+rowObject.id;
		    		
		    	}else if(rowObject.dtype=="健康体检"){
		    		url="../sheets/jktijian.html?cardno="+rowObject.id;
		    	
		    	}else if(rowObject.dtype=="慢病随访"){
		    		url="../sheets/tnbsfjl.html?id="+rowObject.id;
		    		if(cell=="高血压"){
		    			url="../sheets/gxysfjl.html?id="+rowObject.id;
		    		}

		    	}else{
		    		if(rowObject.corp_id!=null&&rowObject.corp_id!=""){
		    		    url="../sheets/settlements.html?id="+rowObject.id+"&cardno="+card_no+"&zd="+cell+"&hos_id="+rowObject.corp_id;
		    		}else{
		    			
		    			return "来自其他医院结算单";
		    			
		    		}
		    	}
		    	if(cell.length>type){
	    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
			}}
		],
		pager:"#pager_list_360",viewrecords:true,hidegrid:false,
	});
	$("#table_list_360").setGridWidth(ww-10);
	$("#table_list_360").setGridHeight(hh-50);
}


var newWindow="";
function openNewWin(url){
    newWindow=window.open(url+"&"+Math.random()+"=1","view_frame");
}