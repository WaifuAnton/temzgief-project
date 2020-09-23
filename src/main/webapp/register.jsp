<%--
  Created by IntelliJ IDEA.
  User: makar
  Date: 21.09.2020
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages"/>
<fmt:message key="register.name" var="register" scope="application"/>
<html>
<head>
    <title><fmt:message key="register.name"/></title>
</head>
<body>
<form method="post" action="controller?action=register">
    <label><input type="email" name="email" size="10"/></label><br>
    <label><input type="password" name="password" size="10" /></label><br>
    <input type="submit" value="${register}"/>
</form>
<fmt:message key="register.phrase"/> <a href="login.jsp"><fmt:message key="login.name"/></a>
</body>
</html>
