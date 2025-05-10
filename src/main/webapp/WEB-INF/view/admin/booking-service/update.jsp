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
    <title>Sửa việc đặt dịch vụ</title>
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
                    <h4 class="card-title mb-4 text-center">
                      Sửa việc đặt dịch vụ
                    </h4>
                    <form:form
                      class="form-horizontal"
                      action="/admin/booking-service/update"
                      method="post"
                      modelAttribute="bookingService"
                    >
                      <form:input type="hidden" path="bookingServiceID" />
                      <form:input type="hidden" path="booking.bookingID" />

                      <c:set var="errorQuantity">
                        <form:errors
                          path="quantity"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorService">
                        <form:errors
                          path="service"
                          cssClass="invalid-feedback"
                        />
                      </c:set>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Mã đặt phòng</label
                        >
                        <div class="col-sm-10">
                          <form:input
                            disabled="true"
                            class="form-control"
                            path="booking.bookingID"
                          />
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Tên dịch vụ <span class="text-danger"></span
                        ></label>
                        <div class="col-sm-10">
                          <input
                            type="text"
                            class="form-control"
                            value="${bookingService.service.serviceName}"
                            readonly
                          />
                          <form:input type="hidden" path="service"></form:input>
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Số lượng <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="number"
                            class="form-control number-separator ${not empty errorQuantity ? 'is-invalid' : ''}"
                            path="quantity"
                          />
                          ${errorQuantity}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2">Mô tả</label>
                        <div class="col-sm-10">
                          <form:textarea
                            type="text"
                            class="form-control"
                            path="description"
                          />
                        </div>
                      </div>

                      <div class="form-group row">
                        <div class="col-sm-offset-2 col-sm-10 text-center">
                          <button
                            type="button"
                            class="btn btn-secondary"
                            onclick="history.back()"
                          >
                            Hủy
                          </button>
                          <button type="submit" class="btn btn-warning">
                            Sửa
                          </button>
                        </div>
                      </div>
                      <div class="data-check" style="display: none;">
                        <input type="hidden" data-can-update="${canUpdate}">
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
    <jsp:include page="../layout/partial/_script-number-separator.jsp" />
    <jsp:include page="_script-modal-warning-update.jsp" />
    <script>
      $(document).ready(function () {
        let canUpdate = $('.data-check input[type="hidden"]').data("can-update");
        let bookingID = $('input[name="booking\\.bookingID"]').val();
    
        if (canUpdate === false) {
          showWarningModal("Đơn đặt dịch vụ này đã được thanh toán, không thể sửa");
          $("#warningModal").on("hidden.bs.modal", function () {
            window.location.href = "/admin/booking/" + bookingID;
          });
        }
      });
    </script>
    
  </body>
</html>
