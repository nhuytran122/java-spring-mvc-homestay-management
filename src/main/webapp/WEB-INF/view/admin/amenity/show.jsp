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
            <ul class="navbar-nav mr-lg-2 my-4">
                <li class="nav-item nav-search d-none d-lg-block" style="display: flex; align-items: center;">
                    <!--TODO: SEARCH DÙNG Specification-->
                    <form action="/admin/amenity" method="get" class="d-flex" style="width: 100%; justify-content: center; align-items: center;">
                        <input type="text" class="form-control form-control-sm me-2" name="keyword" placeholder="Tìm kiếm tiện nghi..." 
                               value="${keyword}" style="width: 400px; font-size: 14px; margin-right: 10px;">
                    
                        <select name="categoryID" class="form-select form-control form-select-sm me-2" style="width: 200px; font-size: 14px; height: 41px;">
                            <option value="">Chọn phân loại</option>
                            <c:forEach var="category" items="${listCategories}">
                                <option value="${category.categoryID}" ${category.categoryID == categoryID ? 'selected' : ''}>
                                    ${category.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    
                        <button type="submit" class="btn btn-primary btn-sm p-2">
                            <i class="bi bi-search"></i>
                        </button>
                    </form>                    
                </li>
            </ul>
            
            
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
                                                        <tr>
                                                            <td>${amenity.amenityName}</td>
                                                            <td>
                                                                <i class="fas ${amenity.amenityCategory.icon} amenity-icon me-2"></i>
                                                                ${amenity.amenityCategory.categoryName}
                                                            </td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <button class="btn btn-success btn-sm" data-amenity-id="${amenity.amenityID}" onclick="showDetail(this)">
                                                                        <i class="bi bi-eye"></i>
                                                                    </button>
                                                                    <a href="/admin/amenity/update/${amenity.amenityID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>

                                                                    <button class="btn btn-danger btn-sm"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${amenity.amenityID}" 
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

                <c:if test="${totalPages > 0}">
                    <div class="text-center">
                        <nav aria-label="Page navigation">
                            <ul class="pagination justify-content-center">
                                <li class="page-item ${currentPage > 1 ? '' : 'disabled'}">
                                    <c:choose>
                                        <c:when test="${currentPage > 1}">
                                            <a class="page-link" href="/admin/amenity?page=${currentPage - 1}${not empty keyword ? '&keyword=' : ''}${keyword}" aria-label="Trước">
                                                <span aria-hidden="true">&laquo;</span>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="page-link disabled" href="#" aria-label="Trước">
                                                <span aria-hidden="true">&laquo;</span>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </li>                                

                                <c:forEach begin="0" end="${totalPages - 1}" varStatus="loop">
                                    <li class="page-item">
                                        <a class="${(loop.index + 1) eq currentPage ? 'active page-link' : 'page-link'} "
                                            href="/admin/amenity?page=${loop.index + 1}">
                                            ${loop.index + 1}
                                        </a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${currentPage < totalPages ? '' : 'disabled'}">
                                    <c:choose>
                                        <c:when test="${currentPage < totalPages}">
                                            <a class="page-link" href="/admin/amenity?page=${currentPage + 1}${not empty keyword ? '&keyword=' : ''}${keyword}" aria-label="Tiếp theo">
                                                <span aria-hidden="true">&raquo;</span>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="page-link disabled" href="#" aria-label="Tiếp theo">
                                                <span aria-hidden="true">&raquo;</span>
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </li>                                
                            </ul>
                        </nav>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
  </div>

    
    <jsp:include page="_modal-view-detail.jsp" />
    <jsp:include page="../layout/partial/_modal-delete-not-check-can-delete.jsp" />
<jsp:include page="../layout/import-js.jsp" />
</body>
</html>
