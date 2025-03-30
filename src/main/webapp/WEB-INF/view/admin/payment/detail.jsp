<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ taglib prefix="f"
uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>Chi tiết thanh toán</title>
    <jsp:include page="../layout/import-css.jsp" />
  </head>

  <body>
    <div class="container-scroller">
      <jsp:include page="../layout/header.jsp" />
      <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
          <div class="content-wrapper">
            <div class="row">
              <div class="col-md-12 grid-margin stretch-card">
                <div class="card position-relative">
                  <div class="card-body">
                    <div
                      class="row mb-3 d-flex justify-content-between align-items-center"
                    >
                      <div class="col-md-6">
                        <h4 class="card-title mb-0">Chi tiết thanh toán</h4>
                      </div>
                      <div class="col-md-6 text-end">
                        <div class="btn-group">
                          <a
                            href="/admin/payment/update/${payment.paymentID}"
                            class="btn btn-warning btn-sm"
                          >
                            <i class="bi bi-pencil"></i> Sửa
                          </a>
                          <a
                            href="/admin/payment"
                            class="btn btn-secondary btn-sm"
                          >
                            <i class="bi bi-arrow-left"></i> Quay lại danh sách
                          </a>
                        </div>
                      </div>
                    </div>

                    <div class="row">
                      <div class="col-md-12">
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Trạng thái:
                          </div>
                          <div class="col-md-8">
                            <span
                              class="badge ${payment.status == 'COMPLETED' ? 'bg-success' : payment.status == 'FAILED' ? 'bg-danger' : payment.status == 'PENDING' ? 'bg-primary' : 'bg-info'}"
                            >
                              ${payment.status.displayName}
                            </span>
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Mã giao dịch:
                          </div>
                          <div class="col-md-8">
                            ${payment.vnpTransactionNo}
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Số hóa đơn:
                          </div>
                          <div class="col-md-8">${payment.vnpTxnRef}</div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Ngày thanh toán:
                          </div>
                          <div class="col-md-8">
                            ${f:formatLocalDateTime(payment.paymentDate)}
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Tổng tiền:
                          </div>
                          <div class="col-md-8">
                            <fmt:formatNumber
                              type="number"
                              value="${payment.totalAmount}"
                            />đ
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Đặt phòng:
                          </div>
                          <div class="col-md-8">
                            <a
                              href="/admin/booking/${payment.booking.bookingID}"
                            >
                              Đặt phòng #${payment.booking.bookingID}
                            </a>
                          </div>
                        </div>
                        <div class="row mb-3 d-flex align-items-center">
                          <div class="col-md-4 fw-bold text-md-start">
                            Loại thanh toán:
                          </div>
                          <div class="col-md-8">
                            <span
                              class="badge ${payment.paymentType == 'TRANSFER' ? 'bg-success' : payment.paymentType == 'CASH'}"
                            >
                              ${payment.paymentType.displayName}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div class="row mt-4">
                      <div class="col-md-12">
                        <h5 class="mb-3">Danh sách chi tiết thanh toán</h5>
                        <c:choose>
                          <c:when test="${not empty payment.paymentDetails}">
                            <div class="table-responsive">
                              <table class="table table-bordered table-hover">
                                <thead>
                                  <tr>
                                    <th>Mục đích thanh toán</th>
                                    <th>Số tiền gốc</th>
                                    <th>Số tiền cuối (đã giảm giá)</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  <c:forEach
                                    var="detail"
                                    items="${payment.paymentDetails}"
                                  >
                                    <tr>
                                      <td>
                                        ${detail.paymentPurpose.description}
                                      </td>
                                      <td>
                                        <fmt:formatNumber
                                          type="number"
                                          value="${detail.baseAmount}"
                                        />đ
                                      </td>
                                      <td>
                                        <fmt:formatNumber
                                          type="number"
                                          value="${detail.finalAmount}"
                                        />đ
                                      </td>
                                    </tr>
                                  </c:forEach>
                                </tbody>
                              </table>
                            </div>
                          </c:when>
                          <c:otherwise>
                            <p class="text-muted">
                              Không có chi tiết thanh toán nào.
                            </p>
                          </c:otherwise>
                        </c:choose>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/partial/_modals-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
  </body>
</html>
