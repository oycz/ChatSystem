var chart_data;
var message_frequencies;
var days_options = {
	onAnimationComplete : show_days_info
}

var units_options = {
	//Function - Fires when the animation is complete
	onAnimationComplete : show_units_info
}

var conversations_options = {
	onAnimationComplete : show_conversations_info
}

var socials_options = {
	onAnimationComplete : show_socials_info
}

var frequencies_options = {
	onAnimationComplete : show_frequencies_info
}

var chart_status = -1;
var units_name = [ '聊天室', '好友动态', '休闲一下', '我与悦聊' ];

function change_chart() {
	$('#statistics').attr('onclick', '');
	if (chart_data == undefined) {
		$.ajax({
			type : "POST",
			url : path_name + "/getStatistics.do",
			data : {
			},
			cache : false,
			async : false,
			success : function(msg) {
				chart_data = msg;
			},
			error : function(XMLHttpRequest, textchart_status, errorThrown) {
				alert("上传失败，请检查网络后重试");
			}
		});
	}
	if (chart_status == -1) {
		//days
		var days_data = [
			{
				label : '未登陆',
				value : chart_data['register_days'] - chart_data['login_days'],
				color : "#00FF7F"
			},
			{
				label : '登陆',
				value : chart_data['login_days'],
				color : "#00BFFF"
			}
		];

		var days_info = '<small>您已经注册<h2>' + chart_data['register_days']
			+ '天,</h2>在其中,您有<h2>' + chart_data['login_days'] + '天</h2>在登陆悦聊<br/>它已经成为了您生活的</small><h2>' +
			+(100 * (chart_data['login_days'] / chart_data['register_days'])).toFixed(0)
			+ '%</h2>';
		$('#charts_container').html('');
		$('#charts_container').append('<div id="days_chart_container"><canvas id="days_chart"></canvas><div id="days_info" class="display-none">' + days_info + '</div></div>');


		var ctx = document.getElementById("days_chart").getContext("2d");
		ctx.clearRect(0, 0, ctx.width, ctx.height);
		new Chart(ctx).Doughnut(days_data, days_options);
		++chart_status;
	} else if (chart_status == 0) {
		//units
		$('#charts_container').fadeOut(1000, function() {
			var units_count = chart_data['access_unit_count'];
			//			console.log(units_count.size());
			var max_unit = 0;
			for (var i in units_count) {
				if (units_count[i] > units_count[max_unit])
					max_unit = i;
			}
			var units_info = '<small>在悦聊中,您最喜欢的是</small><h2>' + units_name[max_unit] + '</h2><small>模块<br/>共访问了</small><h2>' + units_count[max_unit] + '</h2><small>次</small>';
			var units_data = {
				labels : units_name,
				datasets : [ {
					fillColor : "rgba(0,255,127,0.5)",
					strokeColor : "rgba(0,250,154,1)",
					data : [ units_count[0], units_count[1], units_count[2], units_count[3] ]
				} ]
			};
			$('#charts_container').html('');
			$('#charts_container').append('<div id="units_chart_container"><canvas id="units_chart"></canvas><div id="units_info" class="display-none">' + units_info + '</div></div>');
			$('#charts_container').css('display', 'block');


			var ctx = document.getElementById("units_chart").getContext("2d");
			ctx.clearRect(0, 0, ctx.width, ctx.height);
			new Chart(ctx).Bar(units_data, units_options);
			//			new Chart(ctx).Doughnut(days_data, days_options);
			++chart_status;
		});

	} else if (chart_status == 1) {
		//chats
		$('#charts_container').fadeOut(1000, function() {
			var friend_num = chart_data['friend_num'];
			var chat_message_num = chart_data['chat_message_num'];
			var chats_info = '<small>在与悦聊相伴的日子里,您共在聊天室给大家发了</small><h2>'
				+ chat_message_num
				+ '</h2><small>条消息,并交到了</small><h2>'
				+ friend_num
				+ '</h2><small>个朋友</small>';

			$('#charts_container').html('');
			$('#charts_container').append('<div id="chats_chart_container"><div id="chats_info" class="display-none">' + chats_info + '</div></div>');
			$('#charts_container').css('display', 'block');

			show_chats_info();

			++chart_status;
		});
	} else if (chart_status == 2) {
		//conversations
		var conversation_message_num = chart_data['conversation_message_num'];
		var conversation_max_name = chart_data['conversation_max_name'];
		var conversation_max_num = chart_data['conversation_max_num'];
		var conversation_info = '<small>您共与您的所有好友交流了</small><h2>'
			+ conversation_message_num
			+ '</h2><small>条消息<br/>其中,与您交流最多的好友是</small><h2>'
			+ conversation_max_name
			+ '</h2><small>您们共交流了</small><h2>' + conversation_max_num
			+ '</h2><small>条消息<br/>您与ta是否有一段不能言说的故事呢?</small>';
		$("#charts_container").fadeOut(1000, function() {


			var conversations_data = [ {
				label : '其他好友',
				value : conversation_message_num - conversation_max_num,
				color : "#E0E4CC"
			},
				{
					label : conversation_max_name,
					value : conversation_max_num,
					color : "#FF00FF"
				} ];

			$('#charts_container').html('');
			$('#charts_container').append('<div id="converdations_chart_container"><canvas id="conversations_chart"></canvas>'
				+ '<div id="conversations_info" class="display-none">'
				+ conversation_info
				+ '</div></div>');
			$('#charts_container').css('display', 'block');


			var ctx = document.getElementById("conversations_chart").getContext("2d");
			ctx.clearRect(0, 0, ctx.width, ctx.height);
			new Chart(ctx).Pie(conversations_data, conversations_options);

			++chart_status;
		});
	} else if (chart_status == 3) {
		//socials
		var social_message_num = chart_data['social_message_num'];
		var thumb_up_num = chart_data['thumb_up_num'];
		var send_thumb_up_num = chart_data['send_thumb_up_num'];
		var socials_info = '<small>您总共发了</small><h2>'
			+ social_message_num
			+ '</h2><small>条社交动态,并收到了</small><h2>'
			+ thumb_up_num
			+ '</h2><small>个赞<br/>而且,您还给别人的社交动态点了</small><h2>' + send_thumb_up_num
			+ '</h2><small>个赞!</small>';
		$("#charts_container").fadeOut(1000, function() {


			var socials_data = {
				labels : [ "发表动态", "给别人点赞", "收到点赞" ],
				datasets : [
					{
						fillColor : "rgba(175,238,238,0.5)",
						strokeColor : "rgba(0,206,209)",
						pointColor : "rgba(72,209,204,1)",
						pointStrokeColor : "#fff",
						data : [ social_message_num, thumb_up_num, send_thumb_up_num ]
					}
				]
			}
			$('#charts_container').html('');
			$('#charts_container').append('<div id="socials_chart_container"><canvas id="socials_chart"></canvas>'
				+ '<div id="socials_info" class="display-none">'
				+ socials_info
				+ '</div></div>');
			$('#charts_container').css('display', 'block');


			var ctx = document.getElementById("socials_chart").getContext("2d");
			ctx.clearRect(0, 0, ctx.width, ctx.height);
			new Chart(ctx).Radar(socials_data, socials_options);

			++chart_status;
		});
	} else if (chart_status == 4) {
		// frequencies
		if (message_frequencies == undefined) {
			$.ajax({
				type : "POST",
				url : path_name + "/wordFrequency.do",
				data : {
				},
				dataType : "json",
				cache : false,
				async : false,
				success : function(msg) {
					message_frequencies = msg;
				},
				error : function(XMLHttpRequest, textchart_status, errorThrown) {
					alert("上传失败，请检查网络后重试");
				}
			});
		}
		var frequencies_labels = [];
		var frequencies_values = [];
		for (var message in message_frequencies) {
			frequencies_labels.push(message);
			frequencies_values.push(message_frequencies[message]);
		}
		var frequencies_data = {
			labels : frequencies_labels,
			datasets : [
				{
					fillColor : "rgba(255,192,203,0.5)",
					strokeColor : "rgba(255,105,180,1)",
					pointColor : "rgba(199,21,133,1)",
					pointStrokeColor : "#fff",
					data : frequencies_values
				},
			]
		}
		var frequencies_info = "<h4>您的聊天关键词:发言中出现次数top10</h4>";


		$('#charts_container').fadeOut(1000, function() {
			$('#charts_container').html('');
			$('#charts_container').append('<div id="frequencies_chart_container"><canvas id="frequencies_chart"></canvas>'
				+ '<div id="frequencies_info" class="display-none">'
				+ frequencies_info
				+ '</div></div>');
			$('#charts_container').css('display', 'block');


			var ctx = document.getElementById("frequencies_chart").getContext("2d");
			ctx.clearRect(0, 0, ctx.width, ctx.height);new Chart(ctx).Line(frequencies_data, frequencies_options);
		});

		chart_status = -1;
	}
}


function show_days_info(info_id) {
	$('#days_info').fadeIn(1500, function() {
		$('#statistics').attr('onclick', 'change_chart();');
	});
}

function show_units_info() {
	$('#units_info').fadeIn(1500, function() {
		$('#statistics').attr('onclick', 'change_chart();');
	});
}

function show_chats_info() {
	$('#chats_info').fadeIn(1500, function() {
		$('#statistics').attr('onclick', 'change_chart();');
	});
}

function show_conversations_info() {
	$('#conversations_info').fadeIn(1500, function() {
		$('#statistics').attr('onclick', 'change_chart();');
	});
}

function show_socials_info() {
	$('#socials_info').fadeIn(1500, function() {
		$('#statistics').attr('onclick', 'change_chart();');
	});
}

function show_frequencies_info() {
	$('#frequencies_info').fadeIn(1500, function() {
		$('#statistics').attr('onclick', 'change_chart();');
	});
}