<%@ page import="com.shop.dao.CategoryDao" %>
<%@ page import="com.shop.dao.factory.DaoFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="com.shop.entity.Category" %><%--
  Created by IntelliJ IDEA.
  User: makar
  Date: 23.09.2020
  Time: 18:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages"/>
<%CategoryDao categoryDao = DaoFactory.getDaoFactory().getCategoryDao();%>
<%List<Category> categories = categoryDao.findAll();%>
<html>
<head>
    <title><fmt:message key="categories.name"/></title>
</head>
<body>
<table>
    <%for (Category category : categories) {%>
    <tr>
        <td><img src='<%=category.getPicture()%>' height="200" alt='<%=category.getName() + " picture"%>'></td>
    </tr>
    <tr>
        <td><a href='<%=category.getName() + ".jsp"%>'><fmt:message key='<%="category." + category.getName()%>'/></a></td>
    </tr>
    <%}%>
</table>
</body>
</html>
