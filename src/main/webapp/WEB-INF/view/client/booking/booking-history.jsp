<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch sử đặt phòng - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/booking-history-style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
</head>
<body>
    <jsp:include page="../layout/header.jsp" />

    <jsp:include page="../layout/partial/_welcome-banner-profile.jsp" />
 
    <div class="container mb-4">
        <div class="search-bar p-3 bg-white rounded shadow-sm">
            <form class="row g-3">
                <div class="col-md-4">
                    <label class="form-label">Khoảng thời gian check-in</label>
                    <input type="text" id="timeRange" name="timeRange" class="form-control daterange-picker" 
                               value="${criteria.timeRange}" placeholder="Chọn khoảng thời gian...">
                </div>

                <div class="col-md-3">
                    <label class="form-label">Chi nhánh</label>
                    <c:set var="cBranchId" value="${criteria.branchId}" />
                    <select name="branchId" class="form-select">
                        <option value="">Chọn chi nhánh</option>
                        <c:forEach var="branch" items="${listBranches}">
                            <c:set var="fBranchId" value="${branch.branchId}" />
                            <option value="${fBranchId}" ${fBranchId == cBranchId ? 'selected' : ''}>
                                ${branch.branchName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Loại phòng</label>
                    <c:set var="cRoomTypeId" value="${criteria.roomTypeId}" />
                    <select name="roomTypeId" class="form-select">
                        <option value="">Chọn loại phòng</option>
                        <c:forEach var="roomType" items="${listRoomTypes}">
                            <c:set var="rTypeId" value="${roomType.roomTypeId}"/>
                            <option value="${rTypeId}" ${rTypeId == cRoomTypeId ? 'selected' : ''}>
                                ${roomType.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Tình trạng</label>
                    <c:set var="cStatus" value="${criteria.status}" />
                    <select name="status" class="form-select">
                        <option value="" ${cStatus == null || cStatus == '' ? 'selected' : ''}>
                            Tất cả tình trạng
                        </option>
                        <c:forEach var="type" items="${bookingStatuses}">
                            <option value="${type}" ${cStatus == type ? 'selected' : ''}>
                                ${type.displayName} 
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-1 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100"><i class="bi bi-search"></i></button>
                </div>
            </form>
        </div>
    </div>

    <div class="container main-content">
        <div class="row mb-4 g-3">
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Tất cả</p>
                                <h3 class="mb-0">${countTotal}</h3>
                            </div>
                            <div class="stat-icon bg-primary-subtle">
                                <i class="bi bi-calendar-check text-primary"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Đang chờ</p>
                                <h3 class="mb-0">${countPending}</h3>
                            </div>
                            <div class="stat-icon bg-success-subtle">
                                <i class="bi bi-check-circle text-success"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Đã xác nhận</p>
                                <h3 class="mb-0">${countConfirmed}</h3>
                            </div>
                            <div class="stat-icon bg-success-subtle">
                                <i class="bi bi-check-circle text-success"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Đã hoàn tất</p>
                                <h3 class="mb-0">${countCompleted}</h3>
                            </div>
                            <div class="stat-icon bg-info-subtle">
                                <i class="bi bi-check-circle-fill"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-2">
                <div class="card stat-card border-0">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-muted mb-1">Đã hủy</p>
                                <h3 class="mb-0">${countCancelled}</h3>
                            </div>
                            <div class="stat-icon bg-danger-subtle">
                                <i class="bi bi-x-circle text-danger"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card booking-card mb-4">
            <div class="card-header bg-transparent d-flex justify-content-between align-items-center">
                <h2>Lịch đặt phòng</h2>
            </div>
            <c:choose>
                <c:when test="${empty listBookings}">
                   <div class="text-center text-danger p-4">Không tìm thấy lịch đặt phòng nào.</div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="booking" items="${listBookings}">   
                        <c:set var="bookingId" value="${booking.bookingId}"/>         
                        <c:set var="bRoom" value="${booking.room}"/>  
                        <c:set var="totalAmount" value="${booking.totalAmount}"/>
                        <c:set var="paidAmount" value="${booking.paidAmount}"/>       
                        <div class="card-body">
                            <div class="upcoming-booking">
                                <c:choose>
                                    <c:when test="${not empty bRoom.thumbnail}">
                                        <img src="/images/room/${bRoom.thumbnail}" class="booking-image">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="/images/room/default-img.jpg" class="booking-image">
                                    </c:otherwise>
                                </c:choose>                                
                                <div class="booking-details">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div class="d-flex align-items-center gap-2">
                                            <c:set var="bStatus" value="${booking.status}"/>
                                            <span class="badge ${bStatus == 'COMPLETED' ? 'bg-success' : 
                                                                bStatus == 'CANCELLED' ? 'bg-danger' : 
                                                                bStatus == 'CONFIRMED' ? 'bg-primary' : 'bg-info'}">
                                                ${bStatus.displayName}
                                            </span>   
                                            <c:if test="${not empty booking.payments}">
                                                <c:if test="${bStatus == 'CANCELLED'}">
                                                    <span class="badge ${totalAmount == paidAmount ? 'bg-danger' : 'bg-warning'}">
                                                        ${totalAmount == paidAmount ? 'Đang chờ hoàn tiền' : 'Đã hoàn tiền'}
                                                    </span>                 
                                                </c:if>
                                            </c:if>
                                        </div>
                                    
                                        <h3>Phòng ${bRoom.roomNumber} - ${bRoom.roomType.name}</h3>
                                    </div>
                                    <c:set var="branch" value="${bRoom.branch}"/>
                                    <p class="location"><i class="bi bi-geo-alt"></i> ${branch.branchName} - ${branch.address}</p>
                                    
                                    <div class="booking-info">
                                        <span><i class="bi bi-calendar3"></i> Check-in: ${f:formatLocalDateTime(booking.checkIn)}</span>
                                        <span><i class="bi bi-calendar3"></i> Check-out: ${f:formatLocalDateTime(booking.checkOut)}</span>
                                        <span><i class="bi bi-credit-card"></i> Tổng tiền: <fmt:formatNumber type="number" value="${totalAmount}" />đ</span>
                                        <span><i class="bi bi-credit-card"></i> Số tiền đã trả: <fmt:formatNumber type="number" value="${paidAmount != null ? paidAmount : 0}" />đ</span>
                                    </div>

                                    <c:if test="${not empty booking.bookingServices}">
                                        <div class="booking-services mt-3">
                                            <h5 class="mb-2">Dịch vụ đi kèm:</h5>
                                            <ul class="list-group">
                                                <c:forEach var="serviceItem" items="${booking.bookingServices}">
                                                    <c:set var="serviceOfBService" value="${serviceItem.service}"/>
                                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                                        <div>
                                                            <strong>${serviceOfBService.serviceName}</strong> 
                                                            (<c:choose>
                                                                <c:when test="${serviceItem.quantity != null}">
                                                                    x<fmt:formatNumber type="number" value="${serviceItem.quantity}" pattern="#"/> / ${serviceOfBService.unit}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="text-muted">đang chờ cập nhật</span>
                                                                </c:otherwise>
                                                            </c:choose> )
                                                        </div>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </c:if>
                                    <div class="booking-actions mt-3">
                                        <a href="/booking/booking-history/${bookingId}" class="btn btn-outline-primary"><i class="bi bi-arrow-up-right-square"></i> Xem chi tiết</a>
                                        <c:if test="${bStatus == 'PENDING'}">
                                            <a onclick="handlePayment('${bookingId}', 'ROOM_BOOKING', true)" class="btn btn-primary ms-2">
                                                <i class="bi bi-credit-card"></i> Thanh toán
                                            </a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
        <c:set var="customerType" value="${customer.customerType}"/>         
        <c:if test="${customerType.discountRate > 0}">
            <div class="special-offers mb-5">
                <h2 class="section-title mb-4">Ưu đãi đặc biệt</h2>
                <div class="alert alert-info d-flex align-items-center" role="alert">
                    <i class="bi bi-gift-fill me-2"></i>
                    <div>
                        Bạn đang là thành viên hạng <strong>${customerType.name}</strong>, được giảm <strong>
                        <fmt:formatNumber value="${discountRate}" pattern="#"/>%</strong> cho mỗi lần đặt phòng!
                    </div>
                </div>
            </div> 
        </c:if>       
    </div>

    <jsp:include page="../../admin/layout/partial/_pagination-with-param.jsp">
        <jsp:param name="url" value="/booking/booking-history" />
        <jsp:param name="currentPage" value="${currentPage}" />
        <jsp:param name="totalPages" value="${totalPages}" />
    </jsp:include>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
    <script>
        $(document).ready(function () {
            $('.daterange-picker').daterangepicker({
                locale: {
                    format: 'DD/MM/YYYY'
                },
            })
        });
    </script>
    <jsp:include page="../layout/partial/_payment-handler.jsp" />
</body>
</html>