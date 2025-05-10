<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="f"
uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Xác nhận gia hạn đặt phòng</title>
    <jsp:include page="../layout/import-css.jsp" />
    <meta name="_csrf" content="${_csrf.token}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
  </head>
  <body>
    <jsp:include page="../layout/header.jsp" />

    <div class="container-fluid page-body-wrapper">
      <jsp:include page="../layout/theme-settings.jsp" />
      <jsp:include page="../layout/sidebar.jsp" />

      <div class="main-panel">
        <div class="content-wrapper">
          <div class="row justify-content-center">
            <div class="col-lg-10">
              <div class="card p-4">
                <h3 class="text-center mb-3">Xác nhận gia hạn đặt phòng</h3>
                <p class="text-center mb-4">
                  Vui lòng kiểm tra thông tin gia hạn, chọn phương thức thanh
                  toán và xác nhận.
                </p>
                <div id="errorMessage" class="alert alert-danger d-none"></div>

                <div class="row">
                  <div class="col-md-6">
                    <h5>Thông tin phòng</h5>
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
                    <h5>Thông tin gia hạn</h5>
                    <p>
                      <strong>Check-in:</strong>
                      ${f:formatLocalDateTime(extension.booking.checkIn)}
                    </p>
                    <p>
                      <strong>Check-out mới:</strong>
                      ${f:formatLocalDateTime(newCheckout)}
                    </p>
                    <p>
                      <strong>Số lượng khách:</strong>
                      ${extension.booking.guestCount} người
                    </p>
                  </div>
                </div>

                <div class="mt-4 bg-light p-3 border rounded">
                  <h5 class="mb-2">Tổng tiền gia hạn:</h5>
                  <h3 class="text-primary">
                    <fmt:formatNumber
                      value="${extension.totalAmount}"
                      type="number"
                    />đ
                  </h3>
                  <c:if
                    test="${extension.booking.customer.customerType.discountRate > 0}"
                  >
                    <small class="text-muted">
                      Áp dụng giảm giá dành cho thành viên
                      ${extension.booking.customer.customerType.name}
                      (<fmt:formatNumber
                        value="${extension.booking.customer.customerType.discountRate}"
                        pattern="#'%'"
                      />):
                      <span class="text-success fw-bold"
                        >-
                        <fmt:formatNumber
                          value="${discountAmount}"
                          pattern="#,##0"
                        />đ</span
                      >
                    </small>
                  </c:if>
                </div>

                <div class="payment-method mt-4">
                  <h5>
                    Phương thức thanh toán <span class="text-danger">*</span>
                  </h5>
                  <div class="form-check">
                    <input
                      class="form-check-input"
                      type="radio"
                      name="paymentMethod"
                      id="cash"
                      value="CASH"
                    />
                    <label class="form-check-label" for="cash">Tiền mặt</label>
                  </div>
                  <div class="form-check">
                    <input
                      class="form-check-input"
                      type="radio"
                      name="paymentMethod"
                      id="bankTransfer"
                      value="TRANSFER"
                    />
                    <label class="form-check-label" for="bankTransfer"
                      >Chuyển khoản</label
                    >
                  </div>
                  <div
                    id="paymentMethodError"
                    class="invalid-feedback d-block text-danger mt-1"
                    style="display: none"
                  ></div>
                </div>

                <div
                  class="action-buttons my-4 d-flex justify-content-center align-items-center gap-3 flex-wrap"
                >
                  <form
                    method="post"
                    action="/admin/booking/booking-extension/cancel"
                    class="d-inline-block m-0"
                  >
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
                    <button
                      type="submit"
                      class="btn btn-secondary btn-md px-4 py-2"
                    >
                      <i class="bi bi-arrow-left-circle me-1"></i> Quay lại
                    </button>
                  </form>

                  <button
                    type="button"
                    class="btn btn-success btn-md px-4 py-2 d-flex align-items-center"
                    id="confirmPaymentBtn"
                  >
                    <i class="bi bi-credit-card me-2"></i>
                    Xác nhận thanh toán (<fmt:formatNumber
                      type="number"
                      value="${finalAmount}"
                    />đ)
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <script>
      $(document).ready(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        $("#confirmPaymentBtn").on("click", function () {
          let paymentMethod = $('input[name="paymentMethod"]:checked').val();
          if (!paymentMethod) {
            $("#paymentMethodError")
              .text("Vui lòng chọn phương thức thanh toán")
              .show();
            $("#errorMessage")
              .text("Vui lòng chọn phương thức thanh toán")
              .show();
            return;
          }

          let requestData = {
            extensionID: "${extension.extensionID}",
            paymentMethod: paymentMethod,
          };

          $.ajax({
            url: "/admin/booking/booking-extension/confirm-payment",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData),
            beforeSend: function (xhr) {
              xhr.setRequestHeader(header, token);
            },
            success: function (response) {
              window.location.href = "/admin/booking/" + response.data;
            },
            error: function (xhr) {
              let message =
                xhr.responseJSON?.message || "Có lỗi xảy ra. Vui lòng thử lại.";
              $("#errorMessage").text(message).removeClass("d-none");
            },
          });
        });

        $('input[name="paymentMethod"]').on("change", function () {
          $("#paymentMethodError").hide();
          $("#errorMessage").hide();
        });
      });
    </script>
  </body>
</html>
