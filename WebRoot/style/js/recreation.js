/*
 * 五子棋
 * @author:ZP
 * */

var RED_CHESS_COLOR = '#EA0000';
var BLACK_CHESS_COLOR = '#3C3C3C';
var CHESS_LENGTH = 500;
var CHESS_BG_COLOR = 'rgba(120,120,120,0.2)';
var CHESS_LINE_COLOR = 'rgba(88,88,88,0.9)';
var CHESS_BLANK_NUM = 30;
var CHESS_EDGE = 10;

function init_chess(user_color) {
	$('#chess_container').fadeOut(1000, function() {
		$('#chess_container').html('');
		$('#chess_container').html('<canvas id="chess" width="500" height="500""></canvas>');
		$('#chess_container').fadeOut(1000);

		chesses = new Array(CHESS_BLANK_NUM);
		for (var x = 0; x < CHESS_BLANK_NUM; ++x) {
			chesses[x] = new Array(CHESS_BLANK_NUM);
			for (var y = 0; y < CHESS_BLANK_NUM; ++y)
				chesses[x][y] = -1;
		}

		start_wait_interval();

		var c = document.getElementById("chess");
		var cxt = c.getContext("2d");
		cxt.clearRect(0, 0, CHESS_LENGTH, CHESS_LENGTH);
		cxt.fillStyle = CHESS_BG_COLOR;
		cxt.fillRect(0, 0, CHESS_LENGTH, CHESS_LENGTH);
		cxt.lineWidth = 0.1;
		cxt.strokeStyle = CHESS_LINE_COLOR;
		for (i = 0; i < CHESS_BLANK_NUM; ++i) {
			cxt.moveTo(CHESS_EDGE + (i * CHESS_BLANK_NUM), CHESS_EDGE);
			cxt.lineTo(CHESS_EDGE + (i * CHESS_BLANK_NUM), CHESS_LENGTH - CHESS_EDGE);
			cxt.moveTo(CHESS_EDGE, CHESS_EDGE + (i * CHESS_BLANK_NUM));
			cxt.lineTo(CHESS_LENGTH - CHESS_EDGE, CHESS_EDGE + (i * CHESS_BLANK_NUM));
			cxt.stroke();
		}
		color = RED_CHESS_COLOR;
		$('#chess_container').fadeIn(1000);
		chess_loaded = true;
	});
}

//恢复游戏
function init_chess_have_user(json) {
	var ajax_chesses = json['playing_chess'];
	$('#chess_container').fadeOut(1000, function() {
		$('#chess_container').html('');
		$('#chess_container').html('<canvas id="chess" width="500" height="500""></canvas>');
		$('#chess_container').fadeOut(1000);

		chesses = new Array(CHESS_BLANK_NUM);
		for (var x = 0; x < CHESS_BLANK_NUM; ++x) {
			chesses[x] = new Array(CHESS_BLANK_NUM);
			for (var y = 0; y < CHESS_BLANK_NUM; ++y)
				chesses[x][y] = -1;
		}
		start_wait_interval();
		var c = document.getElementById("chess");
		var cxt = c.getContext("2d");
		cxt.clearRect(0, 0, CHESS_LENGTH, CHESS_LENGTH);
		cxt.fillStyle = CHESS_BG_COLOR;
		cxt.fillRect(0, 0, CHESS_LENGTH, CHESS_LENGTH);
		cxt.lineWidth = 0.1;
		cxt.strokeStyle = CHESS_LINE_COLOR;
		for (i = 0; i < CHESS_BLANK_NUM; ++i) {
			cxt.moveTo(CHESS_EDGE + (i * CHESS_BLANK_NUM), CHESS_EDGE);
			cxt.lineTo(CHESS_EDGE + (i * CHESS_BLANK_NUM), CHESS_LENGTH - CHESS_EDGE);
			cxt.moveTo(CHESS_EDGE, CHESS_EDGE + (i * CHESS_BLANK_NUM));
			cxt.lineTo(CHESS_LENGTH - CHESS_EDGE, CHESS_EDGE + (i * CHESS_BLANK_NUM));
			cxt.stroke();
		}
		//		color = (user_color == 0 ? BLACK_CHESS_COLOR : RED_CHESS_COLOR);

		for (var x = 0; x < 17; ++x) {
			for (var y = 0; y < 17; ++y)
				chesses[x][y] = ajax_chesses[x][y];
		}
		chess_loaded = true;
		draw_chesses();
		if (json['placing_color'] == json['user_color']) {
			color = json['user_color'] == '0' ? RED_CHESS_COLOR : BLACK_CHESS_COLOR;
		} else {
			color = json['user_color'] == '0' ? BLACK_CHESS_COLOR : RED_CHESS_COLOR;
		}

		$('#chess_container').fadeIn(1000);
	});
}

