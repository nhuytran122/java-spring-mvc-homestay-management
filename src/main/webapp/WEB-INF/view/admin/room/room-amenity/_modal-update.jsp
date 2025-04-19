<%@page contentType="text/html" pageEncoding="UTF-8" %>

<div
  class="modal fade"
  id="updateRoomAmenityModal"
  tabindex="-1"
  aria-hidden="true"
>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">
          Chỉnh sửa số lượng tiện nghi phòng ${room.roomNumber}
        </h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
          aria-label="Close"
        ></button>
      </div>
      <div class="modal-body">
        <input type="hidden" id="editAmenityId" />
        <input type="hidden" id="editRoomId" />
        <div class="mb-3">
          <label for="editAmenityName" class="form-label">Tên tiện nghi</label>
          <input
            type="text"
            disabled
            class="form-control"
            id="editAmenityName"
          />
        </div>
        <div class="mb-3">
          <label for="editAmenityQuantity" class="form-label">Số lượng</label>
          <input
            type="number"
            min="1"
            class="form-control"
            id="editAmenityQuantity"
          />
          <div
            id="quantityError"
            class="text-danger mt-2"
            style="display: none"
          ></div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
          Hủy
        </button>
        <button
          type="button"
          class="btn btn-warning"
          onclick="updateRoomAmenity()"
        >
          Sửa
        </button>
      </div>
    </div>
  </div>
</div>
