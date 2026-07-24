<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Compose Message" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-officer.jsp"/>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-title"><i class="fa-solid fa-pen-to-square"></i> Compose Secure Message</div>
        <div class="topbar-right">
            <a href="${pageContext.request.contextPath}/officer/inbox" class="btn btn-outline btn-sm">
                <i class="fa-solid fa-arrow-left"></i> Back to Inbox
            </a>
        </div>
    </div>

    <div class="content-area">
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${error}"/>
            </div>
        </c:if>

        <div class="card" style="max-width:750px;">
            <div class="card-header">
                <i class="fa-solid fa-lock"></i> Encrypted Message Composition
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/officer/compose"
                      method="post" enctype="multipart/form-data" novalidate>

                    <div class="form-group">
                        <label class="form-label">Recipient <span class="req">*</span></label>
                        <select name="receiverId" class="form-control" required>
                            <option value="">-- Select Recipient --</option>
                            <c:forEach var="o" items="${activeOfficers}">
                                <c:if test="${o.userId ne sessionScope.userId}">
                                    <option value="${o.userId}">
                                        <c:out value="${o.fullName}"/>
                                        (<c:out value="${o.username}"/> –
                                        <c:out value="${o.department}"/>)
                                    </option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Subject <span class="req">*</span></label>
                        <input type="text" name="subject" class="form-control"
                               placeholder="Message subject" required/>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Priority</label>
                        <select name="priority" class="form-control">
                            <option value="LOW">Low</option>
                            <option value="NORMAL" selected>Normal</option>
                            <option value="HIGH">High</option>
                            <option value="CRITICAL">Critical</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Message Body <span class="req">*</span></label>
                        <textarea name="body" class="form-control" rows="8"
                                  placeholder="Type your secure message here..." required></textarea>
                    </div>

                    <div class="form-group">
                        <label class="form-label">
                            <i class="fa-solid fa-paperclip"></i> Attachment
                            <small class="text-muted">(max 10 MB – PDF, images, docs, zip)</small>
                        </label>
                        <input type="file" name="attachment" class="form-control"
                               accept=".pdf,.jpg,.jpeg,.png,.gif,.txt,.doc,.docx,.xls,.xlsx,.zip"/>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fa-solid fa-paper-plane"></i> Send Secure Message
                        </button>
                        <a href="${pageContext.request.contextPath}/officer/inbox"
                           class="btn btn-outline">Discard</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/components/footer.jsp"/>
