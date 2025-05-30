<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="sidebar sidebar-offcanvas" id="sidebar" style="margin-left: -1%">
  <ul class="nav">
    <li class="nav-item">
      <a class="nav-link" href="/admin/dashboard">
        <i class="bi bi-speedometer2 menu-icon"></i>
        <span class="menu-title">Dashboard</span>
      </a>
    </li>

    <li class="nav-item">
      <a
        class="nav-link"
        data-toggle="collapse"
        href="#booking"
        aria-expanded="false"
        aria-controls="booking"
      >
        <i class="bi bi-calendar-week menu-icon"></i>
        <span class="menu-title">Booking</span>
        <i class="menu-arrow"></i>
      </a>
      <div class="collapse" id="booking">
        <ul class="nav flex-column sub-menu">
          <li class="nav-item">
            <a class="nav-link" href="/admin/booking">Danh sách Booking</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/booking/schedule"
              >Lịch đặt phòng</a
            >
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/booking-service">Dịch vụ đã đặt</a>
          </li>
        </ul>
      </div>
    </li>

    <li class="nav-item">
      <a
        class="nav-link"
        data-toggle="collapse"
        href="#payments"
        aria-expanded="false"
        aria-controls="payment"
      >
        <i class="bi bi-houses menu-icon"></i>
        <span class="menu-title">Thanh toán</span>
        <i class="menu-arrow"></i>
      </a>
      <div class="collapse" id="payments">
        <ul class="nav flex-column sub-menu">
          <li class="nav-item">
            <a class="nav-link" href="/admin/payment">Lịch sử giao dịch</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/refund">Yêu cầu hoàn tiền</a>
          </li>
        </ul>
      </div>
    </li>

    <li class="nav-item">
      <a
        class="nav-link"
        data-toggle="collapse"
        href="#homestay"
        aria-expanded="false"
        aria-controls="homestay"
      >
        <i class="bi bi-house-heart menu-icon"></i>
        <span class="menu-title">Homestay</span>
        <i class="menu-arrow"></i>
      </a>
      <div class="collapse" id="homestay">
        <ul class="nav flex-column sub-menu">
          <li class="nav-item">
            <a class="nav-link" href="/admin/homestay-infor">Thông tin</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/branch">Chi nhánh</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/homestay-infor/rule"
              >Quy tắc chung</a
            >
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/homestay-infor/faq">FAQ</a>
          </li>
        </ul>
      </div>
    </li>

    <li class="nav-item">
      <a
        class="nav-link"
        data-toggle="collapse"
        href="#rooms"
        aria-expanded="false"
        aria-controls="room"
      >
        <i class="bi bi-houses menu-icon"></i>
        <span class="menu-title">Phòng</span>
        <i class="menu-arrow"></i>
      </a>
      <div class="collapse" id="rooms">
        <ul class="nav flex-column sub-menu">
          <li class="nav-item">
            <a class="nav-link" href="/admin/room">Phòng</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/room-type">Loại phòng</a>
          </li>
        </ul>
      </div>
    </li>
    <li class="nav-item">
      <a
        class="nav-link"
        data-toggle="collapse"
        href="#amenities"
        aria-expanded="false"
        aria-controls="amenities"
      >
        <i class="bi bi-easel2 menu-icon"></i>
        <span class="menu-title">Tiện nghi</span>
        <i class="menu-arrow"></i>
      </a>
      <div class="collapse" id="amenities">
        <ul class="nav flex-column sub-menu">
          <li class="nav-item">
            <a class="nav-link" href="/admin/amenity"> Tiện nghi </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/amenity-category"> Phân loại </a>
          </li>
        </ul>
      </div>
    </li>

    <li class="nav-item" id="service">
      <a
        class="nav-link"
        href="/admin/service"
        aria-expanded="false"
        aria-controls="service"
      >
        <i class="bi bi-house-add menu-icon"></i>
        <span class="menu-title">Dịch vụ</span>
      </a>
    </li>

    <li class="nav-item">
      <a
        class="nav-link"
        data-toggle="collapse"
        href="#items"
        aria-expanded="false"
        aria-controls="items"
      >
        <i class="bi bi-basket menu-icon"></i>
        <span class="menu-title">Quản lý kho</span>
        <i class="menu-arrow"></i>
      </a>
      <div class="collapse" id="items">
        <ul class="nav flex-column sub-menu">
          <li class="nav-item">
            <a class="nav-link" href="/admin/warehouse">Kho hiện có</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/warehouse/transaction"
              >Lịch sử giao dịch kho</a
            >
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/inventory-item">Đồ dùng</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/inventory-category">Phân loại</a>
          </li>
        </ul>
      </div>
    </li>

    <li class="nav-item">
      <a
        class="nav-link"
        href="/admin/maintenance"
        aria-expanded="false"
        aria-controls="maintainance"
      >
        <i class="bi bi-house-gear menu-icon"></i>
        <span class="menu-title">Yêu cầu bảo trì</span>
      </a>
    </li>

    <li class="nav-item">
      <a
        class="nav-link"
        href="/admin/employee"
        aria-expanded="false"
        aria-controls="maintainance"
      >
        <i class="bi bi bi-person-bounding-box menu-icon"></i>
        <span class="menu-title">Nhân viên</span>
      </a>
    </li>

    <li class="nav-item">
      <a
        class="nav-link"
        data-toggle="collapse"
        href="#customers"
        aria-expanded="false"
        aria-controls="customers"
      >
        <i class="bi bi-person-lines-fill menu-icon"></i>
        <span class="menu-title">Khách hàng</span>
        <i class="menu-arrow"></i>
      </a>
      <div class="collapse" id="customers">
        <ul class="nav flex-column sub-menu">
          <li class="nav-item">
            <a class="nav-link" href="/admin/customer"> Khách hàng </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/customer/customer-type">
              Phân loại
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/admin/review"> Đánh giá </a>
          </li>
        </ul>
      </div>
    </li>

    <li class="nav-item">
      <a class="nav-link" href="/admin/report">
        <i class="bi bi-clipboard2-data menu-icon"></i>
        <span class="menu-title">Thống kê</span>
      </a>
    </li>
  </ul>
</nav>
