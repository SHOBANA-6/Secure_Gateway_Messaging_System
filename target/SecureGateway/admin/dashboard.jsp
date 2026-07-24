<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Dashboard" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-admin.jsp"/>

<main class="main-content">
  <div class="topbar">
    <div class="topbar-title">
      <i class="fa-solid fa-gauge-high"></i> Admin Dashboard
    </div>
    <div class="topbar-right">
            <span class="topbar-user">
                <i class="fa-solid fa-user-tie"></i>
                <c:out value="${sessionScope.fullName}"/>
            </span>
    </div>
  </div>

  <div class="content-area">

<%--    <h2 style="color:red;">CONTENT TEST</h2>--%>

    <c:if test="${not empty dashError}">
      <div class="alert alert-danger"><i class="fa-solid fa-triangle-exclamation"></i>
        <c:out value="${dashError}"/>
      </div>
    </c:if>

    <!-- Stats row -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon bg-primary"><i class="fa-solid fa-users"></i></div>
        <div class="stat-body">
          <p class="stat-label">Total Officers</p>
          <h3 class="stat-value"><c:out value="${totalOfficers}"/></h3>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-success"><i class="fa-solid fa-user-check"></i></div>
        <div class="stat-body">
          <p class="stat-label">Active Officers</p>
          <h3 class="stat-value"><c:out value="${activeOfficers}"/></h3>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-danger"><i class="fa-solid fa-user-slash"></i></div>
        <div class="stat-body">
          <p class="stat-label">Inactive Officers</p>
          <h3 class="stat-value"><c:out value="${inactiveOfficers}"/></h3>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon bg-warning"><i class="fa-solid fa-envelope"></i></div>
        <div class="stat-body">
          <p class="stat-label">Total Messages</p>
          <h3 class="stat-value"><c:out value="${totalMessages}"/></h3>
        </div>
      </div>
    </div>

    <!-- Quick actions -->
    <div class="card" style="margin-bottom:1.5rem;">
      <div class="card-header">
        <i class="fa-solid fa-bolt"></i> Quick Actions
      </div>

      <div class="card-body quick-actions">

        <a href="${pageContext.request.contextPath}/admin/add-officer" class="btn btn-primary">
          <i class="fa-solid fa-user-plus"></i> Add Officer
        </a>
        <a href="${pageContext.request.contextPath}/admin/view-officers" class="btn btn-outline">
          <i class="fa-solid fa-users"></i> View All Officers
        </a>
        <a href="${pageContext.request.contextPath}/admin/search-officers" class="btn btn-outline">
          <i class="fa-solid fa-magnifying-glass"></i> Search Officers
        </a>
      </div>
    </div>

    <!-- Recent officers -->
    <div class="card">
      <div class="card-header">
        <i class="fa-solid fa-clock-rotate-left"></i> Recent Officers
      </div>
      <div class="card-body p-0">
        <c:choose>
          <c:when test="${empty recentOfficers}">
            <div class="empty-state">
              <i class="fa-solid fa-users-slash"></i>
              <p>No officers registered yet.</p>
            </div>
          </c:when>
          <c:otherwise>
            <table class="table">
              <thead>
              <tr>
                <th>Name</th>
                <th>Username</th>
                <th>Department</th>
                <th>Badge</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="o" items="${recentOfficers}" varStatus="s">
                <c:if test="${s.index lt 8}">
                  <tr>
                    <td><c:out value="${o.fullName}"/></td>
                    <td><code><c:out value="${o.username}"/></code></td>
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
                    <td>
                      <a href="${pageContext.request.contextPath}/admin/edit-officer?id=${o.userId}"
                         class="btn btn-sm btn-outline">
                        <i class="fa-solid fa-pen"></i> Edit
                      </a>
                    </td>
                  </tr>
                </c:if>
              </c:forEach>
              </tbody>
            </table>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

  </div><%-- .content-area --%>
</main>

<jsp:include page="/components/footer.jsp"/>