function start_wait_interval() {
	//轮询监听
	var noticed_40 = false;
	wait_interval = setInterval(function() {
		//更新棋盘
		$.ajax({
			type : "POST",
			url : path_name + "/userPlace.do",
			data : {
			},
			dataType : "json",
			cache : false,
			async : false,
			success : function(msg) {
				console.log(msg);
				if (msg['no_placed'] == '20' && !noticed_40) {
					show_succeed("请您在20秒内下出棋子,否则将视为逃跑!");
					noticed_40 = true;
				} else if (msg['no_placed'] == undefined) {
					noticed_40 = false;
				}
				if (msg['no_placed'] != undefined && msg['no_placed'] == '40') {
					show_succeed("太长时间不下棋,您已自动退出游戏!");
					clearInterval(wait_interval);
					clear_chess();
				} else if (msg['enemy_exited'] == '1') {
					show_succeed("对方已退出游戏,您赢了!");
					clearInterval(wait_interval);
					clear_chess();
				} else if (msg['losed'] != undefined) {
					show_succeed("您输了!再接再厉!");
					clearInterval(wait_interval);
					clear_chess();
				} else {
					if (msg['is_user'] == '0') {
						$('#chess').attr('onclick', '');
						if (msg['enemy_no_placed'] == '40') {
							show_succeed("对手因长时间不下棋已退出游戏,您赢了!");
							clearInterval(wait_interval);
							clear_chess();
						}
					} else if (msg['is_user'] == '1') {
						$('#chess').attr('onclick', 'chess_handler(event);');
						if (msg['y'] != undefined) {
							put_chess(parseInt(msg['y']), parseInt(msg['x']));
						}
					}
				}
			},
			error : function(XMLHttpRequest, textchart_status, errorThrown) {
				show_error("搜寻顺序失败");
			}
		});
	}, 500);
}

function chess_handler(event, tagName) {
	var event = event ? event : window.event;
	var elem = event.toElement ? event.toElement : event.relatedTarget;

	var c = document.getElementById("chess");
	var cxt = c.getContext("2d");
	//	cxt.beginPath();

	var x = parseInt((event.offsetX - 2.5) / CHESS_BLANK_NUM);
	var y = parseInt((event.offsetY - 2.5) / CHESS_BLANK_NUM);

	//	cxt.closePath();


	//	if (check(y, x, chesses) == 1)
	//		alert('Game over!' + color == RED_CHESS_COLOR ? 'Red' : 'Black' + 'wins!');

	$.ajax({
		type : "POST",
		url : path_name + "/playChess.do",
		data : {
			"chess_y" : y,
			"chess_x" : x
		},
		dataType : "text",
		cache : false,
		async : false,
		success : function(msg) {
			if (msg.trim() == '1') {
				put_chess(y, x);
			} else if (msg.trim() == '2') {
				put_chess(y, x);
				show_succeed('获胜!恭喜你!');
				clearInterval(wait_interval);
				clear_chess();
			}
		},
		error : function(XMLHttpRequest, textchart_status, errorThrown) {
			show_error("下棋失败");
		}
	});
}

//function check(y, x, chesses) {
//	var c;
//	if (color == RED_CHESS_COLOR)
//		c = 1;
//	else
//		c = 2;
//	var a = y,
//		b = x;
//	var count = 1;
//	while (chesses[--a][b] == c) {
//		if (++count == 5)
//			return 1;
//	}
//	a = y;
//	while (chesses[++a][b] == c) {
//		if (++count == 5)
//			return 1;
//	}
//	a = y;
//	count = 1;
//	while (chesses[a][--b] == c) {
//		if (++count == 5)
//			return 1;
//	}
//	b = x;
//	while (chesses[a][++b] == c) {
//		if (++count == 5)
//			return 1;
//	}
//	count = 1;
//	while (chesses[--a][--b] == c) {
//		if (++count == 5)
//			return 1;
//	}
//	a = y, b = x;
//	while (chesses[++a][++b] == c) {
//		if (++count == 5)
//			return 1;
//	}
//	count = 1;
//	while (chesses[--a][++b] == c) {
//		if (++count == 5)
//			return 1;
//	}
//	a = y, b = x;
//	while (chesses[++a][--b] == c) {
//		if (++count == 5)
//			return 1;
//	}
//	return 0;
//}

