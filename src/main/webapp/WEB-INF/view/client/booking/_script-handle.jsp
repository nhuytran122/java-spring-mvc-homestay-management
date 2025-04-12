<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
  function saveSelectedBookingServices() {
    let selectedItems = [];

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
      success: function (response) {
        if (response && response.success === false) {
          $("#errorModal .modal-body").html(
            `<p class='text-danger'>${response.message}</p>`
          );
          $("#errorModal").modal("show");
        } else {
          location.reload();
        }
      },
      error: function (xhr) {
        let message = "Có lỗi xảy ra, vui lòng thử lại!";
        if (xhr.responseJSON && xhr.responseJSON.message) {
          message = xhr.responseJSON.message;
        }

        $("#errorModal .modal-body").html(
          `<p class='text-danger'>${message}</p>`
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
        console.log("Response từ server:", response);

        if (response.data === true) {
          window.location.href = "/booking/booking-extension/" + bookingID;
        } else {
          let message =
            response.message || "Không thể thực hiện gia hạn giờ thuê.";
          $("#errorModal .modal-body").html(
            "<p class='text-danger'>" + message + "</p>"
          );
          $("#errorModal").modal("show");
        }
      },

      error: function (xhr, status, error) {
        console.error("Lỗi kiểm tra thuê thêm giờ:", error);
      },
    });
  }

  function checkBeforeBookServices(button) {
    let status = $(button).data("booking-status");

    if (status === "CANCELLED" || status === "PENDING") {
      let message = "";

      if (status === "CANCELLED") {
        message = "Không thể đặt dịch vụ vì đơn đặt phòng đã bị hủy.";
      } else if (status === "PENDING") {
        message = "Không thể đặt dịch vụ vì đơn đặt phòng chưa được xác nhận.";
      }

      $("#errorModal .modal-body")
        .empty()
        .append($("<p>").addClass("text-danger").text(message));

      $("#errorModal").modal("show");
      return;
    }
    $("#bookServicesModal").modal("show");
  }
</script>
