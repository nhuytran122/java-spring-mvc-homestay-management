
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Xác nhận xóa phòng
                </h5>
            </div>
            <div class="modal-body">
                Bạn có chắc chắn muốn xóa phòng <b class="text-primary" id="roomNumberConfirm"></b> không?
            </div>
            <div class="modal-footer">
                <form action="/admin/room/delete" method="post">
                    <input type="hidden" name="roomID" value="${room.roomID}">
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
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Không thể xóa phòng
                </h5>
            </div>
            <div class="modal-body">
                Phòng <b class="text-primary" id="roomNumberWarning"></b> đang có dữ liệu liên quan, không thể xóa.
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script>
    function checkBeforeDelete(button) {
        let roomID = button.getAttribute("data-room-id");
        let roomName = button.getAttribute("data-room-number");

        $.ajax({
            url: "/admin/room/can-delete/" + roomID,
            type: "GET",
            dataType: "json",
            success: function(response) {
                if (response === true) {
                    $("#roomNumberConfirm").text(roomName);
                    $("#deleteConfirmModal").modal("show");
                } else {
                    $("#roomNumberWarning").text(roomName);
                    $("#deleteWarningModal").modal("show");
                }
            },
            error: function(xhr, status, error) {
                console.error("Lỗi kiểm tra xóa:", error);
            }
        });
}
</script>
