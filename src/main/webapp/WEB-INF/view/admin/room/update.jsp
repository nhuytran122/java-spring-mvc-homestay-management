<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Sửa phòng</title>
  <jsp:include page="../layout/import-css.jsp" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
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
                                <h4 class="card-title mb-4 text-center">Sửa phòng</h4>
                                <form:form class="form-horizontal" action="/admin/room/update" method="post"
                                    modelAttribute="room" enctype="multipart/form-data">
                                    <form:input type="hidden" path="roomID" />
                                    <c:set var="errorRoomNumber">
                                        <form:errors path="roomNumber" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorRoomType">
                                        <form:errors path="roomType" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorBranch">
                                        <form:errors path="branch" cssClass="invalid-feedback" />
                                    </c:set>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Số phòng <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control ${not empty errorRoomNumber ? 'is-invalid' : ''}" 
                                            path="roomNumber" /> 
                                            ${errorRoomNumber}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Chi nhánh <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:select class="form-select form-control ${not empty errorBranch ? 'is-invalid' : ''}" path="branch">
                                                <form:option value="">Chọn chi nhánh</form:option>
                                                <form:options items="${listBranches}" itemValue="branchID" itemLabel="branchName"/>
                                            </form:select>
                                            ${errorBranch}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Loại phòng <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:select class="form-select form-control ${not empty errorRoomType ? 'is-invalid' : ''}" path="roomType">
                                                <form:option value="">Chọn loại phòng</form:option>
                                                <form:options items="${listRoomTypes}" itemValue="roomTypeID" itemLabel="name"/>
                                            </form:select>
                                            ${errorRoomType}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Diện tích</label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control" 
                                            path="area" />
                                        </div>
                                    </div>

                                    <div class="form-group row align-items-center">
                                        <label class="col-sm-2 col-form-label">Đang hoạt động</label>
                                        <div class="col-sm-10">
                                            <div class="form-check form-switch">
                                                <form:checkbox path="isActive" class="form-check-input" style="transform: scale(1.3);"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Thumbnail</label>
                                        <form:hidden path="thumbnail" id="oldImage" />
                                        <div class="col-sm-10">
                                            <input type="file" class="form-control" accept="image/*" 
                                                name="fileImg" id="fileInput">
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <div class="col-sm-10 image-preview-container">
                                            <c:if test="${not empty room.thumbnail}">
                                                <img src="/images/room/${room.thumbnail}" class="imagePreview" alt="Preview ảnh">
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                    <div class="col-sm-offset-2 col-sm-10 text-center">
                                        <a href="/admin/room" class="btn btn-secondary">Hủy</a>
                                        <button type="submit" class="btn btn-warning">Sửa</button>
                                    </div>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
                <%@ include file="room-photo/_show.jsp" %>
                <%@ include file="room-amenity/_show.jsp" %>
            </div> 
        </div>
    </div>   
  </div>

  <%@ include file="room-amenity/_modal-add.jsp" %>

  <jsp:include page="../layout/import-js.jsp" />
  <jsp:include page="../layout/partial/_modal-delete-not-check-can-delete.jsp" />
  <jsp:include page="../layout/partial/_script-preview-image-update.jsp" />

  <jsp:include page="room-amenity/_script-handle.jsp" />
  <jsp:include page="room-amenity/_modal-error.jsp" />
  <jsp:include page="room-amenity/_modal-update.jsp" />
  <jsp:include page="room-amenity/_modal-delete.jsp" />
  <jsp:include page="room-amenity/_modal-empty-amenities.jsp" />
  <script>
        setupImagePreview("room");
  </script>
</body>
</html>
