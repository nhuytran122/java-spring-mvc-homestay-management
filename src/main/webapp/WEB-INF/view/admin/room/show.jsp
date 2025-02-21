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
                                                            <td>${room.RoomNumber}</td>
                                                            
                                                            <td><fmt:formatNumber type="number"
                                                                value="${room.roomType.PricePerNight}" /> đ</td>
                                                            <td>${room.roomType.Name}</td>
                                                            <td>${room.branch.BranchName}</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/room/delete/${room.RoomID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                        <i class="bi bi-eye"></i>
                                                                    </a>
                                                                    <a href="/admin/room/${room.RoomID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>
                                                                    <a href="/admin/room/update/${room.RoomID}" class="btn btn-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModal${room.RoomID}" title="Xóa">
                                                                        <i class="bi bi-trash"></i>
                                                                    </a>
                                                                </div>
                                                            </td>
                                                        </tr>

                                                        <!-- Modal xác nhận xóa -->
                                                        <div class="modal fade" id="deleteModal${room.RoomID}" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title text-danger">
                                                                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                                                            Xác nhận xóa phòng
                                                                        </h5>
                                                                    </div>
                                                                    <div class="modal-body">
                                                                        Bạn có chắc chắn muốn xóa phòng <b class="text-primary">${room.RoomNumber}</b> không?
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <form:form method="post" action="/admin/room/delete"
                                                                            modelAttribute="room">
                                                                                <form:input value="${room.RoomID}" type="text" class="form-control"
                                                                                    path="RoomID" />
                                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                                            <button type="submit" class="btn btn-danger">Xóa</button>
                                                                        </form:form>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
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
            </div>
        </div>
    </div>
  </div>
  <jsp:include page="../layout/import-js.jsp" />
</body>
</html>
