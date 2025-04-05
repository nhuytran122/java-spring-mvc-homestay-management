<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
  $(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
  });

  function checkBeforeCancel(button) {
    let bookingId = $(button).data("entity-id");
    let checkUrl = "/booking/check-refund/" + bookingId;

    $.ajax({
      url: checkUrl,
      type: "GET",
      dataType: "json",
      success: function (response) {
        console.log("Response từ server:", response);

        let refundAmount = Number(response.refundAmount) || 0;
        let refundPercentage = response.refundPercentage || 0;

        let formattedRefundAmount = refundAmount.toLocaleString("vi-VN");

        if (refundAmount === 0) {
          let message = "<p>Bạn có chắc chắn muốn hủy đặt phòng không?</p>";
          $("#refundMessage").html(message);
        } else {
          let message =
            "<p>Bạn có chắc chắn muốn hủy đặt phòng không?</p>" +
            "<p>Số tiền hoàn lại: <b>" +
            formattedRefundAmount +
            "đ </b> (" +
            refundPercentage +
            "% số tiền đã thanh toán).</p>";
          $("#refundMessage").html(message);
        }

        $("#bookingIDInput").val(bookingId);
        $("#refundConfirmModal").modal("show");
      },
      error: function (xhr, status, error) {
        console.error("Lỗi kiểm tra hoàn tiền:", xhr.status, xhr.responseText);
        $("#refundMessage").html(
          "<p>Có lỗi xảy ra khi kiểm tra hoàn tiền. Vui lòng thử lại.</p>"
        );
        $("#refundConfirmModal").modal("show");
      },
    });
  }
</script>
