<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 
<!DOCTYPE html>
<html lang="vi">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Cập nhật thông tin tài khoản - Lullaby Homestay</title>
  <jsp:include page="../layout/import-css.jsp" />
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
            <div class="col-md-8 grid-margin stretch-card">
              <div class="card">
                <div class="card-body">
                  <h4 class="card-title mb-4 text-center">Chỉnh sửa thông tin</h4>
                  
                  <form:form action="/admin/profile/update" method="post" modelAttribute="user" enctype="multipart/form-data">                   
                    <form:input type="hidden" path="userID" />
                    <form:input type="hidden" path="avatar" id="oldImage"/>
                    <c:set var="errorName">
                      <form:errors path="fullName" cssClass="invalid-feedback" />
                    </c:set>
                    <c:set var="errorPhone">
                        <form:errors path="phone" cssClass="invalid-feedback" />
                    </c:set>
                    <c:set var="errorAddress">
                        <form:errors path="address" cssClass="invalid-feedback" />
                    </c:set>

                    <div class="text-center mb-3">
                      <img id="avatarPreview" 
                           src="/images/avatar/${user.avatar != null && !user.avatar.isEmpty() ? user.avatar : 'default-img.jpg'}"
                           alt="Avatar"
                           class="rounded-circle border border-primary border-3"
                           style="width: 120px; height: 120px; object-fit: cover;">
                    </div>
                    <div class="mb-3">
                      <label class="form-label">Chọn ảnh đại diện</label>
                      <input type="file" class="form-control" name="fileImg" accept="image/*" id="fileInput">
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Họ tên <span class="text-danger">*</span></label>
                      <form:input path="fullName" class="form-control  ${not empty errorName ? 'is-invalid' : ''}" />
                      ${errorName}
                    </div>
                    
                    <div class="mb-3">
                      <label class="form-label">Email</label>
                      <form:input type="hidden" path="email"/>
                      <input type="email" class="form-control" value="${user.email}" readonly="true">
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Số điện thoại</label>
                      <form:input path="phone" class="form-control ${not empty errorPhone ? 'is-invalid' : ''}" />
                      ${errorPhone}
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Địa chỉ <span class="text-danger">*</span></label>
                      <form:input path="address" class="form-control ${not empty errorAddress ? 'is-invalid' : ''}" />
                      ${errorAddress}
                    </div>
                    
                    <div class="text-center">
                      <a href="/admin/profile" class="btn btn-secondary me-2">Hủy</a>
                      <button type="submit" class="btn btn-primary">Lưu</button>
                    </div>
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
