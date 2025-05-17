<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quên mật khẩu - Lullaby Homestay</title>
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
              <h3 class="title">Quên mật khẩu</h3>

              <form
                method="post"
                action="/forgot-password"
                class="form-horizontal"
              >
                <input
                  type="hidden"
                  name="${_csrf.parameterName}"
                  value="${_csrf.token}"
                />

                <div class="form-group">
                  <label>Email</label> <span class="text-danger">*</span>
                  <input
                    class="form-control mt-2"
                    type="email"
                    name="email"
                    placeholder="Nhập email đã đăng ký"
                  />
                </div>

                <c:if test="${not empty error}">
                  <div class="alert alert-danger">${error}</div>
                </c:if>
                <c:if test="${not empty message}">
                  <div class="alert alert-success">${message}</div>
                </c:if>

                <button
                  type="submit"
                  name="btn-login"
                  class="btn btn-default mt-3"
                >
                  Gửi yêu cầu đặt lại mật khẩu
                </button>
              </form>

              <p class="mt-4 text-center">
                Bạn đã nhớ mật khẩu?
                <a
                  href="/login"
                  class="text-primary fw-bold"
                  style="text-decoration: none"
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
