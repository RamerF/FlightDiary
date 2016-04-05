$(function() {
	// 从xml文件获取城市列表
	var optionNodeCountry = document.getElementById("optionNodeCountry");
	var optionNodeCity = document.getElementById("optionNodeCity");
	// requetsUrl为全局变量,在jsp文件中声明
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

	// 显示私信
	$("#showPrivMess").click(function() {
		$("#privMessPanel").toggle(1000);
	});
	$("#showPrivMessNo").click(function() {
		layer.msg("什么都没有诶", {
			time : 1500
		});
	});
	// 显示私信
	// 读私信
	$(".readPrivMess").click(function() {
		alert("读取通知");
		var id = $(this).children(".notifyId").val();
		var url = $(this).attr("href");
		$("#sendPrivMessForm").attr("aciton", url);
		$("#notifyId").attr("value", id);
		$("#sendPrivMessForm").submit();
		return false;
	});
	// 读私信
	/* 显示用户收藏 */
	$("#showFavouritePanel").click(function() {
		if ($("#favouritePanel").is(":hidden")) {
			$(this).html('收起收藏<i class="icon-arrow-up"></i>');
		}
		if ($("#favouritePanel").is(":visible")) {
			$(this).html('我收藏的<i class="icon-arrow-down"></i>');
		}
		$("#favouritePanel").toggle(1000);
		return false;
	});
	/* 显示关注的用户 */
	$("#showFollowPanel").click(function() {
		if ($("#followPanel").is(":hidden")) {
			$(this).html('收起关注<i class="icon-arrow-up"></i>');
		}
		if ($("#followPanel").is(":visible")) {
			$(this).html('我关注的<i class="icon-arrow-down"></i>');
		}
		$("#followPanel").toggle(1000);
		return false;
	});

	/* 滚轮滚动取消重复点赞提示 */
	$(window).scroll(function() {
		if ($(".errorMessage").text().indexOf("点赞") > 0) {
			$(".errorMessage").text("");
		}
	});
	/* 滚轮滚动取消重复点赞提示 */
	/* 用户点赞 */
	$(".thumbsup").click(
			function() {
				var aNode = $(this);
				var iNode = $(this).children("i");
				var url = $(this).attr("href");
				$.post(url, null, function(data) {
					if (data == "恭喜你骚年,她已经悄悄收下你的赞 !") {
						$(iNode).attr("class", "icon-thumbs-up-ramer");
						$(aNode).attr("href",
								url.replace("praise", "notPraise"));
						var num = parseInt($(aNode).siblings(".upCounts")
								.text().replace("(", "").replace(")", ""));
						$(aNode).siblings(".upCounts").text(
								"(" + (num + 1) + ")");
					}
					if (data == "取消点赞成功 !") {
						$(iNode).attr("class", "icon-thumbs-up");
						$(aNode).attr("href",
								url.replace("notPraise", "praise"));
						var num = parseInt($(aNode).siblings(".upCounts")
								.text().replace("(", "").replace(")", ""));
						$(aNode).siblings(".upCounts").text(
								"(" + (num - 1) + ")");
					}
					layer.msg(data, {
						time : 1500
					});
				});
				return false;
			});
	/* 用户点赞 */

	/* 显示更新用户头像面板 */
	$("#showProfile").click(function() {
		if ($(".hiddenUpdateHeadPanel").is(":visible")) {
			$("#preview2").css("display", "none");
			$("#upPic2").attr("value", "");
			$("#preview2").html("");
		}
		$(".update_head_panel").toggle(1000);
	});
	$(".hiddenUpdateHeadPanel").click(function() {
		$(".update_head_panel").hide(1000);
		$("#preview2").css("display", "none");
		$("#preview2").html('<span></span>');
		$("#upPic2").attr("value", "");
	});
	/* 删除用户评论 */
	$(".trash2").click(
			function() {
				var delete_formNode = $(this).parent().parent().parent()
						.children(".delete_comment_form");
				$(delete_formNode).attr("action", $(this).attr("href"));
				$(delete_formNode).submit();
				layer.msg("评论已从你面前消失 !", {
					time : 2000
				});
				return false;
			});
	/* 显示分享面板 */
	$("#saySomething").click(function() {
		if ($("#topic_panel").is(":visible")) {
			$("#preview").css("display", "none");
			$("#preview").html('<span></span>');
			$("#upPic").attr("value", "");
		}
		$("#topic_panel").toggle(1000);
	});
	/* 隐藏分享面板 */
	$(".hiddenTopic").click(function() {
		$("#topic_panel").hide(1000);
		$("#preview").css("display", "none");
		$("#preview").html('<span></span>');
		$("#upPic").attr("value", "");
	});
	/* 发表用户评论 */
	$(".comment").click(
			function() {
				var formNode = $(this).next(".trash").next(
						".comment_form_panel").children(".comment_form");
				$(formNode).toggle(1000);
				$(formNode).attr("action", $(this).attr("href"));
				return false;
			});
	$(".hiddenCommentForm").click(function() {
		$(".comment_form").hide(1000);
	});

	/* 回复评论 */
	$(".reply").click(
			function() {
				var formNode = $(this).parent("p").next(".reply_form_panel")
						.children(".reply_form");
				$(formNode).toggle(1000);
				$(formNode).attr("action", $(this).attr("href"));
				return false;
			});
	$(".hiddenReplyForm").click(function() {
		$(".reply_form").hide(1000);
	});

	/* 回复回复 */
	$(".reply2").click(
			function() {
				var formNode = $(this).parent("p").next(
						".reply_double_form_panel").children(
						".reply_double_form");
				$(formNode).toggle(1000);
				$(formNode).attr("action", $(this).attr("href"));
				return false;
			});
	$(".hiddenReplyDoubleForm").click(function() {
		$(".reply_double_form").hide(1000);
	});
	/* 图片预览 */
	$("#upPic").change(
			function() {
				$("#picName").val($(this).val());
				var file = this.files[0];
				var reader = new FileReader();
				reader.readAsDataURL(file);
				$(reader).load(
						function() {
							$("#preview").css("display", "block");
							$("#preview").html(
									'<img class="preview_pic" src="'
											+ this.result + '">');
						});
			});
	$("#upPic2").change(
			function() {
				$("#picName2").val($(this).val());
				var file = this.files[0];
				var reader = new FileReader();
				reader.readAsDataURL(file);
				$(reader).load(
						function() {
							$("#preview2").css("display", "block");
							$("#preview2").html(
									'<img class="preview_pic" src="'
											+ this.result + '">');
						});
			});
})
