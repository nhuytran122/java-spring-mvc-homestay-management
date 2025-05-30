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
    <title>Chi tiết khách hàng</title>
    <jsp:include page="../layout/import-css.jsp" />
  </head>

  <body>
    <div class="container-scroller">
      <jsp:include page="../layout/header.jsp" />
      <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
          <div class="content-wrapper">
            <div class="row">
              <div class="col-md-12 grid-margin stretch-card">
                <div class="card position-relative">
                  <div class="card-body">
                    <div
                      class="row mb-3 d-flex justify-content-between align-items-center"
                    >
                      <div class="col-md-6">
                        <h4 class="card-title mb-0">Chi tiết khách hàng</h4>
                      </div>
                      <div class="col-md-6 text-end">
                        <div class="btn-group">
                          <a
                            href="/admin/customer/update/${customer.customerId}"
                            class="btn btn-warning btn-sm"
                          >
                            <i class="bi bi-pencil"></i> Sửa
                          </a>
                          <button
                            class="btn btn-danger btn-sm"
                            title="Xóa"
                            onclick="checkBeforeDelete(this)"
                            data-entity-id="${customer.customerId}"
                            data-entity-name="${customer.fullName}"
                            data-entity-type="Khách hàng"
                            data-delete-url="/admin/customer/delete"
                            data-check-url="/admin/customer/can-delete/"
                            data-id-name="customerId"
                          >
                            <i class="bi bi-trash"></i> Xóa
                          </button>
                          <a
                            href="/admin/customer"
                            class="btn btn-secondary btn-sm"
                          >
                            <i class="bi bi-arrow-left"></i> Quay lại danh sách
                          </a>
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-md-3">
                        <c:choose>
                          <c:when test="${not empty customer.avatar}">
                            <img
                              src="/images/avatar/${customer.avatar}"
                              class="img-fluid rounded"
                              style="
                                width: 100%;
                                height: auto;
                                object-fit: cover;
                              "
                            />
                          </c:when>
                          <c:otherwise>
                            <img
                              src="/images/avatar/default-img.jpg"
                              class="img-fluid rounded"
                              style="
                                width: 100%;
                                height: auto;
                                object-fit: cover;
                              "
                            />
                          </c:otherwise>
                        </c:choose>
                      </div>

                      <div class="col-md-9">
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Tên khách hàng:
                          </div>
                          <div class="col-md-8">${customer.fullName}</div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Số điện thoại:
                          </div>
                          <div class="col-md-8">${customer.phone}</div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Email:
                          </div>
                          <div class="col-md-8">${customer.email}</div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Địa chỉ:
                          </div>
                          <div class="col-md-8">${customer.address}</div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Điểm tích lũy
                          </div>
                          <div class="col-md-8">${customer.rewardPoints}</div>
                        </div>

                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Hạng thành viên:
                          </div>
                          <div class="col-md-8">
                            ${customer.customerType.name}
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Trạng thái tài khoản:
                          </div>
                          <div class="col-md-8">
                            <c:choose>
                              <c:when test="${customer.isEnabled}">
                                <span class="badge bg-success"
                                  >Đã kích hoạt</span
                                >
                              </c:when>
                              <c:otherwise>
                                <span class="badge bg-secondary"
                                  >Chưa kích hoạt</span
                                >
                              </c:otherwise>
                            </c:choose>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <jsp:include page="../layout/footer.jsp" />
        </div>
      </div>
    </div>

    <jsp:include page="../layout/partial/_modals-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
  </body>
</html>
