<%@ page language="java" contentType="text/html; charset=utf-8"
 pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Diary</title>

<!-- 瀑布流导入 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/component.css" >
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/modernizr.custom.js"></script>
<script src="${pageContext.request.contextPath}/js/js.cookie.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css" >
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/balloon.min.css" >
<script src="${pageContext.request.contextPath}/js/TextAreaExpander.js"></script>
<script type="text/javascript">
$(function() {
    path = "${pageContext.request.contextPath}";
    scrollInPage = "${scrollInPage}";
})
</script>
<script src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
<script src="${pageContext.request.contextPath}/js/ramer/home.js"></script>
<style type="text/css">
.more {
 position: absolute;
 top: 300px;
 padding: 10px;
 background: #009f95;
 transition: all 1s;
 z-index:10000;
 width: 122px;
 color:rgba(255,255,255,.5);
 text-align: center;
 cursor: pointer;
}

.moreAm{
 animation: ramerAnimateTwo 1s linear;
}

.moreNew {
 margin-left: -50px;
 width: 120px;
 animation: ramerAnimate 1s linear;
 transform: scale(5); 
 left: 50%;
 top: 50%;
 box-shadow: 0px 0px 30px rgb(0, 159, 149);
}


.showdata {
background:none;
 opacity: 0; 
 color:#FFFFFF;
 overflow: hidden;
 position: absolute;
 top: 40%;
 left: 40px;
 padding: 10px;
 width: 600px;
 transition: all .5s;
 z-index: 10001;
}

.showdata ul {
 line-height: 1.2em;
 width: :100%;
 overflow: hidden;
}

.showdata ul li {
 padding: 10px;
 list-style: none;
 float: left;
}
.showdata a{
  color: #FFFFFF;
}

@keyframes ramerAnimateTwo{
0%{
 top:50%;
 left:50%;
}
100%{
 top:300px;
 left: 0%;
}
}
@keyframes ramerAnimate{
0%{
 left: 0;
 top:300px;
}
100%{
 left:50%;
 top:50%;
}
}
</style>
</head>
<body>
<!-- 标题面板 -->
<div class="top"> 
  <div  class="title textshadow" >Diary</div>
  <div class="about">
      <span><a href="${pageContext.request.contextPath}/about.jsp" class="textshadow">关于</a></span> / 
      <span><a href="${pageContext.request.contextPath}/feedback.jsp" class="textshadow">反馈</a></span>
  </div>
</div>
<!-- 分类面板 -->
<div class="category_panel">
  <div class="category">
    <span>
      <a href="${pageContext.request.contextPath}/home" data-toggle="默认">默认</a>
      <span id="newTopic" style="position: absolute;margin: 0px;" ></span>
    </span> / 
    <span>
        <a href="${pageContext.request.contextPath}/home/orderbyUpCounts" data-toggle="热门">热门</a>
    </span> / 
    <span>
        <a href="${pageContext.request.contextPath}/home/topPeople" data-toggle="达人">达人</a>
    </span> / 
    <span>
        <a href="${pageContext.request.contextPath}/home/tag" id="topTags" data-toggle="热门标签">热门标签</a>
    </span>
  </div>
  <!-- 通过用户输入标签获取分享 -->
  <div class="query">
    <input type="text" name="tag" placeholder="输入关键词" class="querytopic">
    <i class="icon-search search"></i>
  </div>
  <!-- 通过标签获取分享表单 -->
  <form action="${pageContext.request.contextPath}/home/tag" id="tagForm">
    <input type="text" name = "tag" id="tagName">
  </form>
  <c:if test="${user.id gt 0}">
    <div class="user_panel">
      <span>
        <a href="javascript:void(0);" id="saySomething" class="share_link ">给我一个分享平台</a>
      </span>
     <span class="username" id="showProfile">${user.username}</span>
     <span id="newNotify" style="position: absolute;margin: 0px;margin-left: -30px;" ></span>
     <c:if test="${notifiedNumber gt 0}">
      <span id="newTopic" 
        style="position: absolute;margin: 0px;padding: 0px;margin-left: -33px;"
        class="newTopic"></span>
     </c:if>
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
    <a href="${pageContext.request.contextPath}/user/${user.id}">编辑个人信息</a>
  </div>
</div>
<!-- 发表分享面板 -->
<div class="topic_panel" id="topic_panel">
    <form action="${pageContext.request.contextPath}/publish" method="post" enctype="multipart/form-data">
        <textarea rows="7" cols="30" name="content" class="topic_content"></textarea>
        <div class="tool">
         <div id="addPosition" class="add_position" data-balloon="添加当前位置" data-balloon-pos="right">
           <img alt="" src="${pageContext.request.contextPath}/pictures/position.png">
         </div>
         <div id="addTime" class="add_time" data-balloon="添加当前时间" data-balloon-pos="right">
           <img alt="" src="${pageContext.request.contextPath}/pictures/calendar.png">
         </div>
        </div>
        <input class="upbtn" type="file" name="picture" accept="image/*" id="upPic">
        <input class="upbtn2" placeholder="点击添加一张图片">
        <div id="preview" class="preview"></div>
        <div class="input_tag_panel">
         <span>标签：</span>
         <select id="tags">
          <option value="no" id="optionTag">请选择</option>
         </select>
         <input type="text" name="tags" class="input_tags" placeholder="标签使用;隔开" >
        </div>
        <input type="submit" value="分享" id="submitTopic">
        <input type="reset" value="收起" class = "hiddenTopic">
    </form>
