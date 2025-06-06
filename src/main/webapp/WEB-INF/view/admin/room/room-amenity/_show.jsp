<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="row">
  <div class="col-md-12 grid-margin stretch-card">
    <div class="card position-relative">
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center mb-4">
          <h4 class="card-title">
            Danh sách tiện nghi trong phòng ${room.roomNumber}
          </h4>
          <button
            type="button"
            class="btn btn-primary btn-sm"
            data-bs-toggle="modal"
            data-bs-target="${empty listAmenitiesNotInRoom ? '#noAmenitiesModal' : '#addRoomAmenityModal'}"
          >
            <i class="bi bi-plus-circle"></i>
            Thêm tiện nghi
          </button>
        </div>

        <div class="table-responsive">
          <table class="table table-hover">
            <thead class="table-light">
              <tr>
                <th>Tên</th>
                <th>Phân loại</th>
                <th>Số lượng</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <c:choose>
                <c:when test="${empty listAmenities}">
                  <tr>
                    <td colspan="7" class="text-center text-danger">
                      Hiện phòng ${room.roomNumber} không có tiện nghi nào
                    </td>
                  </tr>
                </c:when>
                <c:otherwise>
                  <c:forEach var="roomAmenity" items="${listAmenities}">
                    <tr>
                      <td>${roomAmenity.amenity.amenityName}</td>
                      <td>
                        <i
                          class="fas ${roomAmenity.amenity.amenityCategory.icon} amenity-icon me-2"
                        ></i>
                        ${roomAmenity.amenity.amenityCategory.categoryName}
                      </td>
                      <td>${roomAmenity.quantity}</td>
                      <td>
                        <div class="btn-group" role="group">
                          <button
                            class="btn btn-warning btn-sm"
                            onclick="openEditModal(this)"
                            data-amenity-id="${roomAmenity.amenity.amenityId}"
                            data-room-id="${room.roomId}"
                            data-amenity-name="${roomAmenity.amenity.amenityName}"
                            data-amenity-quantity="${roomAmenity.quantity}"
                          >
                            <i class="bi bi-pencil"></i>
                          </button>
                          <button
                            class="btn btn-danger btn-sm"
                            onclick="checkBeforeDeleteAmenity(this)"
                            data-amenity-id="${roomAmenity.amenity.amenityId}"
                            data-amenity-name="${roomAmenity.amenity.amenityName}"
                            data-room-id="${room.roomId}"
                          >
                            <i class="bi bi-trash"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
