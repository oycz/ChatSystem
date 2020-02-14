var recreation_function = 0;

//动态加载一个js或css文件
function loadjscssfile(filename, filetype) {
	if (filetype == "js") {
		var fileref = document.createElement('script');
		fileref.setAttribute("type", "text/javascript");
		fileref.setAttribute("src", filename);
	} else if (filetype == "css") {
		var fileref = document.createElement("link");
		fileref.setAttribute("rel", "stylesheet");
		fileref.setAttribute("type", "text/css");
		fileref.setAttribute("href", filename);
	}
	if (typeof fileref != "undefined")
		document.getElementsByTagName("head")[0].appendChild(fileref);
}

function removejscssfile(filename, filetype) {
	var targetelement = (filetype == "js") ? "script" : (filetype == "css") ? "link" : "none";
	var targetattr = (filetype == "js") ? "src" : (filetype == "css") ? "href" : "none";
	var allsuspects = document.getElementsByTagName(targetelement);
	for (var i = allsuspects.length; i >= 0; i--) {
		if (allsuspects[i] && allsuspects[i].getAttribute(targetattr) != null && allsuspects[i].getAttribute(targetattr).indexOf(filename) != -1)
			allsuspects[i].parentNode.removeChild(allsuspects[i]);
	}
}

//获取项目地址
function getRootPath() {
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp  
	var curWwwPath = window.document.location.href;
	//获取主机地址之后的目录，如： /uimcardprj/share/meun.jsp  
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8083  
	var localhostPaht = curWwwPath.substring(0, pos);
	//获取带"/"的项目名，如：/uimcardprj  
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht + projectName);

	window.document.location.href.substring(0, window.document.location.href.indexOf(pathName)) + window.document.location.pathname.substring(0, window.document.location.pathname.substr(1).indexOf('/') + 1);
}

window.path_name = getRootPath();

function animate_login(num) {
	var bar = document.getElementById("show-selected");
	//滑条位置
	if (typeof bar_status == "undefined") {
		bar_status = 0;
	}
	if (num == 0) {
		if (bar_status != 0) {
			//滑倒左边
			$(bar).animate({
				left : "0px"
			}, 400, "swing");
			$("#index_register").addClass("display-none");
			$("#index_login").removeClass("display-none");
			$("#login_button").addClass("display-selected");
			$("#register_button").removeClass("display-selected");
			bar_status = 0;
		}
	} else if (num == 1) {
		if (bar_status != 1) {
			//滑倒右边
			$(bar).animate({
				left : "150px"
			}, 400, "swing");
			$("#index_login").addClass("display-none");
			$("#index_register").removeClass("display-none");
			$("#register_button").addClass("display-selected");
			$("#login_button").removeClass("display-selected");
			bar_status = 1;
		}
	}
}

//登陆验证(手动登陆)
function check_login() {
	//清除错误信息
	$("#login_error_password").addClass("display-none");
	$("#login_error_info").addClass("display-none");

	//ajax
	var xmlhttp;
	if (window.XMLHttpRequest) { //IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else { //IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	var login_info = document.getElementById("login_info").value;
	var password = document.getElementById("login_password").value;
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var login_status = xmlhttp.responseText;
			if (login_status == "1") {
				// 登陆成功
				// 渐隐登陆界面
				$(".index_container").fadeOut(1000, login_success);
			} else if (login_status == "2") {
				// 密码错误
				$("#login_error_password").removeClass("display-none");
			} else if (login_status == "4") {
				show_error("该用户已在线!");
			} else {
				// 用户名不存在
				$("#login_error_info").removeClass("display-none");
			}
		}
	}
	xmlhttp.open("POST", path_name + "/loginCheck.do", true);
	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlhttp.send("login_info=" + login_info + "&password=" + password);
}

function log_login() {
}

//登陆(注册后自动登陆)
function login(login_info, password) {
	var xmlhttp;
	if (window.XMLHttpRequest) { //IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else { //IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			$(".index_container").fadeOut(1000, login_success);
			//注册表单验证
			email_success = false;
			username_success = false;
			register_pwd_success = false;
		}
	}
	xmlhttp.open("POST", path_name + "/loginCheck.do", true);
	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlhttp.send("login_info=" + login_info + "&password=" + password);
}


