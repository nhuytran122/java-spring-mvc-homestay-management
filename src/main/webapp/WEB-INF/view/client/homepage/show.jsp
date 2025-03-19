<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                <ul class="navbar-nav me-auto"></ul>
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
                                        <li>
                                            <a class="dropdown-item d-flex align-items-center" href="/booking/booking-history">
                                                <i class="bi bi-calendar-check me-2"></i> Lịch sử đặt phòng
                                            </a>
                                        </li>
                                        <li>
                                            <a class="dropdown-item d-flex align-items-center" href="/profile">
                                                <i class="bi bi-person me-2"></i> Tài khoản của tôi
                                            </a>
                                        </li>
                                        <li><hr class="dropdown-divider"></li>
                                        <li>
                                            <form method="post" action="/logout" class="w-100">
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                                <button class="dropdown-item d-flex align-items-center w-100">
                                                    <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
                                                </button>
                                            </form>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="/login" class="btn btn-primary px-4">Đăng nhập</a>
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

    <div id="about" class="py-5" data-aos="fade-up">
        <div class="container">
            <div class="row">
                <div class="col-12 text-center mb-4">
                    <h2>Về Lullaby Homestay</h2>
                    <p class="text-muted">Thành lập từ năm 2010, được gìn giữ và vun đắp bởi gia đình.</p>
                </div>
            </div>
            <div class="row align-items-center">
                <div class="col-md-6 mb-4 mb-md-0">
                    <img src="/images/branch/1740023066983-536477305.jpg" class="img-fluid rounded" alt="About Serenity Homestay">
                </div>
                <div class="col-md-6">
                    <c:forEach var="infor" items="${listInfors}">
                    <p>${infor.description}</p>
                    </c:forEach>
                </div>

                
            </div>
        </div>
    </div>

    <div id="room-types" class="featured-stays py-5" data-aos="fade-up">
        <div class="container">
            <div class="row g-4">
                <h2 class="mb-4">Các loại phòng của chúng tôi</h2>
                <c:forEach var="roomType" items="${listRoomTypes}">
                    <div class="col-md-4">
                        <div class="card h-100 shadow-sm overflow-hidden border-0 rounded-4">
                            <img src="/images/room/${roomType.photo}" class="card-img-top" alt="${roomType.name}" style="height: 200px; object-fit: cover;">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title mb-2 fw-bold">${roomType.name}</h5>
                                <p class="card-text text-muted mb-3" style="flex-grow: 1;">${roomType.description}</p>
                                
                                <ul class="list-unstyled mb-3">
                                    <li><i class="bi bi-people me-2"></i> Số khách tối đa: ${roomType.maxGuest} người</li>
                                </ul>
    
                                <div class="d-flex justify-content-between align-items-center mt-auto">
                                    <span class="price fw-bold text-danger">
                                        <fmt:formatNumber type="number" value="${roomType.pricePerHour}" />đ/giờ
                                    </span>
                                    <a href="/room?roomTypeID=${roomType.roomTypeID}" class="btn btn-primary px-4 py-2 rounded-3">
                                        Xem các phòng
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <div id="amenities" class="amenities-section py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="mb-4">Tiện nghi của chúng tôi</h2>
            <div class="row">
                <c:forEach var="category" items="${listAmenityCategories}">
                    <c:if test="${not empty category.amenities}">
                        <div class="col-md-4 col-lg-3">
                            <h5 class="mb-3 d-flex align-items-center">
                                <span class="iconify me-2" data-icon="${category.icon}" data-width="24" data-height="24"></span>
                                ${category.categoryName}
                            </h5>
                            <ul class="list-unstyled">
                                <c:forEach var="amenity" items="${category.amenities}">
                                    <li class="d-flex align-items-center mb-2">
                                        <i class="bi bi-check2"></i> <span class="ms-2">${amenity.amenityName}</span>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>

    <div id="services" class="services-section py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="mb-4">Dịch vụ có thể bạn sẽ cần</h2>
            <div class="row">
                <c:forEach var="service" items="${listServices}">
                    <div class="col-md-6 mb-4" data-aos="zoom-in" data-aos-delay="100">
                        <div class="card border-0 shadow-sm">
                            <div class="row g-0">
                                <div class="col-md-4 bg-light d-flex align-items-center justify-content-center p-4">
                                    <span class="iconify text-success" data-icon="${service.icon}" data-width="50" data-height="50"></span></i>
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
    </div>

    <div class="rules-section py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="mb-4">Quy tắc chung</h2>
            <div class="border rounded-3" style="background-color: #fff;">
                <c:forEach var="rule" items="${listRules}">
                    <div class="p-4 border-bottom d-flex">
                        <div class="me-4" style="min-width: 40px;">
                            <i class="fas ${rule.icon} fs-5"></i>
                        </div>
                        <div class="row flex-grow-1">
                            <div class="col-md-3 fw-medium">${rule.ruleTitle}</div>
                            <div class="col-md-9">${rule.description}</div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    
    <div id="location" class="py-5 bg-light" data-aos="fade-up">
        <div class="container">
            <div class="row">
                <div class="col-12 text-center mb-4">
                    <h2>Vị trí của homestay</h2>
                </div>
            </div>
            <div class="row align-items-center">
                <div class="col-lg-5 mb-4 mb-lg-0">
                    <div class="card border-0 shadow-sm h-100">
                        <div class="card-body p-4">
                            <h4 class="mb-3">Làm sao để đến với Lullaby</h4>
                            <c:forEach var="branch" items="${listBranches}">
                                <div class="d-flex align-items-start mb-3">
                                    <i class="bi bi-geo-alt text-primary me-3 fs-4"></i>
                                    <div class="d-flex flex-column">
                                        <span><b>${branch.branchName}:</b> ${branch.address}</span>
                                    </div>
                                </div>
                            </c:forEach>
                            <div class="d-flex align-items-start mb-3">
                                <i class="bi bi-facebook text-primary me-3 fs-4"></i>
                                <div class="d-flex flex-column">
                                    <span>
                                        <b>
                                            <a href="https://www.facebook.com/profile.php?id=61550326932407" target="_blank" class="text-decoration-none text-dark">
                                                Lullaby Homestay
                                            </a>
                                        </b>
                                    </span>
                                </div>
                            </div>                            
                        </div>
                    </div>
                </div>
                
                <div class="col-lg-7">
                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <div class="row g-3">
                                <c:forEach var="branch" items="${listBranches}">
                                    <div class="col-md-6">
                                        <div class="branch-map">
                                            <h6 class="fw-bold mb-2">${branch.branchName}</h6>
                                            <div class="ratio ratio-4x3">
                                                ${branch.mapEmbedURL}
                                            </div>
                                            <a href="${branch.mapURL}" 
                                               target="_blank" 
                                               class="btn btn-outline-primary btn-sm mt-2 w-100">
                                                <i class="bi bi-map me-2"></i>Xem trên Google Maps
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="contact" class="py-5 bg-light" data-aos="fade-up">
        <div class="container">
            <div class="row">
                <div class="col-12 text-center mb-4">
                    <h2>Liên hệ với chúng tôi</h2>
                    <p class="text-muted">Chúng tôi ở đây để giải đáp cho bạn bất cứ lúc nào</p>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-5 mb-4 mb-lg-0">
                    <div class="card border-0 shadow-sm h-100">
                        <div class="card-body p-4">
                            <h4 class="mb-3">Cách thức liên hệ</h4>
                            <p>Có bất cứ câu hỏi nào về homestay hoặc cần giúp đỡ khi đặt phòng? Liên hệ với chúng tôi để nhận phản hồi ngay.</p>
                            <div class="mt-4">
                                <div class="d-flex align-items-center mb-3">
                                    <i class="bi bi-telephone text-primary me-3 fs-4"></i>
                                    <div>
                                        <h6 class="fw-bold mb-0">Số điện thoại</h6>
                                        <c:forEach var="branch" items="${listBranches}">
                                            <p class="mb-0">${branch.phone}</p>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="d-flex align-items-center mb-3">
                                    <i class="bi bi-envelope text-primary me-3 fs-4"></i>
                                    <div>
                                        <h6 class="fw-bold mb-0">Email</h6>
                                        <p class="mb-0">lullabyhomestay@gmail.com</p>
                                    </div>
                                </div>
                                <div class="d-flex align-items-center mb-3">
                                    <i class="bi bi-facebook text-primary me-3 fs-4"></i>
                                    <div>
                                        <h6 class="fw-bold mb-0">Facebook</h6>
                                        <p class="mb-0">
                                            <a href="https://www.facebook.com/profile.php?id=61550326932407"
                                               target="_blank" 
                                               class="text-decoration-none text-dark">
                                               Lullaby Homestay
                                            </a>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
    
                <div class="col-lg-7">
                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <h4 class="mb-3">Những câu hỏi thường gặp</h4>
                            <div class="accordion w-100" id="faqAccordion">
                                <c:forEach var="faq" items="${listFaqs}" varStatus="status">
                                    <div class="accordion-item my-2">
                                        <h2 class="accordion-header" id="heading${status.index}">
                                            <button class="accordion-button collapsed" 
                                                    type="button" data-bs-toggle="collapse" 
                                                    data-bs-target="#collapse${status.index}" 
                                                    aria-expanded="false" 
                                                    aria-controls="collapse${status.index}">
                                                ${faq.question}
                                            </button>
                                        </h2>
                                        <div id="collapse${status.index}" class="accordion-collapse collapse" 
                                             aria-labelledby="heading${status.index}" 
                                             data-bs-parent="#faqAccordion">
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
    </div>

    <div class="testimonials py-5 bg-light" data-aos="fade-up">
        <div class="container">
            <h2 class="text-center mb-5">Khách hàng nói về chúng tôi</h2>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex align-items-center mb-3">
                                <img src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde" class="rounded-circle me-3" alt="Guest" style="width: 50px; height: 50px;">
                                <div>
                                    <h6 class="mb-0">John Doe</h6>
                                    <small class="text-muted">October 2023</small>
                                </div>
                            </div>
                            <p class="card-text">"Amazing experience! The villa was exactly as described, and the host was very accommodating."</p>
                            <div class="text-warning">
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex align-items-center mb-3">
                                <img src="https://images.unsplash.com/photo-1494790108377-be9c29b29330" class="rounded-circle me-3" alt="Guest" style="width: 50px; height: 50px;">
                                <div>
                                    <h6 class="mb-0">Jane Smith</h6>
                                    <small class="text-muted">September 2023</small>
                                </div>
                            </div>
                            <p class="card-text">"The mountain retreat was perfect for a relaxing getaway. Highly recommend!"</p>
                            <div class="text-warning">
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-half"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex align-items-center mb-3">
                                <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d" class="rounded-circle me-3" alt="Guest" style="width: 50px; height: 50px;">
                                <div>
                                    <h6 class="mb-0">Alex Johnson</h6>
                                    <small class="text-muted">August 2023</small>
                                </div>
                            </div>
                            <p class="card-text">"The urban loft was stylish and comfortable. Great location and amenities!"</p>
                            <div class="text-warning">
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                                <i class="bi bi-star-fill"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="member-discount" class="py-5 bg-light" data-aos="fade-up">
        <h2 class="text-center mb-5">
            Chính Sách Ưu Đãi Cho Thành Viên
        </h2>
    
        <div class="text-center text-muted mb-4">
            <p class="mb-0">Yêu cầu điểm tối thiểu: tùy theo từng loại thành viên</p>
            <p class="mb-0"><span class="fw-bold">100.000</span> = 
                <span class="fw-bold text-success">+10 điểm</span>
            </p>
        </div>
    
        <div class="row g-4 justify-content-center">
            <c:forEach var="customerType" items="${listCustomerTypes}">
                <c:if test="${customerType.discountRate > 0}">
                    <div class="col-md-3">
                        <div class="card h-100 shadow-lg border-0 rounded-4 overflow-hidden position-relative 
                                    transition-transform hover-translate-y-n3">
                            <div class="card-body text-center d-flex flex-column justify-content-between">
                                <h5 class="fw-bold mb-2">
                                    ${customerType.name}
                                </h5>
    
                                <div class="text-white p-4 rounded-3 mb-3" style="background-color: #ea8685;">
                                    <h3 class="mb-1 fw-bold">
                                        <fmt:formatNumber value="${customerType.discountRate}" pattern="#'%'"/>
                                    </h3>
                                    <small>Giảm giá trực tiếp</small>
                                </div>
    
                                <p class="text-muted mb-1">
                                    Yêu cầu điểm tối thiểu: 
                                    <span class="fw-bold">
                                        <fmt:formatNumber value="${customerType.minPoint}" pattern="#"/>
                                    </span>
                                </p>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>

    <div class="why-choose-us py-5" data-aos="fade-up">
        <div class="container">
            <h2 class="text-center mb-5">Tại sao bạn nên chọn Lullaby Homestay?</h2>
            <div class="row g-4">
                <div class="col-md-4 text-center">
                    <i class="bi bi-house-door fs-1 text-primary mb-3"></i>
                    <h5>Không gian như ở nhà</h5>
                    <p>Tận hưởng sự thoải mái và ấm áp như chính ngôi nhà của bạn, dù ở bất cứ đâu.</p>
                </div>
                <div class="col-md-4 text-center">
                    <i class="bi bi-list-task fs-1 text-primary mb-3"></i>
                    <h5>Đa dạng lựa chọn</h5>
                    <p>Từ phòng riêng ấm cúng đến phòng dorm sôi động, phù hợp cho mọi nhu cầu.</p>
                </div>
                <div class="col-md-4 text-center">
                    <i class="bi bi-headset fs-1 text-primary mb-3"></i>
                    <h5>Hỗ trợ 24/7</h5>
                    <p>Chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7, bất kể khi nào bạn cần.</p>
                </div>
            </div>
        </div>
    </div>
    
    <div class="cta-section py-5 text-white text-center" style="background-color: #74b9ff;">
        <div class="container">
            <h2 class="mb-4">Bạn đang tìm một nơi dừng chân cho kỳ nghỉ của bạn?</h2>
            <p class="lead mb-4">Đồng hành cùng home để có một kỳ nghỉ đáng nhớ ngay nào!</p>
            <a href="#" class="btn btn-primary btn-lg">Đăng ký ngay</a>
        </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
<script src="/client/js/scroll.js"></script>
</body>
</html>