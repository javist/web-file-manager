<%@ taglib uri="http://braintest.com/walker" prefix="wt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insert title here</title>
        <link href="${pageContext.request.contextPath}/static/css/main.css" rel="stylesheet" />
    </head>
    <body>
        On JSP1:
        <wt:walker/>

        <script src="${pageContext.request.contextPath}/static/js/jquery-1.7.2.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/main.js"></script>
    </body>
</html>