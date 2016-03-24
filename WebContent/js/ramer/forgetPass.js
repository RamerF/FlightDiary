$(function() {
	$("#sendMail").click(function() {
		var href = $("#sendMailForm").attr("action");
		var email = $("#mailAddr").val();
		var args = {
			"email" : email
		}
		$.post(href, args, function(data) {
			layer.msg(data, {
				time : 1500
			});
		});
		return false;
	});
})
