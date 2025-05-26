<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div
  class="modal fade"
  id="deleteConfirmModal"
  tabindex="-1"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title text-danger">
          <i class="bi bi-exclamation-triangle-fill me-2"></i>
          <span id="confirmTitle">Xác nhận xóa</span>
        </h5>
      </div>
      <div class="modal-body">
        Bạn có chắc chắn muốn xóa <span id="confirmEntityType"></span>
        <b class="text-primary" id="confirmEntityName"></b> không?
      </div>
      <div class="modal-footer">
        <form id="deleteForm" method="post">
          <input
            type="hidden"
            name="${_csrf.parameterName}"
            value="${_csrf.token}"
          />
          <input type="hidden" name="id" id="confirmIdInput" />
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Hủy
          </button>
          <button type="submit" class="btn btn-danger">Xóa</button>
        </form>
      </div>
    </div>
  </div>
</div>

<div
  class="modal fade"
  id="deleteWarningModal"
  tabindex="-1"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title text-danger">
          <i class="bi bi-exclamation-triangle-fill me-2"></i>
          <span id="warningTitle"></span>
        </h5>
      </div>
      <div class="modal-body">
        <span id="warningMessage"></span>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
          Đóng
        </button>
      </div>
    </div>
  </div>
</div>

<script>
  function checkBeforeDelete(button) {
    let entityId = button.getAttribute("data-entity-id");
    let entityName = button.getAttribute("data-entity-name");
    let entityType = button.getAttribute("data-entity-type");
    let deleteUrl = button.getAttribute("data-delete-url");
    let checkUrl = button.getAttribute("data-check-url");
    let idName = button.getAttribute("data-id-name");
    let customMessage = button.getAttribute("data-custom-message");
    let defaultWarningMessage =
      entityType +
      " <b class='text-primary'>" +
      entityName +
      "</b> đang có dữ liệu liên quan, không thể xóa.";

    let warningMessage =
      customMessage ||
      button.getAttribute("data-warning-message") ||
      defaultWarningMessage;

    $.ajax({
      url: checkUrl + entityId,
      type: "GET",
      dataType: "json",
      success: function (response) {
        if (response === true) {
          $("#confirmTitle").text("Xác nhận xóa " + entityType);
          $("#confirmEntityType").text(entityType);
          $("#confirmEntityName").text(entityName);
          $("#deleteForm").attr("action", deleteUrl);
          $("#confirmIdInput").attr("name", idName).val(entityId);
          $("#deleteConfirmModal").modal("show");
        } else {
          $("#warningTitle").text("Không thể xóa " + entityType);
          $("#warningMessage").html(warningMessage);
          $("#deleteWarningModal").modal("show");
        }
      },
      error: function (xhr, status, error) {
        if (xhr.status === 403) {
          $("#warningTitle").text("Không đủ quyền");
          $("#warningMessage").html(
            "Bạn không có quyền thực hiện hành động này."
          );
          $("#deleteWarningModal").modal("show");
        } else {
          console.error("Lỗi kiểm tra xóa:", error);
        }
      },
    });
  }
</script>
