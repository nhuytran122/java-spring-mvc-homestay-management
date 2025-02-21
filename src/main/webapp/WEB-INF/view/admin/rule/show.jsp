<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý quy tắc chung</title>
    <jsp:include page="../layout/import-css.jsp" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
                        <a href="/admin/homestay-infor/rule/create" class="btn btn-primary btn-sm">
                            <i class="fa fa-plus-circle"></i> Thêm mới
                        </a>
                    </div>

                    <div class="bg-white rounded shadow-sm p-4">
                        <c:forEach var="rule" items="${rules}">
                            <div class="row g-3 align-items-center">
                                <div class="col-12 col-md-3 d-flex align-items-center">
                                    <i class="fas fa-info-circle rule-icon me-2"></i>
                                    <span class="fw-medium">${rule.ruleTitle}</span>
                                </div>
                                <div class="col-12 col-md-5">
                                    <p class="text-muted mb-0">${rule.description}</p>
                                </div>
                                <div class="col-12 col-md-2 text-center">
                                    <c:choose>
                                        <c:when test="${rule.isHidden}">
                                            <span class="badge bg-secondary">Đang ẩn</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-success">Hiển thị</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="col-12 col-md-2 action-buttons">
                                    <a href="/admin/homestay-infor/rule/update/${rule.ruleID}" class="btn btn-warning btn-sm me-2">
                                        <i class="fa fa-edit"></i> Sửa
                                    </a>
                                    <c:if test="${!rule.isFixed}">
                                        <button class="btn btn-danger btn-sm" onclick="checkBeforeDelete(this)"
                                            data-rule-id="${rule.ruleID}" data-rule-description="${rule.ruleTitle}">
                                            <i class="fa fa-trash"></i> Xóa
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                            <hr class="my-3">
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title text-danger">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> Xác nhận xóa
                    </h5>
                </div>
                <div class="modal-body">
                    Bạn có chắc chắn muốn xóa quy tắc <b class="text-primary" id="titleConfirm"></b> không?
                </div>
                <div class="modal-footer">
                    <form action="/admin/homestay-infor/rule/delete" method="post">
                        <input type="hidden" name="ruleID" id="ruleIdInput">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-danger">Xóa</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <script>
        function checkBeforeDelete(button) {
            let ruleID = button.getAttribute("data-rule-id");
            let question = button.getAttribute("data-rule-description");
            $("#titleConfirm").text(question);
            $("#ruleIdInput").val(ruleID);
            $("#deleteConfirmModal").modal("show");
        }
    </script>
</body>
</html>
