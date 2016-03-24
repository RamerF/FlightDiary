<%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/default.css"
		rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/forgetPass.css"
		rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
<script src="${pageContext.request.contextPath}/js/ramer/forgetPass.js"></script>
</head>
<body>
		<div class="container">
				<div class="title" id="title">重置密码</div>
				<div class="form">
						<c:if test="${empty email}">
								<form id="sendMailForm"
										action="${pageContext.request.contextPath}/user/forgetPass/sendMail"
										method="post">
										<span>您的邮箱 : </span> <input type="text" name="email"
												placeholder="请输入账号关联的邮箱" id="mailAddr"> <input
												type="submit" value="发送邮件" id="sendMail">
								</form>
						</c:if>
						<c:if test="${!empty email}">
								<form
										action="${pageContext.request.contextPath}/user/forgetPassword"
										method="post">
										<span>请输入密码 :</span> <input type="text" name="password"
												placeholder="请输入密码"><br> <span>请确认密码 : </span>
										<input type="text" name="repassword" placeholder="请再次输入密码"><br>
										<input type="hidden"
												value="<%=request.getParameter("email")%>" name="email">
										<input type="submit" value="确认修改"> <input type="reset"
												value="清空">
								</form>
						</c:if>
				</div>
		</div>
		</div>

</body>
</html>