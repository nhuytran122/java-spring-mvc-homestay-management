<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Thêm nhân viên</title>
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
                <div class="row">
                    <div class="col-md-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title mb-4 text-center">Thêm mới nhân viên</h4>
                                <form:form class="form-horizontal" action="/admin/employee/create" method="post"
                                    modelAttribute="newEmployee" enctype="multipart/form-data">
                                    <c:set var="errorName">
                                        <form:errors path="fullName" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorPhone">
                                        <form:errors path="phone" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorEmail">
                                        <form:errors path="email" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorAddress">
                                        <form:errors path="address" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorSalary">
                                        <form:errors path="salary" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorRole">
                                        <form:errors path="role" cssClass="invalid-feedback" />
                                    </c:set>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Tên nhân viên <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" 
                                            path="fullName" /> 
                                            ${errorName}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Số điện thoại <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control ${not empty errorPhone ? 'is-invalid' : ''}"
                                            path="phone" />
                                            ${errorPhone}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Email <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="email" class="form-control ${not empty errorEmail ? 'is-invalid' : ''}"
                                            path="email" />
                                            ${errorEmail}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Địa chỉ <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control ${not empty errorAddress ? 'is-invalid' : ''}" 
                                            path="address" />
                                            ${errorAddress}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Vai trò <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:select class="form-select form-control ${not empty errorRole ? 'is-invalid' : ''}" path="role">
                                                <form:option value="">Chọn vai trò</form:option>
                                                <form:options items="${listRoles}" itemValue="roleId" itemLabel="description"/>
                                            </form:select>
                                            ${errorRole}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Mức lương <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator ${not empty errorSalary ? 'is-invalid' : ''}" 
                                            path="salary" />
                                            ${errorSalary}
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Hình ảnh</label>
                                        <div class="col-sm-10">
                                            <input type="file" class="form-control"  accept="image/*" 
                                                name="fileImg" id="fileInput">
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <div class="col-sm-10">
                                            <img class="imagePreview" alt="Preview ảnh" />
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <div class="col-sm-offset-2 col-sm-10 text-center">
                                            <a href="/admin/employee" class="btn btn-secondary">Hủy</a>
                                            <button type="submit" class="btn btn-primary">Tạo</button>
                                        </div>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 
            <jsp:include page="../layout/footer.jsp" />
        </div>
    </div>   
  </div>

  <jsp:include page="../layout/import-js.jsp" />
  <jsp:include page="../layout/partial/_script-preview-image.jsp" />
  <jsp:include page="../layout/partial/_script-number-separator.jsp" />
</body>
</html>
