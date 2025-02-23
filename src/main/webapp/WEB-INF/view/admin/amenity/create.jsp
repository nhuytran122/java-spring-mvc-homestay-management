<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Thêm tiện nghi</title>
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
                        <h4 class="card-title mb-4 text-center">Thêm mới tiện nghi</h4>
                        <form:form class="form-horizontal" action="/admin/amenity/create" method="post"
                            modelAttribute="newAmenity">
                            <c:set var="errorName">
                                <form:errors path="amenityName" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorCategoryID">
                                <form:errors path="amenityCategory" cssClass="invalid-feedback" />
                            </c:set>

                            <div class="form-group row">
                                <label class="control-label col-sm-2">Tên tiện nghi<span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" 
                                    path="amenityName" /> 
                                    ${errorName}
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="control-label col-sm-2">Phân loại <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <form:select class="form-select form-control ${not empty errorCategoryID ? 'is-invalid' : ''}" path="amenityCategory">
                                        <form:option value="">Chọn phân loại</form:option>
                                        <c:forEach var="category" items="${listCategories}">
                                            <form:option value="${category.categoryID}">${category.categoryName}</form:option>
                                        </c:forEach>
                                    </form:select>
                                    ${errorCategoryID}
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-sm-offset-2 col-sm-10 text-center">
                                <a href="/admin/amenity" class="btn btn-secondary">Hủy</a>
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
</body>
</html>