//注销登录
function checkout() {
	//ajax
	var xmlhttp;
	if (window.XMLHttpRequest) { //IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else { //IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			if (typeof (search_interval) != 'undefined') {
				if (search_interval != null) {
					clearInterval(search_interval);
					search_interval = null
				}
			}
			if (typeof (wait_interval) != 'undefined') {
				if (wait_interval != null) {
					clearInterval(wait_interval);
					wait_interval = null
				}
			}
			$("#home_page").fadeOut(1000, checkout_success);
		}
	}
	xmlhttp.open("GET", path_name + "/checkout.do", true);
	xmlhttp.send();
}

//登陆成功
function login_success() {
	//登陆表单验证
	info_success = false;
	login_pwd_success = false;

	//增加窗口字典
	window.chat_windows = new Object();
	change_windows_dictionary();
	//增加好友列表字典
	window.friendslist = new Object();
	//增加当前窗口
	window.current_window_index = 0;
	change_friendslist_dictionary();
	//剔除登陆界面
	$(".index_container").remove();
	//导入主界面
	$("#index_bg").after("<div id='home_page'></div>");
	$("#home_page").css("display", "none");
	$("#home_page").load(path_name + "/jsp/elements/home_page.jsp");
	//渐显主界面
	$("#home_page").fadeIn(1000, function() {
		// 使用回调函数读取表情
		init_emoji();
	});

}

//注销成功
function checkout_success() {
	//剔除窗口字典
	window.chat_windows = null;
	//剔除好友列表字典
	window.friendslist = null;
	//剔除数据
	chart_data = null;
	message_frequencies = null;
	//剔除主界面
	$("#home_page").remove();
	//按钮位置
	bar_status = 0;
	//导入登陆界面
	$("#index_bg").after("<div class='index_container'></div>");
	$(".index_container").css("display", "none");
	$(".index_container").load(path_name + "/jsp/elements/login.jsp");
	//渐显登陆界面
	$(".index_container").fadeIn(1000, function() {
		check_form_when_flush();
	});

}

//登陆/注册按钮启用/禁用
function check_complete(num) {
	if (num == 0) {
		//登陆表单是否完成
		if ($("#login_info").val() == "" || $("#login_password").val() == "") {
			button_disabled("login_submit");
		} else {
			if (info_success && login_pwd_success) {
				button_abled("login_submit");
			}
		}
	} else if (num == 1) {
		//注册表单是否完成&&两次密码是否相同
		if ($("#register_email").val() == "" || $("#register_name").val() == "" || $("#register_password").val() == "" || $("#register_confirm").val() == "") {
			//注册表单没填全
			button_disabled("register_submit");
			if ($("#register_password").val() != $("#register_confirm").val()) {
				//两次密码不一致
				$("#register_error_confirm").removeClass("display-none");
			} else {
				//两次密码一致
				$("#register_error_confirm").addClass("display-none");
			}
		} else {
			if ($("#register_password").val() != $("#register_confirm").val()) {
				//两次密码不一致
				button_disabled("register_submit");
				$("#register_error_confirm").removeClass("display-none");
			} else {
				console.log('email_success:' + email_success + '...username_success:' + username_success + '...register_pwd_success:' + register_pwd_success);
				//两次密码一致
				$("#register_error_confirm").addClass("display-none");

				if (email_success && username_success && register_pwd_success) {
					button_abled("register_submit");
				}
			}

		}
	}
}

function check_username_or_email() {
	var login_info = $("#login_info").val();
	if (login_info != '') {
		if (login_info.indexOf('@') != -1) {
			//验证邮箱
			var qualified_email = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(login_info);
			if (!qualified_email) {
				show_error('请输入正确的邮箱');
				info_success = false;
			} else if (login_info.length > 20) {
				show_error('邮箱长度在20位以下');
				info_success = false;
			} else {
				info_success = true;
			}
		} else {
			//验证用户名
			var qualified_username = /^[a-zA-Z][a-zA-Z0-9]{5,15}$/.test(login_info);
			if (!qualified_username) {
				show_error('请输入以字母开头,6-15位只包含字母与数字的用户名');
				info_success = false;
			} else {
				info_success = true;
			}
		}
	} else {
		info_success = false;
	}
	check_complete(0);
}

