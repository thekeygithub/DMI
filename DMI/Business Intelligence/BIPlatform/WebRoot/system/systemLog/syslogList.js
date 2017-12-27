jQuery(function($){
    my_initGrid();
    $("#datasearch").click(function(){
    	itemcx();
    });
    $("#dataclear").click(function(){
    	itemclear();
    });
});
/**
初始化表格
CustomersData：要填充的数据
*/
function my_initGrid(){
   window['g']=$("#maingrid").ligerGrid({
           checkbox: true,
           url:"sysLogQuery.action",
           columns: [
           { display: '操作用户', name: 'username',width: "20%",isSort:false},
           { display: '时间', name: 'logdate',width: "20%",isSort:false,
           		render:function(item){
           		var temp = getdate(item.logdate);
           		return temp;
           		}
           
           },
           { display: 'ip地址', name: 'ipaddress',width: "20%",isSort:false},
           { display: '执行操作', name: 'action',width: "38%",isSort:false}
           ],
           pageSize:10,
           root:"listmodel",
           record:"record",
           height: '100%',
           heightDiff:-7,
           frozenCheckbox:false,
           title:"日志列表",
           detail:{
               height:'auto',
               onShowDetail: function (r,p){
				for (var n in r) {
			      if (r[n] == null) r[n] = "";
			 	}
			  $(p).append(
			      "<table class='table_infos'  style='width:90%;margin:5px'>" +
			          "<tr>" +
			              "<td height='25px' width='10%' class='td_tit_Report1'>操作用户:</td><td height='25px'  width='15%'>&nbsp;&nbsp;" + r.username + "</td>" +
			              "<td height='25px' width='10%' class='td_tit_Report1'>操作时间:</td><td height='25px'  width='25%'>&nbsp;&nbsp;" + getdate(r.logdate) + "</td>" +
			              "<td height='25px' width='10%' class='td_tit_Report1'>执行操作:</td><td height='25px'  width='20%'>&nbsp;&nbsp;" + r.action + "</td>" +
			          "</tr>" +
			          "<tr>" +
			              "<td height='25px' width='10%' class='td_tit_Report1'>操作内容:</td><td height='25px'  colspan='5'>&nbsp;&nbsp;" + r.message + "</td>" +
			          "</tr>"+
			      "</table>"
			      );
				}
		   }
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
			     /**
			        删除数据库数据
			     */
			   $.post("sysLogDel.action", { ids: idstr},
			    function(data){
			     	g.deleteSelectedRow();
			     	top.$.ligerDialog.success("删除数据成功!");
			    },"json");
			  }
		 });
	}
   
   
   /**
   查询的方法
   */
   function  itemcx(){
	    var starttime = $("#starttime").val();
	    var endtime = $("#endtime").val();
	    if(endtime < starttime){
	    	top.my_alert("结束时间不能小于开始时间!","warn");
	    	return ;
	     }
	    var username = $("#username").val();
		g.setOptions({newPage:1});
		g.setOptions({
		       parms: [
		       		{ name: "starttime", value: starttime },
		       		{ name: "endtime", value: endtime },
		       		{ name: 'username', value: username }
		      		]
					});
       g.loadData(true);
   }
   
   /**
     清空的方法
   */
   function  itemclear(){
	   $("#starttime").val("");
	   $("#endtime").val("");
	   $("#username").val("");
   }
 