var chess_loaded = false;

//放置棋子
function put_chess(y, x, draw_color) {
	if (draw_color != undefined) {
		var str_draw_color;
		if (draw_color == '1') {
			str_draw_color = BLACK_CHESS_COLOR;
		} else if (draw_color == '0') {
			str_draw_color = RED_CHESS_COLOR;
		}
		var c = document.getElementById("chess");
		var cxt = c.getContext("2d");
		cxt.fillStyle = draw_color != undefined ? str_draw_color : color;
		cxt.beginPath();
		cxt.arc(CHESS_EDGE + CHESS_BLANK_NUM * x, CHESS_EDGE + CHESS_BLANK_NUM * y, 8, 0, Math.PI * 2, true);
		cxt.fill();
		cxt.closePath();
	} else if (chesses[y][x] == -1) {
		var c = document.getElementById("chess");
		var cxt = c.getContext("2d");
		cxt.fillStyle = draw_color != undefined ? str_draw_color : color;
		cxt.beginPath();
		cxt.arc(CHESS_EDGE + CHESS_BLANK_NUM * x, CHESS_EDGE + CHESS_BLANK_NUM * y, 8, 0, Math.PI * 2, true);
		cxt.fill();
		cxt.closePath();
		chesses[y][x] = (color == RED_CHESS_COLOR) ? 0 : 1;
		change_color();
	}
}

//改变颜色
function change_color() {
	if (color == RED_CHESS_COLOR)
		color = BLACK_CHESS_COLOR;
	else
		color = RED_CHESS_COLOR;
}


var search_interval = null;
//控制方法
function search_player() {
	//搜寻游戏
	if (search_interval == null) {
		search_interval = setInterval(function() {
			$.ajax({
				type : "POST",
				url : path_name + "/searchPlayer.do",
				data : {
				},
				dataType : "json",
				cache : false,
				async : false,
				success : function(msg) {
					if (msg['name'] != undefined) {
						clearInterval(search_interval);
						search_interval = null;
						chess_start(msg);
						show_succeed("您的游戏已经开始,您将对战" + msg['name'] + ",您" + (msg['color'] == 1 ? '后' : '先') + '手!');
					}
				},
				error : function(XMLHttpRequest, textchart_status, errorThrown) {
					show_error("搜寻玩家失败");
				}
			});
		}, 500);
	}
	show_succeed("正在为您搜寻游戏,请稍后...");
}

//chess开始
function chess_start(json) {
	init_chess(json["color"]);
}

//读取棋盘
function load_chess() {
	recreation_function++;
	var ajax_chesses;
	var game_id = -1;
	var json;
	if (!chess_loaded) {
		//ajax
		$.ajax({
			type : "POST",
			url : path_name + "/isPlaying.do",
			data : {
			},
			dataType : "json",
			cache : false,
			async : false,
			success : function(msg) {
				console.log(msg);
				json = msg;
				game_id = parseInt(msg['game_id']);
			},
			error : function(XMLHttpRequest, textchart_status, errorThrown) {
				show_error("检查是否正在游戏失败");
			}
		});
		if (game_id != -1) {
			init_chess_have_user(json);
		}
	}
}

function clear_chess() {
	$('#chess_container').fadeOut(1000, function() {
		$('#chess_container').html('<div id="chess_control"><h1>您尚未加入一场游戏</h1><button onclick="search_player();">为您的五子棋游戏搜寻对手!</button></div>');
		$('#chess_container').fadeIn(1000);

	})
}

function draw_chesses() {
	for (var i = 0; i < 17; ++i) {
		for (var j = 0; j < 17; ++j) {
			if (chesses[i][j] != -1) {
				put_chess(i, j, chesses[i][j]);
			}
		}
	}
}