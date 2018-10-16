<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="../css/main.css" />

<title>Login</title>
</head>
<body>

<h3>Login</h3>

<form action="<c:url value="/secure/login.controller" />" method="get">
<table>
	<tr>
		<td>ID : </td>
		<td><input type="text" name="username" value="${param.username}"></td>
		<td>${errors.username}</td>
	</tr>
	<tr>
		<td>PWD : </td>
		<td><input type="text" name="password" value="${param.password}"></td>
		<td>${errors.password}</td>
	</tr>
	<tr>
		<td>
			<select name="myLocale">
				<option value="zh_TW">中文</option>
				<option value="en_US">英文</option>
				<option value="de_DE">德文</option>
				<option value="fr_FR">法文</option>
			</select>
		</td>
		<td align="right"><input type="submit" value="Login"></td>
	</tr>
</table>
</form>

</body>
</html>