<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                        <a href="/room/${booking.room.roomID}" style="text-decoration: none;"><h3 class="card-title">Phòng ${booking.room.roomNumber} - ${booking.room.roomType.name}</h3></a>
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

                <c:if test="${booking.status != 'CANCELLED'}">
                    <div class="card">
                        <div class="card-header bg-white">
                            <h5 class="mb-0"><i class="bi bi-star me-2"></i>Đánh giá của bạn về phòng</h5>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty message}">
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    ${message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    ${error}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>
                    
                            <c:choose>
                                <c:when test="${not empty booking.review}">
                                    <div class="mb-3 review-container" id="review-${booking.review.reviewID}">
                                        <div class="review-content">
                                            <div class="d-flex">
                                                <div class="me-3">
                                                    <div class="d-inline-block rounded-circle p-1">
                                                        <img src="/images/avatar/${not empty sessionScope.avatar ? sessionScope.avatar : 'default-img.jpg'}" alt="Avatar" class="rounded-circle border border-white border-2" width="60" height="60">
                                                    </div>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <div class="d-flex justify-content-between align-items-center">
                                                        <h6 class="mb-0">${sessionScope.fullName}</h6>
                                                        <div class="d-flex align-items-center">
                                                            <small class="text-muted me-3">Đăng vào: ${f:formatLocalDateTime(booking.review.createdAt)}</small>
                                                            <button class="btn btn-warning btn-sm edit-review-btn me-1" data-review-id="${booking.review.reviewID}">
                                                                <i class="bi bi-pencil"></i>
                                                            </button>
                                                            <button class="btn btn-danger btn-sm delete-review-btn"
                                                                data-review-id="${booking.review.reviewID}"
                                                                onclick="checkBeforeDelete(this)" 
                                                                    data-entity-id="${booking.review.reviewID}" 
                                                                    data-entity-name="${booking.review.comment}" 
                                                                    data-entity-type="đánh giá" 
                                                                    data-delete-url="/review/delete" 
                                                                    data-id-name="reviewID">
                                                                <i class="bi bi-trash"></i> 
                                                            </button>
                                                        </div>
                                                    </div>
                                                    
                                                    <div class="mb-2">
                                                        <c:forEach begin="1" end="${booking.review.rating}">
                                                            <i class="bi bi-star-fill text-warning"></i>
                                                        </c:forEach>
                                                        <c:forEach begin="${booking.review.rating + 1}" end="5">
                                                            <i class="bi bi-star text-warning"></i>
                                                        </c:forEach>
                                                    </div>
                                                    
                                                    <p class="mb-1 review-text">${booking.review.comment}</p>
                                                    <c:if test="${not empty booking.review.image}">
                                                        <img src="/images/review/${booking.review.image}" alt="Review Image" style="max-width: 200px;">
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="edit-review-form mt-3" style="display: none;">
                                            <form:form modelAttribute="editReview" action="/review/update" method="post" enctype="multipart/form-data">
                                                <form:hidden path="reviewID" value="${booking.review.reviewID}"/>
                                                <form:hidden path="booking.bookingID" value="${booking.bookingID}"/>
                                                
                                                <div class="d-flex">
                                                    <div class="me-3">
                                                        <div class="d-inline-block rounded-circle p-1">
                                                            <img src="/images/avatar/${not empty sessionScope.avatar ? sessionScope.avatar : 'default-img.jpg'}" alt="Avatar" class="rounded-circle border border-white border-2" width="60" height="60">
                                                        </div>
                                                    </div>
                                                    <div class="flex-grow-1">
                                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                                            <h6 class="mb-0">${sessionScope.fullName}</h6>
                                                            <small class="text-muted">Đăng vào: ${f:formatLocalDateTime(booking.review.createdAt)}</small>
                                                        </div>

                                                        <div class="form-group mb-2">
                                                            <label>Đánh giá của bạn:</label>
                                                            <div class="star-rating">
                                                                <input type="hidden" name="rating" class="rating-input" value="${booking.review.rating}">
                                                                <span class="star" data-value="1"><i class="bi bi-star"></i></span>
                                                                <span class="star" data-value="2"><i class="bi bi-star"></i></span>
                                                                <span class="star" data-value="3"><i class="bi bi-star"></i></span>
                                                                <span class="star" data-value="4"><i class="bi bi-star"></i></span>
                                                                <span class="star" data-value="5"><i class="bi bi-star"></i></span>
                                                            </div>
                                                        </div>

                                                        <div class="form-group mb-2">
                                                            <label>Nhận xét:</label>
                                                            <textarea name="comment" class="form-control">${booking.review.comment}</textarea>
                                                        </div>

                                                        <div class="form-group mb-2">
                                                            <label>Hình ảnh cũ:</label>
                                                            <c:if test="${not empty booking.review.image}">
                                                                <div class="mb-2">
                                                                    <img src="/images/review/${booking.review.image}" alt="Review Image" style="max-width: 200px;">
                                                                </div>
                                                            </c:if>
                                                            <input type="file" name="fileImg" class="form-control">
                                                        </div>

                                                        <div class="d-flex justify-content-end gap-2">
                                                            <button type="submit" class="btn btn-warning btn-sm">
                                                                <i class="bi bi-check"></i> Sửa
                                                            </button>
                                                            <button type="button" class="btn btn-secondary btn-sm cancel-edit-btn">
                                                                <i class="bi bi-x"></i> Hủy
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form:form>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${booking.status == 'COMPLETED'}">
                                            <p>Bạn đã trải nghiệm đặt phòng tại homestay, để lại cảm nhận của bạn ngay!</p>
                                            <form:form id="reviewForm" modelAttribute="newReview" action="/review/create" method="post" enctype="multipart/form-data">
                                                <form:hidden path="booking" value="${booking.bookingID}"/>
                                                <c:set var="errorRating">
                                                    <form:errors path="rating" cssClass="invalid-feedback" />
                                                </c:set>
                                                <c:set var="errorComment">
                                                    <form:errors path="comment" cssClass="invalid-feedback" />
                                                </c:set>
                                                <div class="form-group mb-3">
                                                    <label for="rating">Đánh giá của bạn:</label>
                                                    <div class="star-rating">
                                                        <form:hidden path="rating" id="rating" value="0"/> 
                                                        <span class="star" data-value="1"><i class="bi bi-star"></i></span>
                                                        <span class="star" data-value="2"><i class="bi bi-star"></i></span>
                                                        <span class="star" data-value="3"><i class="bi bi-star"></i></span>
                                                        <span class="star" data-value="4"><i class="bi bi-star"></i></span>
                                                        <span class="star" data-value="5"><i class="bi bi-star"></i></span>
                                                    </div>
                                                    ${errorRating}
                                                </div>
                                                <div class="form-group mb-3">
                                                    <label for="comment">Nhận xét:</label>
                                                    <form:textarea path="comment" id="comment" class="form-control ${not empty errorComment ? 'is-invalid' : ''}" rows="3" placeholder="Nhập nhận xét của bạn..." />
                                                    ${errorComment}
                                                </div>
                                                <div class="form-group mb-3">
                                                    <label for="image">Hình ảnh:</label>
                                                    <input type="file" name="fileImg" id="image" class="form-control">
                                                </div>
                                                <button type="submit" class="btn btn-primary btn-sm">
                                                    <i class="bi bi-star-fill"></i> Gửi đánh giá
                                                </button>
                                            </form:form>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${booking.status == 'PENDING'}">
                                                <p>Vui lòng hoàn tất đặt phòng để có thể gửi đánh giá.</p>
                                            </c:if>
                                            <c:if test="${booking.status == 'CONFIRMED'}">
                                                <p>Bạn sẽ có thể gửi đánh giá sau khi hoàn tất kỳ nghỉ.</p>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
            </div>

            <div class="col-lg-4">
                <c:if test="${booking.status == 'CONFIRMED'}">
                    <div class="mb-4">
                        <div class="d-flex justify-content-between gap-2">
                            <button class="btn btn-primary btn-sm flex-fill"
                                onclick="checkBeforeBookServices(this)"
                                data-booking-id="${booking.bookingID}"
                                data-booking-status="${booking.status}">
                                <i class="bi bi-plus-circle me-1"></i>Đặt dịch vụ
                            </button>
                            <button class="btn btn-success btn-sm"
                                onclick="checkBeforeBookingExtension(this)"
                                data-booking-id="${booking.bookingID}">
                                <i class="bi bi-clock me-1"></i>Gia hạn giờ thuê
                            </button>
                        </div>
                    </div>
                </c:if>

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
                                    booking.status == 'CONFIRMED' ? 'bg-primary' : 'bg-info'}">
                                    ${booking.status.displayName}
                                </span>

                                <c:if test="${booking.status == 'CANCELLED' && not empty booking.payments}">
                                    <span class="badge ${booking.totalAmount == booking.paidAmount ? 'bg-danger' : 'bg-warning'}">
                                        ${booking.totalAmount == booking.paidAmount ? 'Đang chờ hoàn tiền' : 'Đã hoàn tiền'}
                                    </span>
                                </c:if>

                                <c:if test="${booking.status == 'PENDING'}">
                                    <a onclick="handlePayment('${booking.bookingID}', 'ROOM_BOOKING', true)" class="btn btn-sm btn-primary">
                                        <i class="bi bi-credit-card"></i> Thanh toán
                                    </a>
                                </c:if>
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

                <c:if test="${not empty booking.bookingExtensions}">
                    <div class="card mb-4">
                        <div class="card-header bg-white">
                            <h5 class="mb-0"><i class="bi bi-clock-history me-2"></i>Lịch sử gia hạn</h5>
                        </div>
                        <div class="card-body">
                            <c:forEach var="extension" items="${booking.bookingExtensions}">
                                <div class="mb-3">
                                    <div class="d-flex justify-content-between py-2">
                                        <span class="small text-muted">Thời điểm gia hạn</span>
                                        <span class="fw-bold">${f:formatLocalDateTime(extension.createdAt)}</span>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <span class="small text-muted">Số giờ gia hạn</span>
                                        <span class="fw-bold">
                                            <fmt:formatNumber type="number" value="${extension.extendedHours}" pattern="#" /> giờ
                                        </span>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

                
                <c:if test="${not empty booking.bookingServices}">
                    <div class="card mb-4">
                        <div class="card-header bg-white">
                            <h5 class="mb-0"><i class="bi bi-card-checklist me-2"></i>Dịch vụ đã đặt</h5>
                        </div>
                        <div class="card-body">

                                <c:if test="${hasPrepaidBService}">
                                    <h6 class="fw-bold mb-3">Dịch vụ trả trước</h6>
                                    <c:forEach var="bService" items="${booking.bookingServices}">
                                        <c:if test="${bService.service.isPrepaid}">
                                            <div class="d-flex justify-content-between mb-2">
                                                <span>${bService.service.serviceName}</span>
                                                <span class="fw-bold">
                                                    <fmt:formatNumber value="${bService.service.price}" />đ x
                                                    <fmt:formatNumber value="${bService.quantity}" pattern="#" />
                                                </span>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                    <hr />
                                </c:if>
                            

                            <c:if test="${hasPostpaidBService}">
                                <h6 class="fw-bold mb-3">Dịch vụ trả sau</h6>
                                <c:forEach var="bService" items="${booking.bookingServices}">
                                    <c:if test="${!bService.service.isPrepaid}">
                                        <c:set var="status" value="${bService.status}" />
                                        <c:choose>
                                            <c:when test="${not empty bService.quantity}">
                                                <div class="d-flex justify-content-between mb-2 align-items-center">
                                                    <div>
                                                        <span class="me-2">${bService.service.serviceName}</span>
                                                        <span class="badge 
                                                            ${status == 'COMPLETED' ? 'bg-success' : 
                                                            status == 'PENDING' ? 'bg-secondary' : 
                                                            status == 'IN_PROGRESS' ? 'bg-warning' : 
                                                            status == 'CANCELLED' ? 'bg-danger' : 'bg-info'}">
                                                            ${status.displayName}
                                                        </span>
                                                    </div>
                                                    <span class="fw-bold text-end">
                                                        <fmt:formatNumber value="${bService.service.price * bService.quantity}" />đ
                                                        <small class="text-muted">
                                                            (<fmt:formatNumber value="${bService.service.price}" />đ x
                                                            <fmt:formatNumber value="${bService.quantity}" pattern="#" />)
                                                        </small>
                                                    </span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="d-flex justify-content-between mb-2 align-items-center">
                                                    <div>
                                                        <span class="me-2">${bService.service.serviceName}</span>
                                                        <span class="badge 
                                                            ${status == 'COMPLETED' ? 'bg-success' : 
                                                            status == 'PENDING' ? 'bg-secondary' : 
                                                            status == 'IN_PROGRESS' ? 'bg-warning' : 
                                                            status == 'CANCELLED' ? 'bg-danger' : 'bg-info'}">
                                                            ${status.displayName}
                                                        </span>
                                                    </div>
                                                    <span class="text-muted">Đang cập nhật</span>
                                                </div>
                                                <c:set var="hasPaidPostServices" value="false" />
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${totalUnpaidPostpaidAmount != 0}">
                                    <div class="alert alert-info mt-3 p-3">
                                        <div class="row align-items-center">
                                            <div class="col-md">
                                                <div class="text-muted small">Tổng tiền dịch vụ trả sau chưa thanh toán:</div>
                                                <div class="fw-bold text-danger fs-5">
                                                    <fmt:formatNumber value="${totalUnpaidPostpaidAmount}" />đ
                                                </div>
                                            </div>
                                            <div class="col-auto mt-3 mt-md-0">
                                                <c:if test="${canPayBServices}">
                                                    <button class="btn btn-primary btn-sm px-4"
                                                            onclick="handlePayment('${booking.bookingID}', 'ADDITIONAL_SERVICE', true)">
                                                        <i class="bi bi-wallet2 me-1"></i> Thanh toán <br> (<fmt:formatNumber value="${totalUnpaidPostpaidAmount}" />đ)
                                                    </button>
                                                </c:if>
                                            </div>
                                        </div>

                                        <c:if test="${!canPayBServices}">
                                            <hr class="my-2" />
                                            <small class="text-muted">
                                                * Số tiền chính xác sẽ được cập nhật khi nhân viên xác nhận số lượng sử dụng.
                                            </small>
                                        </c:if>
                                    </div>
                                </c:if>

                            </c:if>

                        </div>
                    </div>
                </c:if>

                <div class="card mb-4 shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="mb-0"><i class="bi bi-credit-card me-2"></i>Chi tiết thanh toán</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty booking.payments}">
                                <div class="d-flex justify-content-between align-items-start mb-3">
                                    <span class="fw-semibold">Tổng cộng</span>
                                    <div class="text-end">
                                        <div class="text-danger fw-bold fs-5">
                                            <fmt:formatNumber value="${booking.totalAmount}" />đ
                                        </div>
                                        <c:if test="${hasPaidPostServices}">
                                            <small class="text-muted fst-italic">
                                                <i class="bi bi-info-circle me-1"></i>Đang chờ cập nhật dịch vụ trả sau
                                            </small>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-between fw-bold mb-3">
                                    <span>Đã thanh toán</span>
                                    <span class="text-success">
                                        <fmt:formatNumber value="${booking.paidAmount != null ? booking.paidAmount : 0}" />đ
                                    </span>
                                </div>

                                <div class="mt-3">
                                    <a class="text-primary small" data-bs-toggle="collapse" href="#paymentInfo" role="button">
                                        Xem lịch sử thanh toán
                                    </a>
                                    <div class="collapse" id="paymentInfo">
                                        <div class="text-muted small mt-2">
                                            <c:forEach var="payment" items="${booking.payments}" varStatus="loop">
                                                <div class="card mb-3 border">
                                                    <div class="card-header bg-light d-flex justify-content-between">
                                                        <span><strong>Thanh toán #${loop.count}</strong></span>
                                                        <span class="badge 
                                                            ${payment.status == 'COMPLETED' ? 'bg-success' :
                                                            payment.status == 'FAILED' ? 'bg-danger' :
                                                            payment.status == 'PENDING' ? 'bg-primary' : 'bg-secondary'}">
                                                            ${payment.status.displayName}
                                                        </span>
                                                    </div>
                                                    <div class="card-body">
                                                        <p><strong>Phương thức:</strong>
                                                            <span class="badge 
                                                                ${payment.paymentType == 'TRANSFER' ? 'bg-success' :
                                                                payment.paymentType == 'CASH' ? 'bg-info' : 'bg-secondary'}">
                                                                ${payment.paymentType == 'TRANSFER' ? 'Chuyển khoản' :
                                                                payment.paymentType == 'CASH' ? 'Tiền mặt' : 'Chưa xác định'}
                                                            </span>
                                                        </p>
                                                        <c:if test="${not empty payment.vnpTransactionNo}">
                                                            <p><strong>Mã giao dịch:</strong> ${payment.vnpTransactionNo}</p>
                                                        </c:if>
                                                        <c:if test="${not empty payment.paymentDate}">
                                                            <p><strong>Ngày thanh toán:</strong> ${f:formatLocalDateTime(payment.paymentDate)}</p>
                                                        </c:if>
                                                        <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${payment.totalAmount}" />đ</p>
                                                        <hr />
                                                        <p class="mb-1"><strong>Chi tiết thanh toán:</strong></p>
                                                        <c:forEach var="detail" items="${payment.paymentDetails}">
                                                            <div class="ms-3">
                                                                <p>- ${detail.paymentPurpose.description}:
                                                                    <fmt:formatNumber value="${detail.finalAmount}" />đ
                                                                </p>
                                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>

                                <c:if test="${booking.status == 'CANCELLED'}">
                                    <h6 class="mt-4">Hoàn tiền</h6>
                                    <div class="d-flex justify-content-between mb-2">
                                        <span>Trạng thái hoàn tiền</span>
                                        <span class="fw-bold">
                                            <c:choose>
                                                <c:when test="${booking.totalAmount == booking.paidAmount}">
                                                    <span class="text-warning">Đang chờ hoàn tiền</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-success">Đã hoàn tiền</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">Chưa có chi phí nào được ghi nhận.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <c:if test="${canCancel}">
                    <div class="card">
                        <div class="card-body">
                            <div class="d-grid gap-2">
                                <button class="btn btn-danger btn-sm"
                                    onclick="checkBeforeCancel(this)"
                                    data-entity-id="${booking.bookingID}"
                                    data-role="customer">
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
    <jsp:include page="../../shared/partial/_modal-refund.jsp" />
    <jsp:include page="../../shared/partial/_script-handle-cancel-booking.jsp" />
    <jsp:include page="../../admin/layout/partial/_script-preview-image-update.jsp" />
    <script src="https://code.iconify.design/3/3.1.0/iconify.min.js"></script>
    <%@ include file="./_modal-book-services.jsp" %>
    <jsp:include page="./_modal-error.jsp" />
    <jsp:include page="./_script-handle.jsp" />
    <jsp:include page="../layout/partial/_payment-handler.jsp" />
    <script>
        setupImagePreview("review");
    </script>
    <script>
        $(document).ready(function () {
            function fillStars($stars, rating) {
                $stars.each(function () {
                    let starValue = parseInt($(this).data('value'));
                    if (starValue <= rating) {
                        $(this).addClass('filled');
                    } else {
                        $(this).removeClass('filled');
                    }
                });
            }
    
            let $newStars = $('#reviewForm .star');
            let $newRatingInput = $('#rating');
    
            let initialNewRating = parseInt($newRatingInput.val()) || 0;
            fillStars($newStars, initialNewRating);
    
            $newStars.on('click', function () {
                let value = parseInt($(this).data('value'));
                $newRatingInput.val(value);
                fillStars($newStars, value);
            });
    
            $newStars.on('mouseover', function () {
                let value = parseInt($(this).data('value'));
                fillStars($newStars, value);
            });
    
            $newStars.on('mouseout', function () {
                let selectedValue = parseInt($newRatingInput.val()) || 0;
                fillStars($newStars, selectedValue);
            });
    
            $(".edit-review-btn").on("click", function () {
                let reviewId = $(this).data("review-id");
                let $container = $("#review-" + reviewId);
    
                $container.find(".review-content").hide();
                $container.find(".edit-review-form").show();
    
                let rating = parseInt($container.find(".rating-input").val()) || 0; 
                let $editStars = $container.find(".star");
    
                fillStars($editStars, rating);
    
                $editStars.off('click');
                $editStars.on("click", function () {
                    let value = parseInt($(this).data('value'));
                    $container.find(".rating-input").val(value);
                    fillStars($editStars, value);
                });
    
                $editStars.off('mouseover mouseout');
                $editStars.on('mouseover', function () {
                    let value = parseInt($(this).data('value'));
                    fillStars($editStars, value);
                });
                $editStars.on('mouseout', function () {
                    let selectedValue = parseInt($container.find(".rating-input").val()) || 0;
                    fillStars($editStars, selectedValue);
                });
            });
    
            $(".cancel-edit-btn").on("click", function () {
                let $container = $(this).closest(".review-container");
    
                $container.find(".review-content").show();
                $container.find(".edit-review-form").hide();
            });
        });
    </script>
</body>
</html>