<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý kho</title>
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
          <form action="/admin/warehouse" method="get" class="search-form">
            <input
              type="text"
              class="form-control form-control-sm"
              name="keyword"
              placeholder="Tìm kiếm đồ dùng..."
              value="${keyword}"
            />
            <select name="branchID" class="form-select form-control form-select-sm">
              <option value="">Chọn chi nhánh</option>
              <c:forEach var="branch" items="${listBranches}">
                <option value="${branch.branchID}" ${branch.branchID == branchID ? 'selected' : ''}>
                  ${branch.branchName}
                </option>
              </c:forEach>
            </select>

            <button type="submit" class="btn btn-primary btn-sm">
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
                    <h4 class="card-title">Danh sách kho</h4>
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
                          <th>Tên đồ dùng</th>
                          <th>Phân loại</th>
                          <th>Chi nhánh</th>
                          <th>Số lượng hiện có</th>
                          <th>Thao tác</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:choose>
                          <c:when test="${empty listStocks}">
                            <tr>
                              <td colspan="7" class="text-center text-danger">Không tìm thấy đồ dùng nào.</td>
                            </tr>
                          </c:when>
                          <c:otherwise>
                            <c:forEach var="item" items="${listStocks}">
                              <tr>
                                <td>${item.inventoryItem.itemName}</td>
                                <td>${item.inventoryItem.inventoryCategory.categoryName}</td>
                                <td>${item.branch.branchName}</td>
                                <td><fmt:formatNumber type="number" value="${item.quantity}" /></td>
                                <td>
                                  <div class="btn-group" role="group">
                                    <form action="/admin/warehouse/import" method="get" class="d-inline">
                                      <input type="hidden" name="itemID" value="${item.inventoryItem.itemID}">
                                      <input type="hidden" name="branchID" value="${item.branch.branchID}">
                                      <button type="submit" class="btn btn-primary btn-sm" title="Nhập kho">
                                        <i class="bi bi-plus-circle"></i>
                                      </button>
                                    </form>
                                    <form action="/admin/warehouse/export" method="get" class="d-inline">
                                      <input type="hidden" name="itemID" value="${item.inventoryItem.itemID}">
                                      <input type="hidden" name="branchID" value="${item.branch.branchID}">
                                      <button type="submit" class="btn btn-warning btn-sm" title="Xuất kho">
                                        <i class="bi bi-dash-circle"></i>
                                      </button>
                                    </form>
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
            <jsp:param name="url" value="/admin/warehouse" />
            <jsp:param name="currentPage" value="${currentPage}" />
            <jsp:param name="totalPages" value="${totalPages}" />
            <jsp:param name="extraParams" value="${extraParams}" />
          </jsp:include>
        </div>
        <jsp:include page="../layout/footer.jsp" />
      </div>
    </div>
  </div>
  <jsp:include page="../layout/import-js.jsp" />
</body>
</html>