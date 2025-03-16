<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Thêm dịch vụ</title>
  <jsp:include page="../layout/import-css.jsp" />
</head>
<style>
    .icon-option:hover {
        background-color: #e9ecef;
        border-color: #007bff;
    }

    .icon-option.selected {
        border-color: #007bff;
        background-color: #e9ecef;
    }
</style>
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
                                <h4 class="card-title mb-4 text-center">Thêm mới dịch vụ</h4>
                                <form:form class="form-horizontal" action="/admin/service/update" method="post"
                                    modelAttribute="service">
                                    
                                    <form:hidden path="serviceID" />
                                    <c:set var="errorName">
                                        <form:errors path="serviceName" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorUnit">
                                        <form:errors path="unit" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorPrice">
                                        <form:errors path="price" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorIcon">
                                        <form:errors path="icon" cssClass="invalid-feedback" />
                                    </c:set>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Tên dịch vụ <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" 
                                            path="serviceName" /> 
                                            ${errorName}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Giá <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control number-separator
                                            ${not empty errorPrice ? 'is-invalid' : ''}" 
                                            path="price" />
                                            ${errorPrice}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Đơn vị tính <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input type="text" class="form-control ${not empty errorUnit ? 'is-invalid' : ''}" 
                                            path="unit" />
                                            ${errorUnit}
                                        </div>
                                    </div>

                                    <div class="form-group row align-items-center">
                                        <label class="col-sm-2 col-form-label">Thanh toán trước</label>
                                        <div class="col-sm-10">
                                            <div class="form-check form-switch">
                                                <form:checkbox path="isPrepaid" class="form-check-input" style="transform: scale(1.3);"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Icon <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:hidden path="icon" id="selectedIcon" class="${not empty errorIcon ? 'is-invalid' : ''}" />
                                            <button type="button" class="btn btn-outline-primary" id="openIconModal" data-bs-toggle="modal" data-bs-target="#iconModal" style="display: flex; align-items: center;">
                                                <span class="iconify" data-icon="${not empty category.icon ? category.icon : 'mdi:help-circle'}" data-width="20" data-height="20"></span>
                                                Chọn icon
                                            </button>
                                            ${errorIcon}
                                        </div>
                                    </div>
                                    
                                    <%@ include file="../layout/partial/_modal-show-list-icons.jsp" %>

                                    <div class="form-group row">
                                        <label class="control-label col-sm-2">Mô tả</label>
                                        <div class="col-sm-10">
                                            <form:textarea class="form-control" 
                                            path="description" />
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                    <div class="col-sm-offset-2 col-sm-10 text-center">
                                        <a href="/admin/service" class="btn btn-secondary">Hủy</a>
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
  <script src="https://code.iconify.design/3/3.1.0/iconify.min.js"></script>
  <jsp:include page="../layout/partial/_script-handle-with-icon.jsp" />
</body>
</html>
