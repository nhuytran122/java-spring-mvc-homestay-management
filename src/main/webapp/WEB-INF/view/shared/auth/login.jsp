<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng nhập - Lullaby Homestay</title>
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
              <h3 class="title">Đăng nhập</h3>
              <form method="post" action="/login" class="form-horizontal">
                <div class="form-group">
                  <label>Email</label> <span class="text-danger">*</span>
                  <input
                    class="form-control mt-2"
                    name="username"
                    type="email"
                    placeholder="Nhập email"
                  />
                </div>

                <div class="form-group">
                  <label>Mật khẩu</label> <span class="text-danger">*</span>
                  <input
                    class="form-control mt-2"
                    type="password"
                    name="password"
                    placeholder="Mật khẩu"
                  />
                </div>

                <input
                  type="hidden"
                  name="${_csrf.parameterName}"
                  value="${_csrf.token}"
                />

                <c:if test="${param.error != null}">
                  <div class="my-2" style="color: red">
                    Thông tin đăng nhập không chính xác.
                  </div>
                </c:if>
                <c:if test="${param.logout != null}">
                  <div class="my-2" style="color: green">
                    Đăng xuất thành công.
                  </div>
                </c:if>

                <button
                  type="submit"
                  name="btn-login"
                  class="btn btn-default mt-3 w-100"
                >
                  Đăng nhập
                </button>

                <div class="text-center mt-3">
                  <a
                    href="/forgot-password"
                    class="text-primary fw-bold"
                    style="text-decoration: none"
                  >
                    Quên mật khẩu?
                  </a>
                </div>
              </form>

              <p class="mt-4 text-center">
                Bạn chưa có tài khoản?
                <a
                  href="/register"
                  class="text-primary fw-bold"
                  style="text-decoration: none"
                  >Đăng ký</a
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
