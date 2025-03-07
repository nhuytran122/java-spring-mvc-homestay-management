<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Thêm yêu cầu bảo trì</title>
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
                                <h4 class="card-title mb-4 text-center">Thêm mới yêu cầu bảo trì</h4>
                                <form:form class="form-horizontal" action="/admin/maintenance/create" method="post"
                                    modelAttribute="newRequest" enctype="multipart/form-data">
                                    <c:set var="errorDescription">
                                        <form:errors path="description" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorBranch">
                                        <form:errors path="branch" cssClass="invalid-feedback" />
                                    </c:set>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Chi nhánh <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:select class="form-select form-control ${not empty errorBranch ? 'is-invalid' : ''}" 
                                                path="branch" id="branchSelect">
                                                <form:option value="">Chọn chi nhánh</form:option>
                                                <form:options items="${listBranches}" itemValue="branchID" itemLabel="branchName"/>
                                            </form:select>
                                            ${errorBranch}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Phòng</label>
                                        <div class="col-sm-10">
                                            <form:select class="form-select form-control" 
                                                path="room" id="roomSelect">
                                                <form:option value="">Chọn phòng</form:option>
                                                <form:options items="${listRooms}" itemValue="roomID" itemLabel="roomNumber"/>
                                            </form:select>
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Mô tả <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:textarea  class="form-control ${not empty errorDescription ? 'is-invalid' : ''}" 
                                            path="description" /> 
                                            ${errorDescription}
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
                                        <a href="/admin/maintenance" class="btn btn-secondary">Hủy</a>
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
<script>
    $(document).ready(function() {
        $('#branchSelect').change(function() {
            var branchID = $(this).val();
            var roomSelect = $('#roomSelect');
            if (!branchID) {
                roomSelect.empty().append('<option value="">Chọn phòng</option>');
                return;
            }
            roomSelect.html('<option value="">Đang tải...</option>');

            $.ajax({
                url: '/admin/maintenance/rooms-by-branch',
                type: 'GET',
                data: { branchID: branchID },
                dataType: 'json', 
                success: function(rooms) {
                    if (Array.isArray(rooms) && rooms.length > 0) {
                        roomSelect.empty();
                        roomSelect.append('<option value="">Chọn phòng</option>');
                        $.each(rooms, function(index, room) {
                            if (room && room.roomID && room.roomNumber) {
                                roomSelect.append('<option value="' + room.roomID + '">' + room.roomNumber + '</option>');
                            }
                        });
                    } else {
                        roomSelect.empty().append('<option value="">Không có phòng</option>');
                    }
                    // console.log("Rooms JSON:", rooms); // Bật log để debug
                },
                error: function(xhr, status, error) {
                    console.error("Error:", error, xhr.responseText);
                    roomSelect.empty().append('<option value="">Lỗi tải phòng</option>');
                    alert('Lỗi server: ' + error + '. Vui lòng kiểm tra log hoặc liên hệ admin!');
                },
            });
        });
    });
</script>
</body>
</html>