function check_pwd(num) {
	var pwd;
	if (num == 0) {
		//登陆验证
		pwd = $("#login_password").val();
	} else if (num == 1) {
		//注册验证1
		pwd = $("#register_password").val();
	}
	if (pwd != '') {
		if (!/^[a-zA-Z0-9]{6,15}$/.test(pwd)) {
			//密码错误
			show_error('请输入6-15位由字母和数字组成的密码');
			if (num == 0) {
				login_pwd_success = false;
			} else {
				register_pwd_success = false;
			}
		} else {
			if (num == 0) {
				login_pwd_success = true;
			} else {
				register_pwd_success = true;
			}
		}
	} else {
		if (num == 0) {
			login_pwd_success = false;
		} else {
			register_pwd_success = false;
		}
	}
	check_complete(num);
}

function check_username() {
	var username = $("#register_name").val();
	if (username != '') {
		if (!/^[a-zA-Z][a-zA-Z0-9]{5,15}$/.test(username)) {
			//用户名错误
			show_error('请输入以字母开头,6-15位只包含字母与数字的用户名');
			username_success = false;
		} else {
			username_success = true;
		}
	} else {
		username_success = false;
	}
	check_complete(1);
}

function check_email() {
	var email = $("#register_email").val();
	//验证邮箱
	if (email != '') {
		var qualified_email = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(email);
		if (!qualified_email) {
			show_error('请输入正确的邮箱');
			email_success = false;
		} else if (email.length > 20) {
			show_error('邮箱长度在20位以下');
			email_success = false;
		} else {
			//成功
			email_success = true;
		}
	} else {
		email_success = false;
	}
	check_complete(1);
}

function button_disabled(button_id) {
	$("#" + button_id).attr({
		"disabled" : "disabled"
	});
	$("#" + button_id).css("background-color", "");
}

function button_abled(button_id) {
	$("#" + button_id).removeAttr("disabled");
	$("#" + button_id).css("background-color", "#addfdf");
}

//进行注册
function do_register() {
	//清除所有提示信息
	$("#register_error_email").addClass("display-none");
	$("#register_error_name").addClass("display-none");

	//ajax
	var xmlhttp;
	if (window.XMLHttpRequest) { //IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp = new XMLHttpRequest();
	} else { //IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	var register_email = document.getElementById("register_email").value;
	var register_name = document.getElementById("register_name").value;
	var register_password = document.getElementById("register_password").value;
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var admit_login = true;
			var register_status = xmlhttp.responseText;
			//2:register_error_email 1:register_error_name
			if (register_status == 4) {
				//添加失败
				alert("注册失败，请稍后再试。");
				admit_login = false;
			} else {
				//*1*表示邮箱已被注册
				if (register_status - 2 >= 0) {
					$("#register_error_email").removeClass("display-none");
					register_status -= 2;
					admit_login = false;
				}
				//**1表示用户名已被注册
				if (register_status - 1 >= 0) {
					$("#register_error_name").removeClass("display-none");
					admit_login = false;
				}
			}
			if (admit_login) {
				//注册成功
				//利用闭包实现回调函数传参
				$("#register_succeed").fadeIn(500,
					function() {
						//完全显示后等待三秒再跳转
						var i = 3;
						var login_interval = setInterval(function() {
							--i;
							$("#register_succeed_info").html("注册成功," + i + "秒后跳转……");
							if (i == 0) {
								clearInterval(login_interval);
								login(register_name, register_password);
							}
						}, 1000);
					});
			}
		}
	}
	xmlhttp.open("POST", path_name + "/register.do", true);
	xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlhttp.send("register_email=" + register_email + "&register_name=" + register_name + "&register_password=" + register_password);
}

