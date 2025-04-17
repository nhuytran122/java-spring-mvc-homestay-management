<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="f"
uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>Chi tiết lịch đặt phòng</title>
    <jsp:include page="../layout/import-css.jsp" />
    <meta name="_csrf" content="${_csrf.token}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
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
                        <h4 class="card-title mb-0">Chi tiết lịch đặt phòng</h4>
                      </div>
                      <div class="col-md-6 text-end">
                        <div class="btn-group">
                          <c:if
                            test="${booking.status != 'CANCELLED' and booking.status != 'COMPLETED'}"
                          >
                            <button
                              class="btn btn-danger btn-sm"
                              title="Hủy đặt phòng"
                              onclick="checkBeforeCancel(this)"
                              data-entity-id="${booking.bookingID}"
                              data-id-name="bookingID"
                              data-check-url="/admin/booking/booking-history/can-cancel/"
                              data-cancel-url="/admin/booking/booking-history/cancel"
                            >
                              <i class="bi bi-x-circle"></i>
                              Hủy đặt phòng
                            </button>
                          </c:if>

                          <a
                            href="/admin/booking"
                            class="btn btn-secondary btn-sm"
                          >
                            <i class="bi bi-arrow-left"></i> Trở về
                          </a>
                        </div>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-md-12">
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Khách hàng:
                          </div>
                          <div class="col-md-8">
                            ${booking.customer.fullName}
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Chi nhánh:
                          </div>
                          <div class="col-md-8">
                            ${booking.room.branch.branchName}
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Phòng:
                          </div>
                          <div class="col-md-8">${booking.room.roomNumber}</div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Loại phòng:
                          </div>
                          <div class="col-md-8">
                            ${booking.room.roomType.name}
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Thời gian:
                          </div>
                          <div class="col-md-8">
                            ${f:formatLocalDateTime(booking.checkIn)} -
                            ${f:formatLocalDateTime(booking.checkOut)}
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Số lượng khách:
                          </div>
                          <div class="col-md-8">${booking.guestCount}</div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Tình trạng:
                          </div>
                          <div class="col-md-8">
                            <span
                              class="badge ${booking.status == 'COMPLETED' ? 'bg-success' : booking.status == 'CANCELLED' ? 'bg-danger' : booking.status == 'CONFIRMED' ? 'bg-primary' : 'bg-info'}"
                            >
                              ${booking.status.displayName}
                            </span>

                            <c:if test="${booking.status == 'CANCELLED'}">
                              <span
                                class="badge ${booking.totalAmount == booking.paidAmount ? 'bg-danger' : 'bg-warning'}"
                              >
                                ${booking.totalAmount == booking.paidAmount ?
                                'Đang chờ hoàn tiền' : 'Đã hoàn tiền'}
                              </span>
                            </c:if>
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Tổng tiền:
                          </div>
                          <div class="col-md-8">
                            <fmt:formatNumber
                              type="number"
                              value="${booking.totalAmount}"
                            />đ
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Đã thanh toán:
                          </div>
                          <div class="col-md-8">
                            <fmt:formatNumber
                              type="number"
                              value="${booking.paidAmount != null ? booking.paidAmount : 0}"
                            />đ
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="row mt-4">
                      <div class="col-md-12">
                        <div
                          class="d-flex justify-content-between align-items-center mb-3"
                        >
                          <h5 class="fw-bold mb-0">Dịch vụ bổ sung</h5>
                          <a
                            href="/admin/booking-service/create/${booking.bookingID}"
                            class="btn btn-primary btn-sm"
                          >
                            <i class="bi bi-plus-circle"></i> Thêm mới
                          </a>
                        </div>
                        <c:choose>
                          <c:when test="${not empty booking.bookingServices}">
                            <div class="table-responsive">
                              <table class="table table-bordered">
                                <thead>
                                  <tr>
                                    <th>Mã dịch vụ</th>
                                    <th>Tên dịch vụ</th>
                                    <th>Mô tả</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Tình trạng thanh toán</th>
                                    <th>Ngày tạo</th>
                                    <th>Hành động</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  <c:forEach
                                    var="bService"
                                    items="${booking.bookingServices}"
                                  >
                                    <tr>
                                      <td>${bService.bookingServiceID}</td>
                                      <td>${bService.service.serviceName}</td>
                                      <td>${bService.description}</td>
                                      <td>
                                        <fmt:formatNumber
                                          type="number"
                                          value="${bService.service.price}"
                                        />đ
                                      </td>
                                      <td>
                                        <fmt:formatNumber
                                          type="number"
                                          value="${bService.quantity}"
                                          pattern="#"
                                        />
                                      </td>
                                      <td>
                                        <c:choose>
                                          <c:when
                                            test="${not empty bookingService.paymentDetail}"
                                          >
                                            <c:set
                                              var="paymentStatus"
                                              value="${bookingService.paymentDetail.payment.status}"
                                            />
                                            <span
                                              class="badge ${paymentStatus == 'COMPLETED' ? 'bg-success' : paymentStatus == 'FAILED' ? 'bg-danger' : paymentStatus == 'PENDING' ? 'bg-primary' : 'bg-info'}"
                                            >
                                              ${paymentStatus.displayName}
                                            </span>
                                          </c:when>
                                          <c:otherwise>
                                            <span class="badge bg-secondary"
                                              >Đang chờ</span
                                            >
                                          </c:otherwise>
                                        </c:choose>
                                      </td>
                                      <td>
                                        ${f:formatLocalDateTime(bService.createdAt)}
                                      </td>

                                      <td class="text-center">
                                        <a
                                          href="/admin/booking-service/update/${bService.bookingServiceID}"
                                          class="btn btn-warning btn-sm"
                                          title="Sửa"
                                        >
                                          <i class="bi bi-pencil"></i>
                                        </a>
                                        <button
                                          class="btn btn-danger btn-sm"
                                          title="Xóa"
                                          onclick="checkBeforeDelete(this)"
                                          data-entity-id="${bService.bookingServiceID}"
                                          data-entity-name="${bService.service.serviceName}"
                                          data-entity-type="Việc đặt dịch vụ"
                                          data-delete-url="/admin/booking-service/delete"
                                          data-check-url="/admin/booking-service/can-delete/"
                                          data-id-name="bookingServiceID"
                                        >
                                          <i class="bi bi-trash"></i>
                                        </button>
                                      </td>
                                    </tr>
                                  </c:forEach>
                                </tbody>
                              </table>
                            </div>
                          </c:when>
                          <c:otherwise>
                            <p class="text-muted">
                              Không có dịch vụ bổ sung nào.
                            </p>
                          </c:otherwise>
                        </c:choose>
                      </div>
                    </div>

                    <div class="row mt-4">
                      <div class="col-md-12">
                        <h5 class="mb-3 fw-bold">Gia hạn đặt phòng</h5>
                        <c:choose>
                          <c:when test="${not empty booking.bookingExtensions}">
                            <div class="table-responsive">
                              <table class="table table-bordered">
                                <thead>
                                  <tr>
                                    <th>Số giờ gia hạn</th>
                                    <th>Đơn giá/giờ</th>
                                    <th>Tổng tiền cuối</th>
                                    <th>Tình trạng thanh toán</th>
                                    <th>Ngày tạo</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  <c:forEach
                                    var="extension"
                                    items="${booking.bookingExtensions}"
                                  >
                                    <tr>
                                      <td>
                                        <fmt:formatNumber
                                          type="number"
                                          value="${extension.extendedHours}"
                                          pattern="#"
                                        />
                                      </td>
                                      <td>
                                        <fmt:formatNumber
                                          type="number"
                                          value="${extension.booking.room.roomType.extraPricePerHour}"
                                        />đ
                                      </td>
                                      <td>
                                        <c:if
                                          test="${extension.paymentDetail != null}"
                                        >
                                          <fmt:formatNumber
                                            type="number"
                                            value="${extension.paymentDetail.finalAmount}"
                                          />đ
                                        </c:if>
                                      </td>
                                      <td>
                                        <c:choose>
                                          <c:when
                                            test="${not empty extension.paymentDetail}"
                                          >
                                            <c:set
                                              var="paymentStatus"
                                              value="${extension.paymentDetail.payment.status}"
                                            />
                                            <span
                                              class="badge ${paymentStatus == 'COMPLETED' ? 'bg-success' : paymentStatus == 'FAILED' ? 'bg-danger' : paymentStatus == 'PENDING' ? 'bg-primary' : 'bg-info'}"
                                            >
                                              ${paymentStatus.displayName}
                                            </span>
                                          </c:when>
                                          <c:otherwise>
                                            <span class="badge bg-secondary"
                                              >Đang chờ</span
                                            >
                                          </c:otherwise>
                                        </c:choose>
                                      </td>
                                      <td>
                                        ${f:formatLocalDateTime(extension.createdAt)}
                                      </td>
                                    </tr>
                                  </c:forEach>
                                </tbody>
                              </table>
                            </div>
                          </c:when>
                          <c:otherwise>
                            <p class="text-muted">
                              Không có gia hạn đặt phòng nào.
                            </p>
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
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="../layout/partial/_modals-delete.jsp" />
    <script>
      $("#deleteWarningModal").on("show.bs.modal", function () {
        $(this)
          .find(".modal-body")
          .html("Dịch vụ này đã được thanh toán, không thể xóa.");
      });
    </script>
  </body>
</html>
