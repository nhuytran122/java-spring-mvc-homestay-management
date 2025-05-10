<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<div class="modal fade" id="warningModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title text-danger">
          <i class="bi bi-exclamation-triangle-fill me-2"></i>
          <span id="warningTitle">Thông báo</span>
        </h5>
      </div>
      <div class="modal-body">
        <span id="warningMess"></span>
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
  function showWarningModal(message) {
    $("#warningMess").html(message);
    $("#warningModal").modal("show");
  }
</script>
