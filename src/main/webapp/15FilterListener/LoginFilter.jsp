<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>로그인 페이지[Filter]</h2>
	<span style="color: red; font-size:1.2em">
		<%= request.getAttribute("LoginErrMsg")  == null ?  "":  request.getAttribute("LoginErrMsg") %>
	</span>
	
	<%
		if(session.getAttribute("UserId") == null){
	%>
	<form method="post">
		<input type="hidden" name="backUrl" value="${param.backUrl}">
		아이디: <input type="text" name="user_id"><br>
		비밀번호: <input type="text" name="user_pwd"><br>
		<input type="submit" value="로그인하기">
	</form>
	<%		
		}else{
	%>
		<%=session.getAttribute("UserName") %> 회원님, 로그인하셨습니다.<br>
		<a href="?mode=logout">[로그아웃]</a>
	<%		
		}
	%>
</body>
</html>