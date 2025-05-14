<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý loại phòng</title>
  <jsp:include page="../layout/import-css.jsp" />
</head>

<body>
  <div class="container-scroller">
    <jsp:include page="../layout/header.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
            <div class="search-form-container my-4">
                <form action="/admin/room-type" method="get" class="search-form">
                    <input type="text" class="form-control form-control-sm" name="keyword" placeholder="Tìm kiếm loại phòng..." 
                            value="${keyword}">
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
                                    <h4 class="card-title">Danh sách loại phòng</h4>
                                    <a href="/admin/room-type/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới loại phòng
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th style="width: 150px;">Hình ảnh</th>
                                                <th>Tên loại phòng</th>
                                                <th>Số lượng khách tối đa</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty roomTypes}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy loại phòng nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="roomType" items="${roomTypes}">
                                                        <tr>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty roomType.photo}">
                                                                        <img src="/images/room/${roomType.photo}" class="img-fluid rounded" style="width: auto; height: 100px; object-fit: cover;">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="/images/room/default-img.jpg" class="img-fluid rounded" style="width: auto; height: 100px; object-fit: cover;">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>${roomType.name}</td>
                                                            <td>${roomType.maxGuest}</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/room-type/${roomType.roomTypeID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                        <i class="bi bi-eye"></i>
                                                                    </a>
                                                                    <a href="/admin/room-type/update/${roomType.roomTypeID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>
                                                                    <a href="/admin/room-pricing/create/${roomType.roomTypeID}" class="btn btn-info btn-sm" title="Thêm chính sách giá">
                                                                        <i class="bi bi-plus-circle"></i>
                                                                    </a>

                                                                    <button class="btn btn-danger btn-sm" title="Xóa"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${roomType.roomTypeID}" 
                                                                            data-entity-name="${roomType.name}" 
                                                                            data-entity-type="Loại phòng" 
                                                                            data-delete-url="/admin/room-type/delete" 
                                                                            data-check-url="/admin/room-type/can-delete/" 
                                                                            data-id-name="roomTypeID">
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

                <jsp:include page="../layout/partial/_pagination-with-keyword.jsp">
                    <jsp:param name="url" value="/admin/room-type" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="keyword" value="${keyword}" />
                </jsp:include>
            </div>
        </div>
    </div>
  </div>

  <jsp:include page="../layout/partial/_modals-delete.jsp" />
  <jsp:include page="../layout/import-js.jsp" />
</body>
</html>
