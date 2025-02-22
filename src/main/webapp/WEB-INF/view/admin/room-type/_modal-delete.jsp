
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Xác nhận xóa loại phòng
                </h5>
            </div>
            <div class="modal-body">
                Bạn có chắc chắn muốn xóa loại phòng <b class="text-primary" id="roomTypeNameConfirm"></b> không?
            </div>
            <div class="modal-footer">
                <form action="/admin/room-type/delete" method="post">
                    <input type="hidden" name="roomTypeID" value="${roomType.roomTypeID}">
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
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Không thể xóa loại phòng
                </h5>
            </div>
            <div class="modal-body">
                Loại phòng <b class="text-primary" id="roomTypeNameWarning"></b> đang có phòng liên quan, không thể xóa.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script>
    function checkBeforeDelete(button) {
        let roomTypeID = button.getAttribute("data-roomType-id");
        let roomTypeName = button.getAttribute("data-roomType-name");

        $.ajax({
            url: "/admin/room-type/can-delete/" + roomTypeID,
            type: "GET",
            dataType: "json",
            success: function(response) {
                if (response === true) {
                    $("#roomTypeNameConfirm").text(roomTypeName);
                    $("#roomTypeIdInput").val(roomTypeID);
                    $("#deleteConfirmModal").modal("show");
                } else {
                    $("#roomTypeNameWarning").text(roomTypeName);
                    $("#deleteWarningModal").modal("show");
                }
            },
            error: function(xhr, status, error) {
                console.error("Lỗi kiểm tra xóa:", error);
            }
        });
}
</script>
