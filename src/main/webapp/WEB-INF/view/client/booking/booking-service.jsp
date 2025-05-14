<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt dịch vụ - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <style>
        .service-item {
            transition: all 0.3s ease;
        }
        .service-details {
            display: none;
            opacity: 0;
            transition: opacity 0.3s ease;
        }
        .service-item.selected .service-details {
            display: block;
            opacity: 1;
        }
        .error-message {
            display: none;
            color: #dc3545;
            margin-top: 10px;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <jsp:include page="../layout/header.jsp" />

    <div class="container py-4 mt-5">
        <h2 class="my-4">Chọn các dịch vụ bổ sung</h2>
        <div id="errorMessage" class="error-message"></div>
        <form id="bookingServiceForm">
            <!-- <input type="hidden" name="bookingID" value="${bookingID}"> -->
            <div class="row">
                <div class="col-12">
                    <c:forEach var="service" items="${listServices}" varStatus="loop">
                        <div class="service-item border rounded p-3 mb-3 bg-white" id="service-${service.serviceID}">
                            <div class="d-flex align-items-center">
                                <input type="checkbox" class="form-check-input me-3 service-checkbox" 
                                    name="selectedServices" value="${service.serviceID}"
                                    data-price="${service.price}" 
                                    data-service-id="${service.serviceID}">
                                <div class="flex-grow-1">
                                    <h5 class="mb-1">${service.serviceName}</h5>
                                    <p class="mb-1 text-muted">${service.description}</p>
                                    <p class="mb-0 text-danger">
                                        <b><fmt:formatNumber type="number" value="${service.price}" /> VNĐ / ${service.unit}</b>
                                    </p>
                                </div>
                                <span class="iconify ms-3" data-icon="${service.icon}" data-width="24" data-height="24"></span>
                            </div>
                            <div class="service-details mt-2">
                                <div class="form-group">
                                    <label for="quantity-${service.serviceID}" class="form-label">Số lượng</label>
                                    <input type="number" class="form-control quantity-input" 
                                        id="quantity-${service.serviceID}" 
                                        name="services[${loop.index}].quantity" 
                                        min="1" value="1">
                                </div>
                                <div class="form-group mt-2">
                                    <label for="note-${service.serviceID}" class="form-label">Mô tả yêu cầu</label>
                                    <textarea class="form-control note-input" 
                                        id="note-${service.serviceID}" 
                                        name="services[${loop.index}].description" 
                                        rows="2" placeholder="Nhập mô tả yêu cầu (ví dụ: thời gian phục vụ, yêu cầu đặc biệt,...)"></textarea>
                                </div>
                                <input type="hidden" name="services[${loop.index}].serviceID" value="${service.serviceID}">
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="additional-services mt-4 p-3 bg-light rounded">
                <h4 class="mb-3">Các dịch vụ khác trong thời gian lưu trú</h4>
                <p>Bạn có thể sử dụng thêm các dịch vụ sau tại homestay của chúng tôi:</p>
                <c:forEach var="service" items="${listNotPrePaidServices}">
                    <div class="additional-service-item mb-2">
                        <strong>${service.serviceName}:</strong> <fmt:formatNumber type="number" value="${service.price}" /> VNĐ / ${service.unit}
                    </div>
                </c:forEach>
                <p>Vui lòng liên hệ nhân viên của chúng tôi để được hỗ trợ thêm. Ngoài ra, khi bạn ở homestay, bạn có thể truy cập chức năng <strong>Lịch sử đặt phòng - Xem chi tiết đơn đặt phòng</strong> để đặt thêm các dịch vụ khác.</p>
            </div>

            <div class="total-section fs-4 fw-bold mt-4">
                Tổng tiền: <span id="totalPrice">0</span> VNĐ
            </div>

            <div class="mt-4 text-center">
                <button type="button" class="btn btn-success btn-lg px-5 py-2 mb-3" id="saveServicesBtn">
                    <i class="bi bi-check-circle"></i> Đặt dịch vụ
                </button>
                <div class="d-flex justify-content-center gap-3 flex-wrap">
                    <a href="/room/${bookingRequest.roomID}?fromBookingService=true"
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

    <jsp:include page="../layout/footer.jsp" />
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
                $('.service-checkbox:checked').each(function() {
                    let $checkbox = $(this);
                    let serviceId = $checkbox.data('service-id');
                    let price = parseFloat($checkbox.data('price'));
                    let $serviceItem = $('#service-' + serviceId);
                    let quantity = parseInt($('#quantity-' + serviceId).val()) || 1;

                    $serviceItem.addClass('selected');
                    servicePrices[serviceId] = price * quantity;
                    total += servicePrices[serviceId];
                });

                $('.service-checkbox:not(:checked)').each(function() {
                    let serviceId = $(this).data('service-id');
                    let $serviceItem = $('#service-' + serviceId);
                    $serviceItem.removeClass('selected');
                    delete servicePrices[serviceId];
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
                        hasError = true;
                    } else {
                        quantityInput.removeClass('is-invalid');
                    }

                    selectedServices.push({
                        serviceID: serviceId,
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
                    url: "/booking/confirm-services", 
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(requestData),
                    success: function(response) {
                        console.log(response.data);
                        window.location.href = "/booking/booking-confirmation?bookingID=" + response.data;
                    },
                    error: function(xhr) {
                        let errorMessage = xhr.responseJSON?.message || "Có lỗi xảy ra khi lưu dịch vụ. Vui lòng thử lại!";
                        $('#errorMessage').text(errorMessage).show();
                    }
                });
            }

            function skipServices() {
                // let bookingID = $("input[name='bookingID']").val();
                let requestData = {
                    // bookingID: bookingID,
                    services: [] 
                };

                $.ajax({
                    url: "/booking/confirm-services", 
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(requestData),
                    success: function(response) {
                        console.log(response.data);
                        window.location.href = "/booking/booking-confirmation?bookingID=" + response.data;
                    },
                    error: function(xhr) {
                        let errorMessage = xhr.responseJSON?.message || "Có lỗi xảy ra khi lưu dịch vụ. Vui lòng thử lại!";
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