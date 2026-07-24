<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<aside class="sidebar">
    <div class="sidebar-brand">
        <i class="fa-solid fa-shield-halved"></i>
        <span>SecureGateway</span>
    </div>

    <div class="sidebar-user">
        <div class="user-avatar"><i class="fa-solid fa-user-tie"></i></div>
        <div class="user-info">
            <p class="user-name"><c:out value="${sessionScope.fullName}"/></p>
            <span class="user-role role-admin">ADMIN</span>
        </div>
    </div>

    <nav class="sidebar-nav">
        <p class="nav-section-label">Administration</p>
        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="nav-link ${pageTitle eq "Dashboard" ? 'active' : ''}">
            <i class="fa-solid fa-user-plus"></i> Dash Board
        </a>
        <a href="${pageContext.request.contextPath}/admin/add-officer"
           class="nav-link ${pageTitle eq 'Add Officer' ? 'active' : ''}">
            <i class="fa-solid fa-user-plus"></i> Add Officer
        </a>
        <a href="${pageContext.request.contextPath}/admin/view-officers"
           class="nav-link ${pageTitle eq 'Officers' ? 'active' : ''}">
            <i class="fa-solid fa-users"></i> View Officers
        </a>
        <a href="${pageContext.request.contextPath}/admin/compose"
           class="nav-link ${pageTitle eq 'Compose Message' ? 'active' : ''}">
            <i class="fa-solid fa-paper-plane"></i> Compose Message
        </a>

        <a href="${pageContext.request.contextPath}/admin/sent"
           class="nav-link ${pageTitle eq 'Sent Messages' ? 'active' : ''}">
            <i class="fa-solid fa-envelope"></i> Sent Messages
        </a>
        <a href="${pageContext.request.contextPath}/admin/search-officers"
           class="nav-link ${pageTitle eq 'Search Officers' ? 'active' : ''}">
            <i class="fa-solid fa-magnifying-glass"></i> Search Officers
        </a>

        <p class="nav-section-label" style="margin-top:1.5rem;">Account</p>
        <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-link-danger">
            <i class="fa-solid fa-right-from-bracket"></i> Logout
        </a>

    </nav>

    <div class="sidebar-footer">
        <i class="fa-solid fa-circle-dot text-success"></i> System Online
    </div>
</aside>
