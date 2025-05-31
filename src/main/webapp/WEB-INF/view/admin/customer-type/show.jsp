<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>Quản lý phân loại khách hàng</title>
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
            <form
              action="/admin/customer/customer-type"
              method="get"
              class="search-form"
            >
              <input
                type="text"
                class="form-control form-control-sm"
                name="keyword"
                placeholder="Tìm kiếm phân loại khách hàng..."
                value="${keyword}"
              />
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
                    <div
                      class="d-flex justify-content-between align-items-center mb-3"
                    >
                      <h4 class="card-title">Danh sách phân loại khách hàng</h4>
                      <a
                        href="/admin/customer/customer-type/create"
                        class="btn btn-primary btn-sm"
                      >
                        <i class="bi bi-plus-circle"></i> Thêm mới
                      </a>
                    </div>

                    <div class="table-responsive">
                      <table class="table table-hover">
                        <thead class="table-light">
                          <tr>
                            <th>Tên phân loại</th>
                            <th>Điểm tối thiểu</th>
                            <th>Phần trăm giảm giá</th>
                            <th>Mô tả</th>
                            <th>Thao tác</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:choose>
                            <c:when test="${empty listTypes}">
                              <tr>
                                <td colspan="7" class="text-center text-danger">
                                  Không tìm thấy phân loại khách hàng nào.
                                </td>
                              </tr>
                            </c:when>
                            <c:otherwise>
                              <c:forEach var="type" items="${listTypes}">
                                <tr>
                                  <td>${type.name}</td>
                                  <td>${type.minPoint}</td>
                                  <td>${type.discountRate}</td>
                                  <td>${type.description}</td>
                                  <td>
                                    <div class="btn-group" role="group">
                                      <a
                                        href="/admin/customer/customer-type/update/${type.customerTypeId}"
                                        class="btn btn-warning btn-sm"
                                        title="Sửa"
                                      >
                                        <i class="bi bi-pencil"></i>
                                      </a>

                                      <button
                                        class="btn btn-danger btn-sm"
                                        title="Xóa"
                                        onclick="checkBeforeDelete(this)"
                                        data-entity-id="${type.customerTypeId}"
                                        data-entity-name="${type.name}"
                                        data-entity-type="Phân loại khách hàng"
                                        data-delete-url="/admin/customer/customer-type/delete"
                                        data-check-url="/admin/customer/customer-type/can-delete/"
                                        data-id-name="customerTypeId"
                                      >
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
              <jsp:param name="url" value="/admin/customer-type" />
              <jsp:param name="currentPage" value="${currentPage}" />
              <jsp:param name="totalPages" value="${totalPages}" />
              <jsp:param name="keyword" value="${keyword}" />
            </jsp:include>
          </div>
          <jsp:include page="../layout/footer.jsp" />
        </div>
      </div>
    </div>

    <jsp:include page="../layout/partial/_modals-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
  </body>
</html>
