<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="f"
uri="http://lullabyhomestay.com/functions" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Gia hạn giờ thuê phòng - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css"
    />
  </head>
  <body>
    <jsp:include page="../layout/header.jsp" />
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary mt-4"></nav>

    <div class="booking-extension py-5 bg-light">
      <div class="container">
        <div class="row justify-content-center">
          <div class="col-lg-8">
            <div class="extension-card border rounded-3 p-4 shadow-sm bg-white">
              <h2 class="text-center mb-4 text-primary">
                Gia hạn giờ thuê phòng
              </h2>
              <div class="booking-details mb-4">
                <h5 class="text-muted">Thông tin đặt phòng hiện tại</h5>
                <div class="row">
                  <div class="col-md-12">
                    <p><strong>Phòng:</strong> ${booking.room.roomNumber}</p>
                    <p>
                      <strong>Check-in:</strong>
                      ${f:formatLocalDateTime(booking.checkIn)}
                    </p>
                    <p>
                      <strong>Check-out hiện tại:</strong>
                      ${f:formatLocalDateTime(booking.checkOut)}
                    </p>
                    <p>
                      <strong>Số lượng khách:</strong>
                      ${booking.guestCount}
                    </p>
                  </div>
                </div>
              </div>

              <form
                class="extension-form"
                action="/booking/booking-extension/create"
                method="POST"
              >
                <input
                  type="hidden"
                  name="${_csrf.parameterName}"
                  value="${_csrf.token}"
                />
                <input
                  type="hidden"
                  name="bookingID"
                  value="${booking.bookingID}"
                />
                <div class="mb-4">
                  <label class="form-label" for="newCheckoutTime"
                    >Chọn thời gian check-out mới</label
                  >
                  <input
                    type="text"
                    class="form-control"
                    id="newCheckoutTime"
                    name="newCheckoutTime"
                    readonly
                    placeholder="Chọn thời gian mới"
                  />
                  <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger my-2">${errorMessage}</div>
                  </c:if>
                </div>

                <div class="mb-4">
                  <label class="form-label" for="extensionFee">
                    Phí gia hạn (<fmt:formatNumber
                      type="number"
                      value="${booking.room.roomType.extraPricePerHour}"
                    />đ /
                    <c:if
                      test="${fn:containsIgnoreCase(booking.room.roomType.name, 'dorm')}"
                    >
                      người / </c:if
                    >giờ)
                    <small class="text-muted d-block my-1">
                      <c:choose>
                        <c:when
                          test="${fn:containsIgnoreCase(booking.room.roomType.name, 'dorm')}"
                        >
                          * Phòng Dorm: phí được tính theo số lượng khách và số
                          giờ gia hạn.
                        </c:when>
                        <c:otherwise>
                          * Phí chỉ tính theo số giờ gia hạn.
                        </c:otherwise>
                      </c:choose>
                    </small>
                  </label>

                  <input
                    type="text"
                    class="form-control"
                    id="extensionFee"
                    data-extra-hours-fee="${booking.room.roomType.extraPricePerHour}"
                    value="0"
                    readonly
                    placeholder="0đ"
                  />
                </div>

                <div class="d-grid gap-2">
                  <button type="submit" class="btn btn-primary btn-lg">
                    Xác nhận
                  </button>
                  <a
                    href="/booking/booking-history/${booking.bookingID}"
                    type="button"
                    class="btn btn-outline-secondary btn-lg"
                  >
                    Hủy
                  </a>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include
      page="../../shared/partial/_script-handle-with-booking-extension.jsp"
    />
  </body>
</html>
