<%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${pageContext.request.contextPath}/css/default.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/personal.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/balloon.min.css" >
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/js.cookie.js"></script>
<script src="${pageContext.request.contextPath}/js/layer/layer.js"></script>
<script type="text/javascript">
 $(function() {
  /* 指定标签信息的xml文件,全局变量 */
  path = "${pageContext.request.contextPath}";
  /* 记录滚动条的位置 */
  /* 获取滚动条的位置 */
  var scrollCookie = Cookies.get("scrollCookie_personal" +"${user.id}");
  if (scrollCookie != null && scrollCookie !="") {
   $("html,body").animate({
    scrollTop : scrollCookie + "px"
   }, 1000);
  } else
   Cookies.set("scrollCookie_personal"+"${user.id}", $(document).scrollTop("0px"));
  $(window).scroll(function() {
   Cookies.set("scrollCookie_personal"+"${user.id}", $(document).scrollTop());
  });
 })
</script>
<script src="${pageContext.request.contextPath}/js/ramer/personal.js"></script>
<title>Personal Center</title>
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
<!-- 用户信息面板 -->
<div class="user_panel">
    <div class="head">
        <a id="showProfile">
            <img alt="error" src="${pageContext.request.contextPath}/${user.head }" class="user_head"
              onerror="javascript:this.src='${pageContext.request.contextPath}/pictures/userHead.jpg'">
            <br>
            <span>更换头像</span>
        </a>
    </div>
    <div class="selfinfo">
    <div class="name">${sessionScope.user.name}</div>
        <div class="contact">
            <span> QQ: ${!empty user.qqNum ? user.qqNum : "无" }</span>
            <span> 微博: ${!empty user.weiboNum ? user.weiboNum : "无"}</span>
            <!-- 用户通知 -->
            <span>
                <c:if test="${!empty user.notifies || !empty user.readedNotifies }">
                    <a href="javascript:void(0)" id="showPrivMess">
                        <i class="icon-envelope"></i>
                        <small class="notifyCount">
                            <sup>${notifyCount}</sup>
                        </small>
                    </a>
                </c:if>
                <c:if test="${empty user.notifies && empty user.readedNotifies}">
                    <a href="javascript:void(0)" id="showPrivMessNo">
                        <i class="icon-envelope-empty"></i>
                        <small class="notifyCount">
                            <sup>${notifyCount}</sup>
                        </small>
                    </a>
                </c:if>
            </span>
        </div>
        <div class="info">
            <span>
                <a href="${pageContext.request.contextPath}/logOff">注销</a>
            </span>
            <span>
                <a href="${pageContext.request.contextPath}/user/${user.id}" id="editProfile">编辑个人信息</a>
            </span>
            <span>
                <a href="javascript:void(0);" id="saySomething">给我一个分享平台</a>
            </span>
            <span>
                <a id="showFollowPanel" href="#"> 我关注的<i class="icon-arrow-down"></i>
            </a>
            </span>
            <span>
                <a id="showFavouritePanel" href="#">我收藏的<i class="icon-arrow-down"></i></a>
            </span>
        </div>
    </div>
</div>

<!-- 私信面板 -->
<div class="privMessPanel" id="privMessPanel">
 <c:forEach items="${user.notifies}" var="notify">
  <span class="unreadNotify notify">
   <input type="hidden" value="${notify.id}" name="notifyId" class="notifyId">
   <input type="hidden" value="${notify.notifiedUser.id}" name="notifiedUserId" class="notifiedUserId">
   <a href="${pageContext.request.contextPath}/user/personal/${notify.user.id}"class="notifyUser">
    ${notify.user.name } : 
   </a>
   ${notify.content}
    <sub>
      <fmt:formatDate value="${notify.date}" pattern="HH:mm:ss yyyy-MM-dd"/>
    </sub>
    <img alt="error" src="${pageContext.request.contextPath }/pictures/new.png" class="newNotify">
  </span>
 </c:forEach>
 <c:forEach items="${user.readedNotifies}" var="readedNotify">
  <span class="notify">
   <input type="hidden" value="${readedNotify.id}" name="notifyId" class="notifyId">
   <input type="hidden" value="${readedNotify.notifiedUser.id}" name="notifiedUserId" class="notifiedUserId">
   <a href="${pageContext.request.contextPath}/user/personal/notify/readPrivMess" class="readPrivMess">
    ${readedNotify.user.name } : ${readedNotify.content}
    <sub>
      <fmt:formatDate value="${readedNotify.date}" pattern="HH:mm:ss yyyy-MM-dd"/>
    </sub>
   </a>
  </span>
 </c:forEach>
