<%@page contentType="text/html" pageEncoding="UTF-8" %>

<div class="modal fade" id="updateRuleModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Chỉnh sửa quy tắc</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="editRuleId">
                <div class="mb-3">
                    <label for="editRuleTitle" class="form-label">Nguyên tắc</label>
                    <input type="text" disabled class="form-control" id="editRuleTitle">
                </div>
                <div class="mb-3">
                    <label for="editRuleDescription" class="form-label">Mô tả</label>
                    <input type="text" class="form-control" id="editRuleDescription">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-warning" onclick="updateRule()">Lưu</button>
            </div>
        </div>
    </div>
</div>