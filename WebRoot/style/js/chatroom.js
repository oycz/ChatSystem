var chatroom_left_status = 0;

//获取当前时间
function getNowFormatTime() {
	var date = new Date();
	var seperator1 = "-";
	var seperator2 = ":";
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	+ " " + date.getHours() + seperator2 + date.getMinutes()
	+ seperator2 + date.getSeconds();
	return currentdate;
}

function toggle_left(status) {
	if (chatroom_left_status == status)
		return;
	if (status == 0) {
		//显示好友列表
		$('#friendslist').removeClass('display-none');
		$('#online_users').addClass('display-none');
		$('#friendslist_button').addClass('display-selected');
		$('#online_users_button').removeClass('display-selected');
		chatroom_left_status = 0;
	} else if (status == 1) {
		//显示在线用户
		$('#online_users').removeClass('display-none');
		$('#friendslist').addClass('display-none');
		$('#online_users_button').addClass('display-selected');
		$('#friendslist_button').removeClass('display-selected');
		chatroom_left_status = 1;
	}
}

function chatroom_check_enter(event) {
	if ($("#check-box").is(':checked') == true) {
		//如果回车输入信息
		var currKey = window.event ? event.keyCode : event.which;
		if (currKey == 13) {
			return chatroom_send_message("enter");
		}
	}
	//字数统计
	chatroom_count_words();
}

function chatroom_send_message(isEnter) {
	var message = $('#chatroom_send_context').val();
	if (message.trim() == "" && !(isEnter == "enter")) {
		show_succeed("请输入内容");
	} else if (message.trim() == "" && isEnter == "enter") {

	} else {
		var windows = $('#chat_windows').children();
		var receive_user_name;
		for (i = 0; i < windows.length; ++i) {
			if (!$(windows[i]).hasClass('display-none')) {
				receive_user_name = $($('#chat_select').children()[i]).attr('user-name');
				console.log(receive_user_name);
			}
		}
		if ("聊天室" == receive_user_name) {
			// 群发信息
			$.ajax({
				type : "POST",
				url : path_name + "/chatroomSendMessage.do",
				data : {
					chat_message : message
				},
				cache : false,
				success : function(data) {
					//刷新信息
					listen_chatroom_message();
					//置空信息
					$('#chatroom_send_context').val('');
					chatroom_count_words();
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("上传失败，请检查网络后重试");
				}
			});
		} else {
			//私聊信息
			$.ajax({
				type : "POST",
				url : path_name + "/sendConversationMessage.do",
				dateType : "text",
				data : {
					chat_message : message,
					"receive_user_name" : receive_user_name
				},
				cache : false,
				success : function(data) {
					console.log(data);
					//将已发送信息添加到聊天窗体
					$('#chat_window_' + receive_user_name).append(data);
					//转换表情
					emoji_parse();
					//置空信息
					$('#chatroom_send_context').val('');
					chatroom_count_words();
					$('#chat_window_' + receive_user_name).scrollTop($('#chat_window_' + receive_user_name).prop("scrollHeight"));
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert("上传失败，请检查网络后重试");
				}
			});
		}
	}
	return false;
}

//字数统计
function chatroom_count_words() {
	$('#chatroom_send_context').val($('#chatroom_send_context').val().substr(0, 400));
	$("#chatroom_words_count").text($("#chatroom_send_context").val().length);
}

