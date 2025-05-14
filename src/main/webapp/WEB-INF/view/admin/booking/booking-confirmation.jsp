<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Xác nhận đặt phòng</title>
  <jsp:include page="../layout/import-css.jsp" />
  <meta name="_csrf" content="${_csrf.token}"/>
  <meta name="_csrf_header" content="${_csrf.headerName}"/>
  <style>
    .card {
      border: 1px solid #ebedf2;
      border-radius: 4px;
    }
    .error-message {
      display: none;
      color: #dc3545;
      margin-top: 10px;
      font-weight: bold;
    }
    .total-section {
      font-size: 1.25rem;
      font-weight: bold;
      text-align: right;
    }
    .table th, .table td {
      vertical-align: middle;
    }
    .form-check-input {
      margin-top: 0;
      margin-bottom: 0;
      vertical-align: middle;
    }
  </style>
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
                  <h4 class="card-title mb-4 text-center">Xác nhận đặt phòng</h4>
                  <p class="text-center mb-4">Vui lòng kiểm tra thông tin đặt phòng và dịch vụ, chọn phương thức thanh toán, sau đó nhấn Xác nhận.</p>
                  <div id="errorMessage" class="error-message"></div>
                  <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">${errorMessage}</div>
                  </c:if>

                  <div class="booking-details mb-4">
                    <div class="row">
                      <div class="col-md-6">
                        <h5 class="mb-3">Thông tin phòng</h5>
                        <p><strong>Phòng:</strong> ${booking.room.roomNumber} - ${booking.room.roomType.name}</p>
                        <p><strong>Chi nhánh:</strong> ${booking.room.branch.branchName}</p>
                        <p><strong>Địa chỉ:</strong> ${booking.room.branch.address}</p>
                      </div>
                      <div class="col-md-6">
                        <h5 class="mb-3">Thông tin đặt phòng</h5>
                        <p><strong>Check-in:</strong> ${f:formatLocalDateTime(booking.checkIn)}</p>
                        <p><strong>Check-out:</strong> ${f:formatLocalDateTime(booking.checkOut)}</p>
                        <p><strong>Số lượng khách:</strong> ${booking.guestCount} người</p>
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
                                    <c:forEach var="bookingService" items="${booking.bookingServices}">
                                      <tr>
                                        <td class="text-start">
                                          <i class="${bookingService.service.icon} me-2"></i>
                                          ${bookingService.service.serviceName}
                                        </td>
                                        <td>${bookingService.service.unit}</td>
                                        <td>${bookingService.quantity}</td>
                                        <td><fmt:formatNumber type="number" value="${bookingService.quantity * bookingService.service.price}" />đ</td>
                                        <td>${bookingService.description}</td>
                                      </tr>
                                    </c:forEach>
                                  </tbody>
                                </table>
                              </div>
                            </div>
                          </c:when>
                          <c:otherwise>
                            <p class="text-muted">Không có dịch vụ nào được đặt kèm.</p>
                          </c:otherwise>
                        </c:choose>
                      </div>
                    </div>
                  </div>

                  <div class="total-section bg-light p-3 border rounded mb-4">
                    <h5 class="mb-2">Tổng tiền (bao gồm phòng và dịch vụ):</h5>
                    <h3 class="text-primary"><fmt:formatNumber type="number" value="${booking.totalAmount}" />đ</h3>
                    <c:if test="${booking.customer.customerType.discountRate > 0}">
                      <div class="mt-2">
                        <small class="text-muted">
                          Đã áp dụng giảm giá dành cho thành viên ${booking.customer.customerType.name}
                          (<fmt:formatNumber value="${booking.customer.customerType.discountRate}" pattern="#'%'" />):
                          <span class="text-success fw-bold">
                            - <fmt:formatNumber value="${discountAmount}" pattern="#,##0" />đ
                          </span>
                        </small>
                      </div>
                    </c:if>
                  </div>

                  <div class="payment-method mb-4">
                    <h5 class="mb-3">Phương thức thanh toán <span class="text-danger">*</span></h5>
                    <div class="row">
                      <div class="col-md-12">
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" name="paymentMethod" id="cash" value="CASH">
                          <label class="form-check-label" for="cash">Tiền mặt</label>
                        </div>
                        <div class="form-check mb-2">
                          <input class="form-check-input" type="radio" name="paymentMethod" id="bankTransfer" value="TRANSFER">
                          <label class="form-check-label" for="bankTransfer">Chuyển khoản</label>
                        </div>
                        <div id="paymentMethodError" class="invalid-feedback"></div>
                      </div>
                    </div>
                  </div>

                  <div class="action-buttons mt-4 text-center">
                    <button type="button" class="btn btn-success btn-lg px-5 py-2 mb-3" id="confirmPaymentBtn">
                      <i class="bi bi-check-circle"></i> Xác nhận
                    </button>
                    <div class="d-flex justify-content-center gap-3 flex-wrap">
                      <button type="button" class="btn btn-outline-dark btn-lg px-5 py-2" onclick="window.history.back()">
                        <i class="bi bi-arrow-left"></i> Quay lại
                      </button>
                      <form action="/admin/booking/cancel" method="post" class="m-0">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <input type="hidden" name="bookingID" value="${booking.bookingID}" />
                        <button type="submit" class="btn btn-outline-danger btn-lg px-5 py-2">
                          <i class="bi bi-x-circle"></i> Hủy đặt phòng
                        </button>
                      </form>
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
    <script src="https://code.iconify.design/3/3.1.0/iconify.min.js"></script>
    <script>
      $(document).ready(function() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        $(document).ajaxSend(function(e, xhr, options) {
          xhr.setRequestHeader(header, token);
        });

        $('#confirmPaymentBtn').on('click', function() {
          let paymentMethod = $('input[name="paymentMethod"]:checked').val();
          if (!paymentMethod) {
            $('#paymentMethodError').text('Vui lòng chọn phương thức thanh toán').show();
            
            return;
          }

          let requestData = {
            bookingID: '${booking.bookingID}',
            paymentMethod: paymentMethod
          };

          $.ajax({
            url: '/admin/booking/confirm-payment',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(requestData),
            success: function(response) {
              window.location.href = '/admin/booking/' + response.data;
            },
            error: function(xhr) {
              let errorMessage = xhr.responseJSON?.message || 'Có lỗi xảy ra khi xác nhận thanh toán. Vui lòng thử lại!';
              $('#errorMessage').text(errorMessage).show();
            }
          });
        });

        $('input[name="paymentMethod"]').on('change', function() {
          $('#paymentMethodError').text('').hide();
          $('#errorMessage').hide();
        });
      });
    </script>
  </body>
</html>