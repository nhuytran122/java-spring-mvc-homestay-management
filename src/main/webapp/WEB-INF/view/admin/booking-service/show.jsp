<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý đặt dịch vụ</title>
    <jsp:include page="../layout/import-css.jsp" />
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>
    <div class="container-scroller">
        <jsp:include page="../layout/header.jsp" />
        <div class="container-fluid page-body-wrapper">
            <jsp:include page="../layout/theme-settings.jsp" />
            <jsp:include page="../layout/sidebar.jsp" />
            <div class="main-panel">
                <div class="search-form-container my-4">
                    <form action="/admin/booking-service" method="get" class="search-form">
                        <input type="text" class="form-control form-control-sm"
                               name="keyword" 
                               placeholder="Tìm kiếm đặt dịch vụ (tên dịch vụ)..." 
                               value="${criteria.keyword}"/>
                        <select name="isPrepaid" class="form-select form-control form-select-sm">
                            <option value="">Chọn phân loại</option>
                            <option value="true" ${true == criteria.isPrepaid ? 'selected' : ''}>
                                Đặt trước
                            </option>
                            <option value="false" ${true == criteria.isPrepaid ? 'selected' : ''}>
                                Đặt tại homestay
                            </option>
                        </select>

                        <select name="status" class="form-select form-control form-select-sm">
                            <option value="" ${criteria.status == null || criteria.status == '' ? 'selected' : ''}>
                                Tất cả tình trạng
                            </option>
                            <c:forEach var="type" items="${statuses}">
                                <option value="${type}" ${criteria.status == type ? 'selected' : ''}>
                                    ${type.displayName} 
                                </option>
                            </c:forEach>
                        </select>
                        <select name="sort" class="form-select form-control form-select-sm">
                            <option value="asc" ${criteria.sort == 'asc' ? 'selected' : ''}>
                                Cũ nhất
                            </option>
                            <option value="desc" ${criteria.sort == 'desc' ? 'selected' : ''}>
                                Mới nhất
                            </option>
                        </select>
                        <button type="submit" class="btn btn-primary btn-sm p-2">
                            <i class="bi bi-search"></i>
                        </button>
                    </form>
                </div>
                
                <div class="content-wrapper">
                    <div class="row">
                        <div class="col-md-12 grid-margin stretch-card">
                            <div class="card position-relative">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h4 class="card-title">Danh sách đặt dịch vụ</h4>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-hover">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Mã đặt phòng</th>
                                                    <th>Tên khách hàng</th>
                                                    <th>Chi nhánh</th>
                                                    <th>Phòng</th>
                                                    <th>Dịch vụ</th>
                                                    <th>Số lượng</th>
                                                    <th>Tình trạng</th>
                                                    <th>Ngày đặt</th>
                                                    <th>Tình trạng thanh toán</th>
                                                    <th>Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty listBookingServices}">
                                                        <tr>
                                                            <td colspan="12" class="text-center text-danger">Không tìm thấy phần đặt dịch vụ nào.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="bookingService" items="${listBookingServices}">
                                                            <c:set
                                                                var="status"
                                                                value="${bookingService.status}"
                                                                />
                                                            <tr style="height: 70px;">
                                                                <c:set var="booking" value="${bookingService.booking}"></c:set>
                                                                <c:set var="bookingServiceID" value="${bookingService.bookingServiceID}"></c:set>
                                                                <td>${booking.bookingID}</td>
                                                                <td>${booking.customer.user.fullName}</td>
                                                                <td>${booking.room.branch.branchName}</td>
                                                                <td>${booking.room.roomNumber}</td>
                                                                <td>${bookingService.service.serviceName}</td>
                                                                <td>
                                                                    <fmt:formatNumber type="number" value="${bookingService.quantity}" pattern="#"/>
                                                                </td>
                                                                <td>
                                                                <span class="badge ${status == 'COMPLETED' ? 'bg-success' : 
                                                                                    status == 'PENDING' ? 'bg-secondary' : 
                                                                                    status == 'IN_PROGRESS' ? 'bg-warning' : 
                                                                                    status == 'CANCELLED' ? 'bg-danger' : 'bg-info'}">
                                                                    ${status.displayName}
                                                                </span>
                                                            </td>
                                                                <td>${f:formatLocalDateTime(bookingService.createdAt)}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${booking.status == 'CANCELLED'}">
                                                                        <span class="badge bg-danger">Đã hủy</span>
                                                                        </c:when>
                                                                        <c:when test="${not empty bookingService.paymentDetail}">
                                                                            <c:set var="paymentStatus" value="${bookingService.paymentDetail.payment.status}" />
                                                                            <span class="badge 
                                                                                ${paymentStatus == 'COMPLETED' ? 'bg-success' : 
                                                                                    paymentStatus == 'FAILED' ? 'bg-danger' : 
                                                                                    paymentStatus == 'PENDING' ? 'bg-primary' : 'bg-info'}">
                                                                                ${paymentStatus.displayName}
                                                                            </span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span class="badge bg-secondary">Đang chờ</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    </td>

                                                                
                                                                <td>
                                                                    <div class="btn-group" role="group">
                                                                        <a href="/admin/booking/${booking.bookingID}" class="btn btn-success btn-sm" title="Xem chi tiết đặt phòng liên quan">
                                                                            <i class="bi bi-eye"></i>
                                                                        </a>
                                                                        <button class="btn btn-warning btn-sm" title="Sửa"
                                                                            onclick="checkBeforeUpdate(this)" 
                                                                                data-booking-service-id="${bookingServiceID}"
                                                                                data-entity-type="Đơn đặt dịch vụ"
                                                                                data-check-url="/admin/booking-service/can-handle/" >
                                                                            <i class="bi bi-pencil"></i>
                                                                        </button>
                                                                        <button class="btn btn-info btn-sm status-update-btn" 
                                                                            data-booking-service-id="${bookingServiceID}" 
                                                                            data-current-status="${bookingService.status}"
                                                                            title="Cập nhật trạng thái">
                                                                            <i class="bi bi-gear"></i>
                                                                        </button>
                                                                        <button
                                                                            class="btn btn-danger btn-sm"
                                                                            title="Xóa"
                                                                            onclick="checkBeforeDelete(this)"
                                                                            data-entity-id="${bookingServiceID}"
                                                                            data-entity-name="${bookingService.service.serviceName}"
                                                                            data-entity-type="Đơn đặt dịch vụ"
                                                                            data-delete-url="/admin/booking-service/delete"
                                                                            data-check-url="/admin/booking-service/can-handle/"
                                                                            data-id-name="bookingServiceID"
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

                    <jsp:include page="../layout/partial/_pagination-with-param.jsp">
                        <jsp:param name="url" value="/admin/booking-service" />
                        <jsp:param name="currentPage" value="${currentPage}" />
                        <jsp:param name="totalPages" value="${totalPages}" />
                        <jsp:param name="extraParams" value="${extraParams}" />
                    </jsp:include>
                </div>
                <jsp:include page="../layout/footer.jsp" />
            </div>
        </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include page="../layout/partial/_modals-delete.jsp" />
    <jsp:include page="_script-modal-warning-update.jsp" />
    <jsp:include page="_script-modal-update-status.jsp" />
    <script>
      $("#deleteWarningModal").on("show.bs.modal", function () {

        $(this)
          .find(".modal-body")
          .html(
            'Dịch vụ này đã được thanh toán, không thể xóa.'
          );
      });
    </script>
</body>
</html>