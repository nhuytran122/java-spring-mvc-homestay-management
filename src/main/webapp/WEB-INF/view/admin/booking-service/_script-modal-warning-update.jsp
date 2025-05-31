<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:include page="../layout/partial/_modal-warning.jsp" />
<script>
  function checkBeforeUpdate(button) {
    let bookingServiceId = $(button).data("booking-service-id");
    let checkUrl = $(button).data("check-url");
    // console.log(bookingServiceId);

    $.ajax({
      url: checkUrl + bookingServiceId,

      type: "GET",
      data: { id: bookingServiceId },
      success: function (response) {
        if (response === true) {
          window.location.href =
            "/admin/booking-service/update/" + bookingServiceId;
        } else {
          console.log(checkUrl),
            showWarningModal(
              "Đơn đặt dịch vụ này đã được thanh toán, không thể sửa"
            );
        }
      },
      error: function (xhr, status, error) {
        console.error("Lỗi kiểm tra cập nhật:", error);
      },
    });
  }
</script>
