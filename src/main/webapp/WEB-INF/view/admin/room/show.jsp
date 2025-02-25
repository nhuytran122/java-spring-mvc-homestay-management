<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý phòng</title>
  <jsp:include page="../layout/import-css.jsp" />
</head>

<body>
  <div class="container-scroller">
    <jsp:include page="../layout/header.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
            <ul class="navbar-nav mr-lg-2 my-4" style="display: flex; justify-content: center; width: 100%;">
                <li class="nav-item nav-search d-none d-lg-block" style="display: flex; align-items: center;">
                    <form action="/admin/room" method="post" class="d-flex" style="width: 100%; justify-content: center; align-items: center;">
                        <input type="text" class="form-control form-control-sm me-2" name="txtSearch" placeholder="Tìm kiếm phòng..." 
                               value="${not empty searchKeyword ? searchKeyword : ''}" style="width: 400px; font-size: 14px; margin-right: 10px;">
                        <button type="submit" name="btn-search" class="btn btn-primary btn-sm p-2">
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
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h4 class="card-title">Danh sách phòng</h4>
                                    <a href="/admin/room/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới phòng
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th style="width: 150px;">Thumbnail</th>
                                                <th>Số phòng</th>
                                                <th>Giá mỗi giờ</th>
                                                <th>Loại phòng</th>
                                                <th>Chi nhánh</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty rooms}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy phòng nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="room" items="${rooms}">
                                                        <tr>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty room.thumbnail}">
                                                                        <img src="/images/room/${room.thumbnail}" class="img-fluid rounded" style="width: auto; height: 100px; object-fit: cover;">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="/images/room/default-img.jpg" class="img-fluid rounded" style="width: auto; height: 100px; object-fit: cover;">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>${room.roomNumber}</td>
                                                            
                                                            <td><fmt:formatNumber type="number"
                                                                value="${room.roomType.pricePerHour}" /> đ</td>
                                                            <td>${room.roomType.name}</td>
                                                            <td>${room.branch.branchName}</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/room/${room.roomID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                        <i class="bi bi-eye"></i>
                                                                    </a>
                                                                    <a href="/admin/room/update/${room.roomID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>
                                                                    <button class="btn btn-danger btn-sm"
                                                                        data-room-id="${room.roomID}"
                                                                        data-room-number="${room.roomNumber}"
                                                                        onclick="checkBeforeDelete(this)"
                                                                        title="Xóa">
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

                <!--TODO: phân trang theo các tiêu chí search-->
            </div>
        </div>
    </div>
  </div>
  <jsp:include page="_modal-delete.jsp" />
  <jsp:include page="../layout/import-js.jsp" />
</body>
</html>
