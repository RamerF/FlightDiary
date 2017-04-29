$(function(){
  $("#sendMail").click(function(){
    var href = $("#sendMailForm").attr("action");
    var email = $("#mailAddr").val();
    var args = {
      "email" : email
    }
    $.post(href, args, function(data){
      layer.msg(data, {
        time : 1500
      });
      if(data.indexOf("到家啦") > 0){
        $("#forwardQQMail").css("display", "initial");
      }
    });
    return false;
  });
  /* 验证邮箱 */
  $("#mailAddr").change(function(){
    var email = $(this).val();
    var url = path + "/user/validateEmail";
    if(email == "" || $.trim(email) == ""){ return; }
    var args = {
      "email" : email,
      "time" : new Date()
    };
    $.post(url, args, function(data){
      var result = "";
      if(data == "notEmail" || data == "notExist"){
        result = "<img class='valid' src='" + path + "/pictures/wrong.png' weight='10px' height='10px'>";
      }
      else{
        result = "<img class='valid' src='" + path + "/pictures/right.png' weight='10px' height='10px'>";
      }
      $("#message").html(result);
    });
  });
  // 验证密码是否匹配
  var email = $.trim($("input[name='email']").val());
  if(email != "" || email != null){
    $("#rePass").change(function(){
      var pass = $.trim($("#pass").val());
      var rePass = $.trim($("#rePass").val());
      var result = "";
      if(pass != rePass){
        result = "<img class='valid' src='" + path + "/pictures/wrong.png' weight='10px' height='10px'>";
        $("#modifyPass").attr("disabled", "disabled");
        $("#modifyPass").css("opacity", ".5");
      }
      else{
        result = "<img class='valid' src='" + path + "/pictures/right.png' weight='10px' height='10px'>";
        $("#modifyPass").removeAttr("disabled");
        $("#modifyPass").css("opacity", "1");
      }
      $("#message2").html(result);
    });
  }
  // 点击修改密码
  $("#modifyPass").click(function(){
    if($("#message2").text().indexOf("wrong") >= 0 || $.trim($("#pass")) == ""){
      return false;
    }
    else{
      $("#modifyEmailForm").submit();
    }
  });
})
