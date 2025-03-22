<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> 
                    <span id="modalTitle"></span>
                </h5>
            </div>
            <div class="modal-body">
                Bạn có chắc chắn muốn xóa <span id="entityType"></span> <b class="text-primary" id="entityName"></b> không?
            </div>
            <div class="modal-footer">
                <form id="deleteForm" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="hidden" name="id" id="entityIdInput">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger">Xóa</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    function checkBeforeDelete(button) {
        let entityId = button.getAttribute("data-entity-id");
        let entityName = button.getAttribute("data-entity-name");
        let entityType = button.getAttribute("data-entity-type") || "mục";
        let deleteUrl = button.getAttribute("data-delete-url");
        let idName = button.getAttribute("data-id-name");

        $("#modalTitle").text("Xác nhận xóa " + entityType);
        $("#entityType").text(entityType);
        $("#entityName").text(entityName);
        $("#entityIdInput").attr("name", idName).val(entityId);
        $("#deleteForm").attr("action", deleteUrl);
        $("#deleteConfirmModal").modal("show");
    }
</script>