<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String id=request.getParameter("id");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>角色所属用户页面</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" />
    <script type="text/javascript" src="<%=basePath %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script type="text/javascript" src="<%=basePath %>include/LigerUI/js/ligerui.all.js" ></script>
    <script type="text/javascript">
        $(function (){
        	my_initGrid();
        });
    	
		function my_initGrid(){
			window['g']=$("#maingrid").ligerGrid({
				checkbox: true,
				url:"roleHaveUserList.action?id=<%=id%>",
				columns: [
					{ display: '登陆名', name: 'loginname', minWidth:10 ,width:"12%",isSort:true},
					{ display: '用户姓名', name: 'username', minWidth: 10 ,width:"13%",isSort:true},
					{ display: '性别', name: 'sex', minWidth: 10 ,width:"8%",
						render: function (item){
					    	if (item.sex == 'M') return '男';
					        return '女';
			        	}
					},
					{ display: '电话', name: 'mobileprivate', minWidth: 20 ,width:"12%",isSort:true},
					{ display: '邮箱', name: 'emailprivate', minWidth: 20 ,width:"25%",isSort:true},
					{ display: '备注', name: 'remark', minWidth: 30,width:"20%" }
				],
				pageSize:10,
				root:"listmodel",
				record:"record",
				rownumbers:true,
				usePager:false,
				height:'98%',
				selectRowButtonOnly:true,
				title:"用户列表",
				//onSuccess:function(data){
				//	alert(liger.toJSON(data));
				//},
				toolbar: { 
					items: [
						{ text: '撤销关联', click: itemclick, icon: 'communication' }
					]
				}
			});
		}     

		function  itemclick(){
			var selected = g.getSelected();
			if (!selected) {  top.my_alert('请选择要需要撤销的用户！'); return; }
			if(confirm("确定撤销关联选择的数据？")){
				g.deleteSelectedRow();
				var selecteds = g.getSelecteds();
				var idstr="";//所有选择行的id
				for(var i=0;i<selecteds.length;i++){
					idstr = idstr + selecteds[i].id;
					if(i!=(selecteds.length-1)){
						idstr = idstr + ",";
					}
				}
				$.post("roleDelUser.action", { ids: idstr,id: "<%=id%>"},
					function(data){
						top.my_alert("撤销成功!");
					}
				);
			}
		}
		
		function f_validate() { 
            if (form.valid()) {
              return form.getData();
            }else {
                form.showInvalid();
            }
            return null;
        }
    </script>
    <style type="text/css">
        body{ 
        	font-size:14px;
        }
        .liger-button {
        	float:left;margin-left:20px;
       	}
    </style>
</head>
<body style="padding:10px">   
	<div id="maingrid"></div>
</body>
</html>
