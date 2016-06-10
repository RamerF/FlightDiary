// 页面中path被定义为全局变量
$(function(){
  $("input[type='submit']").attr("disabled", "disabled");
  $("input[type='submit']").css("opacity", ".5");

  /* 更新用户 */
  if($("#userId").val() != null){
    $("#regPanel").css("display", "block");
    $(".update_panel").css("display", "block");
    $("#forgetPass").css("display", "none");
    $("#_form").attr("action", path + "/user");
    $(":input[type='submit']").val("更新");
    $("#title").text("更新");
    $("#reg").css("display", "none");

    /* 邮箱格式化显示 */
    var email = $("input[name='email']").attr("value");
    var startString = email.charAt(0);
    var endString = email.substring(email.indexOf("@") - 2, email.length);
    var showEmail = startString + "***" + endString;
    $("input[name='email']").val(showEmail);
    $("input[type='reset']").click(function(){
      $("#_form")[0].reset();
      /* 邮箱格式化显示 */
      $("input[name='email']").val(showEmail);
      $("#message2").text("");
      return false;
    });
    // 更新用户,顶部导航条
    $("#username").focus(function(event){
      $(".nav_color").css("width", "5%");
      event.stopPropagation();
    });
    $("#email").focus(function(event){
      $(".nav_color").css("width", "10%");
      event.stopPropagation();
    });
    $("#telephone").focus(function(event){
      $(".nav_color").css("width", "15%");
    });
    $("#age").focus(function(event){
      $(".nav_color").css("width", "20%");
    });
    $("#qqNum").focus(function(event){
      $(".nav_color").css("width", "25%");
    });
    $("#weiboNum").focus(function(event){
      $(".nav_color").css("width", "30%");
    });
    $(".radio").focus(function(event){
      $(".nav_color").css("width", "35%");
    });
    // 点击确认更新
    $("input[type='submit']").click(function(event){
      var inputStr = $("input[name='email']").val();
      var valueStr = $("input[name='email']").attr("value");
      if(inputStr.indexOf("*") >= 0 && valueStr == email){
        $("input[name='email']").val(email);
      }
      // event.stopPropagation();
      // $("#_form").submit();
      // return false;
    });
  }

  // 切换登录/注册
  $("#reg").click(function(){
    $("#regPanel,#forgetPass").toggle(1200, function(){
      // 如果切换后注册界面存在,即用户正在注册
      if($("#regPanel").is(":visible")){
        // 将导航条重置
        $(".nav_color").css("width", "12%");
        // 注册用户,顶部导航条
        $("#username").focus(function(){
          $(".nav_color").css("width", "12%");
        });
        $("#password").focus(function(){
          $(".nav_color").css("width", "23%");
        });
        $("#email").focus(function(){
          $(".nav_color").css("width", "35%");
        });
        // 修改标题
        $("#title").text("注册");
        // 修改提交文本
        $(":input[type='submit']").val("注册");
        // 修改切换文本
        $("#reg").text("已有账户,去登录");
        $("#username").attr("placeholder", "用户名");
        // 修改表单action
        $("#_form").attr("action", path + "/user");
        // 验证用户名可用性,此时用户名数据库中不能存在
        var username = $("input[name='name']").val();
        var url = path + "/user/validateUserName";
        if(username == "" || $.trim(username) == ""){ return; }
        var args = {
          "username" : username,
          "time" : new Date()
        };
        $.post(url, args, function(data){
          var result = "";
          if(data == "true"){
            // 注册时用户名应该不存在
            result = "<img class='valid' src='" + path + "/pictures/wrong.png' weight='10px' height='10px'>";
            $("input[type='submit']").attr("disabled", "disabled");
            $("input[type='submit']").css("opacity", ".5");
          }
          else{
            result = "<img class='valid' src='" + path + "/pictures/right.png' weight='10px' height='10px'>";
            $("input[type='submit']").removeAttr("disabled", "disabled");
            $("input[type='submit']").css("opacity", "1");
          }
          $("#message").html(result);
        });
      }
      // 如果切换后注册界面不存在,即用户正在登录
      if($("#regPanel").is(":hidden")){
        // 修改标题
        $("#title").text("登录");
        // 修改提交文本
        $(":input[type='submit']").val("登录");
        // 修改链接文本
        $("#reg").text("没有账户,去注册");
        // 修改表单action
        $("#_form").attr("action", path + "/user/login");
        // 验证
        var username = $("input[name='name']").val();
        if(username == "" || $.trim(username) == ""){ return; }
        var url = path + "/user/validateUserName";
        var args = {
          "username" : username,
          "time" : new Date()
        };

        $.post(url, args, function(data){
          var result = "";
          if(data == "false"){
            // 登录时用户名应该存在
            result = "<img class='valid' src='" + path + "/pictures/wrong.png' weight='10px' height='10px'>";
            $("input[type='submit']").attr("disabled", "disabled");
            $("input[type='submit']").css("opacity", ".5");
          }
          else{
            result = "";
            $("input[type='submit']").removeAttr("disabled", "disabled");
            $("input[type='submit']").css("opacity", "1");
          }
          $("#message").html(result);
        });
      }
    });
  });

  // 验证用户名的有效性
  $("input[name='name']").change(function(){
    var username = $("input[name='name']").val();
    if(username == "" || $.trim(username) == ""){ return; }
    var url = path + "/user/validateUserName";
    var args = {
      "username" : username,
      "time" : new Date()
    };
    $.post(url, args, function(data){
      var result = "";
      if($("#updatePanel").is(":visible")){
        if(data == "true"){
          // 更新时用户名应该不存在
          result = "<img class='valid' src='" + path + "/pictures/wrong.png' weight='10px' height='10px'>";
          $("input[type='submit']").attr("disabled", "disabled");
          $("input[type='submit']").css("opacity", ".5");
        }
        else{
          result = "<img class='valid' src='" + path + "/pictures/right.png' weight='10px' height='10px'>";
          $("input[type='submit']").removeAttr("disabled", "disabled");
          $("input[type='submit']").css("opacity", "1");
        }
      }
      // 如果是用户注册
      else if($("#regPanel").is(":visible")){
        if(data == "true"){
          // 注册时用户名应该不存在
          result = "<img class='valid' src='" + path + "/pictures/wrong.png' weight='10px' height='10px'>";
          $("input[type='submit']").attr("disabled", "disabled");
          $("input[type='submit']").css("opacity", ".5");
        }
        else{
          result = "<img class='valid' src='" + path + "/pictures/right.png' weight='10px' height='10px'>";
          $("input[type='submit']").removeAttr("disabled", "disabled");
          $("input[type='submit']").css("opacity", "1");
        }

      }
      // 如果是用户登录
      else if($("#regPanel").is(":hidden")){
        if(data == "true"){
          // 登录时用户名应该存在
          result = "";
          $("input[type='submit']").removeAttr("disabled", "disabled");
          $("input[type='submit']").css("opacity", "1");
        }
        else{
          result = "<img class='valid' src='" + path + "/pictures/wrong.png' weight='10px' height='10px'>";
          $("input[type='submit']").attr("disabled", "disabled");
          $("input[type='submit']").css("opacity", ".5");
        }
      }
      $("#message").html(result);
      return;
    });
  });

  // 验证邮箱
  $("#email").change(function(){
    var email = $("input[name='email']").val();
    var url = path + "/user/validateEmail";
    if(email == "" || $.trim(email) == ""){ return; }
    var args = {
      "email" : email,
      "time" : new Date()
    };
    $.post(url, args, function(data){
      var result = "";
      if(data == "exist" || data == "notEmail"){
        result = "<img class='valid' src='" + path + "/pictures/wrong.png' weight='10px' height='10px'>";
        $("input[type='submit']").attr("disabled", "disabled");
        $("input[type='submit']").css("opacity", ".5");
      }
      else{
        result = "<img class='valid' src='" + path + "/pictures/right.png' weight='10px' height='10px'>";
        $("input[type='submit']").removeAttr("disabled", "disabled");
        $("input[type='submit']").css("opacity", "1");
      }
      $("#message2").html(result);
    });
  });
  // 表单提交验证
  $("input[type='submit']").click(function(){
    var message = $("#message img").attr("src");
    var message2 = $("#message2 img").attr("src");
    if(!message === undefined) if(message.indexOf("wrong") > 0){
      layer.msg("请确认信息无误", {
        time : 1800,
        color : "#000"
      });
      return false;
    }
    if(!message2 === undefined) if(message2.indexOf("wrong") > 0){ return false; }
    // 如果是登陆，使用ajax验证
    if(~~$("#userId").val() == 0){
      if($("#title").text() == "登录"){
        var url = $("#_form").attr("action"),args = {
          "name" : $("#username").val(),
          "password" : $("#password").val()
        };
        $.post(url, args, function(message){
          if(message == "success"){
            location.href = "http://" + window.location.host + path + "/home";
            return false;
          }
          else{
            layer.msg("用户名或密码不正确", {
              time : 1800,
              color : "red"
            });
          }
        })
        return false;
      }

    }

    $("#_form").submit();
  });
  // 预览图片
  $("#userHead").change(function(){
    $(".picName").val($(this).val());
    var file = this.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    $(reader).load(function(){
      $("#preview").attr("src", this.result);
    })
  });
  // 显示密码
  $("#showPassword").hover(function(){
    $("#password").attr("type", "text");
  });
  // 隐藏密码
  $("#showPassword").mouseleave(function(){
    $("#password").attr("type", "password");
  });

})