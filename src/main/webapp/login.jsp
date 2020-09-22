<%--
  Created by IntelliJ IDEA.
  User: makar
  Date: 21.09.2020
  Time: 16:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="controller?action=login">
    <label><input type="email" name="email" size="10"/></label><br>
    <label><input type="password" name="password" size="10" /></label><br>
    <input type="submit" value="Login" />
</form>
<jstl:if test="${not empty requestScope.wrongEmailOrPassword}">
    <p>Wrong email or password</p>
</jstl:if>
</body>
</html>