</div>

<!-- 更新用户头像面板 -->
<div class="update_head_panel">
    <form
        action="${pageContext.request.contextPath}/user/update?id=${user.id}"
        method="post" enctype="multipart/form-data">
        <input class="upbtn2" placeholder="选择你的头像"  id="picName2">
        <input type="file" name="picture" class="upbtn" accept="image/*" id="upPic2">
        <div id="preview2" class="preview"></div>
        <input type="submit" value="更新头像">
        <input type="reset" value="收起" class="hiddenUpdateHeadPanel">
    </form>
</div>

<!-- 关注面板 -->
<div class="follow_panel clearfix" id="followPanel">
    <c:if test="${empty user.follows }">
        <span>什么都没有诶 !</span>
    </c:if>
    <c:if test="${!empty user.follows }">
        <c:forEach items="${user.follows}" var="follow">
            <div class="followed_user_panel">
                <div>
                    <a href="${pageContext.request.contextPath}/user/personal/${follow.followedUser.id}">
                        <img class="followed_user_head" alt="error" src="${pageContext.request.contextPath}/${follow.followedUser.head }">
                    </a>
                </div>
                <div class="followed_user_name_panel">
                    <a href="${pageContext.request.contextPath}/user/personal/${follow.followedUser.id}">${follow.followedUser.name}</a>
                </div>
            </div>
        </c:forEach>
    </c:if>
</div>

<!-- 收藏面板 -->
<div class="favourite_panel" id="favouritePanel">
    <c:if test="${empty user.favourites }">
        <span>什么都没有诶 !</span>
    </c:if>
    <c:if test="${!empty user.favourites }">
        <c:forEach items="${user.favourites}" var="favourite">
            <div class="favourite_item_panel">
                <div>
                    <a href="${pageContext.request.contextPath}/user/topic/${favourite.topic.id}">
                        <img class="favourite_item_picture" alt="error" src="${pageContext.request.contextPath}/${favourite.topic.picture}">
                    </a>
                </div>
                <div class="favourite_item_content">
                    <a href="${pageContext.request.contextPath}/user/topic/${favourite.topic.id}">${favourite.topic.content}</a>
                </div>
        </div>
        </c:forEach>
    </c:if>
</div>

<!-- 发表分享面板 -->
<div id="topic_panel" class="topic_panel">
    <form action="${pageContext.request.contextPath}/publish"
        method="post" enctype="multipart/form-data">
        <input type="hidden" name="personal" value="true">
        <textarea rows="7" cols="30" class="topic_content" name="content"></textarea>
        <div class="tool">
         <div id="addPosition" class="add_position" data-balloon="添加当前位置" data-balloon-pos="right">
           <img alt="" src="${pageContext.request.contextPath}/pictures/position.png">
         </div>
         <div id="addTime" class="add_time" data-balloon="添加当前时间" data-balloon-pos="right">
           <img alt="" src="${pageContext.request.contextPath}/pictures/calendar.png">
         </div>
        </div>
        <input class="choose_pic" type="text" placeholder="请选择一张图片" id="picName">
        <input type="file" name="picture" class="choose_file" accept="image/*" id="upPic">
        <div id="preview" class="preview"></div>
        <div class="input_tag_panel">
         <span>标签：</span>
         <select id="tags">
          <option value="no" id="optionTag">请选择</option>
         </select>
         <input type="text" name="tags" class="input_tags" placeholder="标签使用;隔开" >
        </div>
        <input type="submit" value="分享">
        <input type="reset" value="收起" class="hiddenTopic">
    </form>
</div>
<!-- 显示个人分享 -->
<c:if test="${empty user.topics}">
    <div class="no_topic">
        <strong>你还没有发布分享,现在开始记录你的生活吧 !</strong>
    </div>
