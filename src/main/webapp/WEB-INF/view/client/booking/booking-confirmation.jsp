<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="f"
uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Xác nhận đặt phòng - Lullaby Homestay</title>
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
            <h1 class="mb-4">Xác nhận đặt phòng</h1>
            <p class="lead mb-4">
              Vui lòng kiểm tra thông tin đặt phòng và dịch vụ của bạn. Chọn
              phương thức thanh toán và tiếp tục để hoàn tất đặt phòng.
            </p>

            <div class="booking-details card p-2">
              <div class="card-body">
                <div class="row">
                  <div class="col-md-6">
                    <h5 class="mb-3">Thông tin phòng</h5>
                    <p>
                      <c:set var="bookingID" value="${booking.bookingID}" />
                      <c:set var="totalAmount" value="${booking.totalAmount}" />
                      <c:set var="room" value="${booking.room}" />
                      <c:set var="branch" value="${room.branch}" />
                      <strong>Phòng:</strong> ${room.roomNumber} -
                      ${room.roomType.name}
                    </p>
                    <p>
                      <strong>Chi nhánh:</strong>
                      ${branch.branchName}
                    </p>
                    <p><strong>Địa chỉ:</strong> ${branch.address}</p>
                  </div>
                  <div class="col-md-6">
                    <h5 class="mb-3">Thông tin đặt phòng</h5>
                    <p>
                      <strong>Check-in:</strong>
                      ${f:formatLocalDateTime(booking.checkIn)}
                    </p>
                    <p>
                      <strong>Check-out:</strong>
                      ${f:formatLocalDateTime(booking.checkOut)}
                    </p>
                    <p>
                      <strong>Số lượng khách:</strong> ${booking.guestCount}
                      người
                    </p>
                  </div>
                </div>

                <div class="row mt-4">
                  <div class="col-md-12">
                    <h5 class="mb-3">Dịch vụ đi kèm</h5>
                    <c:choose>
                      <c:when test="${not empty booking.bookingServices}">
                        <div class="card p-2">
                          <div class="card-body">
                            <table class="table table-bordered text-center">
                              <thead class="table-light">
                                <tr>
                                  <th>Dịch vụ</th>
                                  <th>Đơn vị</th>
                                  <th>Số lượng</th>
                                  <th>Thành tiền</th>
                                  <th>Ghi chú</th>
                                </tr>
                              </thead>
                              <tbody>
                                <c:forEach
                                  var="bookingService"
                                  items="${booking.bookingServices}"
                                >
                                  <c:set
                                    var="serviceOfBService"
                                    value="${bookingService.service}"
                                  />
                                  <tr>
                                    <td class="text-start">
                                      <i
                                        class="${serviceOfBService.icon} me-2"
                                      ></i>
                                      ${serviceOfBService.serviceName}
                                    </td>
                                    <td>${serviceOfBService.unit}</td>
                                    <td>${bookingService.quantity}</td>
                                    <td>
                                      <fmt:formatNumber
                                        type="number"
                                        value="${bookingService.quantity * serviceOfBService.price}"
                                      />đ
                                    </td>
                                    <td>${bookingService.description}</td>
                                  </tr>
                                </c:forEach>
                              </tbody>
                            </table>
                          </div>
                        </div>
                      </c:when>
                      <c:otherwise>
                        <p class="text-muted">
                          Không có dịch vụ nào được đặt kèm.
                        </p>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>
              </div>
            </div>

            <div class="total-amount-section mt-4 p-3 bg-light border rounded">
              <h5 class="mb-2">Tổng tiền (bao gồm phòng và dịch vụ):</h5>
              <h3 class="text-primary">
                <fmt:formatNumber type="number" value="${totalAmount}" />đ
              </h3>

              <c:set
                var="customerType"
                value="${booking.customer.customerType}"
              />
              <c:if test="${customerType.discountRate > 0}">
                <div class="mt-2">
                  <small class="text-muted">
                    Đã áp dụng giảm giá dành cho thành viên ${customerType.name}
                    (<fmt:formatNumber
                      value="${customerType.discountRate}"
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
              <div class="text-center">
                <a
                  onclick="handlePayment('${bookingID}', 'ROOM_BOOKING', true)"
                  class="btn btn-success btn-lg shadow px-5"
                >
                  <i class="bi bi-credit-card"></i> Thanh toán
                  (<fmt:formatNumber type="number" value="${totalAmount}" />đ)
                </a>
              </div>
              <div
                class="d-flex gap-3 justify-content-center align-items-center flex-wrap mt-3"
              >
                <button
                  type="button"
                  class="btn btn-secondary btn-lg"
                  onclick="window.history.back()"
                >
                  <i class="bi bi-arrow-left"></i> Quay lại
                </button>

                <form action="/booking/cancel" method="post" class="m-0">
                  <input
                    type="hidden"
                    name="${_csrf.parameterName}"
                    value="${_csrf.token}"
                  />
                  <input type="hidden" name="bookingID" value="${bookingID}" />
                  <button type="submit" class="btn btn-outline-danger btn-lg">
                    <i class="bi bi-x-circle"></i> Hủy đặt phòng
                  </button>
                </form>
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