//滑动栏目滚动条
function move_scroll(num) {
	var i = 0;
	set_unit_button_disabled();
	//去除所有按钮效果
	$("#chatroom_button").removeClass("active");
	$("#sociality_button").removeClass("active");
	$("#recreation_button").removeClass("active");
	$("#statistics_button").removeClass("active");
	//滚动卷轴并添加按钮效果
	if (num == 0) {
		//聊天室
		//发表动态按钮
		$(".homepage_page").animate({
			"right" : "0%"
		}, 1000, "swing", function() {
			if (i == 0) {
				//(可能要做)聊天室回调函数
				buttons_after_click_func();
			}
			i++;
		});
		$("#chatroom_button").addClass("active");
	} else if (num == 1) {
		//交际圈
		$(".homepage_page").animate({
			"right" : "25%"
		}, 1000, "swing", function() {
			//(可能要做)社交回调函数
			//发送状态按钮
			if (i == 0) {
				$('#show_send').fadeIn(300);
				set_unit_button_abled();
			}
			i++;
		});
		$("#sociality_button").addClass("active");
		//社交模块初始化
		social_init();
	} else if (num == 2) {
		//天黑请闭眼
		//发表动态按钮
		$(".homepage_page").animate({
			"right" : "50%"
		}, 1000, "swing", function() {
			//(可能要做)娱乐回调函数
			if (i == 0) {
				//				init_chess();
				if (recreation_function == 0) {
					buttons_after_click_func();
					load_chess();
				}
			}
			i++;
			recreation_function = 0;
		});
		$("#recreation_button").addClass("active");
	} else if (num == 3) {
		//我与悦聊
		//发表动态按钮
		$(".homepage_page").animate({
			"right" : "75%"
		}, 1000, "swing", function() {
			//(可能要做)我与悦聊回调函数
			if (i == 0) {
				buttons_after_click_func();
				change_chart();
			}
			i++;
		});
		$("#statistics_button").addClass("active");
	}
	log_access_unit(num);
}

//回调函数通用代码
function buttons_after_click_func() {
	hidden_send_message_group();
	set_unit_button_abled();
}

function set_unit_button_disabled() {
	var unit_buttons = $('#homepage_navi').children();
	for (var i = 0; i < unit_buttons.length; ++i) {
		$(unit_buttons[i]).attr('onclick', '');
	}
}

function set_unit_button_abled() {
	var unit_buttons = $('#homepage_navi').children();
	for (var i = 0; i < unit_buttons.length; ++i) {
		$(unit_buttons[i]).attr('onclick', 'move_scroll(' + i + ')');
	}
}
function log_access_unit(unit_num) {
	//记录访问模块
	$.ajax({
		type : "POST",
		url : path_name + "/logAccessUnit.do",
		data : {
			'unit_num' : unit_num
		},
		cache : false,
		success : function(msg) {},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			show_error("log失败");
		}
	});
}

//显示用户菜单
function show_usermenu() {
	$("#user_menu").removeClass("display-none");
}

//隐藏用户菜单
function hidden_usermenu() {
	$("#user_menu").addClass("display-none");
}

//弹出用户设置
function show_settings() {
	$("#user_settings_page").load(path_name + "/jsp/elements/user_settings_page.jsp", function() {
		$("#user_settings_page").removeClass("display-none");
	});
}

//关闭用户设置
function close_settings() {
	$("#user_settings_page").html("");
	$("#user_settings_page").addClass("display-none");

}

//变更用户信息
function changeUserInfo() {
	if (userNickName == $('#nickname_input').value()) {
		alert("aaa");
	}
}

//上传头像
function updateUserIcon() {
	//判断是否有选择上传文件
	var imgPath = $("#update_usericon").val();
	if (imgPath == "") {
		show_error("请选择上传图片！");
		return;
	}
	//判断上传文件的后缀名
	var strExtension = imgPath.substr(imgPath.lastIndexOf('.') + 1);
	if (strExtension != 'jpg' && strExtension != 'jpeg' && strExtension != 'gif'
		&& strExtension != 'png' && strExtension != 'bmp') {
		show_error("请选择图片文件");
		return;
	}
	//	//TODO
	//	var obj_file = document.getElementById("id_file");
	//	var filesize = 0;
	//	fileSize = obj_file.files[0].size;
	//	if (fileSize > 512000) {
	//		alert("图片不能超过500kb!");
	//
	//		$("#id_file").val = "";
	//	} else {
	//		$.ajaxFileUpload({
	//			url : '<%=basePath%>sjtp/tempimg.do',
	//			//用于文件上传的服务器端请求地址
	//			secureuri : false, //一般设置为false
	//			fileElementId : 'id_file', //文件上传空间的id属性  <input type="file" id="id_file" name="file" />
	//			type : 'post',
	//			dataType : 'HTML', //返回值类型 一般设置为json
	//			success : function(data, status) //服务器成功响应处理函数
	//			{
	//				//这里每个人业务逻辑不一样,就不做实例了.
	//
	//			},
	//			error : function(data, status, e) //服务器响应失败处理函数
	//			{
	//				alert(e);
	//			}
	//		})
	//		return false;
	//	}

	console.log($('#upload_icon_form').serialize());
	var formData = new FormData($('#upload_icon_form')[0]);
	console.log($('#upload_icon_form')[0]);
	$.ajax({
		//		type : "POST",
		//		url : path_name + "/uploadUserIcon.do",
		//		data : $('#upload_icon_form').serialize(),
		//		cache : false,
		type : "POST",
		cache : false,
		url : path_name + "/uploadUserIcon.do",
		data : formData,
		processData : false,
		contentType : false,
		success : function(data) {
			show_succeed("上传成功");
			$("#update_usericon").empty();
		//			$("#user_icon").html(data);
		//			$("#user_icon").show();
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			show_error("上传失败，请检查网络后重试");
		}
	});
}

