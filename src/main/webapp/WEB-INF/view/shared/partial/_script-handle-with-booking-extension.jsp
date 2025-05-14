<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
<script>
  $(document).ready(function () {
    let pricePerHour =
      parseFloat("${booking.pricingSnapshot.extraHourPrice}") || 0;
    let roomTypeName = "${booking.room.roomType.name}".toLowerCase();
    let isDorm = roomTypeName.includes("dorm");
    let guestCount = parseInt("${booking.guestCount}") || 1;
    let currentCheckout = moment("${booking.checkOut}", "YYYY-MM-DD HH:mm");

    $("#newCheckoutTime").daterangepicker(
      {
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 30,
        autoUpdateInput: true,
        minDate: currentCheckout,
        startDate: currentCheckout,
        locale: {
          format: "DD/MM/YYYY HH:mm",
          applyLabel: "Chọn",
          cancelLabel: "Hủy",
        },
      },
      function (start) {
        $("#newCheckoutTime").val(start.format("DD/MM/YYYY HH:mm"));
        updateExtensionFee();
      }
    );

    function updateExtensionFee() {
      let newCheckout = moment($("#newCheckoutTime").val(), "DD/MM/YYYY HH:mm");

      if (
        !newCheckout.isValid() ||
        !currentCheckout.isValid() ||
        !newCheckout.isAfter(currentCheckout)
      ) {
        $("#extensionFee").val("0đ");
        return;
      }

      let extendedHours = newCheckout.diff(currentCheckout, "hours", true);
      let totalFee = isDorm
        ? pricePerHour * extendedHours * guestCount
        : pricePerHour * extendedHours;

      $("#extensionFee").val(totalFee.toLocaleString("vi-VN") + "đ");
    }
    updateExtensionFee();
  });
</script>
