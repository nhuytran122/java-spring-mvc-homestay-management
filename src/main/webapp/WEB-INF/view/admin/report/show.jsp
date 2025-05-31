<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <title>Thống kê doanh thu - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
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
            <h4 class="card-title mb-4">Thống kê doanh thu</h4>

            <form method="get" action="/admin/report" class="row g-3 mb-4" id="reportFilter">
              <div class="col-md-2">
                <label class="form-label">Chi nhánh</label>
                <select name="branchId" class="form-select">
                  <option value="">Tất cả</option>
                  <c:forEach var="branch" items="${branches}">
                    <option value="${branch.branchId}" ${branch.branchId == criteria.branchId ? 'selected' : ''}>
                      ${branch.branchName}
                    </option>
                  </c:forEach>
                </select>
              </div>

              <div class="col-md-3">
                <label class="form-label">Mục đích thanh toán</label>
                <select name="purpose" class="form-select">
                  <option value="">Tất cả</option>
                  <c:forEach var="p" items="${paymentPurposes}">
                    <option value="${p}" ${criteria.purpose == p ? 'selected' : ''}>
                      ${p.description}
                    </option>
                  </c:forEach>
                </select>
              </div>

              <div class="col-md-2">
                <label class="form-label">Thống kê theo</label>
                <select name="type" class="form-select" id="typeSelect">
                  <c:forEach var="type" items="${reportTypes}">
                    <option value="${type}" ${criteria.type == type ? 'selected' : ''}>
                      ${type.displayName}
                    </option>
                  </c:forEach>
                </select>
              </div>

              <div class="col-md-3">
                <label class="form-label">Khoảng thời gian</label>
                <input
                    type="text"
                    style="height: 37.6px;"
                    id="timeRange"
                    name="timeRange"
                    class="form-control daterange-picker"
                    value="${criteria.timeRange}"
                    placeholder="Chọn khoảng thời gian..."
                  />
              </div>

              <div class="col-md-2 d-flex align-items-end">
                <button class="btn btn-primary w-100">Thống kê</button>
              </div>
            </form>

            <div>
              <h5 class="mb-3">Biểu đồ doanh thu</h5>
              <div class="card bg-primary text-white">
                <div class="card-body">
                  <h5 class="card-title">Tổng doanh thu</h5>
                  <h3 class="card-text">
                    <fmt:formatNumber value="${reportData.totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                  </h3>
                </div>
              </div>
              <canvas id="revenueChart" height="100"></canvas>
            </div>
          </div>
          <jsp:include page="../layout/footer.jsp" />
        </div>
      </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <script>
      let chartLabels = [
        <c:forEach var="label" items="${reportData.labels}" varStatus="loop">
          "${label}"<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
      ];

      let chartRevenues = [
        <c:forEach var="revenue" items="${reportData.revenues}" varStatus="loop">
          ${revenue}<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
      ];

      let ctx = document.getElementById('revenueChart').getContext('2d');
      new Chart(ctx, {
        type: 'bar',
        data: {
          labels: chartLabels,
          datasets: [{
            label: 'Doanh thu (VNĐ)',
            data: chartRevenues,
            backgroundColor: 'rgba(75, 192, 192, 0.6)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }]
        },
        options: {
          scales: {
            y: { beginAtZero: true }
          }
        }
      });
    </script>

    <script src="/admin/js/dashboard.js"></script>
  </body>
</html>