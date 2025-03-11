<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Thêm phòng</title>
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
                                <h4 class="card-title mb-4 text-center">Thêm mới phòng</h4>
                                <form:form class="form-horizontal" action="/admin/room/create" method="post"
                                    modelAttribute="newRoom" enctype="multipart/form-data">
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
                                    
                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Thumbnail</label>
                                        <div class="col-sm-10">
                                            <input type="file" class="form-control" accept="image/*" 
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
                                        <a href="/admin/room" class="btn btn-secondary">Hủy</a>
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
</body>
</html>