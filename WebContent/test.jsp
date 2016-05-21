<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<style type="text/css">
body,html,div,a{
  padding: 0;
  margin: 0;
}
.more {
 position: absolute;
 padding: 10px;
 background: #FFFFFF;
 top: 100px;
 text-decoration: none;
 color: rgba(0,0,0,.5);
 transition: all 1s;
 background: #000;
 opacity:.4;
 z-index:1000;
}
.moreAm{
height:auto;
 animation: myAnimateTwo 1s linear;
}

.moreNew {
 margin-left: -50px;
 width: 100px;
 left: 50%;
 top: 50%;
 animation: myAnimate 1s linear;
 transform: scale(5); 
}
@keyframes myAnimateTwo{
0%{
top:50%;
left:50%
}
100%{
top:100px;
left: 0%;
}

}
@keyframes myAnimate{
0%{
top:100px;
left: 0%;
}
100%{
left:50%
top:50%;
}

}
.showdata {
 opacity: 0; 
 color:#FFFFFF;
 overflow: hidden;
 position: absolute;
 top: 40%;
 left: 40px;
 padding: 10px;
 width: 580px;
 background: #000;
 transition: all 2s;
}

.showdata ul {
 width: :100%;
 overflow: hidden;
}

.showdata ul li {
 padding: 10px;
 list-style: none;
 float: left;
}
</style>
<script type="text/javascript">
  $(function(){
    $("html").click(function(){
      $(".more").removeClass("moreNew");
      $(".more").addClass("moreAm");
      $(".more").text("更多");
      $(".more").css("height","auto");
      $(".showdata").css("opacity","0");
    });
    $(".more").click(function(e){
      $(".more").addClass("moreNew");
      $(".showdata").css("opacity",".6");
      $(this).text("");
      e.stopPropagation();
      return false;
    });
    $(".more").on("animationend",function(){
      //设置5倍按钮的高度
      var height = getNumber(".showdata","height");
      console.log("height = "+height);
      $(this).css("height",height/5-16+"px");
      console.log(getNumber(".more","left"));
      console.log(getNumber(".more","margin-left"));
      
      
      //定位数据div的左边
      var left = getNumber(this,"left");
      var moreWidth = getNumber(this,"width");
      console.log("left = "+left);
      console.log("morewidth = "+moreWidth);
      var showdataleft = (left - moreWidth*5/2 - 50)+10;
      console.log("showdataleft = "+showdataleft);
      $(".showdata").css("left",showdataleft+"px");
      
      
      //定位数据div的顶部
      var top = getNumber(".more","top");
      console.log("top = "+top);
      $(".showdata").css("top",(top-102)+"px");
      var showdatatop = getNumber(".showdata","top");
      console.log("showdatatop = "+showdatatop);
      
    });
    function getNumber(cssSelector,property){
      var propertyStr = $(cssSelector).css(property);
      var propertyNum = propertyStr.substring(0,propertyStr.indexOf("p"));
      return propertyNum;
    }
  })
</script>
</head>
<body>
 <a href="#" id="showMore" class="more">更多</a>
 <div class="showdata">
  <ul>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
   <li>这里</li>
  </ul>
 </div>
</body>
</html>