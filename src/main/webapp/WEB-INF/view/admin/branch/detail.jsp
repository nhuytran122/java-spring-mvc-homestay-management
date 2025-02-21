<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Chi tiết chi nhánh</title>
    <jsp:include page="../layout/import-css.jsp" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
                                    <h4 class="card-title">Chi tiết chi nhánh</h4>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <c:choose>
                                                <c:when test="${not empty branch.image}">
                                                    <img src="/images/branch/${branch.image}" class="img-fluid rounded" style="width: 100%; height: auto; object-fit: cover;">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="/images/branch/default-img.jpg" class="img-fluid rounded" style="width: 100%; height: auto; object-fit: cover;">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="col-md-8">
                                            <p><strong>Tên chi nhánh:</strong> ${branch.branchName}</p>
                                            <p><strong>Địa chỉ:</strong> ${branch.address}</p>
                                            <p><strong>Số điện thoại:</strong> ${branch.phone}</p>
                                            <div class="btn-group">
                                                <a href="/admin/branch/update/${branch.branchID}" class="btn btn-warning btn-sm">
                                                    <i class="bi bi-pencil"></i> Sửa
                                                </a>
                                                <button class="btn btn-danger btn-sm" data-branch-id="${branch.branchID}" data-branch-name="${branch.branchName}" onclick="checkBeforeDelete(this)">
                                                    <i class="bi bi-trash"></i> Xóa
                                                </button>
                                                <a href="/admin/branch" class="btn btn-secondary btn-sm">
                                                    <i class="bi bi-arrow-left"></i>
                                                    Quay lại danh sách</a>
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

    <jsp:include page="partial-modal-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
</body>

</html>
