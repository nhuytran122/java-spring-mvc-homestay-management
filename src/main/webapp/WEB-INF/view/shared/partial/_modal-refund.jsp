<%@page contentType="text/html" pageEncoding="UTF-8" %>

<div
  class="modal fade"
  id="refundConfirmModal"
  tabindex="-1"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title text-success">
          <i class="bi bi-cash-coin me-2"></i>
          Xác nhận hủy đặt phòng
        </h5>
      </div>
      <div class="modal-body">
        <p id="refundMessage"></p>
      </div>
      <div class="modal-footer">
        <form id="cancelForm" method="post" action="/booking/cancel">
          <input type="hidden" id="bookingIDInput" name="bookingID" />
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Hủy
          </button>
          <button type="submit" class="btn btn-danger">Chắc chắn</button>
          <input
            type="hidden"
            name="${_csrf.parameterName}"
            value="${_csrf.token}"
          />
        </form>
      </div>
    </div>
  </div>
</div>
