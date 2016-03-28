<%@ page language="java" contentType="text/html; charset=utf-8"
 pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>share you idea</title>

<!-- 瀑布流导入 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/component.css" >
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/modernizr.custom.js"></script>
<script src="${pageContext.request.contextPath}/js/js.cookie.js"></script>
<!-- 瀑布流导入 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css" >
<script src="${pageContext.request.contextPath}/js/TextAreaExpander.js"></script>
<script src="${pageContext.request.contextPath}/js/ramer/home.js"></script>
<script src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
</head>
<body>
<!-- 标题面板 -->
<div class="top">
  <div  class="title">旅行日记</div>
  <div class="about">
      <span><a href="#">关于</a></span> / 
      <span><a href="#">反馈</a></span>
  </div>
</div>
<!-- 分类面板 -->
<div class="category_panel">
  <div class="category">
    <span>
      <a href="${pageContext.request.contextPath}/home">默认</a>
    </span> / 
    <span>
        <a href="${pageContext.request.contextPath}/home/orderbyUpCounts">热门</a>
    </span> / 
    <span>
        <a href="${pageContext.request.contextPath}/home/topPeople">达人</a>
    </span> / 
    <span>
        <a href="#">热门城市</a>
    </span>
  </div>
  <c:if test="${user.id gt 0}">
    <div class="user_panel">
      <span>
        <a href="javascript:void(0);" id="saySomething" class="share_link">给我一个分享平台</a>
      </span>
     <span class="username" id="showProfile">${user.name}</span>
      <span class="logoff">
       <a href="${pageContext.request.contextPath}/logOff" id="logOff">注销</a>
      </span>
    </div>
  </c:if>
  <c:if test="${empty user.id || user.id le 0}">
    <div class="signup">
      <span>
        <a href="${pageContext.request.contextPath}/user">登录/注册</a>
      </span>
    </div>
</c:if>
</div>
<!-- 用户链接面板 -->
<div class="user_link" id="personal">
  <div>
    <a href="${pageContext.request.contextPath}/user/personal">我的主页</a>
  </div>
  <div>
    <a href="${pageContext.request.contextPath}/user/personal">我的分享</a>
    <span>${topicNumber}</span>
  </div>
  <div>
    <a href="${pageContext.request.contextPath}/user/personal">我的关注</a>
    <span>${followedNumber}</span>
  </div>
  <div>
    <a href="${pageContext.request.contextPath}/user/personal">我的通知</a>
    <span>${notifiedNumber}</span>
  </div>
  <div>
    <a href="${pageContext.request.contextPath}/user/forwardModifyPassword">修改我的密码</a>
  </div>
  <div>
    <a href="${pageContext.request.contextPath}/user/forwardModifyEmail">修改绑定邮箱</a>
  </div>
  <div>
    <a href="${pageContext.request.contextPath}/user/forwardModifyEmail">编辑个人信息</a>
  </div>
</div>
<!-- 发表分享面板 -->
<div class="topic_panel" id="topic_panel">
    <form action="${pageContext.request.contextPath}/publish" method="post" enctype="multipart/form-data">
        <textarea rows="7" cols="30" name="content" class="topic_content"></textarea>
        <br>
        <input class="upbtn" type="file" name="picture" accept="image/*" id="upPic">
        <input class="upbtn2" placeholder="点击添加一张图片">
        <br>
        <div id="preview" class="preview"></div>
        <input type="submit" value="分享">
        <input type="reset" value="收起" class = "hiddenTopic">
    </form>
</div>
<!-- 显示分享 -->
<c:if test="${showTopic eq 'true' }">
<div class="container">
    <ul class="grid effect-2" id="grid"">
        <c:forEach items="${topics.content}" var="t">
            <li>
                <div class="user_mess">
                  <a href="${pageContext.request.contextPath}/user/topic/${t.id}">
                    <img src="${pageContext.request.contextPath}/${t.picture}" alt="error" />
                  </a>
                  <div class="name_panel">
                    <a href="${pageContext.request.contextPath}/user/personal/${t.user.id}">
                        <img src="${pageContext.request.contextPath}/${t.user.head}" class="head">
                        <br>
                        <span class="name">${t.user.name }</span>
                    </a>
                    <div class="t_content ">${t.content }</div>
                  </div>
                </div>
            </li>
        </c:forEach>
    </ul>
</div>
<!-- 存储记录的总页数 -->
<input id="totalPages" type="hidden" value="${topics.totalPages }">
<!-- 存储当前页号 -->
<input id="number" type="hidden" value="${topics.number}">
<!-- 页码面板 -->
<div class="page_panel">
    <div class="lastPage">
      <a href="?pageNum=${topics.number + 1 - 1}" id="lastPage">上一页</a>&nbsp;&nbsp;
    </div>
    <div class="nextPage">
      <a href="?pageNum=${topics.number + 1 + 1}" id="nextPage">下一页</a>
    </div>
</div>
</c:if>
<!-- 显示达人 -->
<c:if test="${showTopPeople eq 'true' }">
<div class="container">
    <ul class="grid effect-2" id="grid"">
        <c:forEach items="${topPeoples.content}" var="u">
            <li>
                <div class="user_mess">
                  <a href="${pageContext.request.contextPath}/user/topic/${u.id}">
                    <img src="${pageContext.request.contextPath}/${u.head}" alt="error" />
                  </a>
                  <div class="name_panel">
                    <a href="${pageContext.request.contextPath}/user/personal/${u.id}">
                        <img src="${pageContext.request.contextPath}/${u.head}" class="head">
                        <br>
                        <span class="name">${u.name }</span>
                    </a>
                  </div>
                </div>
            </li>
        </c:forEach>
    </ul>
</div>
<!-- 存储记录的总页数 -->
<input id="totalPages" type="hidden" value="${topPeoples.totalPages }">
<!-- 存储当前页号 -->
<input id="number" type="hidden" value="${topPeoples.number - 1}">
<!-- 分页 -->
<div class="page_panel">
    <div class="lastPage">
      <a href="?pageNum=${topPeoples.number - 1}" id="lastPage">上一页</a>&nbsp;&nbsp;
    </div>
    <div class="nextPage">
      <a href="?pageNum=${topPeoples.number  + 1}" id="nextPage">下一页</a>
    </div>
</div>
</c:if>
<script src="${pageContext.request.contextPath}/js/masonry.pkgd.min.js"></script>
<script src="${pageContext.request.contextPath}/js/imagesloaded.js"></script>
<script src="${pageContext.request.contextPath}/js/classie.js"></script>
<script src="${pageContext.request.contextPath}/js/AnimOnScroll.js"></script>
<!-- 瀑布流动画js -->
<script>
 new AnimOnScroll(document.getElementById('grid'), {
  minDuration : 0.4,
  maxDuration : 0.7,
  viewportFactor : 0.2
 });
</script>
</body>
</html>