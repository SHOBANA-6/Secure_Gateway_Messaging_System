
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Compose Message" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-admin.jsp"/>

<main class="main-content">

    <div class="topbar">
        <div class="topbar-title">
            <i class="fa-solid fa-paper-plane"></i>
            Compose Message
        </div>

        <div class="topbar-right">
            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="btn btn-outline btn-sm">
                <i class="fa-solid fa-arrow-left"></i>
                Dashboard
            </a>
        </div>
    </div>

    <div class="content-area">

        <c:if test="${param.success=='1'}">
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i>
                Message sent successfully.
            </div>
        </c:if>

        <div class="card" style="max-width:850px;">

            <div class="card-header">
                <i class="fa-solid fa-envelope"></i>
                Secure Message
            </div>

            <div class="card-body">

                <form action="${pageContext.request.contextPath}/admin/send-message"
                      method="post"
                      enctype="multipart/form-data">

                    <div class="form-group">

                        <label class="form-label">
                            Subject
                        </label>

                        <input type="text"
                               name="subject"
                               class="form-control"
                               required>

                    </div>

                    <div class="form-group">

                        <label class="form-label">

                            Priority

                        </label>

                        <select name="priority"
                                class="form-control">

                            <option value="LOW">Low</option>

                            <option value="NORMAL" selected>
                                Normal
                            </option>

                            <option value="HIGH">
                                High
                            </option>

                            <option value="CRITICAL">
                                Critical
                            </option>

                        </select>

                    </div>

                    <div class="form-group">

                        <label class="form-label">
                            Message
                        </label>

                        <textarea name="message"
                                  rows="6"
                                  class="form-control"
                                  required></textarea>

                    </div>

                    <div class="form-group">

                        <label class="form-label">

                            Attachment

                        </label>

                        <input type="file"
                               name="attachment"
                               class="form-control">

                    </div>

                    <div class="form-group">

                        <label class="form-label">
                            Select Officers
                        </label>

                        <div class="card"
                             style="padding:15px;max-height:220px;overflow:auto;">

                            <c:forEach items="${officers}" var="officer">

                                <div style="margin-bottom:10px;">

                                    <input type="checkbox"
                                           name="receiverIds"
                                           value="${officer.userId}">

                                    <strong>${officer.fullName}</strong>

                                    (${officer.department})

                                </div>

                            </c:forEach>

                        </div>

                    </div>

                    <div class="form-actions">

                        <button class="btn btn-primary">

                            <i class="fa-solid fa-paper-plane"></i>

                            Send Message

                        </button>

                        <a href="${pageContext.request.contextPath}/admin/dashboard"
                           class="btn btn-outline">

                            Cancel

                        </a>

                    </div>

                </form>

            </div>

        </div>

    </div>

</main>

<jsp:include page="/components/footer.jsp"/>