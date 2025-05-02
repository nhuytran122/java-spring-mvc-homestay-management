<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý khách hàng</title>
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
                <form action="/admin/customer" method="get" class="search-form">
                    <input type="text" class="form-control form-control-sm"
                        name="keyword" 
                        placeholder="Tìm kiếm khách hàng..." 
                        value="${keyword}"/>
                    <select name="customerTypeID" class="form-select form-select-sm" >
                        <option value="">Chọn phân loại</option>
                        <c:forEach var="type" items="${listTypes}">
                            <option value="${type.customerTypeID}" ${type.customerTypeID == criteria.customerTypeID ? 'selected' : ''}>
                                ${type.name}
                            </option>
                        </c:forEach>
                    </select>
                    <select name="sort" class="form-select form-control form-select-sm">
                        <option value="" ${criteria.sort == '' ? 'selected' : ''}>
                            Không sắp xếp
                        </option>
                        <option value="asc" ${criteria.sort == 'asc' ? 'selected' : ''}>
                            Điểm tăng dần
                        </option>
                        <option value="desc" ${criteria.sort == 'desc' ? 'selected' : ''}>
                            Điểm giảm dần
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
                                    <h4 class="card-title">Danh sách khách hàng</h4>
                                    <a href="/admin/customer/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Tên khách hàng</th>
                                                <th>Số điện thoại</th>
                                                <th>Email</th>
                                                <th>Địa chỉ</th>
                                                <th>Điểm tích lũy</th>
                                                <th>Phân loại</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty listCustomers}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy khách hàng nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="customer" items="${listCustomers}">
                                                        <tr style="height: 70px;">
                                                            <td>${customer.fullName}</td>
                                                            <td>${customer.phone}</td>
                                                            <td>${customer.email}</td>
                                                            <td>${customer.address}</td>
                                                            <td><fmt:formatNumber type="number"
                                                                value="${customer.rewardPoints}" />
                                                            </td>
                                                            <td>${customer.customerType.name}</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="/admin/customer/update/${customer.customerID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>

                                                                    <button class="btn btn-danger btn-sm" title="Xóa"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${customer.customerID}" 
                                                                            data-entity-name="${customer.fullName}" 
                                                                            data-entity-type="Khách hàng" 
                                                                            data-delete-url="/admin/customer/delete" 
                                                                            data-check-url="/admin/customer/can-delete/" 
                                                                            data-id-name="customerID">
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
                    <jsp:param name="url" value="/admin/customer" />
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
