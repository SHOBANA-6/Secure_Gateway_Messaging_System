<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>404 – Not Found | SecureGateway</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
</head>
<body class="login-body">
    <div class="error-page">
        <div class="error-code text-primary">404</div>
        <div class="error-icon"><i class="fa-solid fa-circle-question"></i></div>
        <h2 class="error-title">Page Not Found</h2>
        <p class="error-msg">The resource you requested could not be found.</p>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
            <i class="fa-solid fa-house"></i> Return to Home
        </a>
    </div>
</body>
</html>
