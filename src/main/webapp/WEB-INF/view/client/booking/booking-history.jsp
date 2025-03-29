<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch sử đặt phòng - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/booking-history-style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
</head>
<body>
    <jsp:include page="../layout/header.jsp" />

    <jsp:include page="../layout/partial/_welcome-banner-profile.jsp" />
 
    <div class="container mb-4">
        <div class="search-bar p-3 bg-white rounded shadow-sm">
            <form class="row g-3">
                <div class="col-md-3">
                    <label class="form-label">Khoảng thời gian checkin</label>
                    <input type="text" id="timeRange" name="timeRange" class="form-control daterange-picker" 
                               value="${criteria.timeRange}" placeholder="Chọn khoảng thời gian...">
                </div>

                <div class="col-md-2">
                    <label class="form-label">Chi nhánh</label>
                    <select name="branchID" class="form-select">
                        <option value="">Chọn chi nhánh</option>
                        <c:forEach var="branch" items="${listBranches}">
                            <option value="${branch.branchID}" ${branch.branchID == criteria.branchID ? 'selected' : ''}>
                                ${branch.branchName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Loại phòng</label>
                    <select name="roomTypeID" class="form-select">
                        <option value="">Chọn loại phòng</option>
                        <c:forEach var="roomType" items="${listRoomTypes}">
                            <option value="${roomType.roomTypeID}" ${roomType.roomTypeID == criteria.roomTypeID ? 'selected' : ''}>
                                ${roomType.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Tình trạng</label>
                    <select name="status" class="form-select">
                        <option value="" ${criteria.status == null || criteria.status == '' ? 'selected' : ''}>
                            Tất cả tình trạng
                        </option>
                        <c:forEach var="type" items="${bookingStatuses}">
                            <option value="${type}" ${criteria.status == type ? 'selected' : ''}>
                                ${type.displayName} 
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Sắp xếp</label>
                    <select name="sort" class="form-select">
                        <option value="asc" ${criteria.sort == 'asc' ? 'selected' : ''}>
                            Check-in sớm nhất
                        </option>
                        <option value="desc" ${criteria.sort == 'desc' ? 'selected' : ''}>
                            Check-in muộn nhất
                        </option>
                    </select>
                </div>
                <div class="col-md-1 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100"><i class="bi bi-search"></i></button>
                </div>
            </form>
        </div>
    </div>

    <div class="container main-content">
        <div class="row mb-4 g-3">
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Tất cả</p>
                                <h3 class="mb-0">${countTotal}</h3>
                            </div>
                            <div class="stat-icon bg-primary-subtle">
                                <i class="bi bi-calendar-check text-primary"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Đang chờ</p>
                                <h3 class="mb-0">${countPending}</h3>
                            </div>
                            <div class="stat-icon bg-success-subtle">
                                <i class="bi bi-check-circle text-success"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Đã xác nhận</p>
                                <h3 class="mb-0">${countConfirmed}</h3>
                            </div>
                            <div class="stat-icon bg-success-subtle">
                                <i class="bi bi-check-circle text-success"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Đã hoàn tất</p>
                                <h3 class="mb-0">${countCompleted}</h3>
                            </div>
                            <div class="stat-icon bg-info-subtle">
                                <i class="bi bi-check-circle-fill"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Đã hủy</p>
                                <h3 class="mb-0">${countCancelled}</h3>
                            </div>
                            <div class="stat-icon bg-danger-subtle">
                                <i class="bi bi-x-circle text-danger"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card booking-card mb-4">
            <div class="card-header bg-transparent d-flex justify-content-between align-items-center">
                <h2>Lịch đặt phòng</h2>
            </div>
            <c:choose>
                <c:when test="${empty listBookings}">
                    <tr>
                        <td colspan="12" class="text-center text-danger">Không tìm thấy lịch đặt phòng nào.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="booking" items="${listBookings}">            
                        <div class="card-body">
                            <div class="upcoming-booking">
                                <c:choose>
                                    <c:when test="${not empty booking.room.thumbnail}">
                                        <img src="/images/room/${booking.room.thumbnail}" class="booking-image">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="/images/room/default-img.jpg" class="booking-image">
                                    </c:otherwise>
                                </c:choose>                                
                                <div class="booking-details">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <span class="badge ${booking.status == 'COMPLETED' ? 'bg-success' : 
                                        booking.status == 'CANCELLED' ? 'bg-danger' : 
                                        booking.status == 'CONFIRMED' ? 'bg-primary' : 'bg-info'}">
                                        ${booking.status.displayName}</span>                    
                                        <h3>Phòng ${booking.room.roomNumber} - ${booking.room.roomType.name}</h3>
                                    </div>
                                    <p class="location"><i class="bi bi-geo-alt"></i> ${booking.room.branch.branchName} - ${booking.room.branch.address}</p>
                                    <div class="booking-info">
                                        <span><i class="bi bi-calendar3"></i> Check-in: ${f:formatLocalDateTime(booking.checkIn)}</span>
                                        <span><i class="bi bi-calendar3"></i> Check-out: ${f:formatLocalDateTime(booking.checkOut)}</span>
                                        <span><i class="bi bi-credit-card"></i> Tổng tiền: <fmt:formatNumber type="number" value="${booking.totalAmount}" />đ</span>
                                        <span><i class="bi bi-credit-card"></i> Số tiền đã trả: <fmt:formatNumber type="number" value="${booking.paidAmount != null ? booking.paidAmount : 0}" />đ</span>
                                    </div>

                                    <c:if test="${not empty booking.bookingServices}">
                                        <div class="booking-services mt-3">
                                            <h5 class="mb-2">Dịch vụ đi kèm:</h5>
                                            <ul class="list-group">
                                                <c:forEach var="serviceItem" items="${booking.bookingServices}">
                                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                                        <div>
                                                            <strong>${serviceItem.service.serviceName}</strong> 
                                                            (x<fmt:formatNumber type="number" value="${serviceItem.quantity}" pattern="#"/> / ${serviceItem.service.unit})
                                                        </div>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </c:if>

                                    <div class="amenities mt-3">
                                        <c:forEach var="amenity" items="${booking.room.roomAmenities}">
                                            <span class="badge bg-light text-dark me-2"><i class="bi bi-check2"></i> ${amenity.amenity.amenityName}</span>
                                        </c:forEach>
                                    </div>
                                    <div class="booking-actions mt-3">
                                        <a href="/booking/booking-history/${booking.bookingID}" class="btn btn-outline-primary"><i class="bi bi-arrow-up-right-square"></i> Xem chi tiết</a>
                                        <c:if test="${booking.status == 'PENDING'}">
                                        <a onclick="handlePayment('${booking.bookingID}', 'ROOM_BOOKING')" class="btn btn-primary ms-2">
                                            <i class="bi bi-credit-card"></i> Thanh toán
                                        </a>
                                </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
        <c:if test="${customer.customerType.discountRate > 0}">
            <div class="special-offers mb-5">
                <h2 class="section-title mb-4">Ưu đãi đặc biệt</h2>
                <div class="alert alert-info d-flex align-items-center" role="alert">
                    <i class="bi bi-gift-fill me-2"></i>
                    <div>
                        Bạn đang là thành viên hạng <strong>${customer.customerType.name}</strong>, được giảm <strong>
                        <fmt:formatNumber value="${customer.customerType.discountRate}" pattern="#"/>%</strong> cho mỗi lần đặt phòng!
                    </div>
                </div>
            </div> 
        </c:if>       
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
    <script>
        $(document).ready(function () {
            $('.daterange-picker').daterangepicker({
                locale: {
                    format: 'DD/MM/YYYY'
                },
            })
        });
    </script>
    <jsp:include page="../layout/partial/_payment-handler.jsp" />
</body>
</html>