<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="f"
uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Xác nhận gia hạn đặt phòng - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
  </head>
  <body>
    <jsp:include page="../layout/header.jsp" />

    <div class="booking-confirmation-section mt-3 pt-5">
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-lg-8 text-center">
            <div class="confirmation-icon mb-4">
              <i
                class="bi bi-info-circle-fill text-primary"
                style="font-size: 5rem"
              ></i>
            </div>
            <h1 class="mb-4">Xác nhận gia hạn đặt phòng</h1>

            <div class="booking-details card p-2">
              <div class="card-body">
                <div class="row">
                  <div class="col-md-6">
                    <h5 class="mb-3">Thông tin phòng</h5>
                    <p>
                      <strong>Phòng:</strong>
                      ${extension.booking.room.roomNumber} -
                      ${extension.booking.room.roomType.name}
                    </p>
                    <p>
                      <strong>Chi nhánh:</strong>
                      ${extension.booking.room.branch.branchName}
                    </p>
                    <p>
                      <strong>Địa chỉ:</strong>
                      ${extension.booking.room.branch.address}
                    </p>
                  </div>
                  <div class="col-md-6">
                    <h5 class="mb-3">Thông tin đặt phòng</h5>
                    <p>
                      <strong>Check-in:</strong>
                      ${f:formatLocalDateTime(extension.booking.checkIn)}
                    </p>
                    <p>
                      <strong>Check-out mới:</strong>
                      ${f:formatLocalDateTime(extension.booking.checkOut)}
                    </p>
                    <p>
                      <strong>Số lượng khách:</strong>
                      ${extension.booking.guestCount} người
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <div class="total-amount-section mt-4 p-3 bg-light border rounded">
              <h5 class="mb-2">Tổng tiền gia hạn:</h5>
              <h3 class="text-primary">
                <fmt:formatNumber
                  type="number"
                  value="${extension.totalAmount}"
                />đ
              </h3>

              <c:if
                test="${extension.booking.customer.customerType.discountRate > 0}"
              >
                <div class="mt-2">
                  <small class="text-muted">
                    Áp dụng giảm giá dành cho thành viên
                    ${extension.booking.customer.customerType.name}
                    (<fmt:formatNumber
                      value="${extension.booking.customer.customerType.discountRate}"
                      pattern="#'%'"
                    />):
                    <span class="text-success fw-bold">
                      -
                      <fmt:formatNumber
                        value="${discountAmount}"
                        pattern="#,##0"
                      />đ
                    </span>
                  </small>
                </div>
              </c:if>
            </div>

            <div class="action-buttons my-4">
              <div class="btn-group" role="group">
                <form method="post" action="/booking/booking-extension/cancel">
                  <input
                    type="hidden"
                    name="${_csrf.parameterName}"
                    value="${_csrf.token}"
                  />
                  <input
                    type="hidden"
                    name="id"
                    value="${extension.extensionID}"
                  />
                  <button type="submit" class="btn btn-secondary btn-lg">
                    <i class="bi bi-arrow-left-circle"></i> Hủy
                  </button>
                </form>

                <a
                  onclick="handlePayment('${extension.booking.bookingID}', 'EXTENDED_HOURS')"
                  class="btn btn-primary btn-lg"
                >
                  <i class="bi bi-credit-card"></i> Thanh toán
                  (<fmt:formatNumber type="number" value="${finalAmount}" />đ)
                </a>
              </div>
            </div>

            <div id="payment-error" class="mt-3"></div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />

    <jsp:include page="../layout/partial/_payment-handler.jsp" />
  </body>
</html>