function show_succeed(message) {
	//	$('#send_succeed').fadeIn(300);
	//	setTimeout(function() {
	//		$('#send_succeed').fadeOut(300);
	//	}, 2000)
	iziToast.success({
		color : 'blue',
		icon : 'icon-person',
		message : message,
		pauseOnHover : false,
		timeout : 3000,
		position : 'center', // bottomRight, bottomLeft, topRight, topLeft, topCenter, bottomCenter
		progressBarColor : 'rgb(180, 180, 180)'
	});
}

function show_error(message) {
	iziToast.error({
		title : 'Error',
		message : message,
		pauseOnHover : false,
		position : 'center',
		transitionIn : 'fadeInDown'
	});
}

function show_friendship_confirm(user_name) {
	//确认按钮
	iziToast.show({
		title : 'Hey',
		icon : 'icon-drafts',
		message : '来自"' + user_name + '"的好友请求',
		position : 'bottomCenter',
		pauseOnHover : true,
		//image : 'img/avatar.jpg',
		balloon : true,
		buttons : [
			[ '<button>同意</button>', function(instance, toast) {

				$.ajax({
					type : "POST",
					url : path_name + "/addFriend.do",
					data : {
						friend_name : user_name
					},
					cache : false,
					success : function(msg) {
						$.ajax({
							type : "POST",
							url : path_name + "/jsp/ajax/friendslist.jsp",
							data : {
								imgPath : $("#updateUserIcon").val()
							},
							cache : false,
							success : function(msg) {
								if (msg != $('#friendslist').html()) {
									$('#friendslist').html(msg);
								}
							},
							error : function(XMLHttpRequest, textStatus, errorThrown) {
								console.log("添加好友失败");
							}
						});
						show_succeed("已添加" + user_name + "为好友");
						flush_friends_list();
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						show_error("添加失败");
					}
				});

			} ],
			[ '<button>拒绝</button>', function(instance, toast) {
				show_succeed("已拒绝");
			} ],
		]
	});

}

//check online or not
function check_online() {
	var status = 0;
	$.ajax({
		type : "POST",
		url : path_name + "/checkOnline.do",
		cache : false,
		//修改方法为同步否则返回undefined
		async : false,
		success : function(msg) {
			if (msg == "1")
				status = 1;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("监听请求失败");
		}
	});
	return status;
}

//监听函数
window.onload = function() {
	//监听好友请求
	setInterval(function() {
		if (check_online() == 1) {
			listen_friendship_req();
			//刷新好友列表
			flush_friends_list();
		}
	}, 5000);
	//监听聊天信息
	setInterval(function() {
		if (check_online() == 1) {
			listen_chatroom_message();
			listen_conversation_message();
		}
	}, 600);
	//监听在线用户
	setInterval(function() {
		if (check_online() == 1) {
			flush_online_user();
		}
	}, 5000);
	//增加窗口字典
	window.chat_windows = new Object();
	change_windows_dictionary();
	//增加好友列表字典
	window.friendslist = new Object();
	change_friendslist_dictionary();
	//添加当前窗口
	window.current_window_index = 0;

	//登录表单验证
	info_success = false;
	login_pwd_success = false;

	//注册表单验证
	email_success = false;
	username_success = false;
	register_pwd_success = false;

	//刷新时表单验证
	if ($('#login_list').children()[0] != undefined) {
		setTimeout('check_form_when_flush()', 300);
	}

	// init emoji
	init_emoji();
}