</div>
<!-- 显示分享 -->
<c:if test="${showTopic eq 'true' }">
<div class="container">
    <ul class="grid effect-2" id="grid">
        <c:forEach items="${topics.content}" var="t">
            <li>
                <div class="user_mess textshadow">
                  <div class="tags_panel">
                     <c:forEach items="${t.tags}" var="tag">
                       <i class="icon-tags"></i>
                       <span class="tags">${tag}</span>
                     </c:forEach>
                  </div>
                  <a href="${pageContext.request.contextPath}/user/topic/${t.id}">
                    <img src="${pageContext.request.contextPath}/${t.picture}" alt="error" />
                  </a>
                  <div class="name_panel">
                    <a href="${pageContext.request.contextPath}/user/personal/${t.user.id}">
                        <img src="${pageContext.request.contextPath}/${t.user.head}" class="head"
                         onerror="javascript:this.src='${pageContext.request.contextPath}/pictures/userHead.jpg'">
                        <br>
                        <span class="name">${t.user.username }</span>
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
<ul class="page_panel">
    <li class="lastPage">
      <a href="?pageNum=${topics.number + 1 - 1}" id="lastPage">
        <img alt="error" src="${pageContext.request.contextPath}/pictures/previous.png" >
      </a>
    </li>
    <li class="nextPage">
      <a href="?pageNum=${topics.number + 1 + 1}" id="nextPage">
        <img alt="error" src="${pageContext.request.contextPath}/pictures/next.png" >
      </a>
    </li>
    <li>
      <c:if test="${scrollInPage eq true}">
       <a id="removeScrollPage">禁止滚动翻页</a>
     </c:if>
     <c:if test="${scrollInPage ne true}" >
       <a id="removeScrollPage">开启滚动翻页</a>
     </c:if>
    </li>
</ul>
</c:if>
<!-- 显示达人 -->
<c:if test="${showTopPeople eq 'true' }">
<div class="container">
    <ul class="grid effect-2" id="grid">
        <c:forEach items="${topPeoples.content}" var="u">
            <li>
                <div class="user_mess">
                  <a href="${pageContext.request.contextPath}/user/personal/${u.id}">
                  <c:forEach items="${u.topics}" begin="0" end="0" var="head">
                    <img src="${pageContext.request.contextPath}/${head.picture}" alt="error" 
                      onerror="javascript:this.src='${pageContext.request.contextPath}/pictures/userHead.jpg'"/>
                      </c:forEach>
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
<input id="number" type="hidden" value="${topPeoples.number}">
<!-- 分页 -->
<ul class="page_panel">
    <li class="lastPage">
      <a href="?pageNum=${topPeoples.number + 1 - 1}" id="lastPage">
        <img alt="error" src="${pageContext.request.contextPath}/pictures/previous.png" >
      </a>
    </li>
    <li class="nextPage">
      <a href="?pageNum=${topPeoples.number + 1 + 1}" id="nextPage">
        <img alt="error" src="${pageContext.request.contextPath}/pictures/next.png" >
      </a>
    </li>
    <li>
     <c:if test="${scrollInPage eq true}">
       <a id="removeScrollPage">禁止滚动翻页</a>
     </c:if>
     <c:if test="${scrollInPage ne true}" >
       <a id="removeScrollPage">开启滚动翻页</a>
     </c:if>
    </li>
</ul>
</c:if>
<!-- 显示热门标签 -->
<c:if test="${showPopularTags eq 'true' }">
<div class="" id="showData">
    <ul class="tag_panel" id="tagPanel">
        <c:forEach items="${tags }" var="tag">
            <li class="tagname">
                <a href="${pageContext.request.contextPath}/home/tag/${tag}" class="tag" data-toggle="${tag}">
                    ${tag}
                </a>
            </li>
         </c:forEach>
    </ul>
    </div>
    <span class="more" data-toggle="更多" id="showMore">更多</span>
    <!-- 显示分享 -->
    <div class="container">
        <ul class="grid effect-2" id="grid">
          <c:forEach items="${tagTopics.content}" var="t">
                <li>
                    <div class="user_mess">
                      <a href="${pageContext.request.contextPath}/user/topic/${t.id}">
                        <img src="${pageContext.request.contextPath}/${t.picture}" alt="error" />
                      </a>
                      <div class="name_panel">
                        <a href="${pageContext.request.contextPath}/user/personal/${t.user.id}">
                            <img src="${pageContext.request.contextPath}/${t.user.head}" class="head"
                              onerror="javascript:this.src='${pageContext.request.contextPath}/pictures/userHead.jpg'">
                            <br>
                            <span class="name">${t.user.username }</span>
                        </a>
                        <div class="t_content ">${t.content }</div>
                      </div>
                    </div>
                </li>
             </c:forEach>
        </ul>
    </div>
    <!-- 存储记录的总页数 -->
    <input id="totalPages" type="hidden" value="${tagTopics.totalPages }">
    <!-- 存储当前页号 -->
    <input id="number" type="hidden" value="${tagTopics.number}">
    <!-- 分页 -->
    <ul class="page_panel">
      <li class="lastPage">
        <a href="?pageNum=${tagTopics.number + 1 - 1}" id="lastPage">
          <img alt="error" src="${pageContext.request.contextPath}/pictures/previous.png" >
        </a>
      </li>
      <li class="nextPage">
        <a href="?pageNum=${tagTopics.number + 1 + 1}" id="nextPage">
          <img alt="error" src="${pageContext.request.contextPath}/pictures/next.png" >
        </a>
      </li>
      <li>
        <c:if test="${scrollInPage eq true}">
       <a id="removeScrollPage">禁止滚动翻页</a>
     </c:if>
     <c:if test="${scrollInPage ne true}" >
       <a id="removeScrollPage">开启滚动翻页</a>
     </c:if>
      </li>
    </ul>
</c:if>
<input type="hidden" id="positionVal">

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