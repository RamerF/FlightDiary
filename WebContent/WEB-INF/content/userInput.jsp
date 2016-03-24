<%@	page language="java" contentType="text/html; charset=utf-8"
		pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login</title>
<link href="${pageContext.request.contextPath}/css/default.css"
		rel="stylesheet">
<link rel="stylesheet"
		href="${pageContext.request.contextPath}/css/userInput.css">
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript">
var path = "";
$(function() {
		path = "${pageContext.request.contextPath}";
})
</script>
<script src="${pageContext.request.contextPath}/js/ramer/userInput.js"></script>

</head>
<body>
		<div class="container">
				<div class="title" id="title">登录</div>
				<div class="form">
						<form:form action="${pageContext.request.contextPath}/user/login "
								method="post" modelAttribute="user" id="_form"
								enctype="multipart/form-data">
								<c:if test="${!empty user.id}">
										<form:hidden path="id" id="userId" />
										<input type="hidden" name="_method" value="put">
								</c:if>
								<span class="error"></span>
								<span>用户名 : </span>
								<form:input path="name" style="margin-right: -3px;"
										placeholder="用户名 / 邮箱" />
								<span id="message"></span>
								<br>
								<c:if test="${empty user.id}">
										<span>密码 : </span>
										<form:password path="password" />
										<br>
								</c:if>
								<!--	用户注册面板 -->
								<div class="reg_panel" id="regPanel">
										<span>电话号码 : </span>
										<form:input path="telephone" />
										<br> <span>年龄 : </span>
										<form:input path="age" />
										<br> <img id="preview" class="preview" alt="error"
												src="${pageContext.request.contextPath}/${user.head}">
										<input type="text" class="picName" placeholder="请选择一张图片">
										<input type="file" class="userHead" id="userHead"
												name="picture" /> <br> <span>QQ : </span>
										<form:input path="qqNum" />
										<br> <span>微博 : </span>
										<form:input path="weiboNum" />
										<br> <span>性别 : </span>
										<form:radiobutton path="sex" label="男" value="M" class="radio"></form:radiobutton>
										<form:radiobutton path="sex" label="女" value="F" class="radio"></form:radiobutton>
										<br>
								</div>
								<div class="forget_pass" id="forgetPass">
										<a
												href="${pageContext.request.contextPath}/user/forwardForgetPassword">
												<span>忘记密码</span>
										</a>
								</div>
								<input type="submit" value="登录" />
								<input type="reset" value="重置" />
								<a href="javascript:void(0);" id="reg">没有账户,去注册</a>
						</form:form>
				</div>
		</div>

</body>
</html>