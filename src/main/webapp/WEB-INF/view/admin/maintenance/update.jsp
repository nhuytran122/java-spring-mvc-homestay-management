<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Sửa yêu cầu bảo trì</title>
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
                                <h4 class="card-title mb-4 text-center">Sửa yêu cầu bảo trì</h4>
                                <form:form class="form-horizontal" action="/admin/maintenance/update" method="post"
                                    modelAttribute="request" enctype="multipart/form-data">
                                    <form:input type="hidden" path="requestID" />
                                    <form:input type="hidden" path="status" />
                                    <c:set var="errorDescription">
                                        <form:errors path="description" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorBranchID">
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
                                                path="room" id="roomSelect" data-selected="${request.room != null ? request.room.roomID : ''}">
                                                <form:option value="">Chọn phòng</form:option>
                                                <form:options items="${listRooms}" itemValue="roomID" itemLabel="roomNumber"/>
                                            </form:select>
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Mô tả <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:textarea class="form-control ${not empty errorDescription ? 'is-invalid' : ''}" 
                                                path="description" /> 
                                            ${errorDescription}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Hình ảnh</label>
                                        <form:hidden path="image" id="oldImage" />
                                        <div class="col-sm-10">
                                            <input type="file" class="form-control" accept="image/*" 
                                                name="fileImg" id="fileInput">
                                        </div>
                                    </div>
                                    
                                    <div class="form-group row">
                                        <div class="col-sm-10 image-preview-container">
                                            <c:if test="${not empty request.image}">
                                                <img src="/images/maintenance/${request.image}" class="imagePreview" alt="Preview ảnh">
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <div class="col-sm-offset-2 col-sm-10 text-center">
                                            <a href="/admin/maintenance" class="btn btn-secondary">Hủy</a>
                                            <button type="submit" class="btn btn-warning">Sửa</button>
                                        </div>
                                    </div>
                                    <div class="data-check" style="display: none;">
                                        <input type="hidden" data-can-update="${canUpdate}">
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
  <jsp:include page="_script-maintenance-room-select.jsp" />
  <jsp:include page="../layout/partial/_script-preview-image-update.jsp" />
  <script>
      setupImagePreview("maintenance");
  </script>
  <jsp:include page="_script-modal-warning-update.jsp" />
  <script>
    $(document).ready(function () {
    let canUpdate = $('.data-check input[type="hidden"]').data('can-update');
    let currentStatus = $('input[name="status"]').val();

    if (canUpdate === false) {
        showWarningModal(getStatusWarningMessage(currentStatus));
        $("#warningModal").on("hidden.bs.modal", function () {
        window.location.href = '/admin/maintenance';
        });
    }
    });
</script>
</body>
</html>