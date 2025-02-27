<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý dịch vụ</title>
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
                    <form action="/admin/service" method="get" class="d-flex" style="width: 100%; justify-content: center; align-items: center;">
                        <input type="text" class="form-control form-control-sm me-2" name="keyword" placeholder="Tìm kiếm dịch vụ..." 
                               value="${keyword}" style="width: 400px; font-size: 14px; margin-right: 10px;">
                        <select name="sort" class="form-select form-control form-select-sm me-2" style="width: 200px; font-size: 14px; height: 41px;">
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
                </li>
            </ul>
            
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-md-12 grid-margin stretch-card">
                        <div class="card position-relative">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h4 class="card-title">Danh sách dịch vụ</h4>
                                    <a href="/admin/service/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới dịch vụ
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Tên dịch vụ</th>
                                                <th>Giá</th>
                                                <th>Đơn vị tính</th>
                                                <th>Mô tả</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty listServices}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy dịch vụ nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="service" items="${listServices}">
                                                        <tr>
                                                            <td>${service.serviceName}</td>
                                                            <td><fmt:formatNumber type="number"
                                                                value="${service.price}" /> đ</td>
                                                            <td>${service.unit}</td>
                                                            <td>${service.description}</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/service/update/${service.serviceID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>

                                                                    <button class="btn btn-danger btn-sm" title="Xóa"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${service.serviceID}" 
                                                                            data-entity-name="${service.serviceName}" 
                                                                            data-entity-type="dịch vụ" 
                                                                            data-delete-url="/admin/service/delete" 
                                                                            data-check-url="/admin/service/can-delete/" 
                                                                            data-id-name="serviceID">
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
                            <!-- Nút Trước -->
                            <li class="page-item ${currentPage > 1 ? '' : 'disabled'}">
                                <a class="page-link" href="/admin/service?page=${currentPage - 1}${params}" aria-label="Trước">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>

                            <!-- Danh sách trang -->
                            <c:forEach begin="1" end="${totalPages}" var="page">
                                <li class="page-item ${page eq currentPage ? 'active' : ''}">
                                    <a class="page-link" href="/admin/service?page=${page}${params}">${page}</a>
                                </li>
                            </c:forEach>

                            <!-- Nút Tiếp theo -->
                            <li class="page-item ${currentPage < totalPages ? '' : 'disabled'}">
                                <a class="page-link" href="/admin/service?page=${currentPage + 1}${params}" aria-label="Tiếp theo">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
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
