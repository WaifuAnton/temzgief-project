<%@ page import="com.shop.entity.Category" %>
<%@ page import="com.shop.dao.CategoryDao" %>
<%@ page import="com.shop.dao.factory.DaoFactory" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: makar
  Date: 24.09.2020
  Time: 20:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages"/>
<%CategoryDao categoryDao = DaoFactory.getDaoFactory().getCategoryDao();%>
<%List<Category> categories = categoryDao.findAllSub("cars");%>
<html>
<head>
    <title><fmt:message key="category.cars"/></title>
</head>
<body>
<table>
    <%for (Category category : categories) {%>
    <%String name = category.getName().toLowerCase();%>
    <tr>
        <td><img src='<%=category.getPicture()%>' height="200" alt='<%=name + " picture"%>'></td>
        <td><a href='<%=name + ".jsp"%>'><fmt:message key='<%="category.cars." + name%>'/></a></td>
    </tr>
    <%}%>
</table>
</body>
</html>
