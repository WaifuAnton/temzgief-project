<%--
  Created by IntelliJ IDEA.
  User: makar
  Date: 25.09.2020
  Time: 14:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages"/>
<fmt:message key="product.buy" var="buy" scope="application"/>
<html>
<head>
    <title><fmt:message key="category.clothes.women"/></title>
</head>
<body>
<table>
    <jstl:forEach items="${requestScope.productsWomen}" var="item">
        <tr>
            <td><img src="${item.picture}" alt='${item.name} + " picture"' height="200"/></td>
            <td>${item.name}</td>
            <td>${item.description}</td>
            <td>${item.manufactureDate}</td>
            <td>${item.price}</td>
            <td>
                <form method="get" action="controller?action=buy">
                    <input type="hidden" name = "deviceId" value="${item.id}"/>
                    <input type="submit" name="buy" value="${buy}">
                </form>
            </td>
        </tr>
    </jstl:forEach>
    <tr>
    <%for (long i = 1; i <= (Long) request.getAttribute("pagesCount"); i++) {%>
        <td><a href='controller?action=women&page=<%=i%>'><%=i%></a></td>
        <td> </td>
    <%}%>
    </tr>
</table>
</body>
</html>
