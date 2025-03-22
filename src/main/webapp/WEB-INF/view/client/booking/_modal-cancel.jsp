<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="cancelConfirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> 
                    <span>Xác nhận huỷ đặt phòng</span>
                </h5>
            </div>
            <div class="modal-body">
                Bạn có chắc chắn muốn hủy việc đặt phòng này không?
            </div>
            <div class="modal-footer">
                <form id="cancelForm" method="post">
                    <input type="hidden" name="id" id="confirmIdInput">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger">Chắc chắn</button>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="cancelWarningModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> 
                    <span>Không thể hủy đặt phòng</span>
                </h5>
            </div>
            <div class="modal-body">
                <span>Chưa đầy 24 giờ nữa là đến giờ check-in! Bạn không thể hủy đặt phòng</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    })

    function checkBeforeCancel(button) {
        let entityId = button.getAttribute("data-entity-id");
        let cancelUrl = button.getAttribute("data-cancel-url");
        let checkUrl = button.getAttribute("data-check-url");
        let idName = button.getAttribute("data-id-name");

        $.ajax({
            url: checkUrl + entityId,
            type: "GET",
            dataType: "json",
            success: function(response) {
                if (response === true) {                    
                    $("#cancelForm").attr("action", cancelUrl);
                    $("#confirmIdInput").attr("name", idName).val(entityId);
                    $("#cancelConfirmModal").modal("show");
                } else {
                    $("#cancelWarningModal").modal("show");
                }
            },
            error: function(xhr, status, error) {
                console.error("Lỗi kiểm tra hủy:", error);
            }
        });
    }
</script>