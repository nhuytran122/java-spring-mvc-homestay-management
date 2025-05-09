<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Thêm loại phòng</title>
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
                                <h4 class="card-title mb-4 text-center">Thêm mới loại phòng</h4>
                                <form:form class="form-horizontal" action="/admin/room-type/create" method="post"
                                    modelAttribute="newRoomType" enctype="multipart/form-data">
                                    <c:set var="errorName">
                                        <form:errors path="name" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorMaxGuest">
                                        <form:errors path="maxGuest" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorPricePerHour">
                                        <form:errors path="pricePerHour" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorExtraPrice">
                                        <form:errors path="extraPricePerHour" cssClass="invalid-feedback" />
                                    </c:set>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Tên loại phòng <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" 
                                            path="name" /> 
                                            ${errorName}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Số lượng khách tối đa <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" step="1" class="form-control number-separator ${not empty errorMaxGuest ? 'is-invalid' : ''}" 
                                            path="maxGuest" />
                                            ${errorMaxGuest}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Giá mỗi giờ <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator
                                            ${not empty errorPricePerHour ? 'is-invalid' : ''}" 
                                            path="pricePerHour" />
                                            ${errorPricePerHour}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Giá bù giờ mỗi giờ <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator
                                            ${not empty errorExtraPrice ? 'is-invalid' : ''}" 
                                            path="extraPricePerHour" />
                                            ${errorExtraPrice}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Mô tả</label>
                                        <div class="col-sm-10">
                                            <form:textarea type="text" class="form-control" 
                                            path="description" />
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Hình ảnh</label>
                                        <div class="col-sm-10">
                                            <input type="file" class="form-control"  accept="image/*" 
                                                name="fileImg" id="fileInput">
                                            <form:errors path="photo" cssClass="text-danger mt-2" />
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <div class="col-sm-10">
                                            <img class="imagePreview" alt="Preview ảnh" />
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <div class="col-sm-offset-2 col-sm-10 text-center">
                                            <a href="/admin/room-type" class="btn btn-secondary">Hủy</a>
                                            <button type="submit" class="btn btn-primary">Tạo</button>
                                        </div>
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
    <jsp:include page="../layout/partial/_script-preview-image.jsp" />
    <jsp:include page="../layout/partial/_script-number-separator.jsp" />
</body>
</html>
