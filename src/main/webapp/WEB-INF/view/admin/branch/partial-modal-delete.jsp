
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Xác nhận xóa chi nhánh
                </h5>
            </div>
            <div class="modal-body">
                Bạn có chắc chắn muốn xóa chi nhánh <b class="text-primary" id="branchNameConfirm"></b> không?
            </div>
            <div class="modal-footer">
                <form action="/admin/branch/delete" method="post">
                    <input type="hidden" name="branchID" value="${branch.branchID}">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger">Xóa</button>
                </form>
            </div>            
        </div>
    </div>
</div>

<div class="modal fade" id="deleteWarningModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Không thể xóa chi nhánh
                </h5>
            </div>
            <div class="modal-body">
                Chi nhánh <b class="text-primary" id="branchNameWarning"></b> đang có dữ liệu liên quan, không thể xóa.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script>
    function checkBeforeDelete(button) {
        let branchID = button.getAttribute("data-branch-id");
        let branchName = button.getAttribute("data-branch-name");

        $.ajax({
            url: "/admin/branch/can-delete/" + branchID,
            type: "GET",
            dataType: "json",
            success: function(response) {
                if (response === true) {
                    $("#branchNameConfirm").text(branchName);
                    $("#branchIdInput").val(branchID);
                    $("#deleteConfirmModal").modal("show");
                } else {
                    $("#branchNameWarning").text(branchName);
                    $("#deleteWarningModal").modal("show");
                }
            },
            error: function(xhr, status, error) {
                console.error("Lỗi kiểm tra xóa:", error);
            }
        });
}
</script>
