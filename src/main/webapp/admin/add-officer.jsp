<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Add Officer" scope="request"/>
<jsp:include page="/components/header.jsp"/>
<jsp:include page="/components/sidebar-admin.jsp"/>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-title"><i class="fa-solid fa-user-plus"></i> Add Officer</div>
        <div class="topbar-right">
            <a href="${pageContext.request.contextPath}/admin/view-officers" class="btn btn-outline btn-sm">
                <i class="fa-solid fa-arrow-left"></i> Back to Officers
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
                <i class="fa-solid fa-id-badge"></i> Officer Registration Form
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/add-officer" method="post" novalidate>
                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Username <span class="req">*</span></label>
                            <input type="text" name="username" class="form-control"
                                   value="<c:out value='${username}'/>"
                                   placeholder="e.g. jsmith" required/>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Password <span class="req">*</span></label>
                            <input type="password" name="password" class="form-control"
                                   placeholder="Minimum 8 characters" required/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Full Name <span class="req">*</span></label>
                        <input type="text" name="fullName" class="form-control"
                               value="<c:out value='${fullName}'/>"
                               placeholder="e.g. James Smith" required/>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Email Address <span class="req">*</span></label>
                        <input type="email" name="email" class="form-control"
                               value="<c:out value='${email}'/>"
                               placeholder="officer@agency.gov" required/>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Department</label>
                            <input type="text" name="department" class="form-control"
                                   value="<c:out value='${department}'/>"
                                   placeholder="e.g. Field Operations"/>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Badge Number</label>
                            <input type="text" name="badgeNumber" class="form-control"
                                   value="<c:out value='${badgeNumber}'/>"
                                   placeholder="e.g. OFC-105"/>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="fa-solid fa-user-plus"></i> Register Officer
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
