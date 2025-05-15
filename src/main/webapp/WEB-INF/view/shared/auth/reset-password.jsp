<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Đặt lại mật khẩu - Lullaby Homestay</title>
    <jsp:include page="../../client/layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/login-style.css" />
  </head>
  <body class="bg-light">
    <jsp:include page="../../client/layout/header.jsp" />

    <div class="container py-5 mt-5">
      <div class="row w-100">
        <div class="col-md-6 d-flex justify-content-center align-items-center">
          <img
            src="/images/branch/1740023066983-536477305.jpg"
            alt="Lullaby Logo"
            class="img-fluid"
            style="max-width: 500px"
          />
        </div>

        <div class="col-md-6">
          <div class="form-bg">
            <div class="form-container">
              <h3 class="title">Đặt lại mật khẩu</h3>

              <form:form
                modelAttribute="resetPasswordDTO"
                method="post"
                class="form-horizontal"
                action="/reset-password"
              >
                <form:hidden path="token" />
                <input
                  type="hidden"
                  name="${_csrf.parameterName}"
                  value="${_csrf.token}"
                />

                <div class="form-group">
                  <label>Mật khẩu mới</label> <span class="text-danger">*</span>
                  <form:password
                    path="password"
                    cssClass="form-control mt-2"
                    placeholder="Nhập mật khẩu mới"
                  />
                  <form:errors path="password" cssClass="text-danger" />
                </div>

                <div class="form-group mt-3">
                  <label>Xác nhận mật khẩu</label>
                  <span class="text-danger">*</span>
                  <form:password
                    path="confirmPassword"
                    cssClass="form-control mt-2"
                    placeholder="Nhập lại mật khẩu"
                  />
                  <form:errors path="confirmPassword" cssClass="text-danger" />
                </div>

                <c:if test="${not empty error}">
                  <div class="alert alert-danger mt-3">${error}</div>
                </c:if>
                <c:if test="${not empty message and empty error}">
                  <div
                    class="alert alert-success alert-dismissible fade show"
                    role="alert"
                  >
                    ${message} Bạn sẽ được chuyển hướng đến trang đăng nhập
                    trong vòng 3 giây.
                    <button
                      type="button"
                      class="btn-close"
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

                <button type="submit" class="btn btn-default mt-3">
                  Cập nhật mật khẩu
                </button>
              </form:form>

              <p class="mt-4 text-center">
                Quay về
                <a href="/login" class="text-primary fw-bold"
                  >trang đăng nhập</a
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
