<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="bookServicesModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="margin-top: 1%;">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Chọn dịch vụ để đặt</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="alert alert-info mb-3" role="alert">
                    <i class="bi bi-info-circle me-2"></i>
                    Lưu ý: Giá dịch vụ cụ thể tùy thuộc vào lượng sử dụng. Nhân viên sẽ báo giá cụ thể trực tiếp khi bạn sử dụng dịch vụ.
                </div>
                <form>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Chọn</th>
                                <th>Tên dịch vụ</th>
                                <th>Giá</th>
                                <th>Mô tả cụ thể yêu cầu</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${listServicesPostPay}">
                                <tr>
                                    <td>
                                        <input type="checkbox" class="item-checkbox" 
                                            data-service-id="${item.serviceID}"
                                            data-booking-id="${booking.bookingID}">
                                    </td>
                                    <td>
                                        <span class="iconify" data-icon="${item.icon}"></span>
                                        <b>${item.serviceName}</b>
                                    </td>
                                    <td>
                                        <fmt:formatNumber
                                          type="number"
                                          value="${item.price}"
                                        />đ / ${item.unit}
                                    </td>
                                    <td>
                                        <textarea type="text" class="form-control service-description"
                                        data-service-id="${item.serviceID}"> </textarea>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </form>
            </div>            
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary" onclick="saveSelectedBookingServices()">Đặt dịch vụ</button>
            </div>
        </div>
    </div>
</div>