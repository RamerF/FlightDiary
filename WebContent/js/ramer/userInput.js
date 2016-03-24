//页面中path被定义为全局变量
$(function() {
 /* 更新用户 */
 if ($("#userId").val() != null) {
  $("#regPanel").css("display", "block");
  $("#forgetPass").css("display", "none");
  $("#_form").attr("action", path + "/user");
  $(":input[type='submit']").val("更新");
  $("#title").text("更新");
  $("#reg").css("display", "none");
  // 验证用户名的有效性
  $("input[name='name']").change(function() {
   var username = $("input[name='name']").val();
   var url = path + "/user/validateUserName";
   var args = {
    "username" : username,
    "time" : new Date(),
    "isLogin" : "false"
   };
   // alert("验证1");
   $.post(url, args, function(data) {
    $("#message").html(data);
   });
  });
 }
 /* 登录界面 */
 if ($("#regPanel").is(":hidden")) {
  // 验证用户名的有效性
  $("input[name='name']").change(function() {
   var username = $("input[name='name']").val();
   var url = path + "/user/validateUserName";
   var args = {
    "username" : username,
    "time" : new Date(),
    "isLogin" : "true"
   };
   // alert("验证0");
   $.post(url, args, function(data) {
    var url = data.replace("../", "/FlightDiary/");
    $("#message").html(url);
   });
  });
 }

 $("#reg").click(function() {
  $("#regPanel,#forgetPass").toggle(1200, function() {
   if ($("#regPanel").is(":visible")) {
    /* 先验证一次 */
    var username = $("input[name='name']").val();
    var url = path + "/user/validateUserName";
    var args = {
     "username" : username,
     "time" : new Date(),
     "isLogin" : "false"
    };
    // alert("验证3");
    $.post(url, args, function(data) {
     var url = data.replace("../", "./");
     $("#message").html(url);
    });
    /* 先验证一次 */
    $("#_form").attr("action", path + "/user");
    $(":input[type='submit']").val("注册");
    $("#title").text("注册");
    $("#reg").text("已有账户,去登录");
    // 验证用户名的有效性
    $("input[name='name']").change(function() {
     var username = $("input[name='name']").val();
     var url = path + "/user/validateUserName";
     var args = {
      "username" : username,
      "time" : new Date(),
      "isLogin" : "false"
     };
     // alert("验证4");
     $.post(url, args, function(data) {
      var url = data.replace("../", "./");
      $("#message").html(url);
     });
    });
   }
   if ($("#regPanel").is(":hidden")) {
    $("#_form").attr("action", path + "/user/login");
    $("#title").text("登录");
    $(":input[type='submit']").val("登录");
    $("#reg").text("没有账户,去注册");
    /* 先验证一次 */
    var username = $("input[name='name']").val();
    var url = path + "/user/validateUserName";
    var args = {
     "username" : username,
     "time" : new Date(),
     "isLogin" : "true"
    };
    // alert("验证5");
    $.post(url, args, function(data) {
     var url = data.replace("../", "./");
     $("#message").html(url);
    });
    /* 先验证一次 */
   }
  });
 });

 $("#userHead").change(function() {
  $(".picName").val($(this).val());
  var file = this.files[0];
  var reader = new FileReader();
  reader.readAsDataURL(file);
  $(reader).load(function() {
   $("#preview").attr("src", this.result);
  })
 });

})
