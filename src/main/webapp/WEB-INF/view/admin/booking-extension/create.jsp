<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="f"
uri="http://lullabyhomestay.com/functions" %> <%@ taglib prefix="fn"
uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Gia hạn thuê phòng</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css"
    />
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
                <h4 class="card-title text-center">Gia hạn giờ thuê phòng</h4>

                <p><strong>Phòng:</strong> ${booking.room.roomNumber}</p>
                <p>
                  <strong>Check-in:</strong>
                  ${f:formatLocalDateTime(booking.checkIn)}
                </p>
                <p>
                  <strong>Check-out hiện tại:</strong>
                  ${f:formatLocalDateTime(booking.checkOut)}
                </p>
                <p><strong>Số lượng khách:</strong> ${booking.guestCount}</p>

                <form action="/admin/booking/booking-extension/create" method="POST">
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

                  <div class="form-group">
                    <label for="newCheckoutTime"
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
                      <div class="alert alert-danger mt-2">${errorMessage}</div>
                    </c:if>
                  </div>

                  <div class="form-group">
                    <label>Phí gia hạn (<fmt:formatNumber
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

                  <div class="text-center mt-4">
                    <a
                      href="/admin/booking/${booking.bookingID}"
                      class="btn btn-secondary"
                      >Hủy</a
                    >
                    <button type="submit" class="btn btn-primary">
                      Xác nhận
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="../../shared/partial/_script-handle-with-booking-extension.jsp" />
    
</html>
