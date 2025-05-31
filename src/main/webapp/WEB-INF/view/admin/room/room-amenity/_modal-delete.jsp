<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div
  class="modal fade"
  id="deleteAmenityConfirmModal"
  tabindex="-1"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title text-danger">
          <i class="bi bi-exclamation-triangle-fill me-2"></i> Xác nhận xóa
        </h5>
      </div>
      <div class="modal-body">
        Bạn có chắc chắn muốn xóa tiện nghi
        <b class="text-primary" id="titleConfirm"> </b> khỏi phòng không?
      </div>
      <div class="modal-footer">
        <form action="/admin/room/room-amenity/delete" method="post">
          <input
            type="hidden"
            name="${_csrf.parameterName}"
            value="${_csrf.token}"
          />
          <input
            type="hidden"
            name="roomAmenityId.roomId"
            id="roomAmenityIdInput"
          />
          <input
            type="hidden"
            name="roomAmenityId.amenityId"
            id="amenityIdInput"
          />
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Hủy
          </button>
          <button type="submit" class="btn btn-danger">Xóa</button>
        </form>
      </div>
    </div>
  </div>
</div>
