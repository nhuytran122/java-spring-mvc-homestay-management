<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Sửa đồ dùng</title>
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
                        <h4 class="card-title mb-4 text-center">Sửa đồ dùng</h4>
                        <form:form class="form-horizontal" action="/admin/inventory-item/update" method="post"
                            modelAttribute="item">
                            
                            <form:hidden path="itemID" />
                            <c:set var="errorName">
                                <form:errors path="itemName" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorCategoryID">
                                <form:errors path="inventoryCategory" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorPrice">
                                <form:errors path="price" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorUnit">
                                <form:errors path="unit" cssClass="invalid-feedback" />
                            </c:set>
                            <c:set var="errorMinQuantity">
                                <form:errors path="minQuantity" cssClass="invalid-feedback" />
                            </c:set>
                            <div class="form-group row">
                                <label class="control-label col-sm-2">Tên đồ dùng <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <form:input type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}" 
                                    path="itemName" /> 
                                    ${errorName}
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="control-label col-sm-2">Phân loại <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <form:select class="form-select form-control ${not empty errorCategoryID ? 'is-invalid' : ''}" 
                                    path="inventoryCategory.categoryID">
                                        <form:option value="">Chọn phân loại</form:option>
                                        <c:forEach var="category" items="${listCategories}">
                                            <form:option value="${category.categoryID}">${category.categoryName}</form:option>
                                        </c:forEach>
                                    </form:select>
                                    ${errorCategoryID}
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

                            <div class="form-group row">
                                <label class="control-label col-sm-2">Giá tiền <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <form:input type="text" class="form-control number-separator 
                                    ${not empty errorPrice ? 'is-invalid' : ''}" 
                                    path="price" /> 
                                    ${errorPrice}
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="control-label col-sm-2">Số lượng tối thiểu <span class="text-danger">*</span></label>
                                <div class="col-sm-10">
                                    <form:input type="text" class="form-control number-separator 
                                    ${not empty errorMinQuantity ? 'is-invalid' : ''}" 
                                    path="minQuantity" /> 
                                    ${errorMinQuantity}
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-sm-offset-2 col-sm-10 text-center">
                                <a href="/admin/inventory-item" class="btn btn-secondary">Hủy</a>
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
  <jsp:include page="../layout/partial/_script-number-separator.jsp" />
</body>
</html>
