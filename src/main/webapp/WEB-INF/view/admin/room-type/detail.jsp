<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>Chi tiết loại phòng</title>
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
            <div class="card">
              <div class="card-body">
                <div
                  class="row mb-3 d-flex justify-content-between align-items-center"
                >
                  <div class="col-md-6">
                    <h4 class="card-title mb-0">Chi tiết loại phòng</h4>
                  </div>
                  <div class="col-md-6 text-end">
                    <div class="btn-group">
                      <a
                        href="/admin/room-type/update/${roomType.roomTypeId}"
                        class="btn btn-warning btn-sm"
                      >
                        <i class="bi bi-pencil"></i> Sửa
                      </a>
                      <button class="btn btn-danger btn-sm" title="Xóa"
                            onclick="checkBeforeDelete(this)" 
                                data-entity-id="${roomType.roomTypeId}" 
                                data-entity-name="${roomType.name}" 
                                data-entity-type="Loại phòng" 
                                data-delete-url="/admin/room-type/delete" 
                                data-check-url="/admin/room-type/can-delete/" 
                                data-id-name="roomTypeId">
                            <i class="bi bi-trash"></i></i> Xóa
                      </button>
                      <a
                        href="/admin/room-type"
                        class="btn btn-secondary btn-sm"
                      >
                        <i class="bi bi-arrow-left"></i> Quay lại danh sách
                      </a>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-8 mx-auto">
                    <div class="row mb-3">
                      <div class="col-md-4 fw-bold text-md-start">
                        Tên loại phòng:
                      </div>
                      <div class="col-md-8">${roomType.name}</div>
                    </div>
                    <div class="row mb-3">
                      <div class="col-md-4 fw-bold text-md-start">
                        Số khách tối đa:
                      </div>
                      <div class="col-md-8">${roomType.maxGuest} Khách</div>
                    </div>
                    <div class="row mb-3">
                      <div class="col-md-4 fw-bold text-md-start">Mô tả:</div>
                      <div class="col-md-8">${roomType.description}</div>
                    </div>
                  </div>
                </div>

                <c:if test="${not empty roomType.roomPricings}">
                  <div class="mt-4">
                    <div class="my-3 d-flex justify-content-between align-items-center">
                      <h5>Chính sách giá áp dụng</h5>
                      <a href="/admin/room-pricing/create/${roomType.roomTypeId}" class="btn btn-info btn-sm" title="Thêm chính sách giá">
                        <i class="bi bi-plus-circle"></i> Thêm chính sách giá
                      </a>
                    </div>

                    <div class="table-responsive">
                      <table
                        class="table table-hover table-striped sm align-middle text-center"
                      >
                        <thead class="table-light">
                            <tr class="text-center">
                                <th>#</th>
                                <th>Thời gian cơ bản (giờ)</th>
                                <th>Giá cơ bản</th>
                                <th>Giá bù giờ</th>
                                <th>Giá qua đêm</th>
                                <th>Giá theo ngày</th>
                                <th>Áp dụng từ</th>
                                <th>Đến</th>
                                <th>Mặc định?</th>
                                <th>Chính sách</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                          <c:forEach
                            var="pricing"
                            items="${roomType.roomPricings}"
                            varStatus="loop"
                          >
                            <tr style="height: 50px">
                                <td>${loop.index + 1}</td>
                                <td>${pricing.baseDuration} giờ</td>
                                <td><fmt:formatNumber value="${pricing.basePrice}"/>đ</td>
                                <td><fmt:formatNumber value="${pricing.extraHourPrice}"/>đ</td>
                                <td><fmt:formatNumber value="${pricing.overnightPrice}"/>đ</td>
                                <td><fmt:formatNumber value="${pricing.dailyPrice}"/>đ</td>
                                <td>${f:formatLocalDate(pricing.startDate)}</td>
                                <td>${f:formatLocalDate(pricing.endDate)}</td>
                                <td>
                                    <c:if test="${pricing.isDefault}">
                                        <span class="badge bg-success">Mặc định</span>
                                    </c:if>
                                    <c:if test="${!pricing.isDefault}">
                                        <span class="text-muted">-</span>
                                    </c:if>
                                </td>
                                <td>${pricing.policy}</td>
                                <td class="text-center">
                                        <a
                                          class="btn btn-warning btn-sm"
                                          title="Sửa"
                                          href="/admin/room-pricing/update/${pricing.roomPricingId}"
                                        >
                                          <i class="bi bi-pencil"></i>
                                        </a>
                                        </button>
                                        <button
                                          class="btn btn-danger btn-sm"
                                          title="Xóa"
                                          onclick="checkBeforeDelete(this)"
                                          data-entity-id="${pricing.roomPricingId}"
                                          data-entity-name="${room.roomNumber}" 
                                          data-entity-type="Chính sách giá của loại phòng"
                                          data-delete-url="/admin/room-pricing/delete"
                                          data-check-url="/admin/room-pricing/can-delete/"
                                          data-id-name="roomPricingId"
                                          data-custom-message='Đây là chính sách giá mặc định, vui lòng chọn chính sách giá mặc định khác thay thế trước khi xóa'
                                        >
                                          <i class="bi bi-trash"></i>
                                        </button>
                                      </td>
                            </tr>
                          </c:forEach>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </c:if>
              </div>
            </div>
          </div>
          <jsp:include page="../layout/footer.jsp" />
        </div>
      </div>
    </div>
    <jsp:include page="../layout/partial/_modals-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
  </body>
</html>
