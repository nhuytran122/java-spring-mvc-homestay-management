<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="addRoomAmenityModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="margin-top: 1%;">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Chọn tiện nghi để thêm</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="addRoomAmenityForm">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Chọn</th>
                                <th>Tiện nghi</th>
                                <th>Số lượng</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${listAmenitiesNotInRoom}">
                                <tr>
                                    <td>
                                        <input type="checkbox" class="item-checkbox" 
                                            data-amenity-id="${item.amenityID}"
                                            data-room-id="${room.roomID}">
                                    </td>
                                    <td>
                                        <i class="fas ${item.amenityCategory.icon} rule-icon me-2"></i> 
                                        <b>${item.amenityName}</b>
                                    </td>
                                    <td>
                                        <input type="number" class="form-control amenity-quantity"
                                               data-amenity-id="${item.amenityID}">
                                               <div class="invalid-feedback">
                                                Số lượng phải là số nguyên dương!
                                            </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </form>
            </div>            
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary" onclick="saveSelectedAmenities()">Lưu</button>
            </div>
        </div>
    </div>
</div>