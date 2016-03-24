<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="${pageContext.request.contextPath}/css/default.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/modifyPass.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js">
    </script>
    <script src="${pageContext.request.contextPath}/js/layer/layer.js">
    </script>
    <script src="${pageContext.request.contextPath}/js/ramer/modifyEmail.js">
    </script>
    <title>
      修改邮箱
    </title>
  </head>
  <body>
    <div class="container">
      <div class="title" id="title">
        修改邮箱
      </div>
      <div class="form">
        <form 
            id="sendMailForm"
            action="${pageContext.request.contextPath}/user/modifyEmail/sendMail"
            method="post"
            id="modifyPassForm">

          <span>
            新的邮箱 :
          </span>
          <input type="text" name="newEmail" placeholder="请输入您的新邮箱" id="newEmail" />
          <br/>
          <input type="submit" value="确认修改" id="sendMail" />
          <input type="reset" value="清空" />
        </form>
      </div>
    </div>

  </body>
</html>
