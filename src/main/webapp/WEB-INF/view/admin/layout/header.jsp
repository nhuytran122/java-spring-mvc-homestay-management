<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar col-lg-12 col-12 p-0 fixed-top d-flex flex-row">
    <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-center">
      <a class="navbar-brand brand-logo mr-5" href="/admin"><img src="/images/homestay/lullaby.jpg" class="mr-2" alt="logo"/></a>
      <a class="navbar-brand brand-logo-mini" href="/admin"><img src="/images/homestay/lullaby.jpg" alt="logo"/></a>
    </div>
    <div class="navbar-menu-wrapper d-flex align-items-center justify-content-end">
      <button class="navbar-toggler navbar-toggler align-self-center" type="button" data-toggle="minimize">
        <span class="icon-menu"></span>
      </button>
      <ul class="navbar-nav navbar-nav-right">
        <!-- <li class="nav-item dropdown">
          <a class="nav-link count-indicator dropdown-toggle" id="notificationDropdown" href="#" data-toggle="dropdown">
            <i class="icon-bell mx-0"></i>
            <span class="count"></span>
          </a>
          <div class="dropdown-menu dropdown-menu-right navbar-dropdown preview-list" aria-labelledby="notificationDropdown">
            <p class="mb-0 font-weight-normal float-left dropdown-header">Notifications</p>
            <a class="dropdown-item preview-item">
              <div class="preview-thumbnail">
                <div class="preview-icon bg-success">
                  <i class="ti-info-alt mx-0"></i>
                </div>
              </div>
              <div class="preview-item-content">
                <h6 class="preview-subject font-weight-normal">Application Error</h6>
                <p class="font-weight-light small-text mb-0 text-muted">
                  Just now
                </p>
              </div>
            </a>
            <a class="dropdown-item preview-item">
              <div class="preview-thumbnail">
                <div class="preview-icon bg-warning">
                  <i class="ti-settings mx-0"></i>
                </div>
              </div>
              <div class="preview-item-content">
                <h6 class="preview-subject font-weight-normal">Settings</h6>
                <p class="font-weight-light small-text mb-0 text-muted">
                  Private message
                </p>
              </div>
            </a>
            <a class="dropdown-item preview-item">
              <div class="preview-thumbnail">
                <div class="preview-icon bg-info">
                  <i class="ti-user mx-0"></i>
                </div>
              </div>
              <div class="preview-item-content">
                <h6 class="preview-subject font-weight-normal">New user registration</h6>
                <p class="font-weight-light small-text mb-0 text-muted">
                  2 days ago
                </p>
              </div>
            </a>
          </div>
        </li> -->
        <c:if test="${not empty pageContext.request.userPrincipal}">
          <li class="nav-item nav-profile dropdown">
              <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" data-toggle="dropdown" id="profileDropdown">
                  <img src="/images/avatar/${not empty sessionScope.avatar ? sessionScope.avatar : 'default-img.jpg'}" 
                      alt="profile" class="rounded-circle me-2" width="40" height="40">
                  <span class="fw-semibold text-dark me-1">${sessionScope.fullName}</span>
                  <i class="bi bi-caret-down-fill text-muted"></i> 
              </a>
              <div class="dropdown-menu dropdown-menu-right navbar-dropdown shadow" aria-labelledby="profileDropdown">
                  <a class="dropdown-item d-flex align-items-center" href="/admin/profile">
                      <i class="ti-settings text-primary me-2"></i> Tài khoản của tôi
                  </a>
                  <div class="dropdown-divider"></div>
                  <form method="post" action="/logout" class="w-100">
                      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                      <button class="dropdown-item d-flex align-items-center w-100">
                          <i class="ti-power-off text-primary me-2"></i> Đăng xuất
                      </button>
                  </form>
              </div>
          </li>
      </c:if>


      </ul>
      <button class="navbar-toggler navbar-toggler-right d-lg-none align-self-center" type="button" data-toggle="offcanvas">
        <span class="icon-menu"></span>
      </button>
    </div>
  </nav>