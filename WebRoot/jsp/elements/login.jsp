<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div id="register_succeed" class="display-none">
	<span id="register_succeed_info">注册成功,3秒后跳转……</span>
</div>
<div class="index_notice text-center">
	<h1>
		悦 聊<br /> <small style="font-size: 15px;">开启美妙时光</small>
	</h1>
</div>
<div class="index_form">
	<div id="show-selected"></div>
	<div id="login_button" class="display-selected float-left"
		onmouseover="animate_login(0)">登陆</div>
	<div id="register_button" class="float-left"
		onmouseover="animate_login(1)">注册</div>
	<div id="index_login">
		<form method="post" id="login_form">
			<ul id="login_list" class="text-center">
				<li><input type="text" style="display:none;" /> <input
					id="login_info" required type="text" placeholder="用户名/邮箱"
					oninput="check_complete(0);" onblur="check_username_or_email();" />
					<label id="login_error_info" class="display-none">用户名或邮箱不存在</label></li>
				<li><input type="password" style="display:none;" /><input
					id="login_password" required type="password" placeholder="密码"
					oninput="check_complete(0);" onblur="check_pwd(0);" /><label
					id="login_error_password" class="display-none">密码错误</label></li>
				<li><button id="login_submit" type="button"
						class="submit_button" onclick="check_login()" disabled="disabled">登陆</button></li>
			</ul>
		</form>
	</div>
	<div id="index_register" class="display-none">
		<form method="post">
			<ul id="register_list" class="text-center">
				<li><input id="register_email" type="text" placeholder="邮箱"
					oninput="check_complete(1);" onblur="check_email();" /><label
					id="register_error_email" class="display-none">该邮箱已被注册</label></li>
				<li><input id="register_name" type="text" placeholder="用户名"
					oninput="check_complete(1);" onblur="check_username();" /><label
					id="register_error_name" class="display-none">该用户名已被注册</label></li>
				<li><input id="register_password" type="password"
					placeholder="密码" oninput="check_complete(1);"
					onblur="check_pwd(1);" /></li>
				<li><input id="register_confirm" type="password"
					placeholder="确认密码" oninput="check_complete(1);" /><label
					id="register_error_confirm" class="display-none">两次密码不一致</label></li>
				<li><button id="register_submit" type="button"
						class="submit_button" onclick="do_register()" disabled="disabled">加入我们</button></li>
			</ul>
		</form>
	</div>
</div>