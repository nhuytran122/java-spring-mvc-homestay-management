<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng ký - Lullaby Homestay</title>
    <jsp:include page="../../client/layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/login-style.css" />
  </head>
  <body class="bg-light">
    <jsp:include page="../../client/layout/header.jsp" />

    <div class="container py-4 mt-5">
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
              <h3 class="title">Đăng ký</h3>
              <form:form
                method="post"
                action="/register"
                modelAttribute="registerUser"
                class="form-horizontal"
              >
                <c:set var="errorPassword">
                  <form:errors path="password" cssClass="invalid-feedback" />
                </c:set>
                <c:set var="errorConfirmPassword">
                  <form:errors
                    path="confirmPassword"
                    cssClass="invalid-feedback"
                  />
                </c:set>
                <c:set var="errorEmail">
                  <form:errors path="email" cssClass="invalid-feedback" />
                </c:set>
                <c:set var="errorFullName">
                  <form:errors path="fullName" cssClass="invalid-feedback" />
                </c:set>
                <div class="form-group">
                  <label>Họ tên</label> <span class="text-danger">*</span>
                  <form:input
                    class="form-control mt-2 ${not empty errorFullName ? 'is-invalid' : ''}"
                    path="fullName"
                    placeholder="Nhập họ tên"
                  />
                  ${errorFullName}
                </div>

                <div class="row mb-3">
                  <div class="col-md-6">
                    <label>Email</label> <span class="text-danger">*</span>
                    <form:input
                      class="form-control mt-2 ${not empty errorEmail ? 'is-invalid' : ''}"
                      type="email"
                      path="email"
                      placeholder="Nhập email"
                    />
                    ${errorEmail}
                  </div>
                  <div class="col-md-6">
                    <label>Số điện thoại</label>
                    <form:input
                      class="form-control mt-2"
                      type="text"
                      path="phone"
                      placeholder="Nhập số điện thoại"
                    />
                  </div>
                </div>

                <div class="form-group">
                  <label>Mật khẩu</label> <span class="text-danger">*</span>
                  <form:input
                    class="form-control mt-2 ${not empty errorPassword ? 'is-invalid' : ''}"
                    type="password"
                    path="password"
                    placeholder="Mật khẩu"
                  />
                  ${errorPassword}
                </div>
                <div class="form-group">
                  <label>Xác nhận mật khẩu</label>
                  <span class="text-danger">*</span>
                  <form:input
                    class="form-control mt-2 ${not empty errorConfirmPassword ? 'is-invalid' : ''}"
                    type="password"
                    path="confirmPassword"
                    placeholder="Xác nhận mật khẩu"
                  />
                  ${errorConfirmPassword}
                </div>

                <c:if test="${not empty message}">
                  <div
                    class="alert alert-success alert-dismissible fade show mt-3"
                    role="alert"
                  >
                    ${message}
                    <button
                      type="button"
                      class="btn-close"
                      data-bs-dismiss="alert"
                      aria-label="Close"
                    ></button>
                  </div>
                </c:if>

                <c:if test="${not empty error}">
                  <div
                    class="alert alert-danger alert-dismissible fade show mt-3"
                    role="alert"
                  >
                    ${error}
                    <button
                      type="button"
                      class="btn-close"
                      data-bs-dismiss="alert"
                      aria-label="Close"
                    ></button>
                  </div>
                </c:if>

                <button
                  type="submit"
                  name="btn-signup"
                  class="btn btn-default mt-3"
                >
                  Đăng ký
                </button>
              </form:form>

              <p class="mt-4 text-center">
                Bạn đã có tài khoản?
                <a href="/login" class="text-primary fw-bold">Đăng nhập</a>
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
