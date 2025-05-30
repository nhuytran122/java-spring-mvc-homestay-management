<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý booking</title>
    <jsp:include page="../layout/import-css.jsp" />
    <meta name="_csrf" content="${_csrf.token}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
</head>
<body>
    <div class="container-scroller">
        <jsp:include page="../layout/header.jsp" />
        <div class="container-fluid page-body-wrapper">
            <jsp:include page="../layout/theme-settings.jsp" />
            <jsp:include page="../layout/sidebar.jsp" />
            <div class="main-panel">
                <div class="search-form-container my-4" style="max-width:900px; width:100%; margin:0 auto; padding:10px 15px; box-sizing:border-box;">
                    <form action="/admin/booking" method="get" class="search-form" style="display:grid; grid-template-columns:repeat(auto-fit, minmax(180px,1fr)); gap:12px 15px; width:100%; align-items:center;">
                        
                        <c:set
                            var="cBranchID"
                            value="${criteria.branchID}"
                        />
                        <c:set
                            var="cRoomTypeID"
                            value="${criteria.roomTypeID}"
                        />
                        <input type="text" class="form-control form-control-sm"
                            name="keyword" 
                            placeholder="Tìm kiếm booking (tên khách hàng)..."
                            value="${criteria.keyword}"
                            style="width:100%; height:38px; box-sizing:border-box;"/>
                        <select name="branchID" class="form-select form-control form-select-sm" 
                                style="width:100%; height:38px; box-sizing:border-box;">
                            <option value="">Chọn chi nhánh</option>
                            <c:forEach var="branch" items="${listBranches}">
                                <c:set
                                    var="branchID"
                                    value="${branch.branchID}"
                                />
                                <option value="${branchID}" ${branchID == cBranchID ? 'selected' : ''}>
                                    ${branch.branchName}
                                </option>
                            </c:forEach>
                        </select>

                        <select name="roomTypeID" class="form-select form-control form-select-sm" 
                                style="width:100%; height:38px; box-sizing:border-box;">
                            <option value="">Chọn loại phòng</option>
                            <c:forEach var="roomType" items="${listRoomTypes}">
                                <c:set
                                    var="roomTypeID"
                                    value="${roomType.roomTypeID}"
                                />
                                <option value="${roomTypeID}" ${roomTypeID == cRoomTypeID ? 'selected' : ''}>
                                    ${roomType.name}
                                </option>
                            </c:forEach>
                        </select>
                        
                        <select name="status" class="form-select form-control form-select-sm"
                                style="width:100%; height:38px; box-sizing:border-box;">
                                <c:set
                                    var="status"
                                    value="${criteria.status}"
                                />
                            <option value="" ${status == null || status == '' ? 'selected' : ''}>
                                Tất cả tình trạng
                            </option>
                            <c:forEach var="type" items="${bookingStatuses}">
                                <option value="${type}" ${status == type ? 'selected' : ''}>
                                    ${type.displayName} 
                                </option>
                            </c:forEach>
                        </select>

                        <input type="text" id="timeRange" name="timeRange" class="form-control dateRange-picker" 
                            value="${criteria.timeRange}" placeholder="Chọn khoảng thời gian..."
                            style="width:100%; height:38px; box-sizing:border-box;"/>
                        <button type="submit" class="btn btn-primary btn-sm p-2" 
                                style="height:38px; padding:0 20px; font-size:14px; cursor:pointer; justify-self:start;">
                            <i class="bi bi-search"></i>
                        </button>
                    </form>
                </div>

                
                <div class="content-wrapper">
                    <c:if test="${not empty errorMessage}">
                        <div id="error-alert" class="alert alert-danger mt-2" role="alert">
                            ${errorMessage}
                        </div>
                    </c:if>
                    <div class="row">
                        <div class="col-md-12 grid-margin stretch-card">
                            <div class="card position-relative">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h4 class="card-title">Danh sách booking</h4>
                                        <a href="/admin/booking/create" class="btn btn-primary btn-sm">
                                            <i class="bi bi-plus-circle"></i> Thêm mới
                                        </a>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-hover">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Tên khách hàng</th>
                                                    <th>Chi nhánh</th>
                                                    <th>Phòng</th>
                                                    <th>Checkin</th>
                                                    <th>Checkout</th>
                                                    <th>Trạng thái</th>
                                                    <th>Tổng tiền</th>
                                                    <th>Đã thanh toán</th>
                                                    <th>Ngày đặt</th>
                                                    <th>Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty listBookings}">
                                                        <tr>
                                                            <td colspan="12" class="text-center text-danger">Không tìm thấy lịch đặt phòng nào.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="booking" items="${listBookings}">
                                                            <c:set
                                                                var="bookingID"
                                                                value="${booking.bookingID}"
                                                            />
                                                            <tr style="height: 70px;">
                                                                <td>${bookingID}</td>
                                                                <td>
                                                                    <c:set
                                                                        var="customer"
                                                                        value="${booking.customer}"
                                                                    />
                                                                    <a href="/admin/customer/${customer.customerID}" 
                                                                       class="text-dark text-decoration-none" 
                                                                       title="Xem chi tiết">
                                                                        ${customer.user.fullName}
                                                                    </a>
                                                                </td>
                                                                
                                                                <td>
                                                                    <c:set
                                                                        var="room"
                                                                        value="${booking.room}"
                                                                    />
                                                                    <c:set
                                                                        var="branch"
                                                                        value="${room.branch}"
                                                                    />
                                                                    <a href="/admin/branch/${branch.branchID}" 
                                                                       class="text-dark text-decoration-none" 
                                                                       title="Xem chi tiết">
                                                                        ${branch.branchName}
                                                                    </a>
                                                                </td>
                                                                <td>${room.roomNumber}</td>
                                                                <td>${f:formatLocalDateTime(booking.checkIn)}</td>
                                                                <td>${f:formatLocalDateTime(booking.checkOut)}</td>
                                                                <td>
                                                                    <c:set
                                                                        var="bStatus"
                                                                        value="${booking.status}"
                                                                    />
                                                                    <span class="badge ${bStatus == 'COMPLETED' ? 'bg-success' : 
                                                                                        bStatus == 'CANCELLED' ? 'bg-danger' : 
                                                                                        bStatus == 'CONFIRMED' ? 'bg-primary' : 'bg-info'}">
                                                                        ${bStatus.displayName}
                                                                    </span>
                                                                </td>
                                                                <c:set
                                                                    var="paidAmount"
                                                                    value="${booking.paidAmount}"
                                                                />
                                                                <c:set
                                                                    var="totalAmount"
                                                                    value="${booking.totalAmount}"
                                                                />
                                                                <td><fmt:formatNumber type="number" value="${totalAmount}" />đ</td>
                                                                <td><fmt:formatNumber type="number" value="${paidAmount != null ? paidAmount : 0}" />đ</td>
                                                                <td>${f:formatLocalDateTime(booking.createdAt)}</td>
                                                                <td>
                                                                    <div class="btn-group" role="group">
                                                                        <a href="/admin/booking/${bookingID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                            <i class="bi bi-eye"></i>
                                                                        </a>
                                                                        <button
                                                                            class="btn btn-danger btn-sm"
                                                                            title="Hủy đặt phòng"
                                                                            onclick="checkBeforeCancel(this)"
                                                                            data-entity-id="${bookingID}"
                                                                            data-id-name="bookingID"
                                                                            data-role="admin"
                                                                            >
                                                                            <i class="bi bi-x-circle"></i>
                                                                            
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
                        <jsp:param name="url" value="/admin/booking" />
                        <jsp:param name="currentPage" value="${currentPage}" />
                        <jsp:param name="totalPages" value="${totalPages}" />
                        <jsp:param name="extraParams" value="${extraParams}" />
                    </jsp:include>
                </div>
                <jsp:include page="../layout/footer.jsp" />
            </div>
        </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="../../shared/partial/_modal-refund.jsp" />
    <jsp:include
      page="../../shared/partial/_script-handle-cancel-booking.jsp"
    />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
    <script>
        $(document).ready(function () {
            window.onload = function() {
                var alertBox = document.getElementById('error-alert');
                if(alertBox) {
                    setTimeout(function() {
                        alertBox.style.display = 'none';
                    }, 5000);
                }
            };
            $('.dateRange-picker').daterangepicker({
                locale: {
                    format: 'DD/MM/YYYY',
                    daysOfWeek: ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
                monthNames: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12']
                },
            })
        });
    </script>
</body>
</html>