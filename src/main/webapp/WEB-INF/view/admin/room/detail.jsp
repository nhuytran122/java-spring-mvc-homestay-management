<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Chi tiết phòng</title>
  <jsp:include page="../layout/import-css.jsp" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
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
              <div class="card">
                <div class="card-body">
                  <h4 class="card-title">Chi tiết phòng ${room.roomNumber}</h4>
                  
                  <div class="row g-4">
                    <div class="d-flex justify-content-end">
                      <div class="btn-group" role="group">
                          <a href="/admin/room/update/${room.roomID}" class="btn btn-warning btn-sm" title="Sửa">
                              <i class="bi bi-pencil"></i> Sửa
                          </a>
                          <button class="btn btn-danger btn-sm" title="Xóa"
                              onclick="checkBeforeDelete(this)" 
                                  data-entity-id="${room.roomID}" 
                                  data-entity-name="${room.roomNumber}" 
                                  data-entity-type="Phòng" 
                                  data-delete-url="/admin/room/delete" 
                                  data-check-url="/admin/room/can-delete/" 
                                  data-id-name="roomID">
                              <i class="bi bi-trash"></i> Xóa
                          </button>
                      </div>
                  </div>
                  
                    <div class="col-lg-8">
                        <div id="roomCarousel" class="carousel slide" data-bs-ride="carousel">
                            <div class="carousel-inner rounded-4">
                                <div class="carousel-item active">
                                  <c:choose>
                                      <c:when test="${not empty room.thumbnail}">
                                          <img src="/images/room/${room.thumbnail}" class="d-block w-100" alt="Thumbnail" style="height: 400px; object-fit: cover">
                                      </c:when>
                                      <c:otherwise>
                                          <img src="/images/room/default-img.jpg" class="d-block w-100" alt="Default Img" style="height: 400px; object-fit: cover">
                                      </c:otherwise>
                                  </c:choose>
                                </div>
                                <c:forEach var="photo" items="${room.roomPhotos}">
                                  <div class="carousel-item">
                                      <img src="/images/room/${photo.photo}" class="d-block w-100" alt="Meeting Room" style="height: 400px; object-fit: cover">
                                  </div>
                              </c:forEach>
                            </div>
                            <button class="carousel-control-prev" type="button" data-bs-target="#roomCarousel" data-bs-slide="prev">
                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                <span class="visually-hidden">Previous</span>
                            </button>
                            <button class="carousel-control-next" type="button" data-bs-target="#roomCarousel" data-bs-slide="next">
                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                <span class="visually-hidden">Next</span>
                            </button>
                        </div>
                    </div>
        
                    <div class="col-lg-4">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body">
                                <h4 class="card-title mb-4">Thông tin phòng</h4>
                                <ul class="list-group list-group-flush">
                                  <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <span><i class="bi bi-door-open me-2"></i>Số phòng</span>
                                    <span class="badge bg-primary rounded-pill">${room.roomNumber}</span>
                                </li>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <span><i class="bi bi-door-open me-2"></i>Loại phòng</span>
                                        <span class="badge bg-primary rounded-pill">${room.roomType.name}</span>
                                    </li>
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <span><i class="bi bi-building me-2"></i>Chi nhánh</span>
                                        <span>${room.branch.branchName}</span>
                                    </li>
                                    
                                  </li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="col-12">
                      <div class="card shadow-sm mt-4">
                          <div class="card-body">
                              <h4 class="card-title mb-4">Bảng giá theo loại phòng</h4>
                              <div class="table-responsive">
                                  <table class="table table-hover table-striped">
                                      <thead class="table-light">
                                          <tr class="text-center">
                                              <th>Thời gian cơ bản (giờ)</th>
                                              <th>Giá cơ bản</th>
                                              <th>Giá bù giờ</th>
                                              <th>Giá qua đêm</th>
                                              <th>Giá theo ngày</th>
                                              <th>Áp dụng từ</th>
                                              <th>Đến</th>
                                              <th>Mặc định?</th>
                                              <th>Chính sách</th>
                                          </tr>
                                      </thead>
                                      <tbody>
                                          <c:choose>
                                              <c:when test="${empty room.roomType.roomPricings}">
                                                  <tr>
                                                      <td style="height: 50px;" colspan="8" class="text-center text-danger">
                                                          Loại phòng này chưa có bảng giá
                                                      </td>
                                                  </tr>
                                              </c:when>
                                              <c:otherwise>
                                                  <c:forEach var="pricing" items="${room.roomType.roomPricings}">
                                                      <tr style="height: 50px;" class="text-center align-middle">
                                                          <td>${pricing.baseDuration} giờ</td>
                                                          <td><fmt:formatNumber value="${pricing.basePrice}"/>đ</td>
                                                          <td><fmt:formatNumber value="${pricing.extraHourPrice}"/>đ</td>
                                                          <td><fmt:formatNumber value="${pricing.overnightPrice}"/>đ</td>
                                                          <td><fmt:formatNumber value="${pricing.dailyPrice}"/>đ</td>
                                                          <td>${f:formatLocalDateTime(pricing.startDate)}</td>
                                                          <td>${f:formatLocalDateTime(pricing.endDate)}</td>
                                                          <td>
                                                              <c:if test="${pricing.isDefault}">
                                                                  <span class="badge bg-success">Mặc định</span>
                                                              </c:if>
                                                              <c:if test="${!pricing.isDefault}">
                                                                  <span class="text-muted">-</span>
                                                              </c:if>
                                                          </td>
                                                          <td>${pricing.policy}</td>
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

        
                    <div class="col-12">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <h4 class="card-title mb-4">Tiện nghi trong phòng</h4>
                                <div class="table-responsive">
                                    <table class="table table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>Tên tiện nghi</th>
                                                <th>Phân loại</th>
                                                <th>Số lượng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                          <c:choose>
                                            <c:when test="${empty room.roomAmenities}">
                                                <tr>
                                                    <td colspan="7" class="text-center text-danger">Hiện phòng ${room.roomNumber} không có tiện nghi nào</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                              <c:forEach var="item" items="${room.roomAmenities}">
                                                <tr style="height: 50px;">
                                                    <td>${item.amenity.amenityName}</td>
                                                    <td><i class="fas ${item.amenity.amenityCategory.icon} amenity-icon me-2"></i>
                                                      ${item.amenity.amenityCategory.categoryName}
                                                    </td>
                                                    <td>${item.quantity}</td>
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
