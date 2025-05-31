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
    <title>Tạo mới đặt dịch vụ</title>
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
                      Tạo đơn đặt dịch vụ
                    </h4>
                    <form:form
                      class="form-horizontal"
                      action="/admin/booking-service/create"
                      method="post"
                      modelAttribute="newBookingService"
                    >
                      <form:input type="hidden" path="booking.bookingId" />
                      <input type="hidden" data-can-book="${canBook}">
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
                            path="booking.bookingId"
                          />
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Dịch vụ <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:select
                            class="form-select form-control ${not empty errorService ? 'is-invalid' : ''}"
                            path="service"
                          >
                            <form:option value="">Chọn dịch vụ</form:option>
                            <form:options
                              items="${listServices}"
                              itemValue="serviceId"
                              itemLabel="serviceName"
                            />
                          </form:select>
                          ${errorService}
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
                          <form:input
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

                          <button type="submit" class="btn btn-primary">
                            Tạo mới
                          </button>
                        </div>
                      </div>
                    </form:form>
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
    <jsp:include page="../layout/partial/_script-number-separator.jsp" />
    <jsp:include page="../layout/partial/_modal-warning.jsp" />
    <script>
      $(document).ready(function () {
        let input = $("input[data-can-book]");
        let canBook = input.data("can-book");
        let bookingId = $("input[name='booking.bookingId']").val();
        if (canBook === false) {
          showWarningModal("Đơn đặt phòng không đủ điều kiện để thêm dịch vụ.");
          $('#warningModal').on('hidden.bs.modal', function () {
            window.location.href = '/admin/booking/' + bookingId;
          });
        }
      });
    </script>
    
  </body>
</html>
