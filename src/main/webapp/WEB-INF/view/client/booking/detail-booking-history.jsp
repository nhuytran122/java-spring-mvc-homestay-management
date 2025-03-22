<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đặt phòng - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/booking-history-style.css">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>
    <jsp:include page="../layout/header.jsp" />
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mt-4">
    </nav>
    <div class="container py-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="mb-0">
                <i class="bi bi-calendar-check me-2"></i>Chi tiết đặt phòng
            </h3>
        </div>

        <div class="row g-4">
            <div class="col-lg-8">
                <div class="card mb-4">
                    <c:choose>
                        <c:when test="${not empty booking.room.thumbnail}">
                            <img src="/images/room/${booking.room.thumbnail}" class="property-image">
                        </c:when>
                        <c:otherwise>
                            <img src="/images/room/default-img.jpg" class="property-image">
                        </c:otherwise>
                    </c:choose> 
                    <div class="card-body">
                        <h3 class="card-title">Phòng ${booking.room.roomNumber} - ${booking.room.roomType.name}</h3>
                        <p class="text-muted">
                            <i class="bi bi-geo-alt me-1"></i> ${booking.room.branch.branchName} - ${booking.room.branch.address}
                        </p>
                        <hr>
                        
                        <h5 class="mb-3">Tiện nghi trong phòng</h5>
                        <div class="row mb-3">
                            <c:forEach var="item" items="${booking.room.roomAmenities}">
                                <div class="col-md-4 col-6 mb-2">
                                    <div class="d-flex align-items-center">
                                        <i class="bi bi-check2 me-2 text-primary"></i> ${item.amenity.amenityName}
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <!-- TODO -->
                <div class="card">
                    <div class="card-header bg-white">
                        <h5 class="mb-0"><i class="bi bi-star me-2"></i>Đánh giá của bạn về phòng</h5>
                    </div>
                    
                    <div class="card-body">
                        <div class="d-flex mb-3">
                            <div class="me-3">
                                <img src="https://images.unsplash.com/photo-1633332755192-727a05c4013d" alt="Avatar" width="60">
                            </div>
                            <div>
                                <h6 class="mb-1">John</h6>
                                <div class="mb-2">
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <i class="bi bi-star-fill text-warning"></i>
                                    <span class="ms-2 text-muted">5.0</span>
                                </div>
                                <p class="mb-1">
                                    Trải nghiệm tuyệt vời
                                </p>
                                <small class="text-muted">18/03/2025</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="card mb-4">
                    <div class="card-header bg-white">
                        <h5 class="mb-0"><i class="bi bi-info-circle me-2"></i>Thông tin đặt phòng</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <div class="small text-muted mb-1">Tình trạng</div>
                            <div>
                                <span class="badge ms-2 ${booking.status == 'COMPLETED' ? 'bg-success' : 
                                    booking.status == 'CANCELLED' ? 'bg-danger' : 
                                    booking.status == 'BOOKED' ? 'bg-primary' : 'bg-info'}">
                                    ${booking.status.displayName}</span>     
                            </div>
                        </div>
                        <div class="mb-3">
                            <div class="small text-muted mb-1">Check-in</div>
                            <div class="fw-bold">${f:formatLocalDateTime(booking.checkIn)}</div>
                        </div>
                        <div class="mb-3">
                            <div class="small text-muted mb-1">Check-out</div>
                            <div class="fw-bold">${f:formatLocalDateTime(booking.checkOut)}</div>
                        </div>
                        <div class="mb-3">
                            <div class="small text-muted mb-1">Số lượng khách</div>
                            <div class="fw-bold">${booking.guestCount}</div>
                        </div>
                        <div>
                            <div class="small text-muted mb-1">Ngày đặt phòng</div>
                            <div class="fw-bold">${f:formatLocalDateTime(booking.createdAt)}</div>
                        </div>
                    </div>
                </div>

                <div class="card mb-4">
                    <div class="card-header bg-white">
                        <h5 class="mb-0"><i class="bi bi-credit-card me-2"></i>Chi tiết thanh toán</h5>
                    </div>
                    <div class="card-body">
                        <div class="payment-card p-3 mb-3 bg-light rounded">
                            <div class="d-flex align-items-center mb-2">
                                <i class="bi bi-credit-card-2-front me-2 fs-4"></i>
                                <div>
                                    <div class="small">Phương thức thanh toán</div>
                                    <div class="fw-bold"></div>
                                </div>
                            </div>
                            <div class="small text-muted">(Ngày)</div>
                        </div>

                        <h6 class="mb-3">Thanh toán</h6>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Tiền phòng</span>
                            <span><fmt:formatNumber type="number" value="${booking.room.roomType.pricePerHour}" /> x <fmt:formatNumber type="number" value="${numberOfHours}" pattern="#"/> giờ</span>
                        </div>                        
                        <c:if test="${not empty booking.bookingServices}">
                            <c:forEach var="bookingService" items="${booking.bookingServices}">            
                                <div class="d-flex justify-content-between mb-2">
                                    <span>${bookingService.service.serviceName}</span>
                                    <span><fmt:formatNumber type="number" value="${bookingService.service.price}" /> x  <fmt:formatNumber type="number" value="${bookingService.quantity}" pattern="#"/></span>
                                </div>
                            </c:forEach>
                        </c:if>
                        <hr>
                        <div class="d-flex justify-content-between fw-bold">
                            <span>Tổng tiền</span>
                            <span><fmt:formatNumber type="number" value="${booking.totalAmount}" />đ</span>
                        </div>
                        <div class="d-flex justify-content-between fw-bold">
                            <span>Đã thanh toán</span>
                            <span><fmt:formatNumber type="number" value="${booking.paidAmount != null ? booking.paidAmount : 0}" />đ</span>
                        </div>
                    </div>
                </div>
                <c:if test="${booking.status.toString() != 'CANCELLED'}">
                    <div class="card">
                        <div class="card-body">
                            <div class="d-grid gap-2">
                                <button class="btn btn-outline-danger" title="Hủy đặt phòng"
                                    onclick="checkBeforeCancel(this)" 
                                        data-entity-id="${booking.bookingID}"
                                        data-id-name="bookingID"
                                        data-check-url="/booking/booking-history/can-cancel/" 
                                        data-cancel-url="/booking/booking-history/cancel">
                                    <i class="bi bi-x-circle"></i>
                                    Hủy đặt phòng
                                </button>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="_modal-cancel.jsp" />
</body>
</html>