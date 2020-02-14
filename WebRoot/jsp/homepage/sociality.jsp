<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div id="social_messages"></div>
<div id="show_send" onclick="toggle_send_message();"
	class="display-none">发表动态</div>
<div id="send_message" class="display-none">
	<textarea id="send_message_context" oninput="count_words();"
		maxlength="400" placeholder="请输入文字"></textarea>
	<div id="send_close_bar">
		<form enctype="multipart/form-data" id="upload_social_image_form">
			<a href="javascript:;" class="file">分享图片 <input type="file"
				name="file" id="upload_social_image"
				onchange="change_social_image();">
			</a> <input type="text" id="social_text_id_area"
				name="new_social_text_id" class="display-none" />
		</form>
		<div id="close_send_button" onclick="close_send_message();"></div>
	</div>
	<div id="send_button" onclick="send_social_message();">发送动态</div>
	<div id="words_count_area">
		<span id="words_count_numerator"></span>/400
	</div>
</div>