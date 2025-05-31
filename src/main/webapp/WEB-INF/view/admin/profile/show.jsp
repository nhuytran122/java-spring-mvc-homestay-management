<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Hồ sơ của tôi - Lullaby Homestay</title>
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
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h4 class="card-title">Hồ sơ của tôi</h4>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-4 text-center">
                                            <img src="/images/avatar/${not empty user.avatar ? user.avatar : 'default-img.jpg'}"
                                                class="img-fluid rounded-circle mb-3"
                                                style="width: 150px; height: 150px;">
                                            <h5>${user.fullName}</h5>
                                        </div>

                                        <div class="col-md-8">
                                            <div class="row mb-3">
                                                <div class="col-md-12">
                                                    <p><strong>Email:</strong> ${user.email}</p>
                                                    <p><strong>Số điện thoại:</strong> ${user.phone}</p>
                                                    <p><strong>Địa chỉ:</strong> ${user.address}</p>
                                                    <p><strong>Vai trò:</strong> ${user.role.description}</p>
                                                    <p><strong>Lương:</strong> <fmt:formatNumber type="number"
                                                        value="${user.salary}" /> đ</p>
                                                </div>
                                            </div>
                                                <div class="row">
                                                    <div class="col-md-6 mb-3">
                                                        <a href="/admin/profile/update"
                                                                class="btn btn-primary btn-sm w-100">
                                                            <i class="bi bi-pencil"></i> Chỉnh sửa thông tin
                                                        </a>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <a href="/admin/profile/change-password" 
                                                           class="btn btn-warning btn-sm w-100">
                                                            <i class="bi bi-lock"></i> Đổi mật khẩu
                                                        </a>
                                                    </div>
                                                </div>
                                            </form>

                                        </div>
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
</body>
<jsp:include page="../layout/import-js.jsp" />
</html>
