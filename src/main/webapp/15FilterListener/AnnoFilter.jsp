<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form>
		<select name="searchField">
			<option value="tilte">제목</option>
			<option value="content">내용</option>
		</select>
		
		<input type="text" name="searchWord">
		<input type="submit" name="검색하기">		
	</form>
</body>
</html>