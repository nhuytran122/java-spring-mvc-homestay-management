<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%@ taglib prefix="f"
uri="http://lullabyhomestay.com/functions" %><%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Chi tiết yêu cầu hoàn tiền</title>
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
                <div class="card">
                  <div class="card-body">
                    <div
                      class="row mb-3 d-flex justify-content-between align-items-center"
                    >
                      <div class="col-md-6">
                        <h4 class="card-title mb-0">
                          Chi tiết yêu cầu hoàn tiền
                        </h4>
                      </div>
                      <div class="col-md-6 text-end">
                        <div class="btn-group">
                          <a
                            href="/admin/refund"
                            class="btn btn-secondary btn-sm"
                          >
                            <i class="bi bi-arrow-left"></i> Quay lại danh sách
                          </a>
                        </div>
                      </div>
                    </div>

                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold"
                        >Mã yêu cầu hoàn tiền:</label
                      >
                      <div class="col-sm-7">${refund.refundID}</div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold">Thời gian yêu cầu:</label>
                      <div class="col-sm-7">
                        ${f:formatLocalDateTime(refund.createdAt)}
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold"
                        >Trạng thái hoàn tiền:</label
                      >
                      <div class="col-sm-7">
                        <span
                          class="badge ${refund.status == 'PENDING' ? 'bg-warning' : refund.status == 'REFUNDED' ? 'bg-success' : 'bg-danger'}"
                        >
                          ${refund.status.displayName}
                        </span>
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold">Kiểu hoàn tiền:</label>
                      <div class="col-sm-7">
                        ${refund.refundType.displayName}
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold">Số tiền hoàn trả:</label>
                      <div class="col-sm-7">
                        <fmt:formatNumber
                          value="${refund.refundAmount}"
                          type="number"
                        />đ
                      </div>
                    </div>

                    <hr class="my-4" />

                    <div
                      class="row mb-3 d-flex justify-content-between align-items-center"
                    >
                      <div class="col-md-6">
                        <h3 class="card-title mb-4">Thông tin thanh toán</h3>
                      </div>
                      <div class="col-md-6 text-end">
                        <a
                          href="/admin/payment/${refund.payment.paymentID}"
                          class="btn btn-outline-primary btn-sm"
                        >
                          <i class="bi bi-eye"></i> Xem chi tiết thanh toán
                        </a>
                      </div>
                    </div>

                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold"
                        >Mã giao dịch ngân hàng:</label
                      >
                      <div class="col-sm-7">
                        ${refund.payment.vnpTransactionNo}
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold"
                        >Mã tham chiếu VNPAY:</label
                      >
                      <div class="col-sm-7">${refund.payment.vnpTxnRef}</div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold"
                        >Tổng tiền đã thanh toán:</label
                      >
                      <div class="col-sm-7">
                        <fmt:formatNumber
                          value="${refund.payment.totalAmount}"
                          type="number"
                        />đ
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold"
                        >Thời gian thanh toán:</label
                      >
                      <div class="col-sm-7">
                        ${f:formatLocalDateTime(refund.payment.paymentDate)}
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold">Phương thức:</label>
                      <div class="col-sm-7">
                        ${refund.payment.paymentType.displayName}
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold"
                        >Trạng thái thanh toán:</label
                      >
                      <div class="col-sm-7">
                        ${refund.payment.status.displayName}
                      </div>
                    </div>

                    <hr class="my-4" />
                    <div
                      class="row mb-3 d-flex justify-content-between align-items-center"
                    >
                      <div class="col-md-6">
                        <h3 class="card-title mb-4">Thông tin đặt phòng</h3>
                      </div>
                      <div class="col-md-6 text-end">
                        <a
                          href="/admin/booking/${refund.payment.booking.bookingID}"
                          class="btn btn-outline-primary btn-sm"
                        >
                          <i class="bi bi-eye"></i> Xem chi tiết đặt phòng
                        </a>
                      </div>
                    </div>

                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold">Khách hàng:</label>
                      <div class="col-sm-7">
                        ${refund.payment.booking.customer.user.fullName}
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold">Phòng:</label>
                      <div class="col-sm-7">
                        ${refund.payment.booking.room.roomNumber} -
                        ${refund.payment.booking.room.roomType.name}
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold">Chi nhánh:</label>
                      <div class="col-sm-7">
                        ${refund.payment.booking.room.branch.branchName}
                      </div>
                    </div>
                    <div class="row mb-3">
                      <label class="col-sm-5 fw-bold"
                        >Thời gian nhận - trả:</label
                      >
                      <div class="col-sm-7">
                        ${f:formatLocalDateTime(refund.payment.booking.checkIn)}
                        -
                        ${f:formatLocalDateTime(refund.payment.booking.checkOut)}
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
  </body>
</html>
