<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quản lý tài khoản - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="/client/css/booking-history-style.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
    />
  </head>
  <body>
    <jsp:include page="../layout/header.jsp" />
    <jsp:include page="../layout/partial/_welcome-banner-profile.jsp" />

    <div class="container my-5">
      <div class="row">
        <div class="col-md-8">
          <div class="card border-1 rounded-3">
            <div class="card-header bg-primary text-white rounded-top-3 py-3">
              <h3 class="card-title mb-0 fw-bold">Thông tin tài khoản</h3>
            </div>
            <div class="card-body p-4">
              <div class="row g-4">
                <div class="col-md-12 text-center mb-3">
                  <label class="form-label fw-bold text-dark"
                    >Ảnh đại diện</label
                  >
                  <div class="mb-3">
                    <img
                      src="/images/avatar/${not empty sessionScope.avatar ? sessionScope.avatar : 'default-img.jpg'}"
                      alt="Avatar"
                      class="rounded-circle border border-primary border-2"
                      style="width: 120px; height: 120px; object-fit: cover"
                    />
                  </div>
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-bold text-dark">Họ và tên</label>
                  <div class="input-group">
                    <span class="input-group-text bg-light"
                      ><i class="fa fa-user"></i
                    ></span>
                    <span class="form-control form-control-lg rounded-end-3"
                      >${sessionScope.fullName}</span
                    >
                  </div>
                </div>

                <div class="col-md-6">
                  <label class="form-label fw-bold text-dark"
                    >Số điện thoại</label
                  >
                  <div class="input-group">
                    <span class="input-group-text bg-light"
                      ><i class="fa fa-phone"></i
                    ></span>
                    <span class="form-control form-control-lg rounded-end-3"
                      >${customer.phone}</span
                    >
                  </div>
                </div>

                <div class="col-md-6">
                  <label class="form-label fw-bold text-dark">Email</label>
                  <div class="input-group">
                    <span class="input-group-text bg-light"
                      ><i class="fa fa-envelope"></i
                    ></span>
                    <span class="form-control form-control-lg rounded-end-3"
                      >${customer.email}</span
                    >
                  </div>
                </div>

                <div class="col-md-6">
                  <label class="form-label fw-bold text-dark">Địa chỉ</label>
                  <div class="input-group">
                    <span class="input-group-text bg-light"
                      ><i class="fa fa-map-marker-alt"></i
                    ></span>
                    <span class="form-control form-control-lg rounded-end-3"
                      >${customer.address}</span
                    >
                  </div>
                </div>

                <div class="col-md-6">
                  <label class="form-label fw-bold text-dark"
                    >Điểm tích lũy</label
                  >
                  <div class="input-group">
                    <span class="input-group-text bg-light"
                      ><i class="fa fa-star"></i
                    ></span>
                    <span class="form-control form-control-lg rounded-end-3"
                      ><fmt:formatNumber
                        type="number"
                        value="${customer.rewardPoints}"
                    /></span>
                  </div>
                </div>
                <div class="col-md-6">
                  <label class="form-label fw-bold text-dark"
                    >Hạng thành viên</label
                  >
                  <div class="input-group">
                    <span class="input-group-text bg-light"
                      ><i class="fa fa-crown"></i
                    ></span>
                    <span class="form-control form-control-lg rounded-end-3"
                      >${customer.customerType.name}</span
                    >
                  </div>
                </div>

                <div class="col-12 text-center mt-4">
                  <a
                    href="/profile/update"
                    class="btn btn-primary btn-lg rounded-3 me-2"
                  >
                    <i class="fa fa-edit"></i> Cập nhật thông tin
                  </a>
                  <a
                    href="/change-password"
                    class="btn btn-outline-primary btn-lg rounded-3"
                  >
                    <i class="fa fa-key"></i> Đổi mật khẩu
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card border-1 rounded-3">
            <div class="card-header bg-warning text-white rounded-top-3 py-3">
              <h3 class="card-title mb-0 fw-bold">Thống kê nhanh</h3>
            </div>
            <div class="card-body p-3">
              <ul class="list-group list-group-flush">
                <li
                  class="list-group-item d-flex justify-content-between align-items-center"
                >
                  <span
                    ><i class="fa fa-calendar-check me-2 text-primary"></i> Số
                    lần đặt phòng</span
                  >
                  <span class="fw-bold">${countTotalBooked}</span>
                </li>
                <li
                  class="list-group-item d-flex justify-content-between align-items-center"
                >
                  <span
                    ><i class="fa fa-money-bill me-2 text-primary"></i> Tổng chi
                    tiêu</span
                  >
                  <span class="fw-bold"
                    ><fmt:formatNumber
                      type="number"
                      value="${paidTotal}"
                    />đ</span
                  >
                </li>
                <li
                  class="list-group-item d-flex justify-content-between align-items-center"
                >
                  <span
                    ><i class="fa fa-check-circle me-2 text-primary"></i> Đặt
                    phòng hoàn tất</span
                  >
                  <span class="fw-bold">${countCompleted}</span>
                </li>
                <li
                  class="list-group-item d-flex justify-content-between align-items-center"
                >
                  <span
                    ><i class="fa fa-times-circle me-2 text-primary"></i> Đặt
                    phòng đã hủy</span
                  >
                  <span class="fw-bold">${countCancelled}</span>
                </li>
              </ul>
              <a
                href="/booking/booking-history"
                class="btn btn-outline-warning btn-sm w-100 mt-3 rounded-3"
              >
                Xem chi tiết lịch sử
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
  </body>
</html>
