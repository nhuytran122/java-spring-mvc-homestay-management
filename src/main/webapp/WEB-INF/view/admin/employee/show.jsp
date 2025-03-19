<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý nhân viên</title>
  <jsp:include page="../layout/import-css.jsp" />
</head>

<body>
  <div class="container-scroller">
    <jsp:include page="../layout/header.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
            <div class="search-form-container my-4">
                <form action="/admin/employee" method="get" class="search-form">
                    <input type="text" class="form-control form-control-sm"
                        name="keyword" 
                        placeholder="Tìm kiếm nhân viên..." 
                        value="${keyword}"/>
                    <select name="isWorking" class="form-select form-select-sm">
                        <option value="" ${criteria.isWorking == null ? 'selected' : ''}>Chọn trạng thái</option>
                        <c:forEach var="option" items="${isWorkingOptions}">
                            <option value="${option}" ${option == criteria.isWorking ? 'selected' : ''}>
                                ${option ? 'Đang làm việc' : 'Nghỉ làm'}
                            </option>
                        </c:forEach>
                    </select>
                    <select name="roleId" class="form-select form-select-sm" >
                        <option value="">Chọn vai trò</option>
                        <c:forEach var="role" items="${listRoles}">
                            <option value="${role.roleID}" ${role.roleID == criteria.roleID ? 'selected' : ''}>
                                ${role.roleName}
                            </option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary btn-sm p-2">
                        <i class="bi bi-search"></i>
                    </button>
                </form>
            </div>
            
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-md-12 grid-margin stretch-card">
                        <div class="card position-relative">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h4 class="card-title">Danh sách nhân viên</h4>
                                    <a href="/admin/employee/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới nhân viên
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th style="width: 150px;">Hình ảnh</th>
                                                <th>Tên nhân viên</th>
                                                <th>Số điện thoại</th>
                                                <th>Email</th>
                                                <th>Vai trò</th>
                                                <th>Mức lương</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty listEmployees}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy nhân viên nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="employee" items="${listEmployees}">
                                                        <tr>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty employee.avatar}">
                                                                        <img src="/images/avatar/${employee.avatar}" class="img-fluid rounded" style="width: auto; height: 100px; object-fit: cover;">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="/images/avatar/default-img.jpg" class="img-fluid rounded" style="width: auto; height: 100px; object-fit: cover;">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>${employee.fullName}</td>
                                                            <td>${employee.phone}</td>
                                                            <td>${employee.email}</td>
                                                            <td>${employee.role.roleName}</td>
                                                            <td><fmt:formatNumber type="number"
                                                                value="${employee.salary}" /> đ</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/employee/${employee.employeeID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                        <i class="bi bi-eye"></i>
                                                                    </a>
                                                                    <a href="/admin/employee/update/${employee.employeeID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>

                                                                    <button class="btn btn-danger btn-sm" title="Xóa"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${employee.employeeID}" 
                                                                            data-entity-name="${employee.fullName}" 
                                                                            data-entity-type="Nhân viên" 
                                                                            data-delete-url="/admin/employee/delete" 
                                                                            data-check-url="/admin/employee/can-delete/" 
                                                                            data-id-name="employeeID">
                                                                        <i class="bi bi-trash"></i>
                                                                    </button>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <jsp:include page="../layout/partial/_pagination-with-param.jsp">
                    <jsp:param name="url" value="/admin/employee" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="extraParams" value="${extraParams}" />
                </jsp:include>
            </div>
        </div>
    </div>
  </div>

  <jsp:include page="../layout/partial/_modals-delete.jsp" />
  <jsp:include page="../layout/import-js.jsp" />
</body>
</html>
