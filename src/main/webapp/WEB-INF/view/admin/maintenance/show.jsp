<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý bảo trì</title>
  <jsp:include page="../layout/import-css.jsp" />
  <meta name="_csrf" content="${_csrf.token}"/>
  <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<body>
  <div class="container-scroller">
    <jsp:include page="../layout/header.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
            <div class="search-form-container my-4">
                <form action="/admin/maintenance" method="get" class="search-form">
                    <input type="text" class="form-control form-control-sm" name="keyword" placeholder="Tìm kiếm bảo trì..." 
                            value="${criteria.keyword}">
                    
                    <select name="branchID" class="form-select form-control form-select-sm">
                        <option value="">Chọn chi nhánh</option>
                        <c:forEach var="branch" items="${listBranches}">
                            <option value="${branch.branchID}" ${branch.branchID == criteria.branchID ? 'selected' : ''}>
                                ${branch.branchName}
                            </option>
                        </c:forEach>
                    </select>
                    <select name="status" class="form-select form-control form-select-sm">
                        <option value="" ${criteria.status == null || criteria.status == '' ? 'selected' : ''}>
                            Tất cả tình trạng
                        </option>
                        <c:forEach var="type" items="${maintenanceStatuses}">
                            <option value="${type}" ${criteria.status == type ? 'selected' : ''}>
                                ${type.displayName} 
                            </option>
                        </c:forEach>
                    </select>

                    <select name="sort" class="form-select form-control form-select-sm">
                        <option value="" ${criteria.sort == '' ? 'selected' : ''}>
                            Không sắp xếp
                        </option>
                        <option value="asc" ${criteria.sort == 'asc' ? 'selected' : ''}>
                            Cũ nhất
                        </option>
                        <option value="desc" ${criteria.sort == 'desc' ? 'selected' : ''}>
                            Mới nhất
                        </option>
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
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h4 class="card-title">Danh sách bảo trì</h4>
                                    <div class="d-flex gap-2">
                                        <a href="/admin/maintenance/create" class="btn btn-primary btn-sm">
                                            <i class="bi bi-plus-circle"></i> Thêm mới
                                        </a>
                                    </div>
                                </div>                                

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Tình trạng</th>
                                                <th>Mô tả</th>
                                                <th>Chi nhánh</th>
                                                <th>Phòng</th>
                                                <th>Người tạo</th>
                                                <th>Ngày tạo</th>
                                                <th>Ngày cập nhật</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty listMaintenances}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy việc bảo trì nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${listMaintenances}">
                                                        <tr style="height: 70px;">
                                                            <td>
                                                                <span class="badge ${item.status == 'COMPLETED' ? 'bg-success' : 
                                                                                    item.status == 'PENDING' ? 'bg-secondary' : 
                                                                                    item.status == 'IN_PROGRESS' ? 'bg-warning' : 
                                                                                    item.status == 'CANCELLED' ? 'bg-danger' : 
                                                                                    item.status == 'ON_HOLD' ? 'bg-primary' : 'bg-info'}">
                                                                    ${item.status.displayName}
                                                                </span>
                                                            </td>
                                                            <td>${item.description}</td>
                                                            <td>${item.branch.branchName}</td>
                                                            <td>${item.room.roomNumber}</td>
                                                            <td>${item.employee.fullName}</td>
                                                            <td>${f:formatLocalDateTime(item.createdAt)}</td>
                                                            <td>${f:formatLocalDateTime(item.updatedAt)}</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/maintenance/${item.requestID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                        <i class="bi bi-eye"></i>
                                                                    </a>
                                                                    <button class="btn btn-warning btn-sm" title="Sửa"
                                                                        onclick="checkBeforeUpdate(this)" 
                                                                            data-request-id="${item.requestID}"
                                                                            data-entity-type="Yêu cầu bảo trì"
                                                                            data-current-status="${item.status}" 
                                                                            data-check-url="/admin/maintenance/can-update/" >
                                                                        <i class="bi bi-pencil"></i>
                                                                    </button>
                                                                    <button class="btn btn-info btn-sm status-update-btn" 
                                                                            data-request-id="${item.requestID}" 
                                                                            data-current-status="${item.status}"
                                                                            title="Cập nhật trạng thái">
                                                                    <i class="bi bi-gear"></i>
                                                                    </button>
                                                                    <button class="btn btn-danger btn-sm" title="Xóa"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                        data-entity-id="${item.requestID}" 
                                                                        data-entity-name="${item.description}" 
                                                                        data-entity-type="Yêu cầu bảo trì" 
                                                                        data-delete-url="/admin/maintenance/delete" 
                                                                        data-check-url="/admin/maintenance/can-delete/" 
                                                                        data-warning-message="Yêu cầu đã được xử lý hoặc bị hủy, không thể xóa."
                                                                        data-id-name="requestID">
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
                    <jsp:param name="url" value="/admin/maintenance" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="extraParams" value="${extraParams}" />
                </jsp:include>
            </div>
        </div>
    </div>
  </div>

  <jsp:include page="../layout/import-js.jsp" />
  <jsp:include page="../layout/partial/_modals-delete.jsp" />
  <jsp:include page="_script-modal-warning-update.jsp" />
  <jsp:include page="_script-modal-update-status.jsp" />
</body>
</html>