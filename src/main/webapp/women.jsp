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
<fmt:message key="product.name" var="name" scope="application"/>
<fmt:message key="product.price" var="price" scope="application"/>
<fmt:message key="product.date" var="date" scope="application"/>
<fmt:message key="product.ascending" var="ascending" scope="application"/>
<fmt:message key="product.descending" var="descending" scope="application"/>
<fmt:message key="product.sort" var="submit" scope="application"/>
<html>
<head>
    <title><fmt:message key="category.clothes.women"/></title>
</head>
<body>
<form action='controller?action=women' method="post">
    <input type="radio" name="sort" value="name" <jstl:if test="${sessionScope.sort == 'name'}">checked</jstl:if>>${name}<br>
    <input type="radio" name="sort" value="price" <jstl:if test="${sessionScope.sort == 'price'}">checked</jstl:if>>${price}<br>
    <input type="radio" name="sort" value="manufacture_date" <jstl:if test="${sessionScope.sort == 'manufacture_date'}">checked</jstl:if>>${date}<br>
    <input type="radio" name="order" value="asc" <jstl:if test="${sessionScope.order == 'asc'}">checked</jstl:if>>${ascending}<br>
    <input type="radio" name="order" value="desc" <jstl:if test="${sessionScope.order == 'desc'}">checked</jstl:if>>${descending}<br>
    <input type="submit" value="${submit}">
</form>
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
