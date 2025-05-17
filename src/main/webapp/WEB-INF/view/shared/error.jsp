<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <title>Đã xảy ra lỗi</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css"
      rel="stylesheet"
    />

    <link rel="shortcut icon" href="/images/homestay/lullaby.jpg" />
  </head>
  <body
    class="bg-light d-flex align-items-center justify-content-center min-vh-100"
  >
    <div
      class="card shadow-lg text-center p-4"
      style="max-width: 520px; width: 100%"
    >
      <div class="card-body">
        <div class="text-danger fs-1 mb-3">
          <i class="bi bi-exclamation-triangle-fill"></i>
        </div>
        <h2 class="card-title mb-2 fw-bold">Đã xảy ra lỗi!</h2>
        <p class="card-text text-muted mb-4">${errorMessage}</p>
        <a href="/" class="btn btn-primary">
          <i class="bi bi-house-door-fill me-2"></i>Về trang chủ
        </a>
      </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  </body>
</html>
