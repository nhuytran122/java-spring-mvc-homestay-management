<%@page contentType="text/html" pageEncoding="UTF-8" %>
<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center" href="#">
            <i class="bi bi-house-heart-fill me-2 text-primary"></i>Lullaby Homestay
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
            </ul>
            <div class="guest-profile d-flex align-items-center ms-auto">
                <div class="dropdown">
                    <button class="btn btn-outline-primary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                        <img src="https://images.unsplash.com/photo-1633332755192-727a05c4013d" alt="Guest" class="guest-avatar me-2 rounded-circle" width="40" height="40">
                        John
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li>
                            <a class="dropdown-item" href="#"><i class="bi bi-calendar-check me-2"></i>Lịch sử đặt phòng</a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="#"><i class="bi bi-person me-2"></i>Tài khoản của tôi</a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item" href="#"><i class="bi bi-box-arrow-right me-2"></i>Logout</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</nav>