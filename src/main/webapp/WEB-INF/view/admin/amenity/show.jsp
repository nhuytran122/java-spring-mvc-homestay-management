<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý tiện nghi</title>
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
            <div class="search-form-container my-4">
                <form action="/admin/amenity" method="get" class="search-form">
                    <input type="text" class="form-control form-control-sm" name="keyword" placeholder="Tìm kiếm tiện nghi..." 
                        value="${keyword}">
                    <select name="categoryID" class="form-select form-control form-select-sm">
                        <option value="">Chọn phân loại</option>
                        <c:forEach var="category" items="${listCategories}">
                            <c:set var="cateID" value="${category.categoryID}" />
                            <option value="${cateID}" ${cateID == categoryID ? 'selected' : ''}>
                                ${category.categoryName}
                            </option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary btn-sm p-2">
                        <i class="bi bi-search"></i>
                    </button>
                </form> 
            </div>
            
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-md-12 grid-margin stretch-card">
                        <div class="card position-relative">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h4 class="card-title">Danh sách tiện nghi</h4>
                                    <a href="/admin/amenity/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới tiện nghi
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Tên tiện nghi</th>
                                                <th>Phân loại</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty amenities}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy tiện nghi nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="amenity" items="${amenities}">
                                                        <c:set var="amenityID" value="${amenity.amenityID}" />
                                                        <tr>
                                                            <td>${amenity.amenityName}</td>
                                                            <td>
                                                                <i class="fas ${amenity.amenityCategory.icon} amenity-icon me-2"></i>
                                                                ${amenity.amenityCategory.categoryName}
                                                            </td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/amenity/update/${amenityID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>

                                                                    <button class="btn btn-danger btn-sm"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${amenityID}" 
                                                                            data-entity-name="${amenity.amenityName}" 
                                                                            data-entity-type="tiện nghi" 
                                                                            data-delete-url="/admin/amenity/delete" 
                                                                            data-id-name="amenityID">
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

                <jsp:include page="../layout/partial/_pagination-with-param.jsp">
                    <jsp:param name="url" value="/admin/amenity" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="extraParams" value="${extraParams}" />
                </jsp:include>
            </div>
            <jsp:include page="../layout/footer.jsp" />
        </div>
    </div>
  </div>

    <jsp:include page="../layout/partial/_modal-delete-not-check-can-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
</body>
</html>
