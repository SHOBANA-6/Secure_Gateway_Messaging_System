<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Edit Officer" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-admin.jsp"/>

<main class="main-content">
  <div class="topbar">
    <div class="topbar-title"><i class="fa-solid fa-user-pen"></i> Edit Officer</div>
    <div class="topbar-right">
      <a href="${pageContext.request.contextPath}/admin/view-officers" class="btn btn-outline btn-sm">
        <i class="fa-solid fa-arrow-left"></i> Back
      </a>
    </div>
  </div>

  <div class="content-area">
    <c:if test="${not empty error}">
      <div class="alert alert-danger">
        <i class="fa-solid fa-triangle-exclamation"></i> <c:out value="${error}"/>
      </div>
    </c:if>

    <div class="card" style="max-width:700px;">
      <div class="card-header">
        <i class="fa-solid fa-id-badge"></i>
        Editing: <c:out value="${officer.fullName}"/>
        &nbsp;<span class="badge ${officer.active ? 'badge-success' : 'badge-danger'}">
        ${officer.active ? 'Active' : 'Inactive'}
      </span>
      </div>
      <div class="card-body">
        <form action="${pageContext.request.contextPath}/admin/update-officer"
              method="post" novalidate>
          <input type="hidden" name="userId" value="${officer.userId}"/>

          <div class="form-group">
            <label class="form-label">Username (read-only)</label>
            <input type="text" class="form-control" value="<c:out value='${officer.username}'/>"
                   disabled readonly/>
          </div>

          <div class="form-group">
            <label class="form-label">Full Name <span class="req">*</span></label>
            <input type="text" name="fullName" class="form-control"
                   value="<c:out value='${officer.fullName}'/>" required/>
          </div>

          <div class="form-group">
            <label class="form-label">Email Address <span class="req">*</span></label>
            <input type="email" name="email" class="form-control"
                   value="<c:out value='${officer.email}'/>" required/>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Department</label>
              <input type="text" name="department" class="form-control"
                     value="<c:out value='${officer.department}'/>"/>
            </div>
            <div class="form-group">
              <label class="form-label">Badge Number</label>
              <input type="text" name="badgeNumber" class="form-control"
                     value="<c:out value='${officer.badgeNumber}'/>"/>
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">Status</label>
            <select name="isActive" class="form-control">
              <option value="1" ${officer.active ? 'selected' : ''}>Active</option>
              <option value="0" ${officer.active ? '' : 'selected'}>Inactive</option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">New Password
              <small class="text-muted">(leave blank to keep current)</small>
            </label>
            <input type="password" name="newPassword" class="form-control"
                   placeholder="Enter new password to change"/>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn btn-primary">
              <i class="fa-solid fa-floppy-disk"></i> Save Changes
            </button>
            <a href="${pageContext.request.contextPath}/admin/view-officers"
               class="btn btn-outline">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  </div>
</main>

<jsp:include page="/components/footer.jsp"/>
