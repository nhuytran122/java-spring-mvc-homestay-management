<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Chi tiết loại phòng</title>
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
                    <div class="card">
                        <div class="card-body">
                            <div class="row mb-3 d-flex justify-content-between align-items-center">
                                <div class="col-md-6">
                                    <h4 class="card-title mb-0">Chi tiết loại phòng</h4>
                                </div>
                                <div class="col-md-6 text-end">
                                    <div class="btn-group">
                                        <a href="/admin/room-type/update/${roomType.roomTypeID}" class="btn btn-warning btn-sm">
                                            <i class="bi bi-pencil"></i> Sửa
                                        </a>
                                        <button class="btn btn-danger btn-sm"
                                            data-roomType-id="${roomType.roomTypeID}"
                                            data-roomType-name="${roomType.name}"
                                            onclick="checkBeforeDelete(this)"
                                            title="Xóa">
                                            <i class="bi bi-trash"></i> Xóa
                                        </button>
                                        <a href="/admin/room-type" class="btn btn-secondary btn-sm">
                                            <i class="bi bi-arrow-left"></i> Quay lại danh sách
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-8 mx-auto">
                                    <div class="row mb-3">
                                        <div class="col-md-4 fw-bold text-md-start">Tên loại phòng:</div>
                                        <div class="col-md-8">${roomType.name}</div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-md-4 fw-bold text-md-start">Số khách tối đa:</div>
                                        <div class="col-md-8">${roomType.maxGuest} Khách</div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-md-4 fw-bold text-md-start">Giá mỗi giờ:</div>
                                        <div class="col-md-8">
                                            <fmt:formatNumber type="number"
                                            value="${roomType.pricePerHour}" />đ
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-md-4 fw-bold text-md-start">Giá bù giờ:</div>
                                        <div class="col-md-8">
                                             <fmt:formatNumber type="number"
                                             value="${roomType.extraPricePerHour}" />đ
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <div class="col-md-4 fw-bold text-md-start">Mô tả:</div>
                                        <div class="col-md-8">${roomType.description}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="_modal-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
</body>
</html>
