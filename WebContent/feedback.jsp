<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>反馈系统</title>
<link href="${pageContext.request.contextPath}/css/feedback.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
$(function() {
  path = "${pageContext.request.contextPath}";
})
</script>
<script src="${pageContext.request.contextPath}/js/TextAreaExpander.js"></script>
<script src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
<script src="${pageContext.request.contextPath}/js/ramer/feedback.js"></script>
</head>
<body>
  <!--  返回-->
<div class="return_link">
        <img alt="error" src="${pageContext.request.contextPath}/pictures/back.png" id="back">
</div>
<!--  返回主页-->
<div class="return_home">
        <img alt="error" src="${pageContext.request.contextPath}/pictures/home.png" id="home">
</div>
  <div class="container">
    <h2>反馈</h2>
    <p>
       您在使用本系统的过程中遇到的任何问题，请及时反馈给我们，帮助完善本系统，请仔细填写下面的反馈资料：
    </p>
    <div class="main">
     <form action="${pageContext.request.contextPath}/user/feedback" method="post">
       <textarea rows="3" cols="7" placeholder="请详细描述使用过程中遇到的问题" name="content"></textarea>
       <div class="environment">
        <span>浏览器：</span>
        <select name="Browser">
          <option value="Chrome">Chrome</option>
          <option value="FireFox">FireFox</option>
          <option value="Edge">Edge</option>
          <option value="Opera">Opera</option>
          <option value="InternetExplorer">InternetExplorer</option>
        </select>
        <span>操作系统：</span>
        <select name="OS">
          <option value="Linux">Linux</option>
          <option value="Windows">Windows</option>
          <option value="Mac">Mac</option>
        </select>
       </div>
       <input type="submit" value="提交反馈" id="feedback"">
     </form>
    </div>
  </div>
</body>
</html>