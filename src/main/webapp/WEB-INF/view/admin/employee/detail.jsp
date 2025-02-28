<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Chi tiết nhân viên</title>
    <jsp:include page="../layout/import-css.jsp" />
</head>

<body>
    <div class="container-scroller">
        <jsp:include page="../layout/header.jsp" />
        <div class="container-fluid page-body-wrapper">
            <jsp:include page="../layout/theme-settings.jsp" />
            <jsp:include page="../layout/sidebar.jsp" />
            <div class="main-panel">
                <div class="content-wrapper">
                    <div class="row">
                        <div class="col-md-12 grid-margin stretch-card">
                            <div class="card position-relative">
                                <div class="card-body">
                                    <div class="row mb-3 d-flex justify-content-between align-items-center">
                                        <div class="col-md-6">
                                            <h4 class="card-title mb-0">Chi tiết nhân viên</h4>
                                        </div>
                                        <div class="col-md-6 text-end">
                                            <div class="btn-group">
                                                <a href="/admin/employee/update/${employee.employeeID}" class="btn btn-warning btn-sm">
                                                    <i class="bi bi-pencil"></i> Sửa
                                                </a>
                                                <button class="btn btn-danger btn-sm" title="Xóa"
                                                    onclick="checkBeforeDelete(this)" 
                                                        data-entity-id="${employee.employeeID}" 
                                                        data-entity-name="${employee.fullName}" 
                                                        data-entity-type="Nhân viên" 
                                                        data-delete-url="/admin/employee/delete" 
                                                        data-check-url="/admin/employee/can-delete/" 
                                                        data-id-name="employeeID">
                                                    <i class="bi bi-trash"></i> Xóa
                                                </button>
                                                <a href="/admin/employee" class="btn btn-secondary btn-sm">
                                                    <i class="bi bi-arrow-left"></i> Quay lại danh sách
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-3">
                                            <c:choose>
                                                <c:when test="${not empty employee.avatar}">
                                                    <img src="/images/employee/${employee.avatar}" class="img-fluid rounded" style="width: 100%; height: auto; object-fit: cover;">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="/images/employee/default-img.jpg" class="img-fluid rounded" style="width: 100%; height: auto; object-fit: cover;">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    
                                        <div class="col-md-9">
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Tên nhân viên:</div>
                                                <div class="col-md-8">${employee.fullName}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Số điện thoại:</div>
                                                <div class="col-md-8">${employee.phone}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Chức vụ :</div>
                                                <div class="col-md-8">${employee.role.roleName}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Mức lương:</div>
                                                <div class="col-md-8"><fmt:formatNumber type="number"
                                                    value="${employee.salary}" /> đ</div>
                                            </div> 
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Email:</div>
                                                <div class="col-md-8">${employee.email}</div>
                                            </div>    
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Địa chỉ:</div>
                                                <div class="col-md-8">${employee.address}</div>
                                            </div>                                           
                                        </div>
                                    </div>
                                    
                                    
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/partial/_modals-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
</body>

</html>
