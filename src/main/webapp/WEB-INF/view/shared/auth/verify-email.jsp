<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Xác nhận Email - Lullaby Homestay</title>
    <jsp:include page="../../client/layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/login-style.css" />
  </head>

  <body class="bg-light">
    <jsp:include page="../../client/layout/header.jsp" />

    <div class="container py-5 mt-5">
      <div class="row justify-content-center">
        <div class="col-lg-10">
          <div class="row bg-white shadow rounded-4 overflow-hidden">
            <div
              class="col-md-6 d-flex justify-content-center align-items-center p-4"
            >
              <img
                src="/images/branch/1740023066983-536477305.jpg"
                alt="Lullaby Logo"
                class="img-fluid rounded-3"
                style="max-height: 400px"
              />
            </div>

            <div class="col-md-6 d-flex flex-column justify-content-center p-4">
              <h3 class="fw-bold text-center mb-4">Xác nhận Email</h3>

              <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">${error}</div>
              </c:if>

              <c:if test="${resend}">
                <form
                  action="/resend-verification-email"
                  method="post"
                  class="mt-4"
                >
                  <input
                    type="hidden"
                    name="${_csrf.parameterName}"
                    value="${_csrf.token}"
                  />
                  <div class="mb-3">
                    <label for="email" class="form-label fw-semibold"
                      >Email của bạn</label
                    >
                    <input
                      type="email"
                      id="email"
                      name="email"
                      class="form-control"
                      placeholder="Nhập email đã dùng để đăng ký"
                      required
                    />
                  </div>
                  <button type="submit" class="btn btn-primary w-100">
                    Gửi lại email xác nhận
                  </button>
                </form>
              </c:if>

              <c:if test="${not empty message and empty error}">
                <div
                  class="alert alert-success alert-dismissible fade show text-center d-flex align-items-center justify-content-center gap-2"
                  role="alert"
                >
                  <i class="bi bi-check-circle-fill fs-4 text-success me-2"></i>
                  <div>
                    ${message}<br />
                    <strong
                      >Bạn sẽ được chuyển hướng đến trang đăng nhập trong vòng 3
                      giây.</strong
                    >
                  </div>
                  <button
                    type="button"
                    class="btn-close position-absolute end-0 me-3"
                    data-bs-dismiss="alert"
                    aria-label="Close"
                  ></button>
                </div>

                <script>
                  setTimeout(function () {
                    window.location.href = "/login";
                  }, 3000);
                </script>
              </c:if>

              <p class="text-center mt-4">
                Quay lại
                <a
                  href="/login"
                  class="fw-bold text-decoration-none text-primary"
                  >Đăng nhập</a
                >
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../../client/layout/footer.jsp" />
    <jsp:include page="../../client/layout/import-js.jsp" />
  </body>
</html>
