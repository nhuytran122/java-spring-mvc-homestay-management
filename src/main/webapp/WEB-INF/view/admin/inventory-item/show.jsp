<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý đồ dùng</title>
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
                <form action="/admin/inventory-item" method="get" class="search-form">
                    <input type="text" class="form-control form-control-sm" name="keyword" placeholder="Tìm kiếm đồ dùng..." 
                            value="${keyword}">
                    <select name="categoryID" class="form-select form-control form-select-sm">
                        <option value="">Chọn phân loại</option>
                        <c:forEach var="category" items="${listCategories}">
                            <option value="${category.categoryID}" ${category.categoryID == categoryID ? 'selected' : ''}>
                                ${category.categoryName}
                            </option>
                        </c:forEach>
                    </select>
                    <select name="sort" class="form-select form-control form-select-sm" style="width: 200px; font-size: 14px; height: 41px;">
                        <option value="" ${sort == '' ? 'selected' : ''}>
                            Không sắp xếp
                        </option>
                        <option value="desc" ${sort == 'desc' ? 'selected' : ''}>
                            Giá giảm dần
                        </option>
                        <option value="asc" ${sort == 'asc' ? 'selected' : ''}>
                            Giá tăng dần
                        </option>
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
                                    <h4 class="card-title">Danh sách đồ dùng</h4>
                                    <a href="/admin/inventory-item/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới đồ dùng
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Tên đồ dùng</th>
                                                <th>Phân loại</th>
                                                <th>Giá tiền</th>
                                                <th>Đơn vị tính</th>
                                                <th>Số lượng tối thiểu</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty items}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy đồ dùng nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${items}">
                                                        <tr style="height: 80px;">
                                                            <td>${item.itemName}</td>
                                                            <td>
                                                                ${item.inventoryCategory.categoryName}
                                                            </td>
                                                            <td>
                                                                <fmt:formatNumber type="number"
                                                                    value="${item.price}" />đ
                                                            </td>
                                                            <td>${item.unit}</td>
                                                            <td>
                                                                <fmt:formatNumber type="number"
                                                                    value="${item.minQuantity}" />
                                                            </td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/inventory-item/update/${item.itemID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>
                                                                    <button class="btn btn-danger btn-sm" title="Xóa"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${item.itemID}" 
                                                                            data-entity-name="${item.itemName}" 
                                                                            data-entity-type="Đồ dùng" 
                                                                            data-delete-url="/admin/inventory-item/delete" 
                                                                            data-check-url="/admin/inventory-item/can-delete/" 
                                                                            data-id-name="itemID">
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
                    <jsp:param name="url" value="/admin/inventory-item" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="extraParams" value="${extraParams}" />
                </jsp:include>
            </div>
        </div>
    </div>
  </div>

    <jsp:include page="../layout/partial/_modals-delete.jsp" />
<jsp:include page="../layout/import-js.jsp" />
</body>
</html>
