<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Thêm đơn đặt dịch vụ</title>
  <jsp:include page="../layout/import-css.jsp" />
  <meta name="_csrf" content="${_csrf.token}"/>
  <meta name="_csrf_header" content="${_csrf.headerName}"/>
  <style>
    .service-item {
      transition: all 0.3s ease;
      border: 1px solid #ebedf2;
      border-radius: 4px;
      padding: 15px;
      margin-bottom: 15px;
      background-color: #fff;
    }

    .service-details {
      display: none;
      opacity: 0;
      transition: opacity 0.3s ease;
      margin-top: 10px;
    }

    .service-item.selected .service-details {
      display: block;
      opacity: 1;
    }

    .service-checkbox-wrapper {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      min-width: 40px;
      height: 100%;
      padding-right: 10px;
    }

    .service-checkbox {
      margin: 0;
      transform: scale(1.2);
    }

    .flex-grow-1 {
      line-height: 1.5;
    }

    .iconify {
      vertical-align: middle;
      align-self: center;
    }

    @media (max-width: 576px) {
      .service-checkbox-wrapper {
        justify-content: flex-start;
        padding-right: 0;
      }
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
                  <h4 class="card-title mb-4 text-center">Đặt dịch vụ</h4>
                  <div id="errorMessage" class="error-message"></div>
                  <form id="bookingServiceForm" class="form-horizontal">
                    <c:if test="${not empty errorMessage}">
                      <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>

                    <div class="row">
                      <div class="col-12">
                        <c:forEach var="service" items="${listServices}" varStatus="loop">
                          <c:set
                            var="serviceId"
                            value="${service.serviceId}"
                          />
                          <c:set
                            var="index"
                            value="${loop.index}"
                          />
                          <div class="service-item" id="service-${serviceId}">
                            <div class="d-flex">
                              <div class="form-check d-flex align-items-center justify-content-center me-3" style="width: 46px; height: 46px;">
                            <input class="form-check-input service-checkbox" type="checkbox"
                              name="services[${index}].selected"
                              value="true"
                              data-service-id="${serviceId}" data-price="${service.price}">
                          </div>


                              <div class="flex-grow-1">
                                <div class="d-flex justify-content-between align-items-center">
                                  <div>
                                    <h5 class="mb-1">${service.serviceName}</h5>
                                    <p class="mb-1 text-muted">${service.description}</p>
                                    <p class="mb-0 text-danger">
                                      <b><fmt:formatNumber type="number" value="${service.price}" /> VNĐ / ${service.unit}</b>
                                    </p>
                                  </div>
                                  <span class="iconify ms-3" data-icon="${service.icon}" data-width="24" data-height="24"></span>
                                </div>
                              </div>
                            </div>
                            
                            <div class="service-details">
                              <div class="form-group">
                                <label for="quantity-${serviceId}" class="form-label">Số lượng <span class="text-danger">*</span></label>
                                <input type="number" class="form-control quantity-input"
                                  id="quantity-${serviceId}" name="services[${index}].quantity"
                                  min="1" value="1"/>
                                <div class="invalid-feedback" id="quantity-error-${serviceId}"></div>
                              </div>
                              <div class="form-group mt-2">
                                <label for="note-${serviceId}" class="form-label">Mô tả yêu cầu</label>
                                <textarea class="form-control note-input"
                                  id="note-${serviceId}" name="services[${index}].description"
                                  rows="2" placeholder="Nhập mô tả yêu cầu (ví dụ: thời gian phục vụ, yêu cầu đặc biệt,...)"></textarea>
                              </div>
                              <input type="hidden" name="services[${index}].serviceId" value="${serviceId}"/>
                            </div>
                          </div>

                        </c:forEach>
                      </div>
                    </div>
                    <div class="total-section">
                      Tổng tiền: <span id="totalPrice">0</span> VNĐ
                    </div>

                    <div class="mt-4 text-center">
                      <button type="button" class="btn btn-success btn-lg px-5 py-2 mb-3" id="saveServicesBtn">
                        <i class="bi bi-check-circle"></i> Đặt dịch vụ
                      </button>
                      <div class="d-flex justify-content-center gap-3 flex-wrap">
                        <!-- <button type="button" class="btn btn-outline-dark btn-lg px-5 py-2" onclick="window.history.back()">
                          <i class="bi bi-arrow-left"></i> Quay lại
                        </button> -->
                        <a href="/admin/booking/create"
                            class="btn btn-outline-dark btn-lg px-5 py-2">
                            <i class="bi bi-arrow-left"></i> Quay lại
                        </a>
                        
                        <button type="button" class="btn btn-secondary btn-lg px-5 py-2" id="skipServicesBtn">
                          <i class="bi bi-slash-circle"></i> Bỏ qua dịch vụ
                        </button>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div> 
        <jsp:include page="../layout/footer.jsp" />
      </div>
    </div>   
  </div>

  <jsp:include page="../layout/import-js.jsp" />
  <script src="https://code.iconify.design/3/3.1.0/iconify.min.js"></script>
  <script>
    $(document).ready(function() {
      let servicePrices = {};
      var token = $("meta[name='_csrf']").attr("content");
      var header = $("meta[name='_csrf_header']").attr("content");

      $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
      });

      $('.service-checkbox, .quantity-input').on('change input', function() {
        let total = 0;
        $('.service-checkbox').each(function() {
          let $checkbox = $(this);
          let serviceId = $checkbox.data('service-id');
          let $serviceItem = $('#service-' + serviceId);

          if ($checkbox.is(':checked')) {
            let price = parseFloat($checkbox.data('price'));
            let quantity = parseInt($('#quantity-' + serviceId).val()) || 1;

            $serviceItem.addClass('selected');
            servicePrices[serviceId] = price * quantity;
            total += servicePrices[serviceId];
          } else {
            $serviceItem.removeClass('selected');
            delete servicePrices[serviceId];
          }
        });

        $('#totalPrice').text(total.toLocaleString('vi-VN'));
        $('#errorMessage').hide();
      });

      $('.service-checkbox, .quantity-input').trigger('change');

      function saveSelectedServices() {
        let selectedServices = [];
        let hasError = false;

        const checkedCheckboxes = $('.service-checkbox:checked');
        if (checkedCheckboxes.length === 0) {
          $('#errorMessage').text("Vui lòng chọn ít nhất một dịch vụ để tiếp tục!").show();
          return;
        }

        checkedCheckboxes.each(function() {
          let serviceId = $(this).data('service-id');
          let quantityInput = $('#quantity-' + serviceId);
          let noteInput = $('#note-' + serviceId);
          let quantity = parseFloat(quantityInput.val()) || 1.0;
          let description = noteInput.val().trim();

          if (quantity < 1) {
            quantityInput.addClass('is-invalid');
            $('#quantity-error-' + serviceId).text('Số lượng phải lớn hơn hoặc bằng 1');
            hasError = true;
          } else {
            quantityInput.removeClass('is-invalid');
            $('#quantity-error-' + serviceId).text('');
          }

          selectedServices.push({
            serviceId: serviceId,
            quantity: quantity,
            description: description,
          });
        });

        if (hasError) {
          $('#errorMessage').text("Số lượng phải lớn hơn hoặc bằng 1!").show();
          return;
        }

        let requestData = {
          services: selectedServices
        };

        $.ajax({
          url: "/admin/booking/confirm-service",
          type: "POST",
          contentType: "application/json",
          data: JSON.stringify(requestData),
          success: function(response) {
            window.location.href = "/admin/booking/booking-confirmation?bookingId=" + response.data;
          },
          error: function(xhr) {
            let errorMessage = xhr.responseJSON?.message || "Có lỗi xảy ra khi lưu dịch vụ. Vui lòng thử lại!";
            $('#errorMessage').text(errorMessage).show();
          }
        });
      }

      function skipServices() {
        let requestData = {
          services: []
        };

        $.ajax({
          url: "/admin/booking/confirm-service",
          type: "POST",
          contentType: "application/json",
          data: JSON.stringify(requestData),
          success: function(response) {
            window.location.href = "/admin/booking/booking-confirmation?bookingId=" + response.data;
          },
          error: function(xhr) {
            let errorMessage = xhr.responseJSON?.message || "Có lỗi xảy ra khi bỏ qua dịch vụ. Vui lòng thử lại!";
            $('#errorMessage').text(errorMessage).show();
          }
        });
      }

      $('#saveServicesBtn').on('click', saveSelectedServices);
      $('#skipServicesBtn').on('click', skipServices);
    });
  </script>
</body>
</html>