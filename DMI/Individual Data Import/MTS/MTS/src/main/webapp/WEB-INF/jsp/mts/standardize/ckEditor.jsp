<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
<title>ckEditor</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<!-- <script type="text/javascript" src="static/login/js/jquery-1.5.1.min.js"></script> -->
<style type="text/css">
</style>
<script type="text/javascript"
	src="<%=basePath%>static/ckeditor/ckeditor.js"></script>
<script type="text/javascript"
	src="<%=basePath%>static/ckeditor/config.js"></script>
<script type="text/javascript">
	CKEDITOR.replace('TextArea1');
</script>
</head>
<body>
	<textarea id="TextArea1" cols="100" rows="30" class="ckeditor">123</textarea>
	<!-- <textarea class="ckeditor" cols="80" id="content" name="content"
		rows="10">       
        CKEditor Test......(此处的内容会在编辑器中显示)  
     </textarea> -->
</body>


</html>