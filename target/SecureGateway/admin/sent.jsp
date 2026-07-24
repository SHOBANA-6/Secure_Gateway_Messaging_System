<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Sent Messages" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-admin.jsp"/>

<main class="main-content">

    <div class="topbar">

        <div class="topbar-title">

            <i class="fa-solid fa-paper-plane"></i>

            Sent Messages

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

        <div class="card">

            <div class="card-header">

                <i class="fa-solid fa-envelope-circle-check"></i>

                Messages Sent by Admin

            </div>

            <div class="card-body">

                <table class="table">

                    <thead>

                    <tr>

                        <th>Receiver</th>

                        <th>Subject</th>

                        <th>Message</th>

                        <th>Sent Time</th>

                    </tr>

                    </thead>

                    <tbody>

                    <c:forEach items="${sentMessages}" var="msg">

                        <tr>

                            <td>${msg.receiverFullName}</td>

                            <td>${msg.subject}</td>

                            <td>${msg.body}</td>

                            <td>

<%--                                <fmt:formatDate--%>
<%--                                        value="${msg.sentAt}"--%>
<%--                                        pattern="dd-MM-yyyy HH:mm"/>--%>
        ${msg.sentAt}

                            </td>

                        </tr>

                    </c:forEach>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</main>

<jsp:include page="/components/footer.jsp"/>