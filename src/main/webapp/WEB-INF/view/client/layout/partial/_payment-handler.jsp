<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
  function handlePayment(bookingID, paymentPurpose, canPay) {
    if (!canPay) {
      alert("Bạn hiện tại không thể thanh toán!");
      return;
    }
    $.ajax({
      url:
        "/checkout?bookingID=" +
        bookingID +
        "&paymentPurpose=" +
        paymentPurpose,
      type: "GET",
      dataType: "json",
      success: function (data) {
        if (data.data) {
          window.location.href = data.data;
        } else {
          showError(
            "Lỗi khi tạo URL thanh toán: " +
              (data.message || "Lỗi không xác định")
          );
        }
      },
      error: function (xhr, status, error) {
        console.error("Lỗi: ", error);
        showError(
          "Có lỗi xảy ra khi tạo URL thanh toán: " +
            (xhr.responseJSON ? xhr.responseJSON.message : error)
        );
      },
    });
  }

  function showError(message) {
    const errorContainer = document.getElementById("payment-error");
    if (errorContainer) {
      errorContainer.innerHTML = `<div class="alert alert-danger">${message}</div>`;
    }
  }
</script>
