<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>Dashboard - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css"
    />

    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
    <link rel="stylesheet" href="/admin/css/dashboard.css" />
  </head>
  <body>
    <div class="container-scroller">
      <jsp:include page="../layout/header.jsp" />

      <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />

        <div class="main-panel">
          <div class="content-wrapper">
            <h4 class="card-title mb-4">Dashboard</h4>
            <c:set var="totalRevenue" value="${stats.totalRevenue}" />
            <c:set var="countBookings" value="${stats.countBookings}" />
            <c:set
              var="countPendingBookingServices"
              value="${stats.countPendingBookingServices}"
            />
            <c:set var="countReviews" value="${stats.countReviews}" />
            <c:set
              var="roomRevenue"
              value="${stats.revenueBreakdown.roomRevenue}"
            />
            <c:set
              var="serviceRevenue"
              value="${stats.revenueBreakdown.serviceRevenue}"
            />
            <c:set
              var="extensionRevenue"
              value="${stats.revenueBreakdown.extensionRevenue}"
            />
            <c:set var="topServices" value="${stats.topServices}" />
            <c:set var="topRooms" value="${stats.topRooms}" />
            <c:set var="topCustomers" value="${stats.topCustomers}" />

            <div class="filter-form">
              <form action="/admin/dashboard" method="get" class="form-inline">
                <div class="form-group mr-3">
                  <label for="timeRange" class="mr-2">Khoảng thời gian:</label>
                  <input
                    type="text"
                    id="timeRange"
                    name="timeRange"
                    class="form-control daterange-picker"
                    value="${timeRange}"
                    placeholder="Chọn khoảng thời gian..."
                  />
                </div>
                <button type="submit" class="btn btn-primary">Lọc</button>
              </form>
            </div>

            <div class="row">
              <div class="col-md-3 col-sm-6">
                <div class="stat-card total-revenue position-relative">
                  <a
                    href="/admin/report?timeRange=${timeRange}"
                    class="stretched-link"
                  ></a>
                  <i class="fas fa-wallet icon"></i>
                  <h5>Tổng doanh thu</h5>
                  <p>
                    <c:choose>
                      <c:when test="${totalRevenue == 0}">
                        <span class="text-muted">Không có dữ liệu</span>
                      </c:when>
                      <c:otherwise>
                        <fmt:formatNumber
                          type="number"
                          value="${totalRevenue}"
                        />đ
                      </c:otherwise>
                    </c:choose>
                  </p>
                </div>
              </div>

              <div class="col-md-3 col-sm-6">
                <div class="stat-card room-revenue position-relative">
                  <a
                    href="/admin/booking?timeRange=${timeRange}"
                    class="stretched-link"
                  ></a>
                  <i class="fas fa-bed icon"></i>
                  <h5>Đơn đặt phòng</h5>
                  <p>${countBookings}</p>
                </div>
              </div>

              <div class="col-md-3 col-sm-6">
                <div class="stat-card service-revenue position-relative">
                  <a
                    href="/admin/booking-service?status=PENDING"
                    class="stretched-link"
                  ></a>
                  <i class="fas fa-spinner icon"></i>
                  <h5>Đơn đặt dịch vụ đang chờ</h5>
                  <p>${countPendingBookingServices}</p>
                </div>
              </div>

              <div class="col-md-3 col-sm-6">
                <div class="stat-card review-revenue position-relative">
                  <a href="/admin/review" class="stretched-link"></a>
                  <i class="fas fa-bed icon"></i>
                  <h5>Đánh giá</h5>
                  <p>${countReviews}</p>
                </div>
              </div>
            </div>

            <div
              style="display: none"
              id="revenue-data"
              data-room="${roomRevenue}"
              data-service="${serviceRevenue}"
              data-extension="${extensionRevenue}"
              data-total-revenue="${totalRevenue}"
            ></div>

            <div class="row mt-4">
              <div class="col-md-6">
                <div class="card">
                  <div class="card-body">
                    <h5 class="card-title">Phân Bổ Doanh Thu</h5>
                    <div class="chart-container">
                      <canvas id="revenueBreakdownChart"></canvas>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="card">
                  <div class="card-body">
                    <h5 class="card-title">Phân Tích Doanh Thu</h5>
                    <div class="table-responsive mt-3">
                      <table class="table">
                        <thead>
                          <tr>
                            <th>Nguồn Thu</th>
                            <th>Doanh Thu</th>
                            <th>Tỷ Lệ</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>
                              <span class="dot bg-success mr-2"></span> Phòng
                            </td>
                            <td>
                              <fmt:formatNumber
                                type="number"
                                value="${roomRevenue}"
                              />đ
                            </td>
                            <td>
                              <c:choose>
                                <c:when test="${totalRevenue > 0}">
                                  <fmt:formatNumber
                                    type="percent"
                                    maxFractionDigits="1"
                                    value="${roomRevenue / totalRevenue}"
                                  />
                                </c:when>
                                <c:otherwise>0%</c:otherwise>
                              </c:choose>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <span class="dot bg-warning mr-2"></span> Dịch vụ
                            </td>
                            <td>
                              <fmt:formatNumber
                                type="number"
                                value="${serviceRevenue}"
                              />đ
                            </td>
                            <td>
                              <c:choose>
                                <c:when test="${totalRevenue > 0}">
                                  <fmt:formatNumber
                                    type="percent"
                                    maxFractionDigits="1"
                                    value="${serviceRevenue / totalRevenue}"
                                  />
                                </c:when>
                                <c:otherwise>0%</c:otherwise>
                              </c:choose>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <span class="dot bg-primary mr-2"></span> Gia hạn
                            </td>
                            <td>
                              <fmt:formatNumber
                                type="number"
                                value="${extensionRevenue}"
                              />đ
                            </td>
                            <td>
                              <c:choose>
                                <c:when test="${totalRevenue > 0}">
                                  <fmt:formatNumber
                                    type="percent"
                                    maxFractionDigits="1"
                                    value="${extensionRevenue / totalRevenue}"
                                  />
                                </c:when>
                                <c:otherwise>0%</c:otherwise>
                              </c:choose>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="row mt-4">
              <div class="col-md-4 stretch-card grid-margin">
                <div class="card">
                  <div class="card-body">
                    <p class="card-title mb-2">Phòng phổ biến nhất</p>
                    <div class="table-responsive">
                      <table class="table table-striped table-borderless">
                        <thead>
                          <tr>
                            <th class="pl-0 pb-2 border-bottom">Phòng</th>
                            <th class="border-bottom pb-2">Chi nhánh</th>
                            <th class="border-bottom pb-2">Số đơn đặt</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:choose>
                            <c:when test="${empty topRooms}">
                              <tr>
                                <td colspan="3" class="text-center">
                                  Không có dữ liệu
                                </td>
                              </tr>
                            </c:when>
                            <c:otherwise>
                              <c:forEach var="roomData" items="${topRooms}">
                                <c:set
                                  var="roomNumber"
                                  value="${roomData[1]}"
                                />
                                <c:set
                                  var="branchName"
                                  value="${roomData[2]}"
                                />
                                <c:set
                                  var="roomTypeName"
                                  value="${roomData[3]}"
                                />
                                <c:set
                                  var="bookingCount"
                                  value="${roomData[4]}"
                                />
                                <tr>
                                  <td class="pl-0">
                                    #${roomNumber} - ${roomTypeName}
                                  </td>
                                  <td>${branchName}</td>
                                  <td>${bookingCount}</td>
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

              <div class="col-md-4 stretch-card grid-margin">
                <div class="card">
                  <div class="card-body">
                    <p class="card-title mb-2">Dịch vụ phổ biến nhất</p>
                    <div class="table-responsive">
                      <table class="table table-striped table-borderless">
                        <thead>
                          <tr>
                            <th class="pl-0 pb-2 border-bottom">Tên dịch vụ</th>
                            <th class="border-bottom pb-2">Số lượng đặt</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:choose>
                            <c:when test="${empty topServices}">
                              <tr>
                                <td colspan="2" class="text-center">
                                  Không có dữ liệu
                                </td>
                              </tr>
                            </c:when>
                            <c:otherwise>
                              <c:forEach
                                var="serviceData"
                                items="${topServices}"
                              >
                                <c:set
                                  var="serviceName"
                                  value="${serviceData[1]}"
                                />
                                <c:set
                                  var="usageCount"
                                  value="${serviceData[2]}"
                                />
                                <tr>
                                  <td class="pl-0">${serviceName}</td>
                                  <td>${usageCount}</td>
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

              <div class="col-md-4 stretch-card grid-margin">
                <div class="card">
                  <div class="card-body">
                    <p class="card-title mb-2">Khách hàng thân thiết</p>
                    <div class="table-responsive">
                      <table class="table table-striped table-borderless">
                        <thead>
                          <tr>
                            <th class="pl-0 pb-2 border-bottom">
                              Tên khách hàng
                            </th>
                            <th class="border-bottom pb-2">Điểm tích lũy</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:choose>
                            <c:when test="${empty topCustomers}">
                              <tr>
                                <td colspan="2" class="text-center">
                                  Không có dữ liệu
                                </td>
                              </tr>
                            </c:when>
                            <c:otherwise>
                              <c:forEach var="customer" items="${topCustomers}">
                                <tr>
                                  <td class="pl-0">
                                    ${customer.user.fullName}
                                  </td>
                                  <td>${customer.rewardPoints}</td>
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
          </div>
          <jsp:include page="../layout/footer.jsp" />
        </div>
      </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
    <script src="/admin/js/dashboard.js"></script>
  </body>
</html>
