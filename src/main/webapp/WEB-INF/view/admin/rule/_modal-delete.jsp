<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-danger">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> Xác nhận xóa
                </h5>
            </div>
            <div class="modal-body">
                Bạn có chắc chắn muốn xóa quy tắc <b class="text-primary" id="titleConfirm"></b> không?
            </div>
            <div class="modal-footer">
                <form action="/admin/homestay-infor/rule/delete" method="post">
                    <input type="hidden" name="ruleID" id="ruleIdInput">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger">Xóa</button>
                </form>
            </div>
        </div>
    </div>
</div>