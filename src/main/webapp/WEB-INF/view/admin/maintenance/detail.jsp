<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Chi tiết bảo trì</title>
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
                <div class="content-wrapper">
                    <div class="row">
                        <div class="col-md-12 grid-margin stretch-card">
                            <div class="card position-relative">
                                <div class="card-body">
                                    <div class="row mb-3 d-flex justify-content-between align-items-center">
                                        <div class="col-md-6">
                                            <h4 class="card-title mb-0">Chi tiết việc bảo trì</h4>
                                        </div>
                                        <div class="col-md-6 text-end">
                                            <div class="btn-group">
                                                <button class="btn btn-warning btn-sm" title="Sửa"
                                                    onclick="checkBeforeUpdate(this)" 
                                                        data-request-id="${request.requestId}"
                                                        data-entity-type="Yêu cầu bảo trì"
                                                        data-current-status="${request.status}" 
                                                        data-check-url="/admin/maintenance/can-update/" >
                                                    <i class="bi bi-pencil"></i>
                                                </button>
                                                <button class="btn btn-info btn-sm status-update-btn" 
                                                        data-request-id="${request.requestId}" 
                                                        data-current-status="${request.status}"
                                                        title="Cập nhật trạng thái">
                                                <i class="bi bi-gear"></i>
                                                </button>
                                                <button class="btn btn-danger btn-sm"
                                                    onclick="checkBeforeDelete(this)" 
                                                        data-entity-id="${request.requestId}" 
                                                        data-entity-name="${request.description}" 
                                                        data-entity-type="Yêu cầu bảo trì" 
                                                        data-delete-url="/admin/maintenance/delete" 
                                                        data-id-name="requestId">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                                <a href="/admin/maintenance" class="btn btn-secondary btn-sm">
                                                    <i class="bi bi-arrow-left"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-3">
                                            <c:choose>
                                                <c:when test="${not empty request.image}">
                                                    <img src="/images/maintenance/${request.image}" class="img-fluid rounded" style="width: 100%; height: auto; object-fit: cover;">
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    
                                        <div class="col-md-9">
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Chi nhánh:</div>
                                                <div class="col-md-8">${request.branch.branchName}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Phòng:</div>
                                                <div class="col-md-8">${request.room.roomNumber}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Mô tả:</div>
                                                <div class="col-md-8">${request.description}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Tình trạng:</div>
                                                <div class="col-md-8">
                                                        <span class="badge ${request.status == 'COMPLETED' ? 'bg-success' : 
                                                        request.status == 'PENDING' ? 'bg-secondary' : 
                                                        request.status == 'IN_PROGRESS' ? 'bg-warning' : 
                                                        request.status == 'CANCELLED' ? 'bg-danger' : 
                                                        request.status == 'ON_HOLD' ? 'bg-primary' : 'bg-info'}">
                                                        ${request.status.displayName}
                                                    </span></div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Người tạo:</div>
                                                <div class="col-md-8">${request.employee.user.fullName}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Ngày tạo:</div>
                                                <div class="col-md-8">${f:formatLocalDateTime(request.createdAt)}</div>
                                            </div> 
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Ngày cập nhật:</div>
                                                <div class="col-md-8">${f:formatLocalDateTime(request.updatedAt)}</div>
                                            </div>  
                                            <input type="hidden" data-view-detail="true">                                        
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <jsp:include page="../layout/footer.jsp" />
            </div>
        </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="../layout/partial/_modal-delete-not-check-can-delete.jsp" />
    <jsp:include page="_script-modal-warning-update.jsp" />
    <jsp:include page="_script-modal-update-status.jsp" />
</body>

</html>
