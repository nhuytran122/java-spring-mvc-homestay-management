<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý lịch sử giao dịch kho</title>
  <jsp:include page="../../layout/import-css.jsp" />
</head>

<body>
  <div class="container-scroller">
    <jsp:include page="../../layout/header.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../../layout/theme-settings.jsp" />
        <jsp:include page="../../layout/sidebar.jsp" />
        <div class="main-panel">
            <div class="search-form-container my-4">
                <form action="/admin/warehouse/transaction" method="get" class="search-form">
                    <input type="text" class="form-control form-control-sm" name="keyword" placeholder="Tìm kiếm giao dịch (tên đồ dùng)..." 
                            value="${criteria.keyword}">
                    
                    <select name="branchId" class="form-select form-control form-select-sm">
                        <option value="">Chọn chi nhánh</option>
                        <c:forEach var="branch" items="${listBranches}">
                            <option value="${branch.branchId}" ${branch.branchId == criteria.branchId ? 'selected' : ''}>
                                ${branch.branchName}
                            </option>
                        </c:forEach>
                    </select>
                    <select name="transactionType" class="form-select form-control form-select-sm">
                        <option value="" ${criteria.transactionType == null || criteria.transactionType == '' ? 'selected' : ''}>
                            Tất cả loại
                        </option>
                        <c:forEach var="type" items="${transactionTypes}">
                            <option value="${type}" ${criteria.transactionType == type ? 'selected' : ''}>
                                ${type.displayName} 
                            </option>
                        </c:forEach>
                    </select>

                    <select name="sort" class="form-select form-control form-select-sm">
                        <option value="" ${criteria.sort == '' ? 'selected' : ''}>
                            Không sắp xếp
                        </option>
                        <option value="asc" ${criteria.sort == 'asc' ? 'selected' : ''}>
                            Cũ nhất
                        </option>
                        <option value="desc" ${criteria.sort == 'desc' ? 'selected' : ''}>
                            Mới nhất
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
                                <div class="d-flex justify-content-between align-items-center mb-4">
                                    <h4 class="card-title">Danh sách giao dịch</h4>
                                    <div class="d-flex gap-2">
                                        <a href="/admin/warehouse/import" class="btn btn-primary btn-sm">
                                            <i class="bi bi-plus-circle"></i> Nhập kho
                                        </a>
                                        <a href="/admin/warehouse/export" class="btn btn-warning btn-sm">
                                            <i class="bi bi-dash-circle"></i> Xuất kho
                                        </a>
                                    </div>
                                </div>                                

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Loại giao dịch</th>
                                                <th>Tên đồ dùng</th>
                                                <th>Phân loại</th>
                                                <th>Chi nhánh</th>
                                                <th>Số lượng</th>
                                                <th>Người tạo</th>
                                                <th>Ngày giao dịch</th>
                                                <th>Ngày cập nhật</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty listTransactions}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy giao dịch nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="item" items="${listTransactions}">
                                                        <tr style="height: 70px;">
                                                            <td>
                                                                <span class="badge ${item.transactionType == 'EXPORT' ? 'bg-danger' : 'bg-success'}">
                                                                    ${item.transactionType == 'EXPORT' ? 'Xuất kho' : 'Nhập kho'}
                                                                </span>
                                                            </td>
                                                            <td>${item.inventoryItem.itemName}</td>
                                                            <td>${item.inventoryItem.inventoryCategory.categoryName}</td>
                                                            <td>${item.branch.branchName}</td>
                                                            <td><fmt:formatNumber type="number" value="${item.quantity}" /></td>
                                                            <td>${item.employee.user.fullName}</td>
                                                            <td>${f:formatLocalDateTime(item.createdAt)}</td>
                                                            <td>${f:formatLocalDateTime(item.updatedAt)}</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <button class="btn btn-warning btn-sm" title="Sửa"
                                                                        onclick="checkBeforeUpdate(this)" 
                                                                            data-transaction-id="${item.transactionId}"
                                                                            data-entity-type="Giao dịch" 
                                                                            data-check-url="/admin/warehouse/transaction/can-update/" >
                                                                        <i class="bi bi-pencil"></i>
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

                <jsp:include page="../../layout/partial/_pagination-with-param.jsp">
                    <jsp:param name="url" value="/admin/warehouse/transaction" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="extraParams" value="${extraParams}" />
                </jsp:include>
            </div>
            <jsp:include page="../../layout/footer.jsp" />
        </div>
    </div>
  </div>
  <jsp:include page="../../layout/import-js.jsp" />
  <jsp:include page="_modal-warning.jsp" />
</body>
</html>