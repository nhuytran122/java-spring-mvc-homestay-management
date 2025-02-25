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
                                    <c:set var="errorRoomTypeID">
                                        <form:errors path="roomType" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorBranchID">
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
                                            <form:select class="form-select form-control ${not empty errorBranchID ? 'is-invalid' : ''}" path="branch.branchID">
                                                <form:option value="0">Chọn chi nhánh</form:option>
                                                <c:forEach var="branch" items="${listBranches}">
                                                    <form:option value="${branch.branchID}">${branch.branchName}</form:option>
                                                </c:forEach>
                                            </form:select>
                                            ${errorBranchID}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Loại phòng <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:select class="form-select form-control ${not empty errorRoomTypeID ? 'is-invalid' : ''}" path="roomType.roomTypeID">
                                                <form:option value="0">Chọn loại phòng</form:option>
                                                <c:forEach var="roomType" items="${listRoomTypes}">
                                                    <form:option value="${roomType.roomTypeID}">${roomType.name}</form:option>
                                                </c:forEach>
                                            </form:select>
                                            ${errorRoomTypeID}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Diện tích</label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control" 
                                            path="area" />
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
                                        <label class="control-label col-sm-2">Thumbnail</label>
                                        <form:hidden path="thumbnail" id="oldImage" />
                                        <div class="col-sm-10">
                                            <input type="file" class="form-control" accept="image/*" 
                                                name="fileImg" id="fileInput">
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <div class="col-sm-10">
                                            <c:choose>
                                                <c:when test="${not empty room.thumbnail}">
                                                    <img src="/images/room/${room.thumbnail}" class="imagePreview" alt="Preview ảnh">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="/images/room/default-img.jpg" class="imagePreview" alt="Preview ảnh">
                                                </c:otherwise>
                                            </c:choose>
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

                <div class="row">
                    <div class="col-md-12 grid-margin stretch-card">
                        <div class="card position-relative">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h4 class="card-title">Danh sách ảnh</h4>
                                    <form action="/admin/room/photo/create" method="get">
                                        <input type="hidden" name="roomID" value="${room.roomID}">
                                        <button type="submit" class="btn btn-primary btn-sm">
                                            <i class="bi bi-plus-circle"></i> Thêm mới ảnh
                                        </button>
                                    </form>
                                    
                                </div>
    
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th style="width: 150px;">Ảnh</th>
                                                <th>Ẩn ảnh</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty listPhotos}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Hiện không có ảnh nào</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="photo" items="${listPhotos}">
                                                        <tr>
                                                            <td>
                                                                <img src="/images/room/${photo.photo}" class="img-fluid rounded" style="width: auto; height: 100px; object-fit: cover;">
                                                            </td>
                                                            <td>
                                                                <span class="badge ${photo.hidden ? 'bg-danger' : 'bg-success'}">
                                                                    ${photo.hidden ? 'Ẩn' : 'Hiển thị'}
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/room/photo/update/${photo.photoID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>
                                                                    <button class="btn btn-danger btn-sm" data-photo-id="${photo.photoID}"
                                                                        onclick="checkBeforeDelete(this)">
                                                                        <i class="bi bi-trash"></i>
                                                                    </button>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 
        </div>
    </div>   
  </div>

  <jsp:include page="../layout/import-js.jsp" />
  <jsp:include page="room-photo/_modal-delete-photo.jsp" />
  <jsp:include page="../layout/partial/_script-preview-image-update.jsp" />
  <script>
        setupImagePreview("room", "default-img.jpg");
  </script>
</body>
</html>
