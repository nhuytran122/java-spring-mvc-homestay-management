<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Sửa loại phòng</title>
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
                                <h4 class="card-title mb-4 text-center">Sửa loại phòng</h4>
                                <form:form class="form-horizontal" action="/admin/room-type/update" method="post"
                                    modelAttribute="roomType" enctype="multipart/form-data">
                                    <form:input type="hidden" path="roomTypeID" />
                                    <c:set var="errorName">
                                        <form:errors path="name" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorMaxGuest">
                                        <form:errors path="maxGuest" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorBasePrice">
                                        <form:errors path="roomPricings[0].basePrice" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorExtraHourPrice">
                                        <form:errors path="roomPricings[0].extraHourPrice" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorOvernightPrice">
                                        <form:errors path="roomPricings[0].overnightPrice" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorBaseDuration">
                                        <form:errors path="roomPricings[0].baseDuration" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorDailyPrice">
                                        <form:errors path="roomPricings[0].dailyPrice" cssClass="invalid-feedback" />
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
                                        <label class="control-label col-sm-2">Thời gian cơ bản (giờ) <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator ${not empty errorBaseDuration ? 'is-invalid' : ''}" 
                                            path="roomPricings[0].baseDuration" />
                                            ${errorBaseDuration}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Giá cơ bản <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator ${not empty errorBasePrice ? 'is-invalid' : ''}" 
                                            path="roomPricings[0].basePrice" />
                                            ${errorBasePrice}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Giá bù giờ mỗi giờ <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator ${not empty errorExtraHourPrice ? 'is-invalid' : ''}" 
                                            path="roomPricings[0].extraHourPrice" />
                                            ${errorExtraHourPrice}
                                        </div>
                                    </div>                                    

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Giá qua đêm <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator ${not empty errorOvernightPrice ? 'is-invalid' : ''}" 
                                            path="roomPricings[0].overnightPrice" />
                                            ${errorOvernightPrice}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Giá theo ngày <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator ${not empty errorDailyPrice ? 'is-invalid' : ''}" 
                                            path="roomPricings[0].dailyPrice" />
                                            ${errorDailyPrice}
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
                                        <form:hidden path="photo" id="oldImage" />
                                        <div class="col-sm-10">
                                            <input type="file" class="form-control" accept="image/*" 
                                                name="fileImg" id="fileInput">
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <div class="col-sm-10 image-preview-container">
                                            <c:if test="${not empty roomType.photo}">
                                                <img src="/images/room/${roomType.photo}" class="imagePreview" alt="Preview ảnh">
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <div class="col-sm-offset-2 col-sm-10 text-center">
                                            <a href="/admin/room-type" class="btn btn-secondary">Hủy</a>
                                            <button type="submit" class="btn btn-warning">Sửa</button>
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
    <jsp:include page="../layout/partial/_script-number-separator.jsp" />
    <jsp:include page="../layout/partial/_script-preview-image-update.jsp" />
    <script>
      setupImagePreview("room");
  </script>
</body>
</html>
