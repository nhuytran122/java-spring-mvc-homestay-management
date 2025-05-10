<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:include page="../layout/partial/_modal-warning.jsp" />
<script>
  function checkBeforeUpdate(button) {
    let bookingServiceID = $(button).data("booking-service-id");
    let checkUrl = $(button).data("check-url");
    // console.log(bookingServiceID);

    $.ajax({
      url: checkUrl + bookingServiceID,

      type: "GET",
      data: { id: bookingServiceID },
      success: function (response) {
        if (response === true) {
          window.location.href =
            "/admin/booking-service/update/" + bookingServiceID;
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
