<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Sent Messages" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-officer.jsp"/>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-title"><i class="fa-solid fa-paper-plane"></i> Sent Messages</div>
        <div class="topbar-right">
            <a href="${pageContext.request.contextPath}/officer/compose" class="btn btn-primary btn-sm">
                <i class="fa-solid fa-pen-to-square"></i> Compose
            </a>
        </div>
    </div>

    <div class="content-area">
        <c:if test="${not empty flashSuccess}">
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i> <c:out value="${flashSuccess}"/>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${error}"/>
            </div>
        </c:if>

        <div class="card">
            <div class="card-header">
                <i class="fa-solid fa-list"></i> Sent Messages (${fn:length(messages)})
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty messages}">
                        <div class="empty-state">
                            <i class="fa-solid fa-paper-plane"></i>
                            <p>No sent messages yet.</p>
                            <a href="${pageContext.request.contextPath}/officer/compose"
                               class="btn btn-primary">Compose First Message</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>To</th>
                                <th>Subject</th>
                                <th>Priority</th>
                                <th>Date</th>
                                <th>Attachment</th>
                                <th>Read</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="m" items="${messages}">
                                <tr>
                                    <td><c:out value="${m.receiverFullName}"/></td>
                                    <td><c:out value="${m.subject}"/></td>
                                    <td>
                                        <span class="badge priority-${fn:toLowerCase(m.priority.name())}">
                                            <c:out value="${m.priority}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <c:out value="${m.formattedSentAt}"/>
                                    </td>
                                    <td>
                                        <c:if test="${m.hasAttachment()}">
                                            <a href="${pageContext.request.contextPath}/officer/download-attachment?messageId=${m.messageId}"
                                               class="btn btn-sm btn-outline" title="${m.attachmentName}">
                                                <i class="fa-solid fa-download"></i>
                                                <c:out value="${m.attachmentName}"/>
                                            </a>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${m.read}">
                                                <span class="badge badge-success">Read</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-outline">Unread</span>
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
