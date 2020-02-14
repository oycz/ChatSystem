/**
 * 社交模块
 * 
 * */

//社交模块初始化
function social_init() {
	count_words();
	//接收社交状态
	receive_social_messages();
}
//字数统计
function count_words() {
	$('#send_message_context').val($('#send_message_context').val().substr(0, 400));
	$("#words_count_numerator").text($("#send_message_context").val().length);
}

//发送社交状态
function send_social_message() {
	//	//ajax
	//	var xmlhttp;
	//	if (window.XMLHttpRequest) { //IE7+, Firefox, Chrome, Opera, Safari
	//		xmlhttp = new XMLHttpRequest();
	//	} else { //IE6, IE5
	//		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	//	}
	//	xmlhttp.onreadystatechange = function() {
	//		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
	//			//更新信息
	//			receive_social_messages();
	//			//显示发送成功
	//			show_succeed("发送成功!");
	//			//置空信息
	//			$("#send_message_context").val("");
	//			//关闭发送框
	//			close_send_message();
	//		}
	//	}
	//	xmlhttp.open("POST", path_name + "/sendSocialMessage.do", true);
	//	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	//	xmlhttp.send("social_message=" +
	//		$("#send_message_context").val());
	//
	var social_content = $("#send_message_context").val();
	if (social_content.trim() == '') {
		show_succeed("请输入内容");
		return;
	}

	var social_id = -1;
	$.ajax({
		type : "POST",
		cache : false,
		async : false,
		url : path_name + "/sendSocialMessage.do",
		data : {
			'social_message' : $("#send_message_context").val()
		},
		success : function(data) {
			//更新信息
			receive_social_messages();
			//显示发送成功
			show_succeed("发送成功!");
			//置空信息
			$("#send_message_context").val("");
			//关闭发送框
			close_send_message();
			console.log(data);
			social_id = parseInt(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			show_error("上传失败，请检查网络后重试");
		}
	});

	//上传图片
	//判断是否有选择上传文件
	var imgPath = $("#upload_social_image").val();
	if (imgPath != "") {
		//判断上传文件的后缀名
		var strExtension = imgPath.substr(imgPath.lastIndexOf('.') + 1);
		//		if (strExtension != 'jpg' && strExtension != 'jpeg' && strExtension != 'gif'
		//			&& strExtension != 'png' && strExtension != 'bmp') {
		//			show_error("请选择图片文件");
		//			return;
		//		}
		var formData = new FormData($('#upload_social_image_form')[0]);

		$.ajax({
			type : "POST",
			cache : false,
			url : path_name + "/uploadSocialImage.do",
			data : formData,
			processData : false,
			contentType : false,
			success : function(data) {
				show_succeed("图片上传成功");
				$("#upload_social_image").empty();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				show_error("上传失败，请检查网络后重试");
			}
		});
	}
}

//上传社交图片
function change_social_image() {
	var imgPath = $("#upload_social_image").val();
	var strExtension = imgPath.substr(imgPath.lastIndexOf('.') + 1);
	if (strExtension != 'jpg' && strExtension != 'jpeg' && strExtension != 'gif'
		&& strExtension != 'png' && strExtension != 'bmp') {
		show_error("请选择图片文件");
		return false;
	} else {
		show_succeed("图片选取成功");
		return true;
	}
}

//接受全部社交状态
function receive_social_messages() {
	//ajax
	var xmlhttp;
	if (window.XMLHttpRequest) { //IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else { //IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			$("#social_messages").html(xmlhttp.responseText);
		}
	}
	xmlhttp.open("POST", path_name + "/jsp/ajax/socialtexts.jsp", true);
	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlhttp.send();
}


//显示/隐藏发送信息按钮
function toggle_send_message() {
	if ($("#send_message").css("display") == 'none') {
		show_send_message();
	} else {
		close_send_message();
	}
}

//显示发送信息按钮
function show_send_message() {
	$('#send_message').fadeIn(100);
}

//关闭发送信息按钮
function close_send_message() {
	$('#send_message').fadeOut(100);
}

//隐藏发送信息按扭组
function hidden_send_message_group() {
	$('#send_message').fadeOut(100);
	$('#show_send').fadeOut(100);
}

//点赞
function do_thumb_up(div) {
	//获取textid
	var id = $(div).prev().children('#text_id').html();
	$.ajax({
		type : "POST",
		url : path_name + "/thumbup.do",
		data : {
			text_id : id
		},
		cache : false,
		success : function(data) {
			thumb_up_success(div, id);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("上传失败，请检查网络后重试");
		}
	});
}

//点赞成功
function thumb_up_success(div, id) {
	var icon_class = $(div).children('#thumb_up_icon').attr('class');
	var thumb_up_num = parseInt($(div).children('#thumb_up_num').html());
	if (icon_class == 'no_thumb_up') {
		//切换点赞图标
		$(div).children('#thumb_up_icon').removeClass('no_thumb_up');
		$(div).children('#thumb_up_icon').addClass('already_thumb_up');
	} else {
		//切换点赞图标
		$(div).children('#thumb_up_icon').removeClass('already_thumb_up');
		$(div).children('#thumb_up_icon').addClass('no_thumb_up');
	}
	//刷新点赞列表
	flush_thumb_up_list(div, id);
}

//显示点赞列表
function show_thumb_up_list(thumb_up_div) {
	$(thumb_up_div).children('#thumb_up_list').removeClass('display-none');
}

//隐藏点赞列表
function hideen_thumb_up_list(thumb_up_div) {
	$(thumb_up_div).children('#thumb_up_list').addClass('display-none');
}

//刷新点赞
function flush_thumb_up_list(div, id) {
	$.ajax({
		type : "POST",
		url : path_name + "/flushThumbUp.do",
		data : {
			text_id : id
		},
		cache : false,
		success : function(msg) {
			//点赞动画
			thumb_up_animation(div);
			var thumb_up_num = 0;
			//刷新点赞用户
			$(div).children('#thumb_up_list').html(function() {
				//遍历json
				var rst = '';
				for (var user in msg) {
					rst += msg[user] + '\n';
					++thumb_up_num;
				}
				rst += "<br>赞过!"
				return rst;
			});
			//刷新点赞个数
			$(div).children('#thumb_up_num').html(thumb_up_num);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("读取数据失败，请检查网络后重试");
		}
	});

}

function thumb_up_animation(div) {
	//TODO
	//	var icon = $(div).children('#thumb_up_icon');
	//	var x = 100;
	//	var y = 900;
	//	var num = Math.floor(Math.random() * 3 + 1);
	//	var index = $(icon).length;
	//	var rand = parseInt(Math.random() * (x - y + 1) + y);
	//
	//	$(icon).append("<img src=''>");
	//	$('img:eq(' + index + ')').attr('src', 'images/' + num + '.png')
	//	$("img").animate({
	//		bottom : "800px",
	//		opacity : "0",
	//		left : rand,
	//	}, 3000)
}