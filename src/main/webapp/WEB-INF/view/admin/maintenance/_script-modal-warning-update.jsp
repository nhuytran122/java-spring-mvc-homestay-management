<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="updateWarningModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> 
                    <span>Không thể sửa yêu cầu bảo trì</span>
                </h5>
            </div>
            <div class="modal-body">
                <span class="text-sub"></span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script>
    function checkBeforeUpdate(button) { 
    let requestId = $(button).data("request-id");
    let checkUrl = $(button).data("check-url");
    let currentStatus = $(button).data("current-status");

    $.ajax({
        url: checkUrl + requestId,
        type: "GET",
        data: { id: requestId }, 
        success: function(response) {
            if (response === true) {
                window.location.href = "/admin/maintenance/update/" + requestId;
            } else {
                let message = '';
                if (currentStatus === 'COMPLETED') {
                    message = 'Việc bảo trì này đã hoàn thành';
                } else if (currentStatus === 'CANCELLED') {
                    message = 'Việc bảo trì này đã bị hủy';
                } else if (currentStatus === 'IN_PROGRESS') {
                    message = 'Việc bảo trì này đang được xử lý';
                } else if (currentStatus === 'ON_HOLD') {
                    message = 'Việc bảo trì này đang tạm hoãn';
                } else if (currentStatus === 'PENDING') {
                    message = 'Việc bảo trì này đang chờ xử lý';
                } else {
                    message = 'Việc bảo trì này có trạng thái không cho phép sửa đổi';
                }
                $('.text-sub').text(message);
                $("#updateWarningModal").modal("show"); 
            }
        },
        error: function(xhr, status, error) {
            console.error("Lỗi kiểm tra cập nhật:", error);
        }
    });
}</script>