function change_user_info() {
	var nick_name = $('#nickname_input').val();
	var pwd = $('#pwd_input').val();
	var pwd_confirm = $('#pwd_confirm_input').val();
	if (pwd == '' && pwd_confirm == '') {
		$.ajax({
			type : "POST",
			url : path_name + "/changeUserInfo.do",
			data : {
				"nick_name" : nick_name
			},
			cache : false,
			success : function(data) {
				show_succeed("修改昵称成功");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				show_error("修改昵称失败,请稍后再试");
			}
		});
		close_settings();
	} else if (nick_name == '') {
		$.ajax({
			type : "POST",
			url : path_name + "/changeUserInfo.do",
			data : {
				"pwd" : pwd
			},
			cache : false,
			success : function(data) {
				show_succeed("修改密码成功");
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				show_error("修改密码失败,请稍后再试");
			}
		});
		close_settings();
	} else {
		if (pwd != pwd_confirm) {
			show_error("两次密码不同");
		} else if (pwd == pwd_confirm) {
			if (pwd.length < 6) {
				show_error('密码需大于六位!');
			} else {
				$.ajax({
					type : "POST",
					url : path_name + "/changeUserInfo.do",
					data : {
						"nick_name" : nick_name,
						"pwd" : pwd
					},
					cache : false,
					success : function(data) {
						show_succeed("修改昵称与密码成功");
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						show_error("修改昵称与密码失败,请稍后再试");
					}
				});
				close_settings();
			}
		}
	}
}

function check_form_when_flush() {
	check_username_or_email();
	check_pwd(0);
	check_pwd(1);
	check_username();
	check_email();
}

//5-29
function show_emoji() {
	//显示表情
	$("#chatroom_send_context").emoji('show');
}


function hide_emoji() {
	//显示表情
	$("#chatroom_send_context").emoji('hide');
}

function toggle_emoji() {
	//toggle emoji
	$("#chatroom_send_context").emoji('toggle');
}

