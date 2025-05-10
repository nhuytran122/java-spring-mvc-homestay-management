<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div
  class="modal fade"
  id="choosePaymentTypeModal"
  tabindex="-1"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <form action="/admin/payment/handle" method="post" class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Chọn phương thức thanh toán</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
        ></button>
      </div>
      <div class="modal-body">
        <input type="hidden" name="bookingID" id="bookingID-input" />
        <input type="hidden" name="purpose" id="purpose-input" />
        <input type="hidden" name="paymentType" id="paymentType-input" />
        <input
          type="hidden"
          name="${_csrf.parameterName}"
          value="${_csrf.token}"
        />

        <div class="mb-3">
          <label for="paymentSelect" class="form-label"
            >Phương thức thanh toán</label
          >
          <select class="form-select" id="paymentSelect">
            <option value="">Chọn phương thức thanh toán</option>
            <option value="CASH">Tiền mặt</option>
            <option value="TRANSFER">Chuyển khoản</option>
          </select>
        </div>

        <div id="error-message" class="text-danger mt-2" style="display: none">
          <p><strong>Vui lòng chọn phương thức thanh toán!</strong></p>
        </div>

        <div class="d-flex justify-content-end mt-3">
          <button
            type="button"
            class="btn btn-secondary me-2"
            data-bs-dismiss="modal"
          >
            Hủy
          </button>
          <button type="button" class="btn btn-primary" id="submitPaymentBtn">
            Xác nhận thanh toán
          </button>
        </div>
      </div>
    </form>
  </div>
</div>

<script>
  function handlePayment(bookingID, purpose, canPayBServices) {
    console.log(canPayBServices);
    if (!canPayBServices) {
      alert("Bạn không thể thanh toán dịch vụ khi chưa đủ điều kiện!");
      return;
    }
    $("#bookingID-input").val(bookingID);
    $("#purpose-input").val(purpose);
    var modal = new bootstrap.Modal($("#choosePaymentTypeModal")[0]);
    modal.show();
  }

  $("#submitPaymentBtn").click(function () {
    var paymentType = $("#paymentSelect").val();
    if (!paymentType) {
      $("#error-message").show();
    } else {
      $("#error-message").hide();
      $("#paymentType-input").val(paymentType);
      $("form").submit();
    }
  });
</script>
