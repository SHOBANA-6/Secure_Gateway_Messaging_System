<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Redirect root to login (or dashboard if already authenticated)
    response.sendRedirect(request.getContextPath() + "/login");
%>
