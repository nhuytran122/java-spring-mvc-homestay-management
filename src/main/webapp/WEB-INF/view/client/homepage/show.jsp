<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/home-style.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center" href="#">
                <i class="bi bi-house-heart-fill me-2 text-primary"></i>Lullaby Homestay
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav mx-auto">
                    <li class="nav-item"><a class="nav-link" href="#about">Thông tin</a></li>
                    <li class="nav-item"><a class="nav-link" href="/room">Phòng</a></li>
                    <li class="nav-item"><a class="nav-link" href="#room-types">Loại phòng</a></li>
                    <li class="nav-item"><a class="nav-link" href="#amenities">Tiện nghi</a></li>
                    <li class="nav-item"><a class="nav-link" href="#services">Dịch vụ</a></li>
                    <li class="nav-item"><a class="nav-link" href="#location">Vị trí</a></li>
                    <li class="nav-item"><a class="nav-link" href="#rules">Quy tắc & FAQ</a></li>
                    <li class="nav-item"><a class="nav-link" href="#member-discount">Ưu đãi</a></li>
                    <li class="nav-item"><a class="nav-link" href="#refund-policy">Chính sách hoàn tiền</a></li>
                </ul>
                <div class="d-flex align-items-center ms-auto">
                    <c:choose>
                        <c:when test="${not empty pageContext.request.userPrincipal}">
                            <div class="guest-profile d-flex align-items-center">
                                <div class="dropdown">
                                    <button class="btn btn-outline-primary dropdown-toggle d-flex align-items-center" type="button" data-bs-toggle="dropdown">
                                        <img src="/images/avatar/${not empty sessionScope.avatar ? sessionScope.avatar : 'default-img.jpg'}" 
                                             class="guest-avatar me-2 rounded-circle border" width="40" height="40">
                                        <span class="fw-semibold">${sessionScope.fullName}</span>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-end shadow">
                                        <li><a class="dropdown-item d-flex align-items-center" href="/booking/booking-history"><i class="bi bi-calendar-check me-2"></i> Lịch sử đặt phòng</a></li>
                                        <li><a class="dropdown-item d-flex align-items-center" href="/profile"><i class="bi bi-person me-2"></i> Tài khoản của tôi</a></li>
                                        <li><hr class="dropdown-divider"></li>
                                        <li>
                                            <form method="post" action="/logout" class="w-100">
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                                <button class="dropdown-item d-flex align-items-center w-100"><i class="bi bi-box-arrow-right me-2"></i> Đăng xuất</button>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="/login" class="btn btn-primary px-3">Đăng nhập</a>
                            <a href="/register" class="btn btn-primary px-3 ms-2">Đăng ký</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </nav>

    <div class="hero-section">
        <div class="container h-100">
            <div class="row h-100 align-items-center">
                <div class="col-lg-6">
                    <h1 class="display-4 fw-bold">Nơi An Yên, Chốn Bình Yên.</h1>
                    <p class="lead">Hơn Cả Một Chỗ Ở – Là Cảm Giác Của Nhà.</p>
                </div>
            </div>
        </div>
    </div>

    <section id="about" class="py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Về Lullaby Homestay</h2>
            <p class="text-center text-muted mb-5">Thành lập từ năm 2024, được gìn giữ và vun đắp bởi gia đình.</p>
            <div class="row align-items-center">
                <div class="col-md-6 mb-4 mb-md-0">
                    <img src="/images/branch/1740023066983-536477305.jpg" class="img-fluid rounded" alt="About Lullaby Homestay">
                </div>
                <div class="col-md-6">
                    <c:forEach var="infor" items="${listInfors}">
                        <p>${infor.description}</p>
                    </c:forEach>
                </div>
            </div>
        </div>
    </section>

    <section id="room-types" class="py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Các loại phòng của chúng tôi</h2>
            <div class="row g-4">
                <c:forEach var="roomType" items="${listRoomTypes}">
                    <div class="col-md-6 col-lg-4">
                        <div class="card h-100 border-0 shadow-sm rounded">
                            <img src="/images/room/${roomType.photo}" class="card-img-top rounded-top" alt="${roomType.name}" style="height: 200px; object-fit: cover;">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title mb-2 fw-bold">${roomType.name}</h5>
                                <p class="card-text text-muted mb-3" style="min-height: 60px;">
                                    ${roomType.description}
                                </p>
                                <ul class="list-unstyled text-muted mb-3">
                                    <li><i class="bi bi-people me-2"></i> Số khách tối đa: ${roomType.maxGuest} người</li>
                                </ul>
                                <div class="mt-auto">
                                    <a href="/room?roomTypeID=${roomType.roomTypeID}" class="btn btn-outline-primary w-100">
                                        <i class="bi bi-door-open me-2"></i> Xem các phòng
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>

    <section id="amenities" class="py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Tiện nghi của chúng tôi</h2>
            <div class="row g-4">
                <c:forEach var="category" items="${listAmenityCategories}">
                    <c:if test="${not empty category.amenities}">
                        <div class="col-md-4 col-lg-3">
                            <div class="card h-100 border-0">
                                <div class="card-body">
                                    <h5 class="mb-3 d-flex align-items-center">
                                        <span class="iconify me-2" data-icon="${category.icon}" data-width="24" data-height="24"></span>
                                        ${category.categoryName}
                                    </h5>
                                    <ul class="list-unstyled">
                                        <c:forEach var="amenity" items="${category.amenities}">
                                            <li class="d-flex align-items-center mb-2">
                                                <i class="bi bi-check2 me-2"></i> ${amenity.amenityName}
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </section>

    <section id="services" class="py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Dịch vụ có thể bạn sẽ cần</h2>
            <div class="row g-4">
                <c:forEach var="service" items="${listServices}">
                    <div class="col-md-6">
                        <div class="card h-100 border-0">
                            <div class="row g-0">
                                <div class="col-md-4 bg-light d-flex align-items-center justify-content-center p-4">
                                    <span class="iconify text-success" data-icon="${service.icon}" data-width="50" data-height="50"></span>
                                </div>
                                <div class="col-md-8">
                                    <div class="card-body">
                                        <h3 class="card-title h5 fw-bold">${service.serviceName}</h3>
                                        <p class="card-text">${service.description}</p>
                                        <div class="d-flex justify-content-between align-items-center">
                                            <p class="fw-bold mb-0"><fmt:formatNumber type="number" value="${service.price}" />đ</p>
                                            <span class="badge bg-success">${service.unit}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>

    <section id="location" class="location-section py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Vị trí và liên hệ</h2>
            <div class="row g-4">
                <div class="col-lg-6">
                    <div class="contact-card">
                        <h4 class="mb-4">Làm sao để đến với Lullaby</h4>
                        <div class="contact-info">
                            <c:forEach var="branch" items="${listBranches}">
                                <div class="info-item">
                                    <i class="bi bi-geo-alt"></i>
                                    <div>
                                        <h6 class="fw-bold mb-0">${branch.branchName}</h6>
                                        <p class="mb-0">${branch.address}</p>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="info-item">
                                <i class="bi bi-facebook"></i>
                                <div>
                                    <a href="https://www.facebook.com/profile.php?id=61550326932407" target="_blank" class="text-decoration-none text-dark">
                                        Lullaby Homestay
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="contact-card">
                        <h4 class="mb-4">Thông tin liên hệ</h4>
                        <p>Có bất cứ câu hỏi nào về homestay hoặc cần giúp đỡ khi đặt phòng? Liên hệ với chúng tôi để nhận phản hồi ngay.</p>
                        <div class="contact-info">
                            <div class="info-item">
                                <i class="bi bi-telephone"></i>
                                <div>
                                    <h6 class="fw-bold mb-0">Số điện thoại</h6>
                                    <c:forEach var="branch" items="${listBranches}">
                                        <p class="mb-0">${branch.phone}</p>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="info-item">
                                <i class="bi bi-envelope"></i>
                                <div>
                                    <h6 class="fw-bold mb-0">Email</h6>
                                    <p class="mb-0">lullabyhomestay@gmail.com</p>
                                </div>
                            </div>
                            <div class="info-item">
                                <i class="bi bi-facebook"></i>
                                <div>
                                    <h6 class="fw-bold mb-0">Facebook</h6>
                                    <a href="https://www.facebook.com/profile.php?id=61550326932407" target="_blank" class="text-decoration-none text-dark">
                                        Lullaby Homestay
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section id="rules" class="rules-section py-5 bg-light" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Quy tắc và câu hỏi thường gặp</h2>
            <div class="row g-4">
                <div class="col-lg-6">
                    <div class="card h-100 border-0">
                        <div class="card-body">
                            <h4 class="mb-4">Quy tắc chung</h4>
                            <c:forEach var="rule" items="${listRules}">
                                <div class="rule-item">
                                    <i class="fas ${rule.icon} fs-5 me-4"></i>
                                    <div>
                                        <h6 class="fw-medium mb-1">${rule.ruleTitle}</h6>
                                        <p class="mb-0">${rule.description}</p>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="card h-100 border-0 faq-section">
                        <div class="card-body">
                            <h4 class="mb-4">Câu hỏi thường gặp</h4>
                            <div class="accordion" id="faqAccordion">
                                <c:forEach var="faq" items="${listFaqs}" varStatus="status">
                                    <c:set var="index" value="${status.index}" />
                                    <div class="accordion-item">
                                        <h2 class="accordion-header" id="heading${index}">
                                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" 
                                                    data-bs-target="#collapse${index}" aria-expanded="false" 
                                                    aria-controls="collapse${index}">
                                                ${faq.question}
                                            </button>
                                        </h2>
                                        <div id="collapse${index}" class="accordion-collapse collapse" 
                                             aria-labelledby="heading${index}" data-bs-parent="#faqAccordion">
                                            <div class="accordion-body">
                                                ${faq.answer}
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <c:if test="${not empty listReviews}">
        <section class="testimonials py-5 bg-light" data-aos="fade-up">
            <div class="container">
                <h2 class="section-title">Khách hàng nói về chúng tôi</h2>
                <div id="reviewCarousel" class="carousel slide" data-bs-ride="carousel">
                    <div class="carousel-inner">
                        <c:forEach var="review" items="${listReviews}" varStatus="status">
                            <c:set var="index" value="${status.index}" />
                            <c:set var="customer" value="${review.booking.customer}" />
                            <c:if test="${index % 3 == 0}">
                                <div class="carousel-item ${index == 0 ? 'active' : ''}">
                                    <div class="row g-4 justify-content-center">
                            </c:if>
                            <div class="col-md-4">
                                <div class="card h-100 border-0">
                                    <div class="card-body">
                                        <div class="d-flex align-items-center mb-3">
                                            <img src="/images/avatar/${customer.avatar}" class="rounded-circle me-3" 
                                                 alt="Avatar" style="width: 50px; height: 50px;">
                                            <div>
                                                <h6 class="mb-0">${customer.fullName}</h6>
                                                <small class="text-muted">${f:formatLocalDateTime(review.createdAt)}</small>
                                            </div>
                                        </div>
                                        <p class="card-text">${review.comment}</p>
                                        <div class="text-warning">
                                            <c:forEach begin="1" end="${review.rating}">
                                                <i class="bi bi-star-fill"></i>
                                            </c:forEach>
                                            <c:forEach begin="${review.rating + 1}" end="5">
                                                <i class="bi bi-star"></i>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${index % 3 == 2 or status.last}">
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#reviewCarousel" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Trước</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#reviewCarousel" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Sau</span>
                    </button>
                </div>
            </div>
        </section>
    </c:if>

    <section id="member-discount" class="py-5 bg-light" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Chính sách ưu đãi cho thành viên</h2>
            <div class="text-center text-muted mb-5">
                <p class="mb-0">Yêu cầu điểm tối thiểu: tùy theo từng loại thành viên</p>
                <p class="mb-0"><span class="fw-bold">100.000</span> = <span class="fw-bold text-success">+10 điểm</span></p>
            </div>
            <div class="row g-4 justify-content-center">
                <c:forEach var="customerType" items="${listCustomerTypes}">
                    <c:if test="${customerType.discountRate > 0}">
                        <div class="col-md-3">
                            <div class="card h-100 border-0">
                                <div class="card-body text-center d-flex flex-column">
                                    <h5 class="fw-bold mb-3">${customerType.name}</h5>
                                    <div class="text-white p-4 rounded-3 mb-3" style="background-color: #ea8685;">
                                        <h3 class="mb-1 fw-bold"><fmt:formatNumber value="${customerType.discountRate}" pattern="#'%'"/></h3>
                                        <small>Giảm giá trực tiếp</small>
                                    </div>
                                    <p class="text-muted mb-0">Yêu cầu điểm tối thiểu: <span class="fw-bold"><fmt:formatNumber value="${customerType.minPoint}" pattern="#"/></span></p>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </section>

    <section id="refund-policy" class="py-5 bg-light" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Chính sách hoàn tiền khi hủy đặt phòng</h2>
            <div class="text-center text-muted mb-5">
                <p class="mb-0">Chính sách hoàn tiền áp dụng tùy theo thời gian hủy phòng trước ngày check-in:</p>
            </div>
            <div class="row g-4 justify-content-center">
                <c:forEach var="refundType" items="${refundTypes}">
                    <div class="col-md-3">
                        <div class="card h-100 border-0">
                            <div class="card-body text-center d-flex flex-column">
                                <div class="mb-3"><i class="bi bi-cash-stack fs-1 text-success"></i></div>
                                <h5 class="fw-bold mb-3">${refundType.displayName}</h5>
                                <div class="p-4 rounded-3 mb-3 flex-grow-1" style="background-color: #6c757d; color: #fff;">
                                    <small>${refundType.descriptionPolicy}</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>

    <section class="why-choose-us py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="section-title">Tại sao bạn nên chọn Lullaby Homestay?</h2>
            <div class="row g-4">
                <div class="col-md-4 text-center">
                    <div class="card h-100 border-0">
                        <div class="card-body">
                            <i class="bi bi-house-door fs-1 text-primary mb-3"></i>
                            <h5 class="fw-bold">Không gian như ở nhà</h5>
                            <p>Tận hưởng sự thoải mái và ấm áp như chính ngôi nhà của bạn, dù ở bất cứ đâu.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 text-center">
                    <div class="card h-100 border-0">
                        <div class="card-body">
                            <i class="bi bi-list-task fs-1 text-primary mb-3"></i>
                            <h5 class="fw-bold">Đa dạng lựa chọn</h5>
                            <p>Từ phòng riêng ấm cúng đến phòng dorm sôi động, phù hợp cho mọi nhu cầu.</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 text-center">
                    <div class="card h-100 border-0">
                        <div class="card-body">
                            <i class="bi bi-headset fs-1 text-primary mb-3"></i>
                            <h5 class="fw-bold">Hỗ trợ 24/7</h5>
                            <p>Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7, bất kể khi nào bạn cần.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <c:if test="${empty pageContext.request.userPrincipal}">
        <section class="cta-section py-5 text-white text-center" style="background-color: #74b9ff;">
            <div class="container">
                <h2 class="mb-4">Bạn đang tìm một nơi dừng chân cho kỳ nghỉ của bạn?</h2>
                <p class="lead mb-4">Đồng hành cùng chúng tôi để có một kỳ nghỉ đáng nhớ!</p>
                <a href="/register" class="btn btn-primary btn-lg">Đăng ký ngay</a>
            </div>
        </section>
    </c:if>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <script src="/client/js/scroll.js"></script>
</body>
</html>