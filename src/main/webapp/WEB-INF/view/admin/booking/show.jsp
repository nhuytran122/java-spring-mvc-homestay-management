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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
</head>
<body>
    <div class="container-scroller">
        <jsp:include page="../layout/header.jsp" />
        <div class="container-fluid page-body-wrapper">
            <jsp:include page="../layout/theme-settings.jsp" />
            <jsp:include page="../layout/sidebar.jsp" />
            <div class="main-panel">
                <div class="search-form-container my-4">
                    <form action="/admin/booking" method="get" class="search-form">
                        <input type="text" class="form-control form-control-sm"
                               name="keyword" 
                               placeholder="Tìm kiếm booking (tên khách hàng)..." 
                               value="${criteria.keyword}"/>
                        <select name="branchID" class="form-select form-control form-select-sm">
                            <option value="">Chọn chi nhánh</option>
                            <c:forEach var="branch" items="${listBranches}">
                                <option value="${branch.branchID}" ${branch.branchID == criteria.branchID ? 'selected' : ''}>
                                    ${branch.branchName}
                                </option>
                            </c:forEach>
                        </select>

                        <select name="roomTypeID" class="form-select form-control form-select-sm">
                            <option value="">Chọn loại phòng</option>
                            <c:forEach var="roomType" items="${listRoomTypes}">
                                <option value="${roomType.roomTypeID}" ${roomType.roomTypeID == criteria.roomTypeID ? 'selected' : ''}>
                                    ${roomType.name}
                                </option>
                            </c:forEach>
                        </select>
                        <select name="status" class="form-select form-control form-select-sm">
                            <option value="" ${criteria.status == null || criteria.status == '' ? 'selected' : ''}>
                                Tất cả tình trạng
                            </option>
                            <c:forEach var="type" items="${bookingStatuses}">
                                <option value="${type}" ${criteria.status == type ? 'selected' : ''}>
                                    ${type.displayName} 
                                </option>
                            </c:forEach>
                        </select>
                        <input type="text" id="timeRange" name="timeRange" class="form-control daterange-picker" 
                               value="${criteria.timeRange}" placeholder="Chọn khoảng thời gian...">
                        <select name="sort" class="form-select form-control form-select-sm">
                            <option value="asc" ${criteria.sort == 'asc' ? 'selected' : ''}>
                                Check-in sớm nhất
                            </option>
                            <option value="desc" ${criteria.sort == 'desc' ? 'selected' : ''}>
                                Check-in muộn nhất
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
                                                    <th>Tên khách hàng</th>
                                                    <th>Số điện thoại</th>
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
                                                            <tr style="height: 70px;">
                                                                <td>
                                                                    <a href="/admin/customer/${booking.customer.customerID}" 
                                                                       class="text-dark text-decoration-none" 
                                                                       title="Xem chi tiết">
                                                                        ${booking.customer.user.fullName}
                                                                    </a>
                                                                </td>
                                                                
                                                                <td>${booking.customer.user.phone}</td>
                                                                <td>
                                                                    <a href="/admin/branch/${booking.room.branch.branchID}" 
                                                                       class="text-dark text-decoration-none" 
                                                                       title="Xem chi tiết">
                                                                        ${booking.room.branch.branchName}
                                                                    </a>
                                                                </td>
                                                                <td>${booking.room.roomNumber}</td>
                                                                <td>${f:formatLocalDateTime(booking.checkIn)}</td>
                                                                <td>${f:formatLocalDateTime(booking.checkOut)}</td>
                                                                <td>
                                                                    <span class="badge ${booking.status == 'COMPLETED' ? 'bg-success' : 
                                                                                        booking.status == 'CANCELLED' ? 'bg-danger' : 
                                                                                        booking.status == 'CONFIRMED' ? 'bg-primary' : 'bg-info'}">
                                                                        ${booking.status.displayName}
                                                                    </span>
                                                                </td>
                                                                <td><fmt:formatNumber type="number" value="${booking.totalAmount}" />đ</td>
                                                                <td><fmt:formatNumber type="number" value="${booking.paidAmount != null ? booking.paidAmount : 0}" />đ</td>
                                                                <td>${f:formatLocalDateTime(booking.createdAt)}</td>
                                                                <td>
                                                                    <div class="btn-group" role="group">
                                                                        <a href="/admin/booking/${booking.bookingID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                            <i class="bi bi-eye"></i>
                                                                        </a>
                                                                        <a href="/admin/booking/update/${booking.bookingID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                            <i class="bi bi-pencil"></i>
                                                                        </a>
                                                                        <button class="btn btn-danger btn-sm" title="Xóa"
                                                                                onclick="checkBeforeDelete(this)" 
                                                                                data-entity-id="${booking.bookingID}" 
                                                                                data-entity-name="${booking.customer.fullName}" 
                                                                                data-entity-type="Lịch đặt phòng của khách hàng" 
                                                                                data-delete-url="/admin/booking/delete" 
                                                                                data-check-url="/admin/booking/can-delete/" 
                                                                                data-id-name="bookingID">
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
                        <jsp:param name="url" value="/admin/booking" />
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
    <script>
        $(document).ready(function () {
            
            $('.daterange-picker').daterangepicker({
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