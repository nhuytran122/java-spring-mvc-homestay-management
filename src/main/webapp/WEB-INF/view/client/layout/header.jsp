<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center" href="/">
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
                        <a href="/register" class="btn btn-primary px-4 mx-2">Đăng ký</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>