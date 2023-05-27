<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
	<html lang="ko">
	<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<style>
	body { font-size: 12px; }
	#info { color: blue; }
	#messages { border: 1px solid black; width: 600px; height: 200px; overflow: scroll; }
	</style>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
	$(function(){
		// 페이지가 최초 로딩 될 때, 메시지를 받아온다.
		getMessages();
		$("#myMessage").focus();
		
		$("#sendMessage").on("click", function() {
			if($("#myMessage").val().length == 0) return;
			
			$.ajax({
				"url" : "sendMessage.do",
				"type" : "post",
				"dataType" : "text",
				"data" : { "message" : $("#myMessage").val() },
				"success" : function(data) {
					if(data === "SUCCESS") {
						$("#myMessage").val("");
					} else {
						$("#info").text("세션이 만료되었습니다.");
					}
				},
				"error" : function() {
					$("#info").text("에러 : " + textStatus);
				}
			});
		});
		
		$("#exit").on("click", function() {
			location.href = "exit.do";
		});
		
		$("#myMessage").on("keydown", function(e){
			if(e.keyCode == 13) {
				$("#sendMessage").trigger("click");
			}
		});
	});
	
	function getMessages() {
		$.ajax({
			"url" : "getMessages.do",
			"type" : "get",
			"dataType" : "json",
			"cache" : false,
			"timeout" : "120000",  // 추가된 부분. 120초 대기
			"success" : function(data) {
				if(data.result === "FAILURE") {
					location.href = "index.do";
				} else {
					$.each(data, function(index, value) {
						$("<span>").text(value).appendTo("#messages");
						$("<br>").appendTo("#messages");
						// 채팅창에 메시지가 가득 찰 때, 채팅창의 최하단으로 스크롤
						$("#messages").scrollTop(200);
					});
					getMessages();
				}
			},
			"error" : function(jqXHR, textStatus, errorThrown) {
				setTimeout(getMessages, 5000);
			}
		});
	}
	</script>
	<title>궁금하면500원 폴링 채팅 코드</title>
	</head>
	<body>
	<h3>채팅룸</h3>
	<div id="info">현재 닉네임은 ${param.nickname}입니다.</div>
	<hr />
	<div id="messages"></div><br />
	<input type="text" id="myMessage" name="myMessage" size="60">&nbsp;
	<input type="button" id="sendMessage" value="전송">&nbsp;
	<input type="button" id="exit" value="나가기">
	<hr />
	</body>
	</html>
	