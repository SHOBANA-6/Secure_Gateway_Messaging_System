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
        <div class="user-avatar"><i class="fa-solid fa-user-shield"></i></div>
        <div class="user-info">
            <p class="user-name"><c:out value="${sessionScope.fullName}"/></p>
            <span class="user-role role-officer">OFFICER</span>
        </div>
    </div>

    <nav class="sidebar-nav">
        <p class="nav-section-label">Messaging</p>
        <a href="${pageContext.request.contextPath}/officer/dashboard"
           class="nav-link <c:if test='${pageTitle eq "Dashboard"}'>active</c:if>">
            <i class="fa-solid fa-gauge-high"></i> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/officer/compose"
           class="nav-link <c:if test='${pageTitle eq "Compose Message"}'>active</c:if>">
            <i class="fa-solid fa-pen-to-square"></i> Compose
        </a>
        <a href="${pageContext.request.contextPath}/officer/inbox"
           class="nav-link <c:if test='${pageTitle eq "Inbox"}'>active</c:if>">
            <i class="fa-solid fa-inbox"></i> Inbox
            <c:if test="${unreadCount > 0}">
                <span class="badge badge-danger">${unreadCount}</span>
            </c:if>
        </a>
        <a href="${pageContext.request.contextPath}/officer/sent"
           class="nav-link <c:if test='${pageTitle eq "Sent Messages"}'>active</c:if>">
            <i class="fa-solid fa-paper-plane"></i> Sent
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
