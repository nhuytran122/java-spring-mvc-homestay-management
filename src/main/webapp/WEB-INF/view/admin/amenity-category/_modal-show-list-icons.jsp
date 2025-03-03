<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="iconModal" tabindex="-1" aria-labelledby="iconModalLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="iconModalLabel">Chọn Icon</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="search-container" style="padding: 10px 0;">
                    <input type="text" id="iconSearch" class="form-control" placeholder="Tìm kiếm icon..." style="width: 100%;">
                </div>
                <div class="icon-picker-content" style="max-height: 300px; overflow-y: auto; padding: 10px; display: flex; flex-wrap: wrap; gap: 10px;">
                    <c:forEach var="icon" items="${iconList}">
                        <div class="icon-option" data-icon-name="${icon}" style="cursor: pointer; text-align: center; padding: 5px; border: 1px solid #dee2e6; border-radius: 4px;">
                            <span class="iconify" data-icon="${icon}" data-width="24" data-height="24"></span>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>