<%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/default.css"
		rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/modifyPass.css"
		rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
<script type="text/javascript">
 $(function() {
   var errorMess = $("#errorModifyPass").val();
   if (errorMess == "") {
    return ;
   } else {
   layer.msg(errorMess,{
    time: 1500
   });
   }
 })
</script>
<title>修改密码</title>
</head>
<body>
<div class="container">
 <div class="title" id="title">修改密码</div>
  <div class="form">
   <form
    action="${pageContext.request.contextPath}/user/modifyPassword"
    method="post" id="modifyPassForm">
    <span>原始密码: </span>
    <input type="text" name="oldPassword">
    <br>
    <span>新密码: </span>
    <input type="text" name="newPassword">
    <br>
    <input type="submit" value="确认">
    <input type="reset" value="取消">
   </form>
  </div>
  <div>
    <input type="hidden" value="${error_modifyPass}" id="errorModifyPass">
  </div>
</div>
</body>
</html>