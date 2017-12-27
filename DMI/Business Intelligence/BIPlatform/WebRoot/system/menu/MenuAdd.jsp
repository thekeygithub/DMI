	<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String pid = request.getParameter("pid");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>主菜单管理页面</title>
    <base href="<%=basePath %>"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>css-ligerui/reportMain.css"/>
    <link    rel="stylesheet" type="text/css" href="<%=basePath %>include/LigerUI/skins/Aqua/css/ligerui-all.css"/>
    <link    rel="stylesheet" type="text/css"  href="<%=basePath %>include/LigerUI/skins/ligerui-icons.css" /> 
    <script  type="text/javascript"  src="<%=basePath  %>include/jQuery/jquery-1.3.2.min.js"></script>    
    <script  type="text/javascript"  src="<%=basePath  %>include/LigerUI/js/ligerui.all.js" ></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.validate.min.js"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
    <script src="<%=basePath  %>include/LigerUI/jquery-validation/messages_cn.js" type="text/javascript"></script>
    <script type="text/javascript">
    	var jiconlist, winicons;
        $(function (){
            jiconlist = $("body > .iconlist:first");
            if (!jiconlist.length) jiconlist = $('<ul class="iconlist"></ul>').appendTo('body');
	        $(".iconlist li").live('mouseover', function () {
	            $(this).addClass("over");
	        }).live('mouseout', function () {
	            $(this).removeClass("over");
	        }).live('click', function () {
	            var src = $("img", this).attr("src");
	            $("[name='image']").val(src);
	            $(this).removeClass("over");
	             winicons.close();
	        });
        });
        
		function addMenu() {
			var title  = liger.get("title").getValue();//菜单名称
			var pid  = liger.get("pid").getValue();//上级菜单
			if(pid == null || pid == "" || pid == "null"){
				pid = "0";
			}
			var ordernum  = liger.get("ordernum").getValue();//菜单排序
			var url  = liger.get("url").getValue();//菜单url
			var image  = liger.get("image").getValue();//菜单图标
			var whetherpublic  = liger.get("whetherpublic").getValue();//菜单状态
			var actiontype  = liger.get("actiontype").getValue();//菜单打开方式
			var remark  = $("#remark").val();//菜单备注
			var menuData = {"modal.title":title,"modal.pid":pid,"modal.ordernum":ordernum,"modal.url":url,"modal.image":image,"modal.whetherpublic":whetherpublic,"modal.actiontype":actiontype,"modal.remark":remark};
			return menuData;
		}  
		function selectIcon(){
		 	if (winicons) {
                winicons.show();
            }
            winicons = $.ligerDialog.open({
                title: '选取图标',
                target: jiconlist,
                width: 400, 
                height: 220, 
                modal: true,
                isHidden: false,
                cls:"diaclass"
            });
			if (!jiconlist.attr("loaded")) {
				$.ajax({
					url: "menuIconQuery", 
					type: "post",
					data: {rnd: Math.random()},
					dataType: "json",
					success: function (data) {
						var obj = eval(data);
						for (var i = 0, l = obj.length; i < l; i++) {
							var src = obj[i];
						    jiconlist.append("<li><img src='" + obj[i] + "' /></li>");
						}
						jiconlist.attr("loaded", true);
					}
				});
			}
		}
		function f_validate(){ 
			if (form.valid()){
				return addMenu();
			}else{
			    form.showInvalid();
			}
			return null;
		}
		
		var form;
        $(function (){
        	form = $("#form2").ligerForm({
                inputWidth: 170, 
                labelWidth: 110, 
                space: 40,
				validate : true,
                fields: [ 
                    { label: "菜单名称", name: "title", newline: true, type: "text", validate: { required: true,minlength:1,maxlength:20,messages:{minlength:'不能少于1个字符！',maxlength:'不能超过20个字符！'}}},
                    { label: "上级菜单", name: "pid", newline: true, type: "select", 
                    	editor: {
	                        width : 180, 
				            selectBoxWidth: 200,
				            selectBoxHeight: 200,
				            valueField: 'id',
				            treeLeafOnly: false,
	                        tree: { 
	                        	url:"topMenuQueryTree.action",
								ajaxType:'post',
				                idFieldName: 'id',
				                parentIDFieldName :'pid',
				                iconFieldName: 'icon',
                				checkbox: false
							}
	                    }
                    },
                    { label: "菜单排序", name: "ordernum", newline: true, type: "number", validate: { required: true, maxlength: 3,messages:{maxlength:'最大不能超过三位数字！'} } },
                    { label: "执行地址", name: "url", newline: true, type: "text"},
                    { label: "菜单图片URL", name: "image", newline: true, type: "text", validate: { required: true} ,editor: {onFocus:selectIcon}},
                    { label: "菜单状态", name: "whetherpublic", newline: true, type: "select", validate: { required: true },
                    	editor: {
							width : 180, 
				            selectBoxWidth: 200,
				            selectBoxHeight: 200,
				            valueField: 'id',
				            data:[{"id":"01","text":"公开页面"},{"id":"02","text":"私有页面"}]
	                    }
                    },
                    { label: "打开方式", name: "actiontype", newline: true, type: "select", validate: { required: true },
                    	editor: {
							width : 180, 
				            selectBoxWidth: 200,
				            selectBoxHeight: 200,
				            valueField: 'id',
				            data:[{"id":"1","text":"刷新当前页面"},{"id":"2","text":"tab页打开"},{"id":"3","text":"弹出窗口"}]
	                    }
                    },
                    { label: "信息描述", name: "remark", newline: true,width:300,type: "textarea", validate: { required: false,maxlength:200} }
                ]
            }); 
            form.setData({
				actiontype:"1",
				whetherpublic:"01"
			});
            if("<%=pid %>" != "null" && "<%=pid %>" != "0"){
           		form.setData({
					pid:"<%=pid %>" 
	            });
            }
        });
    </script>
    <style type="text/css">
        body{ font-size:14px;}
		.iconlist { width: 360px; padding: 3px; }
		.iconlist li { border: 1px solid #FFFFFF; float: left; display: block; padding: 2px; cursor: pointer; }
		.iconlist li.over { border: 1px solid #516B9F; }
		.iconlist li img { height: 16px; height: 16px; }
    </style>
</head>
<body style="padding:10px">   
	<form id="form2"></form> 
</body>
</html>
