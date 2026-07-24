<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Officers" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-admin.jsp"/>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-title"><i class="fa-solid fa-users"></i> Officer Management</div>
        <div class="topbar-right">
            <a href="${pageContext.request.contextPath}/admin/add-officer" class="btn btn-primary btn-sm">
                <i class="fa-solid fa-user-plus"></i> Add Officer
            </a>
        </div>
    </div>

    <div class="content-area">

        <c:if test="${not empty flashSuccess}">
            <div class="alert alert-success">
                <i class="fa-solid fa-circle-check"></i> <c:out value="${flashSuccess}"/>
            </div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${flashError}"/>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${error}"/>
            </div>
        </c:if>

        <!-- Search bar -->
        <div class="card" style="margin-bottom:1rem;">
            <div class="card-body" style="padding:1rem;">
                <form action="${pageContext.request.contextPath}/admin/search-officers"
                      method="get" class="search-form">
                    <input type="text" name="keyword" class="form-control"
                           placeholder="Search by name, username, badge or department..."
                           value="<c:out value='${searchKeyword}'/>"/>
                    <button type="submit" class="btn btn-primary">
                        <i class="fa-solid fa-magnifying-glass"></i> Search
                    </button>
                    <c:if test="${not empty searchKeyword}">
                        <a href="${pageContext.request.contextPath}/admin/view-officers"
                           class="btn btn-outline">Clear</a>
                    </c:if>
                </form>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <i class="fa-solid fa-list"></i>
                <c:choose>
                    <c:when test="${not empty searchKeyword}">
                        Search Results for "<c:out value='${searchKeyword}'/>"
                        (${fn:length(officers)} found)
                    </c:when>
                    <c:otherwise>All Officers (${fn:length(officers)})</c:otherwise>
                </c:choose>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty officers}">
                        <div class="empty-state">
                            <i class="fa-solid fa-users-slash"></i>
                            <p>No officers found.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Full Name</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Department</th>
                                <th>Badge</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="o" items="${officers}" varStatus="s">
                                <tr class="${o.active ? '' : 'row-inactive'}">
                                    <td>${s.count}</td>
                                    <td><c:out value="${o.fullName}"/></td>
                                    <td><code><c:out value="${o.username}"/></code></td>
                                    <td><c:out value="${o.email}"/></td>
                                    <td><c:out value="${o.department}"/></td>
                                    <td><c:out value="${o.badgeNumber}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.active}">
                                                <span class="badge badge-success">Active</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-danger">Inactive</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/admin/edit-officer?id=${o.userId}"
                                           class="btn btn-sm btn-outline" title="Edit">
                                            <i class="fa-solid fa-pen"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/toggle-officer?id=${o.userId}"
                                           class="btn btn-sm ${o.active ? 'btn-danger' : 'btn-success'}"
                                           title="${o.active ? 'Deactivate' : 'Activate'}"
                                           onclick="return confirm('${o.active ? 'Deactivate' : 'Activate'} this officer?')">
                                            <i class="fa-solid ${o.active ? 'fa-user-slash' : 'fa-user-check'}"></i>
                                        </a>
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
