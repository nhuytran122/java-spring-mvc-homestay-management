<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Thêm đơn đặt phòng</title>
  <jsp:include page="../layout/import-css.jsp" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
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
                                <h4 class="card-title mb-4 text-center">Thêm mới đơn đặt phòng</h4>
                                <form:form class="form-horizontal" action="/admin/booking/create" method="post"
                                    modelAttribute="newBooking">
                                    <c:set var="errorCheckIn">
                                        <form:errors path="checkIn" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorCheckOut">
                                        <form:errors path="checkOut" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorGuestCount">
                                        <form:errors path="guestCount" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorCustomer">
                                        <form:errors path="customer" cssClass="invalid-feedback" />
                                    </c:set>
                                    <c:set var="errorRoom">
                                        <form:errors path="room" cssClass="invalid-feedback" />
                                    </c:set>
                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">Khách hàng <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:select class="form-select form-control ${not empty errorCustomer ? 'is-invalid' : ''}" path="customer">
                                                <form:option value="">Chọn khách hàng</form:option>
                                                <form:options items="${listCustomers}" itemValue="customerID" itemLabel="user.fullName"/>
                                            </form:select>
                                            ${errorCustomer}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">Phòng <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <select id="roomSelect" name="room" class="form-control ${not empty errorRoom ? 'is-invalid' : ''}">
                                                <option value="">Chọn phòng</option>
                                                <c:forEach var="room" items="${listRooms}">
                                                    <option value="${room.roomID}"
                                                            data-price="${room.roomType.pricePerHour}"
                                                            data-maxguest="${room.roomType.maxGuest}"
                                                            data-roomtype="${room.roomType.name}">
                                                        ${room.branch.branchName} - ${room.roomType.name} - #${room.roomNumber}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            ${errorRoom}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">Check-in <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input path="checkIn" id="checkin" class="form-control datetime-picker ${not empty errorCheckIn ? 'is-invalid' : ''}" placeholder="Chọn thời gian check-in"/>
                                            ${errorCheckIn}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">Check-out <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <form:input path="checkOut" id="checkout" class="form-control datetime-picker ${not empty errorCheckOut ? 'is-invalid' : ''}" placeholder="Chọn thời gian check-out"/>
                                            ${errorCheckOut}
                                        </div>
                                    </div>

                                    <div class="form-group row">
                                        <label class="col-sm-2 col-form-label">Số lượng khách <span class="text-danger">*</span></label>
                                        <div class="col-sm-10">
                                            <select name="guestCount" id="guestCount" class="form-control ${not empty errorGuestCount ? 'is-invalid' : ''}">
                                                <option value="1">1 người</option>
                                            </select>
                                            ${errorGuestCount}
                                        </div>
                                    </div>

                                    <div class="price-details my-4">
                                        <div class="d-flex justify-content-between mb-2">
                                            <span id="hours-text">0đ x 0 giờ</span>
                                            <span id="subtotal">0đ</span>
                                        </div>
                                        <hr>
                                        <div class="d-flex justify-content-between fw-bold">
                                            <span>Tổng cộng</span>
                                            <span id="total">0đ</span>
                                        </div>
                                    </div>

                                    <c:if test="${not empty errorMessage}">
                                        <div class="alert alert-danger">${errorMessage}</div>
                                    </c:if>

                                    <div class="form-group row">
                                        <div class="col-sm-12 text-center">
                                            <a href="/admin/booking" class="btn btn-light">Hủy</a>
                                            <button type="submit" class="btn btn-primary">Tạo</button>
                                        </div>
                                    </div>

                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 
        </div>
    </div>   
  </div>

  <jsp:include page="../layout/import-js.jsp" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
  <script>
    $(document).ready(function () {
        let now = moment();
        let pricePerHour = 0;
        let isDorm = false;

        function updatePrice() {
            let checkIn = moment($('#checkin').val(), 'DD/MM/YYYY HH:mm');
            let checkOut = moment($('#checkout').val(), 'DD/MM/YYYY HH:mm');
            let guestCount = parseInt($('#guestCount').val()) || 1;

            if (checkIn.isValid() && checkOut.isValid() && checkOut.isAfter(checkIn)) {
                let hours = Math.ceil(checkOut.diff(checkIn, 'hours', true));
                let total = isDorm ? pricePerHour * hours * guestCount : pricePerHour * hours;

                $('#hours-text').text(pricePerHour.toLocaleString('vi-VN') + "đ x " + hours + " giờ" + (isDorm ? " x " + guestCount + " người" : ""));
                $('#subtotal').text(total.toLocaleString('vi-VN') + "đ");
                $('#total').text(total.toLocaleString('vi-VN') + "đ");
            } else {
                $('#hours-text').text(pricePerHour.toLocaleString('vi-VN') + "đ x 0 giờ");
                $('#subtotal').text("0đ");
                $('#total').text("0đ");
            }
        }

        $('#checkin, #checkout').daterangepicker({
            singleDatePicker: true,
            timePicker: true,
            timePicker24Hour: true,
            timePickerIncrement: 30,
            minDate: now,
            locale: { format: 'DD/MM/YYYY HH:mm' }
        }).on('apply.daterangepicker', updatePrice);

        $('#roomSelect').on('change', function () {
            const selected = $(this).find(':selected');
            pricePerHour = parseFloat(selected.data('price')) || 0;
            let maxGuest = parseInt(selected.data('maxguest')) || 1;
            isDorm = (selected.data('roomtype') || '').toLowerCase().includes('dorm');

            let $guestCount = $('#guestCount');
            $guestCount.empty();
            for (let i = 1; i <= maxGuest; i++) {
                $guestCount.append('<option value="' + i + '">' + i + ' người</option>');

            }
            updatePrice();
        });

        $('#guestCount').on('change', updatePrice);
    });
  </script>
</body>
</html>
