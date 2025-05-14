<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập nhật thông tin - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/booking-history-style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
</head>
<body>
    <jsp:include page="../layout/header.jsp" />
    <jsp:include page="../layout/partial/_welcome-banner-profile.jsp" />

    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card border-1 rounded-3">
                    <div class="card-header bg-primary text-white rounded-top-3 py-3">
                        <h3 class="card-title mb-0 fw-bold">Cập nhật thông tin tài khoản</h3>
                    </div>
                    <div class="card-body p-4">
                        <form:form class="row g-4" action="/profile/update" method="POST" enctype="multipart/form-data" modelAttribute="user">
                            

                            <c:set var="errorName">
                                <form:errors path="fullName" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorPhone">
                                <form:errors path="phone" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorAddress">
                                <form:errors path="address" cssClass="invalid-feedback" />
                            </c:set>

                            <div class="col-md-12 text-center mb-3">
                                <label class="form-label fw-bold text-dark">Ảnh đại diện</label>
                                <form:input type="hidden" path="avatar" id="oldImage"/>
                                <div class="mb-3">
                                    <img src="/images/avatar/${user.avatar != null && !user.avatar.isEmpty() ? user.avatar : 'default-img.jpg'}"
                                         alt="Avatar" class="rounded-circle border border-primary border-2 image-preview" 
                                         style="width: 120px; height: 120px; object-fit: cover;" id="avatarPreview">
                                    
                                </div>
                                <input type="file" name="fileImg" id="fileInput" class="form-control form-control-lg w-50 mx-auto rounded-3" 
                                       accept="image/*">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold text-dark">Họ và tên</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa fa-user"></i></span>
                                    <form:input type="text" path="fullName" class="form-control form-control-lg rounded-end-3
                                        ${not empty errorName ? 'is-invalid' : ''}"  />
                                        ${errorName}
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold text-dark">Số điện thoại</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa fa-phone"></i></span>
                                    <form:input type="text" path="phone" class="form-control form-control-lg rounded-end-3
                                    ${not empty errorPhone ? 'is-invalid' : ''}" />
                                    ${errorPhone}
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold text-dark">Email</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa fa-envelope"></i></span>
                                    <form:input type="email" path="email" class="form-control form-control-lg rounded-end-3 bg-light" readonly="true" />
                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label fw-bold text-dark">Địa chỉ</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light"><i class="fa fa-map-marker-alt"></i></span>
                                    <form:input type="text" path="address" class="form-control form-control-lg rounded-end-3
                                        ${not empty errorAddress ? 'is-invalid' : ''}" />
                                        ${errorAddress}
                                </div>
                            </div>

                            <div class="col-12 text-center mt-4">
                                <button type="submit" class="btn btn-primary btn-lg rounded-3 me-2">
                                    <i class="fa fa-save"></i> Lưu thay đổi
                                </button>
                                <a href="/profile" class="btn btn-outline-secondary btn-lg rounded-3">
                                    <i class="fa fa-arrow-left"></i> Quay lại
                                </a>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <script>
        $(document).ready(function () {
            var $fileInput = $('#fileInput');
            var $avatarPreview = $('#avatarPreview');

            $fileInput.on('change', function (e) {
                if (e.target.files && e.target.files.length > 0) {
                    var imgURL = URL.createObjectURL(e.target.files[0]);
                    $avatarPreview.attr('src', imgURL);
                }
            });
        });
    </script>
</body>
</html>