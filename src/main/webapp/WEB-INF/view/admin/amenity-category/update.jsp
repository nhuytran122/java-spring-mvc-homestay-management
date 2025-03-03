<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Sửa phân loại</title>
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
                                    <h4 class="card-title mb-4 text-center">Sửa phân loại</h4>
                                    <form:form class="form-horizontal" action="/admin/amenity-category/update" method="post" modelAttribute="category">
                                        <form:hidden path="categoryID" />

                                        <c:set var="errorName">
                                            <form:errors path="categoryName" cssClass="invalid-feedback" />
                                        </c:set>
                                        <c:set var="errorIcon">
                                            <form:errors path="icon" cssClass="invalid-feedback" />
                                        </c:set>

                                        <div class="form-group row">
                                            <label class="control-label col-sm-2">Tên phân loại <span class="text-danger">*</span></label>
                                            <div class="col-sm-10">
                                                <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" path="categoryName" />
                                                ${errorName}
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
                                        
                                        <%@ include file="_modal-show-list-icons.jsp" %>

                                        <div class="form-group row">
                                            <label class="control-label col-sm-2">Mô tả</label>
                                            <div class="col-sm-10">
                                                <form:textarea type="text" class="form-control" path="description" />
                                            </div>
                                        </div>

                                        <div class="form-group row">
                                            <div class="col-sm-offset-2 col-sm-10 text-center">
                                                <a href="/admin/amenity-category" class="btn btn-secondary">Hủy</a>
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
    <script src="https://code.iconify.design/3/3.1.0/iconify.min.js"></script>
    <jsp:include page="_script-handle-with-icon.jsp" />
</body>
</html>