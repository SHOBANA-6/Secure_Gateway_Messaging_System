<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Inbox" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-officer.jsp"/>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-title">
            <i class="fa-solid fa-inbox"></i> Inbox
            <c:if test="${unreadCount > 0}">
                <span class="badge badge-danger">${unreadCount} unread</span>
            </c:if>
        </div>
        <div class="topbar-right">
            <a href="${pageContext.request.contextPath}/officer/compose" class="btn btn-primary btn-sm">
                <i class="fa-solid fa-pen-to-square"></i> Compose
            </a>
        </div>
    </div>

    <div class="content-area">
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${error}"/>
            </div>
        </c:if>

        <!-- Message viewer (when readId is passed) -->
        <c:if test="${not empty viewMessage}">
            <div class="card message-viewer">
                <div class="card-header">
                    <i class="fa-solid fa-envelope-open"></i>
                    <c:out value="${viewMessage.subject}"/>
                    <span class="badge priority-${fn:toLowerCase(viewMessage.priority.name())}">
                        <c:out value="${viewMessage.priority}"/>
                    </span>
                </div>
                <div class="card-body">
                    <div class="msg-meta">
                        <span><strong>From:</strong> <c:out value="${viewMessage.senderFullName}"/></span>
                        <span><strong>Date:</strong>
                              <c:out value="${viewMessage.formattedSentAt}"/>
                        </span>
                    </div>
                    <div class="msg-body">
                        <pre><c:out value="${viewMessage.body}"/></pre>
                    </div>
                    <c:if test="${viewMessage.hasAttachment()}">
                        <div class="msg-attachment">
                            <i class="fa-solid fa-paperclip"></i>
                            <a href="${pageContext.request.contextPath}/officer/download-attachment?messageId=${viewMessage.messageId}"
                               class="btn btn-outline btn-sm">
                                <i class="fa-solid fa-download"></i>
                                <c:out value="${viewMessage.attachmentName}"/>
                            </a>
                        </div>
                    </c:if>
                </div>
            </div>
        </c:if>

        <!-- Message list -->
        <div class="card">
            <div class="card-header">
                <i class="fa-solid fa-list"></i> All Messages (${fn:length(messages)})
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty messages}">
                        <div class="empty-state">
                            <i class="fa-solid fa-inbox"></i>
                            <p>Your inbox is empty.</p>
                            <a href="${pageContext.request.contextPath}/officer/compose"
                               class="btn btn-primary">Send First Message</a>
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
                                <th>Attach</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="m" items="${messages}">
                                <tr class="${m.read ? '' : 'row-unread'}">
                                    <td><c:out value="${m.senderFullName}"/></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/officer/inbox?readId=${m.messageId}"
                                           class="msg-subject-link">
                                            <c:if test="${!m.read}">
                                                <i class="fa-solid fa-circle-dot text-primary" style="font-size:.6rem"></i>
                                            </c:if>
                                            <c:out value="${m.subject}"/>
                                        </a>
                                    </td>
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
                                            </a>
                                        </c:if>
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
