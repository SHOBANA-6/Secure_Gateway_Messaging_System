<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Dashboard" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-officer.jsp"/>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-title"><i class="fa-solid fa-gauge-high"></i> Officer Dashboard</div>
        <div class="topbar-right">
            <span class="topbar-user">
                <i class="fa-solid fa-user-shield"></i>
                <c:out value="${sessionScope.fullName}"/>
            </span>
        </div>
    </div>

    <div class="content-area">

        <c:if test="${not empty flashSuccess}">
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i> <c:out value="${flashSuccess}"/>
            </div>
        </c:if>
        <c:if test="${not empty dashError}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${dashError}"/>
            </div>
        </c:if>

        <!-- Stats row -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon bg-danger"><i class="fa-solid fa-envelope-open-text"></i></div>
                <div class="stat-body">
                    <p class="stat-label">Unread Messages</p>
                    <h3 class="stat-value"><c:out value="${unreadCount}"/></h3>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon bg-primary"><i class="fa-solid fa-inbox"></i></div>
                <div class="stat-body">
                    <p class="stat-label">Total Inbox</p>
                    <h3 class="stat-value"><c:out value="${inboxCount}"/></h3>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon bg-success"><i class="fa-solid fa-paper-plane"></i></div>
                <div class="stat-body">
                    <p class="stat-label">Messages Sent</p>
                    <h3 class="stat-value"><c:out value="${sentCount}"/></h3>
                </div>
            </div>
        </div>

        <!-- Quick actions -->
        <div class="card" style="margin-bottom:1.5rem;">
            <div class="card-header"><i class="fa-solid fa-bolt"></i> Quick Actions</div>
            <div class="card-body quick-actions">
                <a href="${pageContext.request.contextPath}/officer/compose" class="btn btn-primary">
                    <i class="fa-solid fa-pen-to-square"></i> Compose Message
                </a>
                <a href="${pageContext.request.contextPath}/officer/inbox" class="btn btn-outline">
                    <i class="fa-solid fa-inbox"></i> View Inbox
                    <c:if test="${unreadCount > 0}">
                        <span class="badge badge-danger">${unreadCount}</span>
                    </c:if>
                </a>
                <a href="${pageContext.request.contextPath}/officer/sent" class="btn btn-outline">
                    <i class="fa-solid fa-paper-plane"></i> Sent Messages
                </a>
            </div>
        </div>

        <!-- Recent messages -->
        <div class="card">
            <div class="card-header"><i class="fa-solid fa-clock-rotate-left"></i> Recent Inbox</div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty recentMessages}">
                        <div class="empty-state">
                            <i class="fa-solid fa-inbox"></i>
                            <p>Your inbox is empty.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>From</th>
                                <th>Subject</th>
                                <th>Priority</th>
                                <th>Date</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="m" items="${recentMessages}">
<%--                                <tr class="${m.read ? '' : 'row-unread'}">--%>
                                <tr>
                                    <td><c:out value="${m.senderFullName}"/></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/officer/inbox?readId=${m.messageId}"
                                           class="msg-subject-link">
                                            <c:out value="${m.subject}"/>
                                        </a>
                                    </td>
                                    <td>
                                            <span class="badge">
<%--                                        <span class="badge priority-${fn:toLowerCase(m.priority)}">--%>
                                            <c:out value="${m.priority}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <c:out value="${m.sentAt}"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${m.read}">
                                                <span class="badge badge-outline">Read</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Unread</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/components/footer.jsp"/>
