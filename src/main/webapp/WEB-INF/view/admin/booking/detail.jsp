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
    <title>Chi tiết đơn đặt phòng</title>
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
                        <h4 class="card-title mb-0">Chi tiết đơn đặt phòng</h4>
                        <c:set var="pricing" value="${booking.pricingSnapshot}" />
                      </div>
                      <div class="col-md-6 text-end">
                        <div class="btn-group">
                          <c:if test="${booking.status != 'CANCELLED' and booking.status != 'COMPLETED'}">
                              <button
                                  class="btn btn-danger btn-sm"
                                  title="Hủy đặt phòng"
                                  onclick="checkBeforeCancel(this)"
                                  data-entity-id="${booking.bookingID}"
                                  data-id-name="bookingID"
                                  data-role="admin"
                              >
                                  <i class="bi bi-x-circle"></i>
                                  Hủy đặt phòng
                              </button>
                          </c:if>

                          <c:if test="${booking.status == 'PENDING'}">
                              <a href="/admin/booking/booking-confirmation?bookingID=${booking.bookingID}"
                                class="btn btn-success btn-sm"
                                title="Xác nhận thanh toán">
                                  <i class="bi bi-check-circle"></i>
                                  Xác nhận thanh toán
                              </a>
                          </c:if>

                          <a href="/admin/booking"
                            class="btn btn-secondary btn-sm"
                            title="Trở về">
                              <i class="bi bi-arrow-left"></i>
                              Trở về
                          </a>
                      </div>

                    </div>
                    <div class="row mt-3">
                      <div class="col-md-12">
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Mã đơn đặt phòng:
                          </div>
                          <div class="col-md-8">
                              ${booking.bookingID}
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Khách hàng:
                          </div>
                          <div class="col-md-8">
                            <a
                              href="/admin/customer/${booking.customer.customerID}"
                              class="fw-bold text-dark text-decoration-none"
                              title="Xem chi tiết"
                            >
                              ${booking.customer.user.fullName}
                            </a>
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

                            <c:if test="${not empty booking.payments}">
                                <c:if test="${booking.status == 'CANCELLED'}">
                                    <span class="badge ${booking.totalAmount == booking.paidAmount ? 'bg-danger' : 'bg-warning'}">
                                        ${booking.totalAmount == booking.paidAmount ? 'Đang chờ hoàn tiền' : 'Đã hoàn tiền'}
                                    </span>                 
                                </c:if>
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
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Đã gửi email thông tin?:
                          </div>
                          <div class="col-md-8">
                            <c:choose>
                              <c:when test="${booking.hasSentReminder}">
                                <span class="badge bg-success">Đã gửi</span>
                              </c:when>
                              <c:otherwise>
                                <span class="badge bg-secondary">Chưa gửi</span>
                              </c:otherwise>
                            </c:choose>
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
                          <button
                            class="btn btn-primary btn-sm check-booking-service"
                            data-booking-id="${booking.bookingID}"
                          >
                            Đặt dịch vụ
                          </button>
                        </div>
                        <c:choose>
                          <c:when test="${not empty booking.bookingServices}">
                            <div class="table-responsive">
                              <table class="table table-bordered">
                                <thead>
                                  <tr>
                                    <th>Tên dịch vụ</th>
                                    <th>Mô tả</th>
                                    <th>Đơn giá</th>
                                    <th>Số lượng</th>
                                    <th>Tình trạng phục vụ</th>
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
                                  <c:set
                                    var="status"
                                    value="${bService.status}"
                                  />
                                    <tr>
                                      <td>${bService.service.serviceName}</td>
                                      <td>${bService.description}</td>
                                      <td>
                                        <fmt:formatNumber
                                          type="number"
                                          value="${bService.service.price}"
                                        />đ
                                      </td>
                                      <td>
                                        <c:if test="${bService.quantity != null}">
                                          <fmt:formatNumber type="number" value="${bService.quantity}" pattern="#" />
                                        </c:if>
                                        <c:if test="${bService.quantity == null}">
                                          Đang chờ cập nhật
                                        </c:if>
                                      </td>
                                      <td>
                                        <span class="badge ${status == 'COMPLETED' ? 'bg-success' : 
                                                            status == 'PENDING' ? 'bg-secondary' : 
                                                            status == 'IN_PROGRESS' ? 'bg-warning' : 
                                                            status == 'CANCELLED' ? 'bg-danger' : 'bg-info'}">
                                            ${status.displayName}
                                        </span>
                                      <td>
                                        <c:choose>
                                          <c:when
                                            test="${not empty bService.paymentDetail}"
                                          >
                                            <c:set
                                              var="paymentStatus"
                                              value="${bService.paymentDetail.payment.status}"
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
                                        <button
                                          class="btn btn-warning btn-sm"
                                          title="Sửa"
                                          onclick="checkBeforeUpdate(this)"
                                          data-booking-service-id="${bService.bookingServiceID}"
                                          data-entity-type="Đơn đặt dịch vụ"
                                          data-check-url="/admin/booking-service/can-handle/"
                                        >
                                          <i class="bi bi-pencil"></i>
                                        </button>
                                        <button class="btn btn-info btn-sm status-update-btn" 
                                            data-booking-service-id="${bService.bookingServiceID}" 
                                            data-current-status="${bService.status}"
                                            title="Cập nhật trạng thái">
                                            <i class="bi bi-gear"></i>
                                        </button>
                                        <button
                                          class="btn btn-danger btn-sm"
                                          title="Xóa"
                                          onclick="checkBeforeDelete(this)"
                                          data-entity-id="${bService.bookingServiceID}"
                                          data-entity-name="${bService.service.serviceName}"
                                          data-entity-type="Đơn đặt dịch vụ"
                                          data-delete-url="/admin/booking-service/delete"
                                          data-check-url="/admin/booking-service/can-handle/"
                                          data-id-name="bookingServiceID"
                                          data-custom-message = "Dịch vụ này đã được thanh toán, không thể xóa."
                                        >
                                          <i class="bi bi-trash"></i>
                                        </button>
                                      </td>
                                    </tr>
                                  </c:forEach>
                                </tbody>
                              </table>

                              <c:if test="${totalUnpaidPostpaidAmount != 0}">
                                <div class="card border-0 shadow-sm mt-4">
                                  <div
                                    class="card-body d-flex justify-content-between align-items-center"
                                  >
                                    <div>
                                      <span class="fw-semibold text-secondary"
                                        >Tổng tiền dịch vụ trả sau chưa thanh
                                        toán:</span
                                      >
                                      <span
                                        class="fw-bold text-danger fs-5 ms-2"
                                      >
                                        <fmt:formatNumber
                                          type="number"
                                          value="${totalUnpaidPostpaidAmount}"
                                        />đ
                                      </span>
                                    </div>

                                    <c:choose>
                                      <c:when test="${canPayBServices}">
                                        <button
                                          class="btn btn-primary btn-sm px-4"
                                          onclick="handlePayment('${booking.bookingID}', 'ADDITIONAL_SERVICE', true)"
                                        >
                                          <i class="bi bi-wallet2 me-1"></i>
                                          Xác nhận thanh toán đơn đặt dịch vụ
                                        </button>
                                      </c:when>
                                      <c:otherwise>
                                        <span
                                          class="text-muted small fst-italic"
                                        >
                                          * Số tiền chính xác cần thanh toán sẽ
                                          được cập nhật khi nhân viên cập nhật
                                          số lượng dùng
                                        </span>
                                      </c:otherwise>
                                    </c:choose>
                                  </div>
                                </div>
                              </c:if>
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
                        <div
                          class="d-flex justify-content-between align-items-center mb-3"
                        >
                          <h5 class="mb-3 fw-bold">Gia hạn đặt phòng</h5>
                          <button
                            class="btn btn-primary btn-sm check-booking-extend"
                            data-booking-id="${booking.bookingID}"
                          >
                            Gia hạn giờ thuê
                          </button>
                        </div>
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
                                          value="${pricing.extraHourPrice}"
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
        <jsp:include page="../layout/footer.jsp" />
      </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="../layout/partial/_modals-delete.jsp" />
    <jsp:include page="../../shared/partial/_modal-refund.jsp" />
    <jsp:include
      page="../../shared/partial/_script-handle-cancel-booking.jsp"
    />
    <jsp:include page="../layout/partial/_modal-warning.jsp" />
    <jsp:include page="../booking-service/_script-modal-warning-update.jsp" />
    <jsp:include page="../booking-service/_script-modal-update-status.jsp" />
    <%@ include file="./_modal-payment.jsp" %>
    <script>
      function checkBookingEligibility(
        bookingID,
        successRedirectUrl,
        failureMessage
      ) {
        $.ajax({
          url: "/admin/booking/can-add-service-or-extend/" + bookingID,
          method: "GET",
          success: function (response) {
            if (response === true) {
              window.location.href = successRedirectUrl;
            } else {
              showWarningModal(failureMessage);
            }
          },
          error: function () {
            showWarningModal("Đã xảy ra lỗi khi kiểm tra điều kiện.");
          },
        });
      }

      $(".check-booking-service").click(function () {
        let bookingID = $(this).data("booking-id");
        checkBookingEligibility(
          bookingID,
          "/admin/booking-service/create/" + bookingID,
          "Đơn đặt phòng không đủ điều kiện để thêm dịch vụ."
        );
      });

      $(".check-booking-extend").click(function () {
        let bookingID = $(this).data("booking-id");
        checkBookingEligibility(
          bookingID,
          "/admin/booking/booking-extension/create/" + bookingID,
          "Đơn đặt phòng không đủ điều kiện để gia hạn."
        );
      });
    </script>
  </body>
</html>
