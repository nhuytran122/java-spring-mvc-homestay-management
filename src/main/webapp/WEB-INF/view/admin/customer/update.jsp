<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="form"
uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>Sửa khách hàng</title>
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
                    <h4 class="card-title mb-4 text-center">Sửa khách hàng</h4>
                    <form:form
                      class="form-horizontal"
                      action="/admin/customer/update"
                      method="post"
                      modelAttribute="customer"
                    >
                      <form:input type="hidden" path="customerID" />

                      <c:set var="errorName">
                        <form:errors
                          path="fullName"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorPhone">
                        <form:errors path="phone" cssClass="invalid-feedback" />
                      </c:set>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Tên khách hàng
                          <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control ${not empty errorName ? 'is-invalid' : ''}"
                            path="fullName"
                          />
                          ${errorName}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Số điện thoại
                          <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control ${not empty errorPhone ? 'is-invalid' : ''}"
                            path="phone"
                          />
                          ${errorPhone}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2">Email</label>
                        <div class="col-sm-10">
                          <form:input
                            type="email"
                            class="form-control"
                            path="email"
                            disabled="true"
                          />
                          <form:hidden path="email" />
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2">Địa chỉ</label>
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control"
                            path="address"
                          />
                        </div>
                      </div>
                      <div class="form-group row">
                        <div class="col-sm-offset-2 col-sm-10 text-center">
                          <a href="/admin/customer" class="btn btn-secondary"
                            >Hủy</a
                          >
                          <button type="submit" class="btn btn-warning">
                            Sửa
                          </button>
                        </div>
                      </div>
                    </form:form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
  </body>
</html>
