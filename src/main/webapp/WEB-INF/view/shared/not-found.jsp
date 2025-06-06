<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Không tìm thấy dữ liệu</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
    <link rel="stylesheet" href="/admin/css/404.css">
    <link rel="shortcut icon" href="/images/homestay/lullaby.jpg" />
</head>
<body class="error-page">
    <div class="container min-vh-100 d-flex align-items-center justify-content-center">
        <div class="text-center">
            <div class="error-image mb-4">
                <img src="https://images.unsplash.com/photo-1578328819058-b69f3a3b0f6b" alt="Not Found Error" class="img-fluid rounded-circle" style="width: 200px; height: 200px; object-fit: cover;">
            </div>
            <h1 class="error-code mb-4">Dữ liệu không tồn tại</h1>
            <h2 class="error-title mb-4">${errorMessage != null ? errorMessage : 'Dữ liệu bạn yêu cầu không tồn tại trong hệ thống.'}</h2>
            <div class="error-actions">
                <a href="/" class="btn btn-primary my-4">
                    <i class="bi bi-house-door me-2"></i>Về trang chủ
                </a>
            </div>
        </div>
            
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>