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
    <title>Quản lý phân loại tiện nghi</title>
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
              action="/admin/amenity-category"
              method="get"
              class="search-form"
            >
              <input
                type="text"
                class="form-control form-control-sm"
                name="keyword"
                placeholder="Tìm kiếm phân loại tiện nghi..."
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
                      <h4 class="card-title">Danh sách phân loại tiện nghi</h4>
                      <a
                        href="/admin/amenity-category/create"
                        class="btn btn-primary btn-sm"
                      >
                        <i class="bi bi-plus-circle"></i> Thêm mới
                      </a>
                    </div>

                    <div class="table-responsive">
                      <table class="table table-hover">
                        <thead class="table-light">
                          <tr>
                            <th>Tên phân loại tiện nghi</th>
                            <th>Mô tả</th>
                            <th>Thao tác</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:choose>
                            <c:when test="${empty categories}">
                              <tr>
                                <td colspan="7" class="text-center text-danger">
                                  Không tìm thấy phân loại tiện nghi nào.
                                </td>
                              </tr>
                            </c:when>
                            <c:otherwise>
                              <c:forEach var="category" items="${categories}">
                                <tr>
                                  <td>
                                    <span
                                      class="iconify"
                                      data-icon="${category.icon}"
                                      data-width="24"
                                      data-height="24"
                                    ></span>
                                    ${category.categoryName}
                                  </td>
                                  <td>${category.description}</td>
                                  <td>
                                    <div class="btn-group" role="group">
                                      <a
                                        href="/admin/amenity-category/update/${category.categoryId}"
                                        class="btn btn-warning btn-sm"
                                        title="Sửa"
                                      >
                                        <i class="bi bi-pencil"></i>
                                      </a>

                                      <button
                                        class="btn btn-danger btn-sm"
                                        title="Xóa"
                                        onclick="checkBeforeDelete(this)"
                                        data-entity-id="${category.categoryId}"
                                        data-entity-name="${category.categoryName}"
                                        data-entity-type="Phân loại"
                                        data-delete-url="/admin/amenity-category/delete"
                                        data-check-url="/admin/amenity-category/can-delete/"
                                        data-id-name="categoryId"
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
              <jsp:param name="url" value="/admin/amenity-category" />
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
    <script src="https://code.iconify.design/3/3.1.0/iconify.min.js"></script>
  </body>
</html>
