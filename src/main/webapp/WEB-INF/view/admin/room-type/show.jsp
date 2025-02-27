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
            <ul class="navbar-nav mr-lg-2 my-4">
                <li class="nav-item nav-search d-none d-lg-block" style="display: flex; align-items: center;">
                    <form action="/admin/room-type" method="get" class="d-flex" style="width: 100%; justify-content: center; align-items: center;">
                        <input type="text" class="form-control form-control-sm me-2" name="keyword" placeholder="Tìm kiếm loại phòng..." 
                               value="${keyword}" style="width: 400px; font-size: 14px; margin-right: 10px;">
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
                                    <h4 class="card-title">Danh sách loại phòng</h4>
                                    <a href="/admin/room-type/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới loại phòng
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Tên loại phòng</th>
                                                <th>Giá mỗi giờ</th>
                                                <th>Số lượng khách tối đa</th>
                                                <th>Phí bù giờ</th>
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
                                                            <td>${roomType.name}</td>
                                                            <td>
                                                                <fmt:formatNumber type="number"
                                                                    value="${roomType.pricePerHour}" />
                                                                đ
                                                            </td>
                                                            <td>${roomType.maxGuest}</td>
                                                            <td>
                                                                <fmt:formatNumber type="number"
                                                                    value="${roomType.extraPricePerHour}" />
                                                                đ</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/room-type/${roomType.roomTypeID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                        <i class="bi bi-eye"></i>
                                                                    </a>
                                                                    <a href="/admin/room-type/update/${roomType.roomTypeID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>
                                                                    <button class="btn btn-danger btn-sm"
                                                                        data-roomType-id="${roomType.roomTypeID}"
                                                                        data-roomType-name="${roomType.name}"
                                                                        onclick="checkBeforeDelete(this)"
                                                                        title="Xóa">
                                                                        <i class="bi bi-trash"></i>
                                                                    </button>

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

                <c:if test="${totalPages > 0}">
                    <div class="text-center">
                        <nav aria-label="Page navigation">
                            <ul class="pagination justify-content-center">
                                <li class="page-item ${currentPage > 1 ? '' : 'disabled'}">
                                    <c:choose>
                                        <c:when test="${currentPage > 1}">
                                            <a class="page-link" href="/admin/room-type?page=${currentPage - 1}${not empty keyword ? '&keyword=' : ''}${keyword}" aria-label="Trước">
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
                                            href="/admin/room-type?page=${loop.index + 1}">
                                            ${loop.index + 1}
                                        </a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${currentPage < totalPages ? '' : 'disabled'}">
                                    <c:choose>
                                        <c:when test="${currentPage < totalPages}">
                                            <a class="page-link" href="/admin/room-type?page=${currentPage + 1}${not empty keyword ? '&keyword=' : ''}${keyword}" aria-label="Tiếp theo">
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

  <jsp:include page="../layout/partial/_modals-delete.jsp" />
  <jsp:include page="../layout/import-js.jsp" />
</body>
</html>
