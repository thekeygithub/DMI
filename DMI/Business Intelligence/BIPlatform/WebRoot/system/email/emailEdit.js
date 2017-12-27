$(function(){
	f_getEmail();
    $("#email").attr("disabled",'disabled');
});

function f_getEmail(){
	$.ajax({
		url:"getEmailInfo.action",
		type:"post",
		dataType:"json",
		success:function(data){
			$("#id").val(data.id);
			$("#mailServerHost").val(data.mailServerHost);
			$("#mailServerPort").val(data.mailServerPort);
			$("#fromAddress").val(data.fromAddress);
		    $("#userName").val(data.userName);
		    $("#emailPwd").val(data.password);
		    $("#reEmailPwd").val(data.password);
		    if(data.validate==true){
		    	$("#validate_1").attr("checked","checked");
		    }else{
		    	$("#validate_0").attr("checked","checked");
		    }
		}
	});	
}

function f_editEmail(){
	$("#email").removeAttr("disabled");
}

function f_saveEmail(){	
	$.ajax({
		url:"updateEmailInfo.action",
		data:{
			"tsemailconfig.id":$("#id").val(),
			"tsemailconfig.mailServerHost":$("#mailServerHost").val(),
			"tsemailconfig.mailServerPort":$("#mailServerPort").val(),
			"tsemailconfig.fromAddress":$("#fromAddress").val(),
			"tsemailconfig.userName":$("#userName").val(),
			"tsemailconfig.password":$("#emailPwd").val(),
			"validate":$('input:radio[name="validate"]:checked').val()
		},
		type:"post",
		dataType:"json",
		success:function(data){
			f_getEmail();
    		$("#email").attr("disabled",'disabled');
		}
	});
}

function f_testEmail(){
		$.ajax({
		url:"testEmailInfo.action",
		data:{
			"tsemailconfig.mailServerHost":$("#mailServerHost").val(),
			"tsemailconfig.mailServerPort":$("#mailServerPort").val(),
			"tsemailconfig.fromAddress":$("#fromAddress").val(),
			"tsemailconfig.userName":$("#userName").val(),
			"tsemailconfig.password":$("#emailPwd").val(),
			"validate":$('input:radio[name="validate"]:checked').val()
		},
		type:"post",
		dataType:"json",
		success:function(data){
			top.$.ligerDialog.success('发送邮件成功且邮箱配置信息保存成功!');
			f_saveEmail();
			f_getEmail();
    		$("#email").attr("disabled",'disabled');
		},
		error:function(data){
			top.$.ligerDialog.error('发送邮件失败邮箱配置信息保存失败!');
			f_editEmail();
		}
	});
}
