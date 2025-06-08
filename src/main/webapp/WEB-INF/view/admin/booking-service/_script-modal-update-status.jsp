<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div
  class="modal fade"
  id="statusModal"
  tabindex="-1"
  aria-labelledby="statusModalLabel"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="statusModalLabel">Cập nhật trạng thái</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
          aria-label="Close"
        ></button>
      </div>
      <div class="modal-body">
        <form id="statusForm">
          <input type="hidden" id="bookingServiceId" />
          <input type="hidden" id="currentStatus" />
          <div class="mb-3">
            <label for="statusSelect" class="form-label">Trạng thái mới</label>
            <select class="form-select" id="statusSelect"></select>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
          Hủy
        </button>
        <button type="button" class="btn btn-warning" id="saveStatus">
          Cập nhật
        </button>
      </div>
    </div>
  </div>
</div>

<div
  class="modal fade"
  id="infoModal"
  tabindex="-1"
  aria-labelledby="infoModalLabel"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="infoModalLabel">Thông báo</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
          aria-label="Close"
        ></button>
      </div>
      <div class="modal-body" id="infoModalBody"></div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bs-dismiss="modal">
          OK
        </button>
      </div>
    </div>
  </div>
</div>

<script>
  $(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });

    $(".status-update-btn").click(function () {
      var bookingServiceId = $(this).data("booking-service-id");
      var currentStatus = $(this).data("current-status");
      var quantity = $(this).data("current-quantity");
      console.log(quantity);

      $("#bookingServiceId").val(bookingServiceId);
      $("#currentStatus").val(currentStatus);

      var $select = $("#statusSelect");
      $select.empty();

      if (currentStatus === "COMPLETED") {
        $("#infoModalBody").text("Đơn đặt dịch vụ này đã hoàn tất!");
        $("#infoModal").modal("show");
        return false;
      } else if (currentStatus === "CANCELLED") {
        $("#infoModalBody").text("Đơn đặt dịch vụ này đã bị hủy!");
        $("#infoModal").modal("show");
        return false;
      } else {
        if (currentStatus === "PENDING") {
          $select.append('<option value="IN_PROGRESS">Đang phục vụ</option>');

          if (quantity != null && quantity !== "" && Number(quantity) > 0) {
            $select.append('<option value="COMPLETED">Hoàn thành</option>');
          }
        } else if (currentStatus === "IN_PROGRESS") {
          if (quantity == null || quantity === "" || Number(quantity) <= 0) {
            $("#infoModalBody").text(
              "Vui lòng cập nhật số lượng trước khi cập nhật trạng thái mới cho đơn đặt dịch vụ này"
            );
            $("#infoModal").modal("show");
            return false;
          } else {
            $select.append('<option value="COMPLETED">Hoàn thành</option>');
          }
        }
      }
      if ($select.find("option").length === 0) {
        $("#infoModalBody").text("Không có trạng thái nào để cập nhật!");
        $("#infoModal").modal("show");
        return false;
      }
      $("#statusModal").modal("show");
    });

    $("#saveStatus").click(function () {
      var bookingServiceId = $("#bookingServiceId").val();
      var newStatus = $("#statusSelect").val();
      $.ajax({
        url: "/admin/booking-service/update-status",
        type: "POST",
        data: {
          bookingServiceId: bookingServiceId,
          status: newStatus,
        },
        success: function (response) {
          location.reload();
        },
        error: function (xhr) {
          var errorMessage = xhr.responseText || "Lỗi không xác định!";
          alert("Cập nhật thất bại: " + errorMessage);
        },
      });
    });
  });
</script>
