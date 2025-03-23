<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đổi mật khẩu - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/booking-history-style.css">
</head>
<body>
    <jsp:include page="../layout/header.jsp" />
    <div class="container my-5 pt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card border-1 rounded-3">
                    <div class="card-header bg-primary text-white rounded-top-3 py-3">
                        <h3 class="card-title mb-0 fw-bold">Đổi mật khẩu</h3>
                    </div>
                    <div class="card-body p-4">
                        <form:form class="row g-3" action="/change-password" method="POST" modelAttribute="passwordForm">
                            <c:set var="errorOldPassword">
                                <form:errors path="oldPassword" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorNewPassword">
                                <form:errors path="newPassword" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorConfirmPassword">
                                <form:errors path="confirmPassword" cssClass="invalid-feedback" />
                            </c:set>
                        
                            <div class="col-12">
                                <label class="form-label fw-bold text-dark">Mật khẩu cũ</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa fa-lock"></i></span>
                                    <form:password path="oldPassword" class="form-control form-control-lg rounded-end-3
                                        ${not empty errorOldPassword ? 'is-invalid' : ''}" />
                                        ${errorOldPassword}
                                </div>
                            </div>

                            <div class="col-12">
                                <label class="form-label fw-bold text-dark">Mật khẩu mới</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa fa-lock"></i></span>
                                    <form:password path="newPassword" class="form-control form-control-lg rounded-end-3 
                                        ${not empty errorNewPassword ? 'is-invalid' : ''}" 
                                        value="${passwordForm.newPassword}"/>
                                        ${errorNewPassword}
                                </div>
                            </div>

                            <div class="col-12">
                                <label class="form-label fw-bold text-dark">Xác nhận mật khẩu mới</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa fa-lock"></i></span>
                                    <form:password path="confirmPassword" class="form-control form-control-lg rounded-end-3
                                        ${not empty errorConfirmPassword ? 'is-invalid' : ''}" 
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
                                <div class="col-12 text-center mt-4">
                                    <button type="submit" class="btn btn-primary btn-lg rounded-3 me-2">
                                        <i class="fa fa-save"></i> Đổi mật khẩu
                                    </button>
                                    <a href="/profile" class="btn btn-outline-secondary btn-lg rounded-3">
                                        <i class="fa fa-arrow-left"></i> Hủy
                                    </a>
                                </div>
                            </c:if>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</body>
</html>