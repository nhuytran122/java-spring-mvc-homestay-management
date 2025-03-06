<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="updateWarningModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> 
                    <span>Không thể sửa giao dịch</span>
                </h5>
            </div>
            <div class="modal-body">
                <span>Giao dịch này đã quá thời thời hạn được phép chỉnh sửa</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script>
    function checkBeforeUpdate(button) { 
    let transactionId = $(button).data("transaction-id");
    let checkUrl = $(button).data("check-url");

    $.ajax({
        url: checkUrl + transactionId,
        type: "GET",
        data: { id: transactionId }, 
        success: function(response) {
            console.log("Response từ server:", response); // Debug
            if (response === true) {
                window.location.href = "/admin/warehouse/transaction/update/" + transactionId;
            } else {
                $("#updateWarningModal").modal("show"); 
            }
        },
        error: function(xhr, status, error) {
            console.error("Lỗi kiểm tra cập nhật:", error);
        }
    });
}</script>