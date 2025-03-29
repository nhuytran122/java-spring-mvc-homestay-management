<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết phòng - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
    <link rel="stylesheet" href="/client/css/detail-room-style.css">
</head>
<body>
    <jsp:include page="../layout/header.jsp" />

    <div class="room-details-section mt-5 pt-5">
        <div class="container">
            <div class="row">
                <div class="col-lg-8">
                    <div id="roomCarousel" class="carousel slide" data-bs-ride="carousel">
                        <div class="carousel-inner rounded-4">
                            <div class="carousel-item active">
                                <img src="/images/room/${not empty room.thumbnail ? room.thumbnail : 'default-img.jpg'}" class="d-block w-100" alt="Ảnh phòng" style="height: 400px; object-fit: cover">
                            </div>
                            <c:forEach var="photo" items="${room.roomPhotos}">
                              <div class="carousel-item">
                                  <img src="/images/room/${photo.photo}" class="d-block w-100" alt="Ảnh phòng" style="height: 400px; object-fit: cover">
                              </div>
                            </c:forEach>
                        </div>
                        <button class="carousel-control-prev" type="button" data-bs-target="#roomCarousel" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Trước</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#roomCarousel" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Sau</span>
                        </button>
                    </div>
                    
                    <div class="room-info my-4">
                        <h1 class="mb-3">Phòng ${room.roomNumber} - ${room.roomType.name}</h1>
                        <div class="d-flex align-items-center mb-3 ">
                            <span class="badge bg-primary me-2">${room.branch.branchName}</span>
                            <span class="text-muted">${room.branch.address}</span>
                        </div>
                        <hr>
                        <h4>Thông tin về loại phòng</h4>
                        <p>${room.roomType.description}</p>
                        <div class="amenities mt-4">
                            <h4>Tiện nghi trong phòng</h4>
                            <div class="row mt-3">
                                <div class="col-md-6">
                                    <ul class="list-unstyled">
                                        <c:forEach var="item" items="${room.roomAmenities}">
                                            <li class="mb-2">
                                                <i class="bi bi-check2"></i> <span class="ms-2">${item.amenity.amenityName}</span>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </div>                        
                    </div>
                </div>
                
                <div class="col-lg-4">
                    <div class="booking-card card sticky-top p-3">
                        <div class="card-body">
                            <h3 class="price mb-4"><fmt:formatNumber type="number" value="${room.roomType.pricePerHour}" />đ <small class="text-muted">/giờ</small></h3>
                            <form:form action="/booking" method="post" modelAttribute="newBooking">
                                <c:set var="errorCheckIn">
                                    <form:errors path="checkIn" cssClass="invalid-feedback" />
                                </c:set>
                                <c:set var="errorCheckOut">
                                    <form:errors path="checkOut" cssClass="invalid-feedback" />
                                </c:set>
                                <c:set var="errorGuestCount">
                                    <form:errors path="guestCount" cssClass="invalid-feedback" />
                                </c:set>
                                <form:input type="hidden" path="room.roomID" value="${room.roomID}" />
                                <form:input type="hidden" path="room.roomType.maxGuest" value="${room.roomType.maxGuest}" />
                                <div class="row g-3 mb-4">
                                    <div class="col-6">
                                        <label class="form-label">Check-in</label>
                                        <form:input type="text" id="checkin" class="form-control datetime-picker 
                                            ${not empty errorCheckIn ? 'is-invalid' : ''}" path="checkIn"  placeholder="Chọn thời gian checkin"/>
                                            ${errorCheckIn}
                                    </div>
                                    <div class="col-6">
                                        <label class="form-label">Check-out</label>
                                        <form:input type="text" id="checkout" class="form-control datetime-picker 
                                            ${not empty errorCheckOut ? 'is-invalid' : ''}" path="checkOut"  placeholder="Chọn thời gian checkout"/>
                                            ${errorCheckOut}
                                    </div>
                                    </div>
                                    <div class="col-12">
                                        <label class="form-label">Số lượng khách</label>
                                        <form:select path="guestCount" class="form-select ${not empty errorGuestCount ? 'is-invalid' : ''}" id="guestCount">
                                            <c:forEach var="i" begin="1" end="${room.roomType.maxGuest}">
                                                <option value="${i}" ${i == newBooking.guestCount ? 'selected' : ''}>${i} người</option>
                                            </c:forEach>
                                        </form:select>
                                        ${errorGuestCount}                                    
                                    </div>
                                
                                <button type="submit" class="btn btn-primary btn-custom w-100 mt-4">Đặt phòng</button>
                            </div>
                                <div class="price-details my-4">
                                    <div class="d-flex justify-content-between mb-2">
                                        <span id="hours-text"><fmt:formatNumber type="number" value="${room.roomType.pricePerHour}" />đ x 0 giờ</span>
                                        <span id="subtotal">0đ</span>
                                    </div>
                                    <hr>
                                    <div class="d-flex justify-content-between fw-bold">
                                        <span>Tổng cộng</span>
                                        <span id="total">0đ</span>
                                    </div>
                                </div>
                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger" id="booking-error">${errorMessage}</div>
                                </c:if>
                                <div class="alert alert-danger" id="booking-error-client" style="display: none;"></div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="reviews-section mt-4 mb-5">
        <div class="container">
            <div class="row">
                <div class="col-lg-8">
                    <h4 class="mb-4">Đánh giá của khách hàng</h4>
                    <div class="reviews-list">
                        <c:if test="${empty listReviews}">
                            <p class="text-center">
                                Hiện không có đánh giá dành cho phòng này.
                            </p>
                        </c:if>
                        <c:forEach var="review" items="${listReviews}">
                            <div class="review-card mb-4">
                                <div class="d-flex">
                                    <div class="me-3">
                                        <div class="d-inline-block rounded-circle p-1">
                                            <img src="/images/avatar/${not empty review.booking.customer.avatar ? review.booking.customer.avatar : 'default-img.jpg'}" alt="Avatar" class="rounded-circle border border-white border-2" width="60" height="60">
                                        </div>
                                    </div>
                                    <div class="flex-grow-1">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <h6 class="mb-0">${review.booking.customer.fullName}</h6>
                                            <div class="d-flex align-items-center">
                                                <small class="text-muted me-3">Đăng vào: ${f:formatLocalDateTime(review.createdAt)}</small>
                                            </div>
                                        </div>
                                        
                                        <div class="mb-2">
                                            <c:forEach begin="1" end="${review.rating}">
                                                <i class="bi bi-star-fill text-warning"></i>
                                            </c:forEach>
                                            <c:forEach begin="${review.rating + 1}" end="5">
                                                <i class="bi bi-star text-warning"></i>
                                            </c:forEach>
                                        </div>
                                        
                                        <p class="mb-3 review-text">${review.comment}</p>
                                        <c:if test="${not empty review.image}">
                                            <img src="/images/review/${review.image}" alt="Review Image" style="max-width: 200px;">
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
    <script>
        $(document).ready(function () {
            let now = moment();
            let pricePerHour = parseFloat("${room.roomType.pricePerHour}") || 0;
            let roomTypeName = "${room.roomType.name}".toLowerCase(); 
            let isDorm = roomTypeName.includes("dorm"); 

            let checkInVal = $('#checkin').val();
            let checkOutVal = $('#checkout').val();
            let startDate = checkInVal && moment(checkInVal, 'DD/MM/YYYY HH:mm').isValid() ? moment(checkInVal, 'DD/MM/YYYY HH:mm') : now;
            let endDate = checkOutVal && moment(checkOutVal, 'DD/MM/YYYY HH:mm').isValid() ? moment(checkOutVal, 'DD/MM/YYYY HH:mm') : now;

            $('#checkin').daterangepicker({
                singleDatePicker: true,
                timePicker: true,
                timePicker24Hour: true,
                timePickerIncrement: 30,
                minDate: now,
                startDate: startDate, 
                locale: { 
                    format: 'DD/MM/YYYY HH:mm' 
                }
            }).on('apply.daterangepicker', updatePrice);

            $('#checkout').daterangepicker({
                singleDatePicker: true,
                timePicker: true,
                timePicker24Hour: true,
                timePickerIncrement: 30,
                minDate: now,
                startDate: endDate, 
                locale: { 
                    format: 'DD/MM/YYYY HH:mm' 
                }
            }).on('apply.daterangepicker', updatePrice);

            $('#guestCount').on('change', updatePrice); 

            function updatePrice() {
                let checkIn = moment($('#checkin').val(), 'DD/MM/YYYY HH:mm');
                let checkOut = moment($('#checkout').val(), 'DD/MM/YYYY HH:mm');
                let guestCount = parseInt($('#guestCount').val()) || 1; 

                if (checkIn.isValid() && checkOut.isValid() && checkOut.isAfter(checkIn)) {
                    let hours = Math.ceil(checkOut.diff(checkIn, 'hours', true));
                    let total = isDorm ? pricePerHour * hours * guestCount : pricePerHour * hours;

                    $('#hours-text').text(pricePerHour.toLocaleString('vi-VN') + "đ x " + hours + " giờ" + (isDorm ? " x " + guestCount + " người" : ""));
                    $('#subtotal').text(total.toLocaleString('vi-VN') + "đ");
                    $('#total').text(total.toLocaleString('vi-VN') + "đ");
                } else {
                    $('#hours-text').text(pricePerHour.toLocaleString('vi-VN') + "đ x 0 giờ");
                    $('#subtotal').text("0đ");
                    $('#total').text("0đ");
                }
            }
            updatePrice();
        });
    </script>
</body>
</html>