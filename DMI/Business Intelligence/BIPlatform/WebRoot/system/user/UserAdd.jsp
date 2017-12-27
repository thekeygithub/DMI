<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String id = request.getParameter("corpid");//公司或部门id
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  	<base href="<%=basePath %>"/>
    <title>用户添加页面</title>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script  src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.validate.min.js"></script>
    <script  src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
    <script  src="<%=basePath  %>include/LigerUI/jquery-validation/messages_cn.js" type="text/javascript"></script>
    <script  type="text/javascript"  src="<%=basePath  %>js/dateformat.js"></script> 
    <script type="text/javascript">
		var form;
		//var type = true;
        $(function (){
        	form = $("#form2").ligerForm({
                inputWidth: 170, 
                labelWidth: 100, 
                space: 40,
				validate : true,
                fields: [ 
                	{ name: "sex",type: "hidden"},
                	{ name: "postid", type: "hidden" },
                	{ name: "deptid", type: "hidden" },
                	{ name: "roleid", type: "hidden" },
                	{ name: "stationid", type: "hidden" },
                	{ name: "birth",type: "hidden"},
                	{ name: "area",type: "hidden"},
                	{ name: "dcid",type: "hidden"},
                    { label: "登陆账号", name: "loginname", newline: true, type: "text", validate: { required: true,maxlength: 50 },editor: { onChangeValue: getloginname }  },
                    { label: "用户姓名", name: "username", newline: false, type: "text", validate: { required: true,maxlength: 8 } },
                    { label: "用户密码", name: "userpass", newline: true, type: "password", validate: { required: true,minlength: 8,maxlength: 32 },editor: { onChangeValue: getpassword } },
                    { label: "确认密码", name: "passwordqr", newline: false, type: "password", validate: { required: true,minlength: 8,maxlength: 32 },editor: { onChangeValue: getpasswordqr } },
                    { label: "用户性别", name: "sexModel", newline: true, type: "select", validate: { required: true }, dictionary: '男|女',
                    	editor: {
							onSelected: function(e){
								if (e=="男"){
					            	$("[name=sex]").val("M");
					            }else if(e=="女"){
					            	$("[name=sex]").val("W");
					            }
							}
	                    }
                    },
                    { label: "出生年月", name: "birthday", newline: false, type: "date", validate: { required: true},
                   		editor:{
	                   		onChangeDate:checkdate
                   		} 
                	},
                    { label: "所属单位", name: "dept", newline: true, type: "select", validate: { required: true},
	                    editor: {
	                        width : 180, 
				            selectBoxWidth: 200,
				            selectBoxHeight: 200, 
				            valueField: 'id',
				            treeLeafOnly: false,//只能选中子节点的属性
	                        tree: { 
								url:"cropDeptTreeQuery.action", 
								ajaxType:'post',
								idFieldName: 'id',
                				parentIDFieldName: 'pid',
                				autoCheckboxEven:false,
                				checkbox: true
							},
							onSelected: function(id,value){
								if(''!=id&&''!=value&&"null"!=value){
										$("[name=deptid]").val(id);
										var topcorpid = "";
										$.ajax({
											url:"QueryTopCorpId.action?corpid="+id, 
											async:false,
											type:"post",
											success:function (data) {
												topcorpid = data;
											}, 
											error:function (error) {
												top.my_alert("获取信息失败！" + error.status);
											}
										});
										liger.get("role").treeManager.set("url","QueryRoleByDept.action?corpid="+topcorpid);
										liger.get("station").treeManager.set("url","QueryStationByDept.action?corpid="+topcorpid);
										liger.get("post").treeManager.set("url","QueryPostByDept.action?corpid="+topcorpid);
										liger.get("areaid").treeManager.set("url","QueryAreaByDept.action?corpid="+topcorpid);
								}
							}
	                    }
                    },
                    { label: "岗位",id:'station', name: "station", newline: false, type: "select", 
                    	editor: {
	                        width : 180, 
				            selectBoxWidth: 200,
				            selectBoxHeight: 200, 
				            valueField: 'id',
				            treeLeafOnly: true,
	                        tree: { 
								ajaxType:'post',
								idFieldName: 'id',
                				parentIDFieldName: 'pid',
                				onSuccess:function(){
                					clearNullValue(liger.get("station"));
                				},
                				checkbox: true
							},
							onSelected: function(id,value){
							$("[name=stationid]").val("");
								if(''!=id&&''!=value&&"null"!=value){
									$("[name=stationid]").val(id);
								}
							}
	                    }
                    },
                    { label: "用户角色", name: "role", newline: true, type: "select", validate: { required: true},
                    	editor: {
	                        width : 180, 
				            selectBoxWidth: 200,
				            selectBoxHeight: 200, 
				            valueField: 'id',
				            treeLeafOnly: true,
	                        tree: { 
								ajaxType:'post',
								idFieldName: 'id',
                				parentIDFieldName: 'pid',
                				onSuccess:function(){
                					clearNullValue(liger.get("role"));
                				},
                				checkbox: true
							},
							onSelected: function(id,value){
								if(''!=id&&''!=value&&"null"!=value){
									$("[name=roleid]").val(id);
								}
							}
	                    }
                    },
                    { label: "职务", name: "post", newline: false, type: "select", validate: { required: true},
                    	editor: {
	                        width : 180, 
				            selectBoxWidth: 200,
				            selectBoxHeight: 200, 
				            valueField: 'id',
				            treeLeafOnly: true,
	                        tree: { 
								ajaxType:'post',
								idFieldName: 'id',
                				parentIDFieldName: 'pid',
                				onSuccess:function(){
                					clearNullValue(liger.get("post"));
                				},
                				checkbox: true
							},
							onSelected: function(id,value){
								if(''!=id&&''!=value&&"null"!=value){
									$("[name=postid]").val(id);
								}
							}
	                    }
                    },
                    { label: "所属区域", name: "areaid", newline: true, type: "select", validate: { required: true},
                    	editor: {
	                        width : 180, 
				            selectBoxWidth: 200,
				            selectBoxHeight: 200, 
				            valueField: 'id',
				            treeLeafOnly: true,
	                        tree: { 
								ajaxType:'post',
								idFieldName: 'id',
	            				parentIDFieldName: 'pid',
	            				onSuccess:function(){
	            					clearNullValue(liger.get("areaid"));
	            				},
	            				checkbox: false
							},
							onSelected: function(id,value){
								if(''!=id&&''!=value&&"null"!=value){
									$("[name=area]").val(id);
								}
							}
	                    }
                    },
                    { label: "所属数据中心", name: "dcidid", newline: false, type: "select", validate: { required: true},
                    	editor:{
	               			url:"dcidTreeQuery.action",
							isMultiSelect:false,
							valueField: 'id',
							onSelected: function(id,value){
								if(''!=id&&''!=value&&"null"!=value){
									$("[name=dcid]").val(id);
								}
							}
	                	} 
                    },
                    { label: "公司座机", name: "phonepublic", newline: true, type: "text" },
                    { label: "家庭座机", name: "phoneprivate", newline: false, type: "text" },
                    { label: "公司手机", name: "mobilepublic", newline: true, type: "digits", validate: { required: false,minlength: 11,maxlength: 11 },editor: { onChangeValue: getmobilepublic }  },
                    { label: "私人手机", name: "mobileprivate", newline: false, type: "digits", validate: { required: true,minlength: 11,maxlength: 11 },editor: { onChangeValue: getmobileprivate }  },
                    { label: "公司邮箱", name: "emailpublic", newline: true, type: "text",editor: { onChangeValue: getemailpublic } },
                    { label: "私人邮箱", name: "emailprivate", newline: false, type: "text", validate: { required: true },editor: { onChangeValue: getemailprivate } },
                    { label: "用户描述", name: "remark", newline: true, type: "textarea",width:480 , validate: { required: false,maxlength:200} }
                ]
            });
            if("null"!="<%=id%>"){
	            form.setData({
					dept: "<%=id%>"
	            });
            }
        });
        
        /**登录名验证*/
        function getloginname(){
        	var data = form.getData();
        	var datas = {"loginname":data.loginname};
           	$.ajax({
				url:"treeuserExist.action", 
				data:datas, 
				type:"post",
				success:function (mm) {
					if(mm == "EXIST"){
						//top.my_alert("该登录名已经存在，请更换登录名称！","warn");
						form.showFieldError("loginname","该登录名已经存在，请更换登录名称！");
						$("[name=loginname]").val(data.loginname);
					}
				}, 
				error:function (error) {
					top.my_alert("该登录名已存在" + error.status);
			}});
        }
        function getpassword(){
        	var data = form.getData();
           	var str = /[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/;
           	if(!str.test(data.userpass)){
           		form.showFieldError("userpass","密码只许包含字母和数字！");
           		$("[name=userpass]").val("");
           	}
        }
        
        function getpasswordqr(){
        	var data = form.getData();
        	if(data.userpass!=data.passwordqr){
        		form.showFieldError("passwordqr","确认密码与密码输入不同！");
        		$("[name=passwordqr]").val("");
        	}
        }
        function getmobilepublic(){
        	var data = form.getData();
        	if(!checkPhoneNum(data.mobilepublic)){
        		form.showFieldError("mobilepublic","请正确输入手机号！");
           		$("[name=mobilepublic]").val("");
        	}
        }
        function getmobileprivate(){
       		var data = form.getData();
       		if(!checkPhoneNum(data.mobileprivate)){
        		form.showFieldError("mobileprivate","请正确输入手机号！");
           		$("[name=mobileprivate]").val("");
        	}
        }
        function getemailpublic(){
        	var data = form.getData();
        	if(!checkEmail(data.emailpublic)){
        		form.showFieldError("emailpublic","请正确输入邮箱地址！");
           		$("[name=emailpublic]").val("");
        	}
        }
        function getemailprivate(){
       		var data = form.getData();
       		if(!checkEmail(data.emailprivate)){
        		form.showFieldError("emailprivate","请正确输入邮箱地址！");
           		$("[name=emailprivate]").val("");
        	}
        }
        /**取消按钮*/
        function cancel(){
           window.top.my_closewindow();
        }
        /**手机验证*/
        function checkPhoneNum(value){
			var tel=/^1[3|5|8][0-9]\d{8}$/;
			if(!tel.test(value)){
				return false;
			}
			return true;
		}

		/**验证邮箱*/
		function checkEmail(value){
			var email = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			if(!email.test(value)){
				return false;
			}
			return true;
		}
		/**出生年月日*/
		function checkdate(value){
	   		var bdate = value;
	   		var currentDate = getFormatDate(new Date());
		   	if(bdate > currentDate){
		   		form.showFieldError("birth","出生年月不能大于今天！");
           		$("[name=birth]").val("");
		   	}else{
		   		form.hideFieldError("birth");
		   	}
	   }
		 /**提交验证*/
        function f_validate() { 
            if(form.valid()) {
               var birthday = $("[name=birthday]").val();
               $("[name=birth]").val(birthday);
              return form.getData();
            }else {
                form.showInvalid();
            }
            return null;
        }
       
	function clearNullValue(obj){//当前下拉框的ligerui对象。liger.get("id");
		var ids = obj.getValue();
		var tempArr = new Array();
		if(ids != "" && ids != "null"){
			var idArr = ids.split(";");
			for(var i=0;i<idArr.length;i++){
				var id = idArr[i];
				if(id != "" && id != "null"){
					var t = obj.treeManager.getTextByID(id);//
					if(t != null && t != "" && t != "null"){
						tempArr.push(id);
					}
				}
			}
		}
		obj.setValue(tempArr.join(";"));
		if(!tempArr.join(";")){
			obj.setText("");
		}
		return tempArr.join(";");
	}
	</script>
	<style type="text/css">
        html,body{ 
        	margin:0;
        	padding:0;
        	font-size:14px;
        }
    </style>
</head>
<body style="padding:10px">   
	<form id="form2"></form> 
</body>
</html>
