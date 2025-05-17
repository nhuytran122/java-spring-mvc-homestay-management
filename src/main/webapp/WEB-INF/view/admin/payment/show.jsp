<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý thanh toán</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
</head>
<body>
    <div class="container-scroller">
        <jsp:include page="../layout/header.jsp" />
        <div class="container-fluid page-body-wrapper">
            <jsp:include page="../layout/theme-settings.jsp" />
            <jsp:include page="../layout/sidebar.jsp" />
            <div class="main-panel">
                <div class="search-form-container my-4">
                    <form action="/admin/payment" method="get" class="search-form">
                        <select name="status" class="form-select form-control form-select-sm">
                            <option value="" ${criteria.status == null || criteria.status == '' ? 'selected' : ''}>
                                Tất cả tình trạng
                            </option>
                            <c:forEach var="status" items="${paymentStatuses}">
                                <option value="${status}" ${criteria.status == status ? 'selected' : ''}>
                                    ${status.displayName} 
                                </option>
                            </c:forEach>
                        </select>
                        <select name="type" class="form-select form-control form-select-sm">
                            <option value="" ${criteria.type == null || criteria.type == '' ? 'selected' : ''}>
                                Tất cả hình thức
                            </option>
                            <c:forEach var="type" items="${paymentTypes}">
                                <option value="${type}" ${criteria.type == type ? 'selected' : ''}>
                                    ${type.displayName} 
                                </option>
                            </c:forEach>
                        </select>
                        <input type="text" id="timeRange" name="timeRange" class="form-control daterange-picker" 
                               value="${criteria.timeRange}" placeholder="Chọn khoảng thời gian...">
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
                                        <h4 class="card-title">Danh sách thanh toán</h4>
                                        <a href="/admin/payment/create" class="btn btn-primary btn-sm">
                                            <i class="bi bi-plus-circle"></i> Thêm mới
                                        </a>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-hover">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Mã đặt phòng</th>
                                                    <th>Tên khách hàng</th>
                                                    <th>Hình thức thanh toán</th>
                                                    <th>Tình trạng</th>
                                                    <th>Ngày thanh toán</th>
                                                    <th>Tổng tiền</th>
                                                    <th>Mã giao dịch</th>
                                                    <th>Số hóa đơn</th>
                                                    <th>Thao tác</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:choose>
                                                    <c:when test="${empty listPayments}">
                                                        <tr>
                                                            <td colspan="12" class="text-center text-danger">Không tìm thấy lịch sử thanh toán nào.</td>
                                                        </tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="payment" items="${listPayments}">
                                                            <tr style="height: 70px;">
                                                                <td>${payment.booking.bookingID}</td>
                                                                <td>${payment.booking.customer.user.fullName}</td>
                                                                <td>
                                                                    <span class="badge ${payment.paymentType == 'TRANSFER' ? 'bg-success' : 
                                                                                        payment.paymentType == 'CASH' ? 'bg-info' : 'bg-secondary'}">
                                                                        ${payment.paymentType == 'TRANSFER' ? 'Chuyển khoản' : 
                                                                            payment.paymentType == 'CASH' ? 'Tiền mặt' : 'Chưa xác định'}
                                                                    </span>
                                                                </td>

                                                                <td>
                                                                    <span class="badge ${payment.status == 'COMPLETED' ? 'bg-success' : 
                                                                                        payment.status == 'FAILED' ? 'bg-danger' : 
                                                                                        payment.status == 'PENDING' ? 'bg-primary' : 'bg-info'}">
                                                                        ${payment.status.displayName}
                                                                    </span>
                                                                </td>
                                                                <td>${f:formatLocalDateTime(payment.paymentDate)}</td>
                                                                
                                                                <td><fmt:formatNumber type="number" value="${payment.totalAmount != null ? payment.totalAmount : 0}" />đ</td>
                                                                <td>${payment.vnpTransactionNo}</td>
                                                                <td>${payment.vnpTxnRef}</td>
                                                                <td>
                                                                    <div class="btn-group" role="group">
                                                                        <a href="/admin/payment/${payment.paymentID}" class="btn btn-success btn-sm" title="Xem chi tiết">
                                                                            <i class="bi bi-eye"></i>
                                                                        </a>
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
                        <jsp:param name="url" value="/admin/payment" />
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
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
    <script>
        $(document).ready(function () {
            
            $('.daterange-picker').daterangepicker({
                locale: {
                    format: 'DD/MM/YYYY',
                    daysOfWeek: ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
                monthNames: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12']
                },
            })
        });
    </script>
</body>
</html>