<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt phòng thành công - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
</head>
<body>
    <jsp:include page="../layout/header.jsp" />

    <div class="success-booking-section mt-3 pt-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8 text-center">
                    <div class="success-icon mb-4">
                        <i class="bi bi-check-circle-fill text-success" style="font-size: 5rem;"></i>
                    </div>
                    <h1 class="mb-4">Đặt phòng thành công!</h1>
                    <p class="lead mb-4">Cảm ơn bạn đã đặt phòng tại Lullaby Homestay. Dưới đây là thông tin chi tiết về đặt phòng của bạn:</p>

                    <div class="booking-details card p-2">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <h5 class="mb-3">Thông tin phòng</h5>
                                    <p><strong>Phòng:</strong> ${booking.room.roomNumber} - ${booking.room.roomType.name}</p>
                                    <p><strong>Chi nhánh:</strong> ${booking.room.branch.branchName}</p>
                                    <p><strong>Địa chỉ:</strong> ${booking.room.branch.address}</p>
                                    <p><strong>Mật khẩu cổng:</strong> ${booking.room.branch.branchPassword}</p>
                                </div>
                                <div class="col-md-6">
                                    <h5 class="mb-3">Thông tin đặt phòng</h5>
                                    <p><strong>Check-in:</strong> ${f:formatLocalDateTime(booking.checkIn)}</p>
                                    <p><strong>Check-out:</strong> ${f:formatLocalDateTime(booking.checkOut)}</p>
                                    <p><strong>Số lượng khách:</strong> ${booking.guestCount} người</p>
                                    <p><strong>Tổng cộng:</strong> <fmt:formatNumber type="number" value="${booking.totalAmount}" />đ</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="action-buttons my-4">
                        <a href="/" class="btn btn-primary btn-lg me-3">
                            <i class="bi bi-house-door"></i> Quay lại trang chủ
                        </a>
                        <a href="/booking-details/${booking.bookingID}" class="btn btn-outline-primary btn-lg">
                            <i class="bi bi-receipt"></i> Xem chi tiết đơn đặt phòng
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
</body>
</html>