function init_emoji() {
	//读取表情
	//	alert(path_name + "/style/imgs/tieba/");
	//	$("#chatroom_send_context").emoji({
	//		icons : [ {
	//			name : 'tieba_icon',
	//			path : path_name + "/style/imgs/tieba/",
	//			//			path : "../imgs/tieba/",
	//			file : ".jpg",
	//			placeholder : ":{alias}:",
	//			alias : {
	//				1 : "hehe",
	//				2 : "haha",
	//				3 : "tushe",
	//				4 : "a",
	//				5 : "ku",
	//				6 : "lu",
	//				7 : "kaixin",
	//				8 : "han",
	//				9 : "lei",
	//				10 : "heixian",
	//				11 : "bishi",
	//				12 : "bugaoxing",
	//				13 : "zhenbang",
	//				14 : "qian",
	//				15 : "yiwen",
	//				16 : "yinxian",
	//				17 : "tu",
	//				18 : "yi",
	//				19 : "weiqu",
	//				20 : "huaxin",
	//				21 : "hu",
	//				22 : "xiaonian",
	//				23 : "neng",
	//				24 : "taikaixin",
	//				25 : "huaji",
	//				26 : "mianqiang",
	//				27 : "kuanghan",
	//				28 : "guai",
	//				29 : "shuijiao",
	//				30 : "jinku",
	//				31 : "shengqi",
	//				32 : "jinya",
	//				33 : "pen",
	//				34 : "aixin",
	//				35 : "xinsui",
	//				36 : "meigui",
	//				37 : "liwu",
	//				38 : "caihong",
	//				39 : "xxyl",
	//				40 : "taiyang",
	//				41 : "qianbi",
	//				42 : "dnegpao",
	//				43 : "chabei",
	//				44 : "dangao",
	//				45 : "yinyue",
	//				46 : "haha2",
	//				47 : "shenli",
	//				48 : "damuzhi",
	//				49 : "ruo",
	//				50 : "OK"
	//			}
	//		} ]
	//	});

	$("#chatroom_send_context").emoji({
		showTab : true,
		animation : 'fade',
		button : '#emoji_btn',
		icons : [ {
			name : "贴吧表情",
			path : path_name + "/style/imgs/tieba/",
			maxNum : 50,
			file : ".jpg",
			placeholder : ":{alias}:",
			alias : {
				1 : "hehe",
				2 : "haha",
				3 : "tushe",
				4 : "a",
				5 : "ku",
				6 : "lu",
				7 : "kaixin",
				8 : "han",
				9 : "lei",
				10 : "heixian",
				11 : "bishi",
				12 : "bugaoxing",
				13 : "zhenbang",
				14 : "qian",
				15 : "yiwen",
				16 : "yinxian",
				17 : "tu",
				18 : "yi",
				19 : "weiqu",
				20 : "huaxin",
				21 : "hu",
				22 : "xiaonian",
				23 : "neng",
				24 : "taikaixin",
				25 : "huaji",
				26 : "mianqiang",
				27 : "kuanghan",
				28 : "guai",
				29 : "shuijiao",
				30 : "jinku",
				31 : "shengqi",
				32 : "jinya",
				33 : "pen",
				34 : "aixin",
				35 : "xinsui",
				36 : "meigui",
				37 : "liwu",
				38 : "caihong",
				39 : "xxyl",
				40 : "taiyang",
				41 : "qianbi",
				42 : "dnegpao",
				43 : "chabei",
				44 : "dangao",
				45 : "yinyue",
				46 : "haha2",
				47 : "shenli",
				48 : "damuzhi",
				49 : "ruo",
				50 : "OK"
			},
			title : {
				1 : "呵呵",
				2 : "哈哈",
				3 : "吐舌",
				4 : "啊",
				5 : "酷",
				6 : "怒",
				7 : "开心",
				8 : "汗",
				9 : "泪",
				10 : "黑线",
				11 : "鄙视",
				12 : "不高兴",
				13 : "真棒",
				14 : "钱",
				15 : "疑问",
				16 : "阴脸",
				17 : "吐",
				18 : "咦",
				19 : "委屈",
				20 : "花心",
				21 : "呼~",
				22 : "笑脸",
				23 : "冷",
				24 : "太开心",
				25 : "滑稽",
				26 : "勉强",
				27 : "狂汗",
				28 : "乖",
				29 : "睡觉",
				30 : "惊哭",
				31 : "生气",
				32 : "惊讶",
				33 : "喷",
				34 : "爱心",
				35 : "心碎",
				36 : "玫瑰",
				37 : "礼物",
				38 : "彩虹",
				39 : "星星月亮",
				40 : "太阳",
				41 : "钱币",
				42 : "灯泡",
				43 : "茶杯",
				44 : "蛋糕",
				45 : "音乐",
				46 : "haha",
				47 : "胜利",
				48 : "大拇指",
				49 : "弱",
				50 : "OK"
			}
		} ]
	});
}

//转换表情
function emoji_parse() {
	$('#chat_windows .chatmessage_content').emojiParse({
		icons : [ {
			name : "贴吧表情",
			path : path_name + "/style/imgs/tieba/",
			maxNum : 50,
			file : ".jpg",
			placeholder : ":{alias}:",
			alias : {
				1 : "hehe",
				2 : "haha",
				3 : "tushe",
				4 : "a",
				5 : "ku",
				6 : "lu",
				7 : "kaixin",
				8 : "han",
				9 : "lei",
				10 : "heixian",
				11 : "bishi",
				12 : "bugaoxing",
				13 : "zhenbang",
				14 : "qian",
				15 : "yiwen",
				16 : "yinxian",
				17 : "tu",
				18 : "yi",
				19 : "weiqu",
				20 : "huaxin",
				21 : "hu",
				22 : "xiaonian",
				23 : "neng",
				24 : "taikaixin",
				25 : "huaji",
				26 : "mianqiang",
				27 : "kuanghan",
				28 : "guai",
				29 : "shuijiao",
				30 : "jinku",
				31 : "shengqi",
				32 : "jinya",
				33 : "pen",
				34 : "aixin",
				35 : "xinsui",
				36 : "meigui",
				37 : "liwu",
				38 : "caihong",
				39 : "xxyl",
				40 : "taiyang",
				41 : "qianbi",
				42 : "dnegpao",
				43 : "chabei",
				44 : "dangao",
				45 : "yinyue",
				46 : "haha2",
				47 : "shenli",
				48 : "damuzhi",
				49 : "ruo",
				50 : "OK"
			}
		} ]
	});
}