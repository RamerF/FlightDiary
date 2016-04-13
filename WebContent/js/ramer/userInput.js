//页面中path被定义为全局变量
$(function() {

	/* 更新用户 */
	if ($("#userId").val() != null) {
		$("#regPanel").css("display", "block");
		$(".update_panel").css("display", "block");
		$("#forgetPass").css("display", "none");
		$("#_form").attr("action", path + "/user");
		$(":input[type='submit']").val("更新");
		$("#title").text("更新");
		$("#reg").css("display", "none");
	}

	// ,注册用户,顶部导航条
	$("#username").focus(function() {
		$(".nav_color").css("width", "12%");
	});
	$("#password").focus(function() {
		$(".nav_color").css("width", "23%");
	});
	$("#email").focus(function() {
		$(".nav_color").css("width", "35%");
	});

	// 切换登录/注册
	$("#reg")
			.click(
					function() {
						$("#regPanel,#forgetPass")
								.toggle(
										1200,
										function() {
											// 如果切换后注册界面存在,即用户正在注册
											if ($("#regPanel").is(":visible")) {
												// 将导航条重置
												$(".nav_color").css("width",
														"12%");
												// 修改标题
												$("#title").text("注册");
												// 修改提交文本
												$(":input[type='submit']").val(
														"注册");
												// 修改切换文本
												$("#reg").text("已有账户,去登录");
												$("#username").attr(
														"placeholder", "用户名");
												// 修改表单action
												$("#_form").attr("action",
														path + "/user");
												// 验证用户名可用性,此时用户名数据库中不能存在
												var username = $(
														"input[name='name']")
														.val();
												var url = path
														+ "/user/validateUserName";
												if (username == ""
														|| $.trim(username) == "") {
													return;
												}
												var args = {
													"username" : username,
													"time" : new Date()
												};
												$
														.post(
																url,
																args,
																function(data) {
																	var result = "";
																	if (data == "true") {
																		// 注册时用户名应该不存在
																		result = "<img class='valid' src='"
																				+ path
																				+ "/pictures/wrong.png' weight='10px' height='10px'>";
																	} else {
																		result = "<img class='valid' src='"
																				+ path
																				+ "/pictures/right.png' weight='10px' height='10px'>";
																	}
																	$(
																			"#message")
																			.html(
																					result);
																});
											}
											// 如果切换后注册界面不存在,即用户正在登录
											if ($("#regPanel").is(":hidden")) {
												// 修改标题
												$("#title").text("登录");
												// 修改提交文本
												$(":input[type='submit']").val(
														"登录");
												// 修改链接文本
												$("#reg").text("没有账户,去注册");
												// 修改表单action
												$("#_form").attr("action",
														path + "/user/login");
												// 验证
												var username = $(
														"input[name='name']")
														.val();
												if (username == ""
														|| $.trim(username) == "") {
													return;
												}
												var url = path
														+ "/user/validateUserName";
												var args = {
													"username" : username,
													"time" : new Date()
												};
												$
														.post(
																url,
																args,
																function(data) {
																	var result = "";
																	if (data == "false") {
																		// 登录时用户名应该存在
																		result = "<img class='valid' src='"
																				+ path
																				+ "/pictures/wrong.png' weight='10px' height='10px'>";
																	} else {
																		result = "";
																	}
																	$(
																			"#message")
																			.html(
																					result);
																});
											}
										});
					});

	// 验证用户名的有效性
	$("input[name='name']")
			.change(
					function() {
						var username = $("input[name='name']").val();
						if (username == "" || $.trim(username) == "") {
							return;
						}
						var url = path + "/user/validateUserName";
						var args = {
							"username" : username,
							"time" : new Date()
						};
						$
								.post(
										url,
										args,
										function(data) {
											var result = "";
											if ($("#updatePanel")
													.is(":visible")) {
												if (data == "true") {
													// 更新时用户名应该不存在
													result = "<img class='valid' src='"
															+ path
															+ "/pictures/wrong.png' weight='10px' height='10px'>";
												} else {
													result = "<img class='valid' src='"
															+ path
															+ "/pictures/right.png' weight='10px' height='10px'>";
												}
											}
											// 如果是用户注册
											else if ($("#regPanel").is(
													":visible")) {
												if (data == "true") {
													// 注册时用户名应该不存在
													result = "<img class='valid' src='"
															+ path
															+ "/pictures/wrong.png' weight='10px' height='10px'>";
												} else {
													result = "<img class='valid' src='"
															+ path
															+ "/pictures/right.png' weight='10px' height='10px'>";
												}

											}
											// 如果是用户登录
											else if ($("#regPanel").is(
													":hidden")) {
												if (data == "true") {
													// 登录时用户名应该存在
													result = "";
												} else {
													result = "<img class='valid' src='"
															+ path
															+ "/pictures/wrong.png' weight='10px' height='10px'>";
												}
											}
											$("#message").html(result);
											return;
										});
					});

	// 验证邮箱
	$("#email").change(function() {
		var email = $("input[name='email']").val();
		var url = path + "/user/validateEmail";
		if (email == "" || $.trim(email) == "") {
			return;
		}
		var args = {
			"email" : email,
			"time" : new Date()
		};
		$.post(url, args, function(data) {
			var url = data.replace("../", "./");
			$("#message2").html(url);
		});
	});
	// 表单提交验证
	$("input[type='submit']").click(function() {
		var message = $("#message img").attr("src");
		var message2 = $("#message2 img").attr("src");
		if (message.indexOf("wrong") > 0 || message2.indexOf("wrong") > 0) {
			$("#message3").html("<font color='red'>请确认信息无误</font>");
			return false;
		}
		
		$("_form").submit();
	});
	// 预览图片
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
