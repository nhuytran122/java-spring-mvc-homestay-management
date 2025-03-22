<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Chi tiết lịch đặt phòng</title>
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
                                            <h4 class="card-title mb-0">Chi tiết lịch đặt phòng</h4>
                                        </div>
                                        <div class="col-md-6 text-end">
                                            <div class="btn-group">
                                                <c:if test="${booking.status.toString() != 'CANCELLED'}">
                                                    <button class="btn btn-danger btn-sm" title="Hủy đặt phòng"
                                                        onclick="checkBeforeCancel(this)" 
                                                            data-entity-id="${booking.bookingID}"
                                                            data-id-name="bookingID"
                                                            data-check-url="/admin/booking/booking-history/can-cancel/" 
                                                            data-cancel-url="/admin/booking/booking-history/cancel">
                                                        <i class="bi bi-x-circle"></i>
                                                        Hủy đặt phòng
                                                    </button>
                                                </c:if>
                                                
                                                <a href="/admin/booking" class="btn btn-secondary btn-sm">
                                                    <i class="bi bi-arrow-left"></i> Trở về
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Khách hàng:</div>
                                                <div class="col-md-8">${booking.customer.fullName}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Chi nhánh:</div>
                                                <div class="col-md-8">${booking.room.branch.branchName}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Phòng:</div>
                                                <div class="col-md-8">${booking.room.roomNumber}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Loại phòng:</div>
                                                <div class="col-md-8">${booking.room.roomType.name}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Thời gian:</div>
                                                <div class="col-md-8">${f:formatLocalDateTime(booking.checkIn)} - ${f:formatLocalDateTime(booking.checkOut)}</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Số lượng khách:</div>
                                                <div class="col-md-8">${booking.guestCount}</div>
                                            </div> 
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Tình trạng:</div>
                                                <div class="col-md-8">
                                                    <span class="badge ${booking.status == 'COMPLETED' ? 'bg-success' : 
                                                    booking.status == 'CANCELLED' ? 'bg-danger' : 
                                                    booking.status == 'BOOKED' ? 'bg-primary' : 'bg-info'}">
                                                        ${booking.status.displayName}
                                                    </span>
                                                </div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Tổng tiền:</div>
                                                <div class="col-md-8"><fmt:formatNumber type="number" value="${booking.totalAmount}" />đ</div>
                                            </div>
                                            <div class="row mb-3 d-flex align-items-center">
                                                <div class="col-md-4 fw-bold text-md-start">Đã thanh toán:</div>
                                                <div class="col-md-8"><fmt:formatNumber type="number" value="${booking.paidAmount != null ? booking.paidAmount : 0}" />đ</div>
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

    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="../../client/booking/_modal-cancel.jsp" />
</body>

</html>
