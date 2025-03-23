<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Đổi mật khẩu - Lullaby Homestay</title>
  <jsp:include page="../layout/import-css.jsp" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
</head>

<body>
  <div class="container-scroller">
    <jsp:include page="../layout/header.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
      
      <div class="main-panel">
        <div class="content-wrapper">
          <div class="row justify-content-center">
            <div class="col-md-6 grid-margin stretch-card">
              <div class="card">
                <div class="card-body">
                  <h4 class="card-title mb-4 text-center">Đổi mật khẩu</h4>

                  <form:form action="/admin/change-password" method="post" modelAttribute="passwordForm">
                    <c:set var="errorOldPassword">
                        <form:errors path="oldPassword" cssClass="invalid-feedback" />
                    </c:set>
                    <c:set var="errorNewPassword">
                        <form:errors path="newPassword" cssClass="invalid-feedback" />
                    </c:set>
                    <c:set var="errorConfirmPassword">
                        <form:errors path="confirmPassword" cssClass="invalid-feedback" />
                    </c:set>

                    <div class="mb-3">
                      <label class="form-label fw-bold">Mật khẩu hiện tại</label>
                      <div class="input-group">
                        <span class="input-group-text bg-light"><i class="fa fa-lock"></i></span>
                        <form:password path="oldPassword" class="form-control ${not empty errorOldPassword ? 'is-invalid' : ''}" />
                        ${errorOldPassword}
                      </div>
                    </div>

                    <div class="mb-3">
                      <label class="form-label fw-bold">Mật khẩu mới</label>
                      <div class="input-group">
                        <span class="input-group-text bg-light"><i class="fa fa-lock"></i></span>
                        <form:password path="newPassword" class="form-control ${not empty errorNewPassword ? 'is-invalid' : ''}" 
                        value="${passwordForm.newPassword}" />
                        ${errorNewPassword}
                      </div>
                    </div>

                    <div class="mb-3">
                      <label class="form-label fw-bold">Xác nhận mật khẩu mới</label>
                      <div class="input-group">
                        <span class="input-group-text bg-light"><i class="fa fa-lock"></i></span>
                        <form:password path="confirmPassword" class="form-control ${not empty errorConfirmPassword ? 'is-invalid' : ''}"
                        value="${passwordForm.confirmPassword}" />
                        ${errorConfirmPassword}
                      </div>
                    </div>

                    <c:if test="${not empty message}">
                      <div class="alert alert-success alert-dismissible fade show" role="alert">
                          ${message}
                          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                      </div>
                      
                      <c:if test="${not empty redirect}">
                          <script>
                              setTimeout(function() {
                                  window.location.href = "${redirect}";
                              }, 3000); 
                          </script>
                      </c:if>
                    </c:if>
                    <c:if test="${empty message}">
                      <div class="text-center mt-4">
                        <a href="/admin/profile" class="btn btn-secondary me-2">Hủy</a>
                        <button type="submit" class="btn btn-warning">Đổi mật khẩu</button>
                      </div>
                    </c:if>
                  </form:form>
                </div>
              </div>
            </div>
          </div>
        </div>
        
      </div>
    </div>   
  </div>

  <jsp:include page="../layout/import-js.jsp" />
</body>
</html>
