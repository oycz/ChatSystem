/*
项目背景
@author ZP
*/

//画布长度
var BG_WIDTH = window.innerWidth;
//画布高度
var BG_HEIGHT = window.innerHeight;

//球信息初始化
//球半径
var BALL_RADIUS = 20;
//球半径波动
var BALL_RADIUS_FLUCTUATE = 10;
//球水平速度(正负)
var BALL_X_SPEED = 0.6;
//球水平速度波动
var BALL_X_SPEED_FLUCTUATE = 0.4;
//球垂直速度(正负)
var BALL_Y_SPEED = 0.6;
//球垂直速度波动
var BALL_Y_SPEED_FLUCTUATE = 0.4;
//球的个数
var BALL_NUM = 16;
//球颜色
//var BALL_COLOR = "61,120,120";
var BALL_COLORS = new Array('255,0,255', '139,0,139', '148,0,211', '138,43,226', '147,112,219', '230,230,250', '65,105,225', '30,144,255', '224,255,255', '0,206,209');
//球颜色个数
var BALL_COLORS_LENGTH = BALL_COLORS.length;
//球透明度
var BALL_TRANSPARENT = "0.6";
//新建球数组
var balls = new Array();

//线颜色
var LINE_COLOR = "199,199,199";
//线最大不透明度
var LINE_MAX_TRANSPARENT = "0.1";
//线宽度
var LINE_WIDTH = 50;
//获取画布
var bg = document.getElementById('index_bg');


//画布内容
bg.width = BG_WIDTH,
bg.height = BG_HEIGHT;
var context = bg.getContext('2d');

//根据上下限确定随机数
function range_random(min, max) {
	return (min + Math.round(Math.random() * (max - min)));
}

//同上,正负号各一半几率
function double_range_random(min, max) {
	if (Math.random() <= 0.5)
		return (min + Math.round(Math.random() * (max - min)));
	else
		return -(min + Math.round(Math.random() * (max - min)));
}

//定义球类
function Ball(x, y, radius, speed_x, speed_y) {
	//水平距离 垂直距离 半径 水平速度 垂直速度
	this.x = x;
	this.y = y;
	this.radius = radius;
	this.speed_x = speed_x;
	this.speed_y = speed_y;
	//球颜色
	this.color = BALL_COLORS[range_random(0, BALL_COLORS_LENGTH - 1)];
}

//初始化
function init() {
	//添加球
	for (var i = 0; i < BALL_NUM; ++i) {
		//水平距离 垂直距离 半径 水平速度 垂直速度
		balls.push(new Ball(
			range_random(0, BG_WIDTH),
			range_random(0, BG_HEIGHT),
			range_random(BALL_RADIUS - BALL_RADIUS_FLUCTUATE, BALL_RADIUS + BALL_RADIUS_FLUCTUATE),
			double_range_random(BALL_X_SPEED - BALL_X_SPEED_FLUCTUATE, BALL_X_SPEED + BALL_X_SPEED_FLUCTUATE),
			double_range_random(BALL_Y_SPEED - BALL_Y_SPEED_FLUCTUATE, BALL_Y_SPEED + BALL_Y_SPEED_FLUCTUATE)
		));
	}
	//线粗细
	context.strokeWidth = LINE_WIDTH;
	//设置动画
	setInterval(flush_canvas, 20);
}

//刷新画布
function flush_canvas() {
	//画出此帧
	draw_canvas();
	//计算下一帧
	next_location();
}

//计算球下一帧位置
function next_location() {
	for (var i = 0; i < balls.length; ++i) {
		//算出下一帧x和y
		var next_x = balls[i].x + balls[i].speed_x;
		var next_y = balls[i].y + balls[i].speed_y;
		//超出回退到另一端
		if (next_x > BG_WIDTH)
			next_x = 0 + (next_x - BG_WIDTH);
		else if (next_x < 0)
			next_x = BG_WIDTH + next_x;
		if (next_y > BG_HEIGHT)
			next_y = 0 + (next_y - BG_HEIGHT);
		else if (next_y < 0)
			next_y = BG_HEIGHT + next_y;
		balls[i].x = next_x;
		balls[i].y = next_y;
	}
}


//画出下一帧
function draw_canvas() {
	//清除画布
	context.clearRect(0, 0, bg.width, bg.height);
	//画出所有球
//	draw_lines();
	//画出所有球
	draw_balls();
}

function draw_lines() {
	//画出所有球之间的线
	for (var i = 0; i < balls.length; ++i) {
		for (var j = i + 1; j < balls.length; ++j) {
			var distance = Math.sqrt((balls[j].x - balls[i].x) * (balls[j].x - balls[i].x) + (balls[j].y - balls[i].y) * (balls[j].y - balls[i].y));
			//线越近越不透明
			var transparent = (97 / distance);
			context.beginPath();
			context.moveTo(balls[i].x, balls[i].y);
			context.lineTo(balls[j].x, balls[j].y);
			//context.closePath();
			context.strokeStyle = "rgba("
				+ LINE_COLOR
				+ ","
				//线的不透明度最高不超过最大不透明度
				+ (transparent > LINE_MAX_TRANSPARENT ? LINE_MAX_TRANSPARENT : transparent)
				+ ")";
			context.stroke();
		}
	}
}
function draw_balls() {
	for (var i = 0; i < balls.length; ++i) {
		//圆
		//球颜色
		context.fillStyle = "rgba(" + balls[i].color + "," + BALL_TRANSPARENT + ")";
		//console.log(context.fillStyle);
		context.beginPath();
		context.arc(balls[i].x, balls[i].y, balls[i].radius, 0, 2 * Math.PI);
		context.closePath();
		context.fill();
	//正方形
	//context.fillRect(balls[i].x, balls[i].y, balls[i].radius, balls[i].radius);
	//context.strokeRect(balls[i].x, balls[i].y, balls[i].radius, balls[i].radius);
	}
}

//初始化
init();