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
          <input type="hidden" id="statusRequestID" />
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
      var requestID = $(this).data("request-id");
      var currentStatus = $(this).data("current-status");
      $("#statusRequestID").val(requestID);
      $("#currentStatus").val(currentStatus);

      var $select = $("#statusSelect");
      $select.empty();

      // console.log("Current Status:", currentStatus);

      if (currentStatus === "COMPLETED") {
        $("#infoModalBody").text("Bảo trì này đã hoàn thành!");
        $("#infoModal").modal("show");
        return false;
      } else if (currentStatus === "CANCELLED") {
        $("#infoModalBody").text("Bảo trì này đã bị hủy!");
        $("#infoModal").modal("show");
        return false;
      } else {
        if (currentStatus === "PENDING") {
          $select.append('<option value="IN_PROGRESS">Đang xử lý</option>');
          $select.append('<option value="ON_HOLD">Tạm hoãn</option>');
          $select.append('<option value="COMPLETED">Hoàn thành</option>');
          $select.append('<option value="CANCELLED">Đã hủy</option>');
        } else if (currentStatus === "IN_PROGRESS") {
          $select.append('<option value="COMPLETED">Hoàn thành</option>');
        } else if (currentStatus === "ON_HOLD") {
          $select.append('<option value="PENDING">Chờ xử lý</option>');
          $select.append('<option value="IN_PROGRESS">Đang xử lý</option>');
          $select.append('<option value="COMPLETED">Hoàn thành</option>');
        } else {
          $select.append('<option value="PENDING">Chờ xử lý</option>');
          $select.append('<option value="IN_PROGRESS">Đang xử lý</option>');
          $select.append('<option value="ON_HOLD">Tạm hoãn</option>');
          $select.append('<option value="COMPLETED">Hoàn thành</option>');
          $select.append('<option value="CANCELLED">Đã hủy</option>');
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
      var requestID = $("#statusRequestID").val();
      var newStatus = $("#statusSelect").val();
      $.ajax({
        url: "/admin/maintenance/update-status",
        type: "POST",
        data: {
          requestID: requestID,
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
