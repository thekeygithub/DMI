//清空
function resetinpt(){
	$("#bdate").val("");
	$("#edate").val("");
	$("#bname").val("");
	sel2();
}

//获取数据
function getTalbeDate(mtype,bname){
	var d;

	if(bname==""){
		bname=$("#bname").val();//名称	
	}
	var isAuth="0";
	if($("#ckauth").prop("checked")){
		isAuth="1";
	}
	var bdate=$("#bdate").val();//查询开始时间
	var edate=$("#edate").val();//查询结束时间
	$.ajax({
		url:"tian/findData.action",
		dataType:"json",
		type:"post",
		data:{cardno:getvalue("card_no"),bname:bname,bdate:bdate,edate:edate,ttype:mtype,isAuth:isAuth},
		async:false,
		success:function(data){
			d=data.list;
			if(mtype=="00"){
				$("#typelist2 button").each(function(){
					var dtype=$(this).attr("data-type");
					if(typeof(data[dtype])!="undefined"){
						$(this).hide();
					}
				});
			}
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
	    		}else if(i=="DZ"){
	    			if(""==data[i]||null==data[i]){
	    				$("#tsDZ").hide();
	    			}
	    		}else{
	    			$("#"+i).attr("title",data[i]).text(data[i]);
	    		}
	    		
	    	}
	    }
	});
	$.ajax({
		url:"isUserHaveRole.action",
		dataType:"json",
		type:"post",
	    async:false,
	    success:function(data){
	    	if(data.result=="success"){
	    		$("#isAuth").show();
	    	}
	    }
	});
	//初始化表格
	allGrid(card_no,xm);
	
	
	//更多个人社会保险信息
	$(".grsbxx").attr("href","insuranceInformation.html?card_no="+card_no+"&name="+$("#XM").text());
	$("#ckauth").click(function(){
	
		sel3();
	});
	
});


//360度视图
//住院
function allGrid(card_no,xm){
	$.jgrid.defaults.styleUI="Bootstrap";
	var allData=getTalbeDate("00","");
	//console.info(allData);
	var ww = $('div.mblock').width();
	var hh = $('div.mblock').height();
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
		    {name:"dtype",index:"dtype",width:280,formatter:function(cell,options,rowObject){
		    	if(rowObject.dtype=="门诊"||rowObject.dtype=="住院"||rowObject.dtype=="医保结算单"){
		    		if(cell=="****"){
		    			return "****";
		    		}
		    	}
		    	return "<a href=\"javascript:void(0)\" onclick=\"sel2('"+cell+"')\">"+cell+"</a>";
		    	
		    }},
		    {name:"dcname",index:"dcname",width:280},
		    {name:"corp",index:"corp",width:580,formatter:function(cell,options,rowObject){
		    	if(rowObject.dtype=="门诊"||rowObject.dtype=="住院"||rowObject.dtype=="购药"||rowObject.dtype=="医保结算单"){
		    		if(cell=="****"){
		    			return "****";
		    		}
		    		return "<a href=\"javascript:void(0)\" onclick=\"sel2('"+cell+"')\">"+cell+"</a>";
		    	}else{
		    		return cell;
		    	}
		    }},
		    {name:"unit",index:"unit",width:280,formatter:function(cell,options,rowObject){
		    	if(rowObject.dtype=="门诊"||rowObject.dtype=="住院"||rowObject.dtype=="医保结算单"){
		    		if(cell=="****"){
		    			return "****";
		    		}
		    	}
		    	return "<a href=\"javascript:void(0)\" onclick=\"sel2('"+cell+"')\">"+cell+"</a>";
		    	
		    }},
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
		    		if(rowObject.dtype=="门诊"||rowObject.dtype=="住院"||rowObject.dtype=="医保结算单"){
		    			return "<a href=\"javascript:void(0)\" onclick=\"sel2('"+cell+"')\">"+cell.substring(0,type)+"</a>   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
		    		}else if(rowObject.dtype=="购药"||rowObject.dtype=="APP使用行为"){
			    		var yps=cell.substring(0,type).split("|");
			    		var yp="";
			    		for(var i in yps){
			    			yp+="<a href=\"javascript:void(0)\" onclick=\"sel2('"+yps[i]+"')\">"+yps[i]+"</a>|";
			    		}
			    		yp=yp.substring(0,yp.length-1)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
			    	    return yp;
		    		}else{
		    			return cell.substring(0,type)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
		    		}
	    		}
		    	if(rowObject.dtype=="门诊"||rowObject.dtype=="住院"||rowObject.dtype=="医保结算单"){
		    		if(cell=="****"){
		    			return "****";
		    		}
	    			return "<a href=\"javascript:void(0)\" onclick=\"sel2('"+cell+"')\">"+cell+"</a>   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
		    	}else if(rowObject.dtype=="购药"||rowObject.dtype=="APP使用行为"){
		    		var yps=cell.split("|");
		    		var yp="";
		    		for(var i in yps){
		    			yp+="<a href=\"javascript:void(0)\" onclick=\"sel2('"+yps[i]+"')\">"+yps[i]+"</a>|";
		    		}
		    		yp=yp.substring(0,yp.length-1)+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
		    	    return yp;
		    	}else{
	    			return cell+"   <a href=\"javascript:void(0)\" onclick=\"openNewWin('"+url+"')\">查看详情</a>";
	    		}
	    		
			}}
		],
		pager:"#pager_list_360",viewrecords:true,hidegrid:false,
	});
	$("#table_list_360").setGridWidth(ww-10);

	$("#table_list_360").setGridHeight(hh-10);
}
$("#typelist").find("button.btn").click(function(){
    $(this).siblings().removeClass("btn-success").addClass("btn-white")
    $(this).removeClass("btn-white").addClass("btn-success");
    sel2();
})
function sel2(bname){
	var mtype=$("#typelist").find("button.btn-success:visible").attr("data-type");//类型
	var b=""
	if(typeof(bname)!="undefined"){
		b=bname
	}
	var data=getTalbeDate(mtype,b);
	$("#table_list_360").clearGridData();
	$("#table_list_360").setGridParam({
		datatype:"local",
		data:data
	}).trigger("reloadGrid");
}
function sel3(){
	var mtype=$("#typelist").find("button.btn-success:visible").attr("data-type");//类型
	var b=""
	var data=getTalbeDate(mtype,b);
	var page = $('#table_list_360').getGridParam('page'); // current page
    var rows = $('#table_list_360').getGridParam('rows'); // rows  
    var sidx = $('#table_list_360').getGridParam('sidx'); // sidx
    var sord = $('#table_list_360').getGridParam('sord'); // sord
	$("#table_list_360").clearGridData();
	$("#table_list_360").setGridParam({
		datatype:"local",
		data:data,
		page:page,
        rows:rows,
        sidx:sidx,
        sord:sord
	}).trigger("reloadGrid");
}
var newWindow="";
function openNewWin(url){
    newWindow=window.open(url+"&"+Math.random()+"=1","view_frame");
}