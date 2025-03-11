<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                              <c:choose>
                                  <c:when test="${not empty room.thumbnail}">
                                      <img src="/images/room/${room.thumbnail}" class="d-block w-100" alt="Thumbnail" style="height: 400px; object-fit: cover">
                                  </c:when>
                                  <c:otherwise>
                                      <img src="/images/room/default-img.jpg" class="d-block w-100" alt="Default Img" style="height: 400px; object-fit: cover">
                                  </c:otherwise>
                              </c:choose>
                            </div>
                            <c:forEach var="photo" items="${room.roomPhotos}">
                              <div class="carousel-item">
                                  <img src="/images/room/${photo.photo}" class="d-block w-100" alt="Meeting Room" style="height: 400px; object-fit: cover">
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
                    <div class="booking-card card sticky-top">
                        <div class="card-body">
                            <h3 class="price mb-4"><fmt:formatNumber type="number"
                                value="${room.roomType.pricePerHour}" />đ <small class="text-muted">/giờ</small></h3>
                            <form>
                                <div class="row g-3 mb-4">
                                    <div class="col-6">
                                        <label class="form-label">Check-in</label>
                                        <input type="text" id="checkin" class="form-control datetime-picker" placeholder="Chọn thời gian checkin">
                                    </div>
                                    <div class="col-6">
                                        <label class="form-label">Check-out</label>
                                        <input type="text" id="checkout" class="form-control datetime-picker" placeholder="Chọn thời gian checkout">
                                    </div>
                                    <div class="col-12">
                                        <label class="form-label">Số lượng khách</label>
                                        <select class="form-select">
                                            <c:forEach var="i" begin="1" end="${room.roomType.maxGuest}">
                                                <option>${i} người</option>
                                            </c:forEach>
                                        </select>                                        
                                    </div>
                                </div>
                                <button class="btn btn-primary btn-custom w-100 mb-3">Đặt phòng</button>
                                
                                <div class="price-details mt-4">
                                    <div class="d-flex justify-content-between mb-2">
                                        <span><fmt:formatNumber type="number"
                                            value="${room.roomType.pricePerHour}" /> x 5 giờ</span>
                                        <span>0đ</span>
                                    </div>
                                    <hr>
                                    <div class="d-flex justify-content-between fw-bold">
                                        <span>Tổng cộng</span>
                                        <span>0đ</span>
                                    </div>
                                </div>
                                <div class="alert alert-danger d-none" id="booking-error"></div>
                            </form>
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
                    <h4 class="mb-4">Đánh giá của khách hàng <span class="badge bg-success">4.9</span></h4>
                    <div class="reviews-list">
                        <div class="review-card mb-4">
                            <div class="d-flex mb-3">
                                <img src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde" class="rounded-circle reviewer-img" alt="Reviewer">
                                <div class="ms-3">
                                    <h6 class="mb-1">Như Ý</h6>
                                    <div class="text-muted">11/03/2025</div>
                                </div>
                            </div>
                            <div class="review-stars mb-2">
                                <i class="bi bi-star-fill text-warning"></i>
                                <i class="bi bi-star-fill text-warning"></i>
                                <i class="bi bi-star-fill text-warning"></i>
                                <i class="bi bi-star-fill text-warning"></i>
                                <i class="bi bi-star-fill text-warning"></i>
                            </div>
                            <p>Mọi thứ rất tốt, nhân viên nhiệt tình, dễ thương và thân thiện.</p>
                        </div>

                        <div class="review-card mb-4">
                            <div class="d-flex mb-3">
                                <img src="https://images.unsplash.com/photo-1494790108377-be9c29b29330" class="rounded-circle reviewer-img" alt="Reviewer">
                                <div class="ms-3">
                                    <h6 class="mb-1">Như Ý</h6>
                                    <div class="text-muted">12/03/2025</div>
                                </div>
                            </div>
                            <div class="review-stars mb-2">
                                <i class="bi bi-star-fill text-warning"></i>
                                <i class="bi bi-star-fill text-warning"></i>
                                <i class="bi bi-star-fill text-warning"></i>
                                <i class="bi bi-star-fill text-warning"></i>
                                <i class="bi bi-star-half text-warning"></i>
                            </div>
                            <p>Homestay sạch sẽ, thoáng. Anh chị chủ siêu nhiệt tình và thân thiện chuẩn bị đầy đủ đồ dùng trong phòng ko có mình có thể xuống mượn!</p>
                        </div>

                        <div class="write-review-section">
                            <h5 class="mb-3">Viết đánh giá</h5>
                            <form class="review-form">
                                <div class="mb-3">
                                    <label class="form-label">Rating</label>
                                    <div class="rating-input mb-2">
                                        <i class="bi bi-star fs-4 me-1"></i>
                                        <i class="bi bi-star fs-4 me-1"></i>
                                        <i class="bi bi-star fs-4 me-1"></i>
                                        <i class="bi bi-star fs-4 me-1"></i>
                                        <i class="bi bi-star fs-4"></i>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Đánh giá của bạn</label>
                                    <textarea class="form-control" rows="4" placeholder="Chia sẻ trải nghiệm của bạn..."></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">Gửi</button>
                            </form>
                        </div>
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
            $('.datetime-picker').daterangepicker({
                singleDatePicker: true, 
                timePicker: true,  
                timePicker24Hour: true,  
                timePickerIncrement: 10,
                locale: {
                    format: 'DD/MM/YYYY HH:mm'
                }
            });
        });
    </script>    
</body>
</html>