</c:if>
<c:if test="${!empty user.topics}">
    <c:forEach items="${user.topics}" var="topic">
        <div class="show_topic">
            <div class="side">
                <div class="day">
                    <a href="#">${topic.date.date}/${topic.date.month+1}</a>
                </div>
            </div>
            <div class="main">
                <div class="content">
                    <c:if test="${!empty topic.picture }">
                        <div class="img">
                            <img alt="error" src="${pageContext.request.contextPath}/${topic.picture}" class="topic_pic">
                        </div>
                        <p class="desc"></p>
                    </c:if>
                    <c:if test="${empty topic.picture }">
                        <p class="desc" style="height: 35px;"></p>
                    </c:if>
                    <div class="text">${topic.content }</div>
                </div>
            </div>
            <hr class="h_line">
            <c:set var="isPraise" value="false"></c:set>
            <c:forEach items="${praises }" var="p">
                <c:if test="${topic.id eq p }">
                    <c:set var="isPraise" value="true"></c:set>
                </c:if>
            </c:forEach>
            <c:if test="${ isPraise eq true}">
                <a href="${pageContext.request.contextPath}/user/topic/notPraise/${topic.id}" class="thumbsup">
                    <i class="icon-thumbs-up-ramer"></i>
                </a>
            </c:if>
            <c:if test="${ isPraise ne true}">
                <a href="${pageContext.request.contextPath}/user/topic/praise/${topic.id}" class="thumbsup">
                    <i class="icon-thumbs-up"></i>
                </a>
            </c:if>
            <small class="upCounts">(${empty topic.upCounts ? 0 : topic.upCounts})</small>
            <a href="${pageContext.request.contextPath}/user/topic/comment/${topic.id}" class="comment">
                <i class="icon-edit"></i>
            </a>
            <a href="${pageContext.request.contextPath}/user/topic/deleteTopic/${topic.id}" class="trash">
                <i class="icon-trash"></i>
            </a>
            <!-- 发表评论表单 -->
            <div class="comment_form_panel">
                <form action="" method="post" class="comment_form">
                    <textarea rows="7" cols="30" class="comment_content" name="content"></textarea>
                    <br>
                    <input type="submit" value="发表评论">
                    <input type="reset" value="收起" class="hiddenCommentForm">
                </form>
            </div>
            <!-- 删除用户评论表单 -->
            <form action="" method="post" class="delete_comment_form">
                <input type="hidden" name="topic" value="${topic.id}">
            </form>
        
        <!-- 显示评论 -->
        <c:forEach items="${topic.comments}" var="comment">
            <div class="comment_panel">
                <p>${comment.user.name } : ${comment.content }
                  <sub>
                    &nbsp;&nbsp;<fmt:formatDate value="${comment.date}" pattern="HH:mm:ss yyyy-MM-dd"/>
                  </sub>
                  <a href="${pageContext.request.contextPath}/user/topic/comment/reply/${comment.id}" class="reply">
                      <i class="icon-edit"></i>
                  </a>
                  <a href="${pageContext.request.contextPath}/user/topic/comment/delete/${comment.id}" class="trash2">
                      <i class="icon-trash"></i>
                  </a>
                </p>
                <!-- 回复评论表单 -->
                <div class="reply_form_panel">
                    <form action="" method="post" class="reply_form">
                        <textarea rows="7" cols="30" class="reply_content" name="content"></textarea>
                        <br>
                        <input type="submit" value="回复评论">
                        <input type="reset" value="收起" class="hiddenReplyForm">
                    </form>
                </div>
                <c:forEach items="${comment.replies}" var="reply">
                    <p>${reply.user.name}&nbsp;回复&nbsp;${comment.user.name} : ${reply.content }
                      <sub>
                        &nbsp;&nbsp;<fmt:formatDate value="${reply.date}" pattern="HH:mm:ss yyyy-MM-dd"/>
                      </sub>
                        <a href="${pageContext.request.contextPath}/user/topic/comment/reply/${reply.comment.id}" class="reply2">
                            <i class="icon-edit"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/user/topic/reply/delete/${reply.id}" class="trash_reply">
                            <i class="icon-trash"></i>
                        </a>
                    </p>
                    <!-- 回复回复表单 -->
                    <div class="reply_double_form_panel">
                        <form action="" method="post" class="reply_double_form">
                            <textarea rows="7" cols="30" class="reply_double_content" name="content"></textarea>
                            <input type="submit" value="回复">
                            <input type="reset" value="收起" class="hiddenReplyDoubleForm">
                        </form>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
        </div>
    </c:forEach>
</c:if>
<input type="hidden" id="positionVal">
</body>
</html>