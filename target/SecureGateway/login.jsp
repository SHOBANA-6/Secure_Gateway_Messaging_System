<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>SecureGateway – Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
</head>
<body class="login-body">

<div class="login-wrapper">

    <!-- Brand -->
    <div class="login-brand">
        <div class="brand-icon"><i class="fa-solid fa-shield-halved"></i></div>
        <h1 class="brand-name">SecureGateway</h1>
        <p class="brand-tagline">Secure Messaging &amp; Communication System</p>
    </div>

    <!-- Alert: logout -->
    <c:if test="${param.logout eq 'true'}">
        <div class="alert alert-info">
            <i class="fa-solid fa-circle-check"></i> You have been logged out securely.
        </div>
    </c:if>

    <!-- Alert: error -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${error}"/>
        </div>
    </c:if>

    <!-- Login card -->
    <div class="login-card">
        <h2 class="login-title"><i class="fa-solid fa-lock"></i> Secure Sign In</h2>

        <!-- Login Role Tabs -->
        <div class="login-tabs">

            <button type="button"
                    id="adminTab"
                    class="login-tab active"
                    onclick="selectRole('ADMIN')">

                <i class="fa-solid fa-user-shield"></i>
                Admin

            </button>

            <button type="button"
                    id="officerTab"
                    class="login-tab"
                    onclick="selectRole('OFFICER')">

                <i class="fa-solid fa-user"></i>
                Officer

            </button>

        </div>


        <form action="${pageContext.request.contextPath}/login" method="post" novalidate>
            <input type="hidden"
                   id="loginRole"
                   name="loginRole"
                   value="ADMIN">
            <div class="form-group">
                <label for="username" class="form-label">
                    <i class="fa-solid fa-user"></i> Username
                </label>
                <input type="text" id="username" name="username" class="form-control"
                       placeholder="Enter your username"
                       value="<c:out value='${username}'/>"
                       required autocomplete="username"/>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">
                    <i class="fa-solid fa-key"></i> Password
                </label>
                <div class="input-group">
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="Enter your password"
                           required autocomplete="current-password"/>
                    <button type="button" class="input-suffix" onclick="togglePassword()" title="Show/Hide">
                        <i class="fa-solid fa-eye" id="eyeIcon"></i>
                    </button>
                </div>
            </div>

            <button type="submit" class="btn btn-primary btn-full">
                <i class="fa-solid fa-right-to-bracket"></i> Sign In
            </button>
        </form>
    </div>

    <p class="login-footer">
        <i class="fa-solid fa-shield-virus"></i>
        Government &amp; Enterprise Classified Platform &mdash; Authorised Access Only
    </p>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>

<script>

    function selectRole(role){

        document.getElementById("loginRole").value = role;

        if(role==="ADMIN"){

            document.getElementById("adminTab").classList.add("active");
            document.getElementById("officerTab").classList.remove("active");

        }else{

            document.getElementById("officerTab").classList.add("active");
            document.getElementById("adminTab").classList.remove("active");

        }

    }

</script>
</body>
</html>