//添加好友
function send_add_friend(li) {
	var user_name = $(li).attr('user-name');
	$.ajax({
		type : "POST",
		url : path_name + "/sendFriendReq.do",
		data : {
			rececive_user_name : user_name
		},
		cache : false,
		success : function(data) {
			if (data == "0") {
				show_error("不能添加自己哦");
			} else if (data == "2") {
				show_succeed("你们已经是好友啦");
			} else if (data == "1") {
				show_succeed("好友请求已发送");
			} else {
				show_error("发送好友请求失败");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("发送好友请求失败");
		}
	});
}

function listen_friendship_req() {
	$.ajax({
		type : "POST",
		url : path_name + "/listenFriendshipReq.do",
		data : {
			imgPath : $("#updateUserIcon").val()
		},
		cache : false,
		success : function(msg) {
			//遍历json
			for (var user in msg) {
				show_friendship_confirm(msg[user]);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("监听好友请求失败");
		}
	});
}

function listen_chatroom_message() {
	// 监听聊天信息
	$.ajax({
		type : "POST",
		url : path_name + "/jsp/ajax/chat_messages.jsp",
		cache : false,
		success : function(msg) {
			if (msg.trim() != '') {
				$('#chat_window_chatroom').append(msg);
				$('#chat_window_chatroom').scrollTop($('#chat_window_chatroom').prop("scrollHeight"));
				//转换表情
				emoji_parse();
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("刷新聊天信息失败");
		}
	});
}

function listen_conversation_message() {
	// 监听私聊信息
	$.ajax({
		type : "POST",
		url : path_name + "/jsp/ajax/conversation_messages.jsp",
		cache : false,
		success : function(msg) {
			//检查是否有新信息
			if (msg.trim() != '') {
				// 有新信息
				var msgs = msg.split('</message-end>');
				for (i = 0; i < msgs.length - 1; ++i) {
					var send_user_name = msgs[i].substring(
						msgs[i].indexOf('=') + 2,
						msgs[i].indexOf('%') - 1);
					//检查窗口是否打开
					var index = window.chat_windows[send_user_name];
					//窗口已打开
					if (index != undefined) {
						//将信息添加到窗口
						var id_loc_start = msgs[i].indexOf('%') + 1;
						$($('#chat_windows').children()[index]).append(msgs[i]);
						var msg_id = msgs[i].substring(msgs[i].indexOf("=", id_loc_start) + 2,
							msgs[i].indexOf("%", id_loc_start) - 1);
						//转换表情
						emoji_parse();
						//标记信息为已读
						set_conversation_message_visited(msg_id);
						//滚动聊天窗口
						$($('#chat_windows').children()[index]).scrollTop($($('#chat_windows').children()[index]).prop("scrollHeight"));
						if (window.current_window_index != index) {
							//当前窗体不是信息窗体,在聊天窗体标签上添加小红点
							var selects = $('#chat_select').children();
							if ($(selects[index]).html().trim().indexOf('<span class="red_notice"></span>') < 0) {
								$(selects[index]).append('<span class="red_notice"></span>');
							}
						}
					} else {
						//窗体没有打开,在好友列表好友项里添加小红点
						var friends = $('#chatroom_friendslist').children();
						if (window.friendslist[send_user_name] != undefined && $(friends[window.friendslist[send_user_name]]).html().trim().indexOf('<span class="red_notice"></span>') < 0) {
							$(friends[window.friendslist[send_user_name]]).append('<span class="red_notice"></span>');
						}
					}
				}
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("刷新私聊信息失败");
		}
	});

}

function set_conversation_message_visited(msg_id) {
	//  设置私聊消息已看过
	$.ajax({
		type : "POST",
		url : path_name + "/setConversationMessageVisited.do",
		cache : false,
		data : {
			id : msg_id
		},
		success : function(msg) {},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("刷新聊天信息失败");
		}
	});
}

//返回userName聊天窗口的下标，无则返回-1
function user_window_index(user) {
	var selects = $('#chat_select').children();
	for (i = 0; i < selects.length; ++i) {
		if ($(selects[i].attr('user-name')) == user)
			return i;
	}
	return -1;
}

function flush_online_user() {
	$.ajax({
		type : "POST",
		url : path_name + "/jsp/ajax/onlines.jsp",
		data : {
		},
		cache : false,
		success : function(msg) {
			if (msg != $('#online_users').html()) {
				$('#online_users').html(msg);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("刷新在线用户失败");
		}
	});
}

function flush_friends_list() {
	$.ajax({
		type : "POST",
		url : path_name + "/jsp/ajax/friendslist.jsp",
		data : {
		},
		cache : false,
		success : function(msg) {
			if (msg != '' && msg.trim() != $('#friendslist').html().trim()) {
				$('#friendslist').html(msg);
				change_friendslist_dictionary();
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("刷新好友失败");
		}
	});
}

function chat(friend_name, li) {
	// 私聊
	var name = $(li).attr('user-name');
	var window_exist = false;
	var selects = $('#chat_select').children();
	// 检查窗口是否存在
	console.log(window.chat_windows[name]);
	if (window.chat_windows[name] != undefined)
		window_exist = true;
	// 不存在则创建
	if (!window_exist) {
		var str_friend_name = '"' + friend_name + '"';
		var str_name = '"' + name + '"';
		$('#chat_select').append(
			"<li user-name="
			+ name + "><a>"
			+ friend_name
			+ "</a><span class='close_window_button' onclick='chatroom_close_window(" + str_name + ")'></span></li>");
		$('#chat_windows').append(
			'<div class="chat_window display-none" id="chat_window_'
			+ name + '">'
			+ '<ul id="message_list_'
			+ name + '">'
			+ '</ul>'
			+ '</div>'
		);
		console.log($('#chat_select').children.length - 1);
		var new_select = $('#chat_select').children()[$('#chat_select').children().length - 1];
		$(new_select).attr("onclick", "active_chat_window(" + str_name + ", true)");
		console.log(new_select);
		change_windows_dictionary();
	}
	active_chat_window(name, window_exist);
}

function change_windows_dictionary() {
	window.chat_windows = {};
	var selects = $('#chat_select').children();
	for (i = 0; i < selects.length; ++i) {
		var user = $(selects[i]).attr('user-name');
		window.chat_windows[user] = i;
	}
}


function change_friendslist_dictionary() {
	var friends = $('#chatroom_friendslist').children();
	for (i = 0; i < friends.length; ++i) {
		var friend = $(friends[i]).attr('user-name');
		window.friendslist[friend] = i;
	}
}

function active_chat_window(friend_name, window_exist) {
	var selects = $('#chat_select').children();
	var windows = $('#chat_windows').children();
	if (friend_name == 0) {
		//聊天室窗口
		for (i = 0; i < $('#chat_select').children().length; ++i) {
			$(selects[i]).removeClass('active');
			$(windows[i]).addClass('display-none');
		}
		$(selects[0]).addClass('active');
		$(windows[0]).removeClass('display-none');
		window.current_window_index = 0;
	} else {
		// 切换到聊天窗口
		var friends = $('#chatroom_friendslist').children();
		var window_num = 0;
		// 去除所有标签的点击
		for (i = 0; i < $('#chat_select').children().length; ++i) {
			$(selects[i]).removeClass('active');
			$(windows[i]).addClass('display-none');
			if ($(selects[i]).attr('user-name') == friend_name) {
				window_num = i;
			}
		}
		// 切换到指定窗口
		$(selects[window_num]).addClass('active');
		$(windows[window_num]).removeClass('display-none');
		window.current_window_index = window_num;
		//消除好友列表/窗口标签上的小红点
		if (window_exist == true) {
			//消除窗口标签的小红点
			remove_select_notice(friend_name);
		} else {
			//消除好友列表条目上的小红点
			remove_friendlist_notice(friend_name);
		}
	}
}

function remove_select_notice(friend_name) {
	//	$($('#chat_select').children()[window.chat_windows[friend_name]]).remove(".red_notice");
	var select = $('#chat_select').children()[window.chat_windows[friend_name]];
	$(select).html(remove_sub_str($(select).html(), '<span class="red_notice"></span>'));
}

function remove_friendlist_notice(friend_name) {
	//	$($('#chatroom_friendslist').children()[window.friendslist[friend_name]]).remove(".red_notice");
	//	$($('#chatroom_friendslist').children()[window.friendslist[friend_name]]).html(friend_name);
	var friendlist = $('#chatroom_friendslist').children()[window.friendslist[friend_name]];
	$(friendlist).html(remove_sub_str($(friendlist).html(), '<span class="red_notice"></span>'));
}

function remove_sub_str(str, sub_str) {
	return str.replace('<span class="red_notice"></span>', "");
}

function chatroom_close_window(name) {
	var index = window.chat_windows[name];
	console.log(index);
	$('#chat_select')[0].removeChild($('#chat_select').children()[index]);
	$('#chat_windows')[0].removeChild($('#chat_windows').children()[index]);
	change_windows_dictionary();
}