<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
	<html>
	<head>
	<meta charset="UTF-8">
	<style>
	body {
	  font-size: 12px;
	}
	
	.error {
	  color: red;
	}
	</style>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
	function check() {
	  if ($("#nickname").val().length == 0) {
		alert("닉네임을 지정해주세요.");
		$("#nickname").focus();
		return false;
	  }
	  return true;
	}
	</script>
	<title>궁금하면500원 폴링 채팅 코드</title>
	</head>
	<body>
	<h3>Long polling 방식의 초간단 채팅 예제</h3>
	- 소스는 <a href="https://blog.naver.com/sleekydz86">궁금하면 500원 블로그</a>에서 제공합니다.<br /><br />
	- 예제는 세션 기반으로 닉네임을 구분하기 때문에 동일한 브라우저들로 채팅방에 동시에 입장 할 수 없습니다.<br /><br />
	- 테스트하시려면 서로 다른 브라우저로 접속하세요. 예) 익스플로러엣지와 크롬<br /><br />
	- 상대방의 메시지를 확인하기 까지 최대 0.5초가 소요될 수 있습니다.<br /><br />
	<div class="error">${error}</div>
	<hr />
	<form action="enterRoom.do" method="post" onsubmit="return check();">
		<label for="nickname">닉네임 : </label>&nbsp;
		<input type="text" name="nickname" id="nickname" size="20">&nbsp;
		<input type="submit" value="입장">
	</form>
	<hr />
	</body>
	</html>
	