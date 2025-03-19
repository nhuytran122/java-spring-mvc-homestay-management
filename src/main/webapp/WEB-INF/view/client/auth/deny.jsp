<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Từ chối truy cập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="shortcut icon" href="/images/homestay/lullaby.jpg" />
</head>
<body class="bg-light">
    <div class="container min-vh-100 d-flex align-items-center justify-content-center">
        <div class="text-center bg-white p-5 rounded shadow">
            <div class="text-danger mb-4">
                <i class="fas fa-ban fa-5x"></i>
            </div>
            <h1 class="text-danger mb-3 fw-bold">Truy cập bị từ chối</h1>
            <p class="text-muted mb-4 fs-5">Bạn không có quyền truy cập vào trang này. Vui lòng liên hệ với quản trị viên để được hỗ trợ.</p>
            <c:if test="${not empty errorMessage}">
               <p class="text-muted mb-4 fs-5">${errorMessage}</p>
            </c:if>
            <a href="/" class="btn btn-primary btn-lg">
                <i class="fas fa-home me-2"></i>Về trang chủ
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>