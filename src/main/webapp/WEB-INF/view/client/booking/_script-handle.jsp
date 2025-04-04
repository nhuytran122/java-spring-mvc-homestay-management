<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
  function saveSelectedBookingServices() {
    let selectedItems = [];
    let hasError = false;

    $(".item-checkbox:checked").each(function () {
      let bookingID = $(this).data("booking-id");
      let serviceID = $(this).data("service-id");

      let descriptionInput = $(
        ".service-description[data-service-id='" + serviceID + "']"
      );
      let description = descriptionInput.val();

      selectedItems.push({
        booking: { bookingID },
        service: { serviceID },
        description: description,
      });
    });

    if (hasError) return;

    if (selectedItems.length === 0) {
      $("#errorModal .modal-body").html(
        "<p class='text-danger'>Vui lòng chọn ít nhất một dịch vụ để đặt!</p>"
      );
      $("#errorModal").modal("show");
      return;
    }

    $.ajax({
      url: "/booking/booking-service/create",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(selectedItems),
      success: function () {
        location.reload();
      },
      error: function () {
        $("#errorModal .modal-body").html(
          "<p class='text-danger'>Có lỗi xảy ra, vui lòng thử lại!</p>"
        );
        $("#errorModal").modal("show");
      },
    });
  }

  function checkBeforeBookingExtension(button) {
    let bookingID = $(button).data("booking-id");

    $.ajax({
      url: "/booking/can-booking-extension/" + bookingID,
      type: "GET",
      dataType: "json",
      success: function (response) {
        if (response === true) {
          window.location.href = "/booking/booking-extension/" + bookingID;
        } else {
          $("#errorModal .modal-body").html(
            "<p class='text-danger'>Vui lòng thanh toán yêu cầu gia hạn giờ thuê phòng trước đó!</p>"
          );
          $("#errorModal").modal("show");
        }
      },
      error: function (xhr, status, error) {
        console.error("Lỗi kiểm tra thuê thêm giờ:", error);
      },
    });
  }
</script>
