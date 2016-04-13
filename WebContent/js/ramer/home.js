$(function() {
	// 从xml文件获取城市列表
	var optionNodeCountry = document.getElementById("optionNodeCountry");
	var optionNodeCity = document.getElementById("optionNodeCity");
	// requestUrl为全局变量,在jsp文件中声明
	$.get(requestUrl, function(xml) {
		var country = $(xml).find("country");
		country.each(function(index, content) {
			optionNodeCountry += "<option value='" + $(content).attr('name')
					+ "'>" + $(content).attr('name') + "</option>";
		})
		$("#country").append(optionNodeCountry);
	});
	// 二级下拉菜单,获取城市列表
	$("#country").change(
			function() {
				$("#city").empty();
				optionNodeCity = "";
				var countryName = $(this).val();
				$.get(requestUrl,
						function(xml) {
							var city = $(xml).find(
									"country[name='" + countryName + "']")
									.find("city");
							city
									.each(function(index, content) {
										optionNodeCity += "<option value='"
												+ $(content).attr('name')
												+ "'>"
												+ $(content).attr('name')
												+ "</option>";
									});
							$("#city").append(optionNodeCity);
						});
			})
	// 通过城市名查询分享
	$(".querytopic").change(function() {
		var url = path + "/home/groupByCity/" + $(this).val();
		$("#queryTopic").attr("action", url);
		$("#queryTopic").submit();
		return false;
	});

	// 上一页
	$("#lastPage").click(function() {
		var number = new Number($("#number").val()) + 1 - 1;
		if (number < 1) {
			layer.msg("报告主人,上一页已结婚	(^v^)", {
				time : 1800
			});
			return false;
		}
		Cookies.set("scrollCookie_home", "0");
	});
	// 下一页
	$("#nextPage").click(function() {
		var totalPages = $("#totalPages").val();
		var number = new Number($("#number").val()) + 2;
		Cookies.set("scrollCookie_home", "0");
		if (number > totalPages) {
			layer.msg("报告主人,下一页已结婚	(^v^)", {
				time : 1800
			});
			return false;
		}
	});
	// 注销
	$("#logOff").click(function() {
		Cookies.set("scrollCookie_home", 0);
	});
	/* 文本域自适应 */
	$(".topic_content").TextAreaExpander(117, 250);
	/* 记录滚动条的位置 */
	/* 获取滚动条的位置 */
	var scrollCookie = Cookies.get("scrollCookie_home");
	if (scrollCookie != null && scrollCookie != "") {
		$("html,body").animate({
			scrollTop : scrollCookie + "px"
		}, 1000);
	} else {
		Cookies.set("scrollCookie_home", $(document).scrollTop("0px"));
	}
	var scroll = 0;
	var interval = null;
	// 滚动条滚动时记录滚动条高度,判断滚动条是否停止滚动
	$(window).scroll(function() {
		Cookies.set("scrollCookie_home", $(document).scrollTop());
		if (interval == null) {
			interval = setInterval(checkScroll, 1000);
		}
		scroll = $(document).scrollTop();
	});
	// 测试滚动条是否滚动
	function checkScroll() {
		if ($(document).scrollTop() == scroll) {
			clearTimeout(interval);
			interval = null;
			$("::-webkit-scrollbar").css("display", "none");
			// alert("停止滚动");
		}
	}
	/* 显示用户链接面板 */
	$("#showProfile").click(function() {
		$("#personal").show(1000);
	});
	$("#personal").mouseleave(function() {
		$("#personal").hide(1000);
	});
	/* 用户发表分享面板 */
	$("#saySomething").click(function() {
		if ($("#topic_panel").is(":visible")) {
			$("#preview").css("display", "none");
			$("#upPic").attr("value", "");
			$("#preview").html("");
		}
		$("#topic_panel").toggle(1000);
	});
	// 隐藏分享面板
	$(".hiddenTopic").click(function() {
		$("#topic_panel").hide(1000);
		$("#preview").css("display", "none");
		$("#preview").html('<span></span>');
		$("#upPic").attr("value", "");
		$("#preview").html("");
	});
	/* 图片预览 */
	$("#upPic").change(
			function() {
				var file = this.files[0];
				var reader = new FileReader();
				reader.readAsDataURL(file);
				$(reader).load(
						function() {
							$("#preview").css("display", "block");
							$("#preview").html(
									'<img	class="preview_pic"	src="'
											+ this.result + '">');
						})
			});
})
