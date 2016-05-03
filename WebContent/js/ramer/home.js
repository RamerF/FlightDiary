$(function(){
  // 获取地理位置
  if(navigator.geolocation){
    navigator.geolocation
        .watchPosition(function(position){
          var latlon = position.coords.latitude + ',' + position.coords.longitude;
          // baidu
          var url = "http://api.map.baidu.com/geocoder/v2/?ak=C93b5178d7a8ebdb830b9b557abce78b&callback=renderReverse&location="
              + latlon + "&output=json&pois=0";
          $.ajax({
            type : "GET",
            dataType : "jsonp",
            url : url,
            success : function(json){
              if(json.status == 0){
                $("#positionVal").val(json.result.addressComponent.city);
              }
            },
            error : function(XMLHttpRequest, textStatus, errorThrown){
              $("#positionVal").val("位置获取失败");
            }
          });
        });
  }
  else{
    alert("浏览器不支持位置信息获取");
  }

  // 从xml文件获取标签列表
  var optionTag = document.getElementById("optionTag");
  /* 指定标签信息的xml文件,全局变量 */
  requestUrl = path + "/xml/tags.xml";
  $.get(requestUrl, function(xml){
    var tag = $(xml).find("tag");
    tag.each(function(index, content){
      optionTag += "<option value='" + $(content).attr('name') + "'>" + $(content).attr('name') + "</option>";
    })
    $("#tags").append(optionTag);
  });
  // 将下拉标签的值，显示到输入框
  $("#tags").change(function(){
    var tags = $(".input_tags").val();
    var tag = $(this).val();
    if(tag == "no"){ return; }
    if($.trim(tags) == ""){
      $(".input_tags").val(tag);
      return;
    }
    else if(tags.indexOf(tag) >= 0){
      return;
    }
    else $(".input_tags").val(tags + ";" + tag);
  })

  // 添加用户当前位置
  $("#addPosition").click(function(){
    // 保存用户输入
    var text = $(".topic_content").val();
    $(".topic_content").val(text + "  " + $("#positionVal").val());
  });

  // 添加当前时间
  $("#addTime").click(
      function(){
        var text = $(".topic_content").val();
        $(".topic_content").val(
            text + "  " + new Date().getFullYear() + "." + (new Date().getMonth() + 1) + "." + new Date().getDate());

      });

  // 由于路径中含有中文时的问题，使用ajax提交标签
  $(".tag").click(function(){
    var url = $(this).attr("href");
    // var prefix = url.substring(0, url.lastIndexOf("/"));
    var tag = url.substring(url.lastIndexOf("/") + 1);
    // $("#tagForm").attr("action", prefix);
    $("#tagName").attr("value", encodeURI(tag));
    $("#tagForm").submit();
    return false;
  });

  // 验证用户标签是否符合格式
  $("#submitTopic").click(function(){
    var tags = $(".input_tags").val();
    if(tags.indexOf("；") >= 0 || tags.indexOf("，") >= 0 || tags.indexOf(",") >= 0){
      layer.msg("请使用英文;分隔标签"), {
        time : 1500,
      }
      return false;
    }
  });

  // 通过标签查询分享
  $(".querytopic").change(function(){
    var tag = $(this).val();
    $("#tagName").attr("value", encodeURI(tag));
    $("#tagForm").submit();
    return false;
  });

  // 上一页
  $("#lastPage").click(function(){
    var number = new Number($("#number").val()) + 1 - 1;
    if(number < 1){
      layer.msg("报告主人,上一页已结婚	(^v^)", {
        time : 1800
      });
      return false;
    }
    Cookies.set("scrollCookie_home", "1");
  });
  // 下一页
  $("#nextPage").click(function(){
    var totalPages = $("#totalPages").val();
    var number = new Number($("#number").val()) + 2;
    Cookies.set("scrollCookie_home", "1");
    if(number > totalPages){
      layer.msg("报告主人,下一页已结婚	(^v^)", {
        time : 1800
      });
      return false;
    }
  });

  // 注销
  $("#logOff").click(function(){
    Cookies.set("scrollCookie_home", "1");
  });
  /* 文本域自适应 */
  $(".topic_content").TextAreaExpander(117, 250);

  /* 显示用户链接面板 */
  $("#showProfile").click(function(){
    $("#personal").show(1000);
  });
  $("#personal").mouseleave(function(){
    $("#personal").hide(1000);
  });
  /* 用户发表分享面板 */
  $("#saySomething").click(function(){
    if($("#topic_panel").is(":visible")){
      $("#preview").css("display", "none");
      $("#upPic").attr("value", "");
      $("#preview").html("");
    }
    $("#topic_panel").toggle(1000);
  });
  // 隐藏分享面板
  $(".hiddenTopic").click(function(){
    $("#topic_panel").hide(1000);
    $("#preview").css("display", "none");
    $("#preview").html('<span></span>');
    $("#upPic").attr("value", "");
    $("#preview").html("");
  });
  /* 图片预览 */
  $("#upPic").change(function(){
    var file = this.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    $(reader).load(function(){
      $("#preview").css("display", "block");
      $("#preview").html('<img	class="preview_pic"	src="' + this.result + '">');
    })
  });
})
