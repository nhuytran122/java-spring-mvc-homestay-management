<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý quy tắc chung</title>
    <jsp:include page="../layout/import-css.jsp" />
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .rule-icon {
            width: 24px;
            color: #6c757d;
        }
        .action-buttons {
            text-align: right;
        }
    </style>
</head>

<body>
    <div class="container-scroller">
        <jsp:include page="../layout/header.jsp" />
        <div class="container-fluid page-body-wrapper">
            <jsp:include page="../layout/theme-settings.jsp" />
            <jsp:include page="../layout/sidebar.jsp" />
            <div class="main-panel">
                <div class="content-wrapper">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h4 class="card-title">Danh sách quy tắc chung</h4>
                        <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addRuleModal">
                            <i class="fa fa-plus-circle"></i> Thêm mới quy tắc
                        </button>
                    </div>

                    <div class="bg-white rounded shadow-sm p-4">
                        <c:forEach var="rule" items="${activeRules}">
                            <div class="row g-3 align-items-center">
                                <div class="col-12 col-md-4 d-flex align-items-center">
                                    <i class="fas ${rule.icon} rule-icon me-2"></i>
                                    <span class="fw-medium">${rule.ruleTitle}</span>
                                </div>
                                <div class="col-12 col-md-6">
                                    <p class="text-muted mb-0">${rule.description}</p>
                                </div>
                                <div class="col-12 col-md-2 action-buttons">
                                    <button class="btn btn-warning btn-sm me-2" onclick="openEditModal(this)" 
                                        data-rule-id="${rule.ruleId}" 
                                        data-rule-title="${rule.ruleTitle}" 
                                        data-rule-description="${rule.description}">
                                        <i class="bi bi-pencil"></i> Sửa
                                    </button>

                                    <button class="btn btn-danger btn-sm"
                                        onclick="checkBeforeDelete(this)" 
                                            data-entity-id="${rule.ruleId}" 
                                            data-entity-name="${rule.ruleTitle}" 
                                            data-entity-type="Quy tắc" 
                                            data-delete-url="/admin/homestay-infor/rule/delete" 
                                            data-id-name="ruleId">
                                            <i class="bi bi-trash"></i> Xóa
                                    </button>
                                </div>
                            </div>
                            <hr class="my-3">
                        </c:forEach>
                    </div>
                </div>
                <jsp:include page="../layout/footer.jsp" />
            </div>
        </div>
    </div>

    <%@ include file="_modal-add.jsp" %>
    <jsp:include page="../layout/partial/_modal-delete-not-check-can-delete.jsp" />
    <jsp:include page="_modal-update.jsp" />
    <jsp:include page="_modal-error.jsp" />
    
    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="_script-handle.jsp" />
    
</body>
</html>
