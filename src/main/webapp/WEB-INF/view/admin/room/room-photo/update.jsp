<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Sửa ảnh phòng</title>
  <jsp:include page="../../layout/import-css.jsp" />
</head>
<body>
  <div class="container-scroller">
    <jsp:include page="../../layout/header.jsp" />
    
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../../layout/theme-settings.jsp" />
        <jsp:include page="../../layout/sidebar.jsp" />
      
        <div class="main-panel">
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-md-12 grid-margin stretch-card">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title mb-4 text-center">Sửa ảnh phòng</h4>
                                <form:form class="form-horizontal" action="/admin/room/photo/update" method="post"
                                    modelAttribute="roomPhoto" enctype="multipart/form-data">
                                    <input type="hidden" name="roomID" value="${roomID}">
                                    <form:input type="hidden" path="photoID" />
                                    <form:input type="hidden" path="room" />
                                    
                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Hình ảnh</label>
                                        <form:hidden path="photo" id="oldImage" />
                                        <div class="col-sm-10">
                                            <input type="file" class="form-control"  
                                                accept="image/*" name="fileImg" id="fileInput">
                                        </div>
                                    </div>

                                    <div class="form-group row align-items-center">
                                        <label class="col-sm-2 col-form-label">Ẩn ảnh</label>
                                        <div class="col-sm-10">
                                            <div class="form-check form-switch">
                                                <form:checkbox path="hidden" class="form-check-input" style="transform: scale(1.3);"/>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <div class="col-sm-10">
                                            <img src="/images/room/${roomPhoto.photo}" class="imagePreview" alt="Preview ảnh">
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <div class="col-sm-offset-2 col-sm-10 text-center">
                                            <a href="/admin/room/update/${roomPhoto.room.roomID}" class="btn btn-secondary">Hủy</a>
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

  <jsp:include page="../../layout/import-js.jsp" />
  <jsp:include page="../../layout/partial/_script-preview-image-update.jsp" />
  <script>
    setupImagePreview("room", "default-img.jpg");
</script>
</body>
</html>
