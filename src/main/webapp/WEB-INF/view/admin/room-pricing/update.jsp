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
    <title>Sửa mức giá phòng - ${roomPricing.roomType.name}</title>
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
                      Sửa mới mức giá phòng - ${roomPricing.roomType.name}
                    </h4>
                    <form:form
                      class="form-horizontal"
                      action="/admin/room-pricing/update"
                      method="post"
                      modelAttribute="roomPricing"
                    >
                      <form:input type="hidden" path="roomType" />
                      <form:input type="hidden" path="roomPricingId" />
                      <c:set var="errorBasePrice">
                        <form:errors
                          path="basePrice"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorExtraHourPrice">
                        <form:errors
                          path="extraHourPrice"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorOvernightPrice">
                        <form:errors
                          path="overnightPrice"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorDailyPrice">
                        <form:errors
                          path="dailyPrice"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorBaseDuration">
                        <form:errors
                          path="baseDuration"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorStartDate">
                        <form:errors
                          path="startDate"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorEndDate">
                        <form:errors
                          path="endDate"
                          cssClass="invalid-feedback"
                        />
                      </c:set>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Thời gian cơ bản (giờ)
                          <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control number-separator ${not empty errorBaseDuration ? 'is-invalid' : ''}"
                            path="baseDuration"
                          />
                          ${errorBaseDuration}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Giá cơ bản <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control number-separator ${not empty errorBasePrice ? 'is-invalid' : ''}"
                            path="basePrice"
                          />
                          ${errorBasePrice}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Giá bù giờ mỗi giờ
                          <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control number-separator ${not empty errorExtraHourPrice ? 'is-invalid' : ''}"
                            path="extraHourPrice"
                          />
                          ${errorExtraHourPrice}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Giá qua đêm <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control number-separator ${not empty errorOvernightPrice ? 'is-invalid' : ''}"
                            path="overnightPrice"
                          />
                          ${errorOvernightPrice}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Giá theo ngày
                          <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control number-separator ${not empty errorDailyPrice ? 'is-invalid' : ''}"
                            path="dailyPrice"
                          />
                          ${errorDailyPrice}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Ngày bắt đầu</label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control date-picker ${not empty errorStartDate ? 'is-invalid' : ''}"
                            path="startDate"
                          />
                          ${errorStartDate}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Ngày kết thúc</label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control date-picker ${not empty errorEndDate ? 'is-invalid' : ''}"
                            path="endDate"
                          />
                          ${errorEndDate}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2">Chính sách</label>
                        <div class="col-sm-10">
                          <form:textarea class="form-control" path="policy" />
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2">Mặc định</label>
                        <div class="col-sm-10">
                          <div class="form-check form-switch">
                            <form:checkbox
                              path="isDefault"
                              class="form-check-input"
                              style="transform: scale(1.3)"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="form-group row">
                        <div class="col-sm-12 text-center">
                          <button
                            type="button"
                            class="btn btn btn-secondary"
                            onclick="window.history.back()"
                          >
                            Hủy
                          </button>
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
          <jsp:include page="../layout/footer.jsp" />
        </div>
      </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <link
      rel="stylesheet"
      href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"
    />
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
    <jsp:include page="../layout/partial/_script-number-separator.jsp" />

    <script>
      $(document).ready(function () {
        $(".date-picker").datepicker({
          dateFormat: "dd/mm/yy",
          showAnim: "fadeIn",
          changeMonth: true,
          changeYear: true,
          yearRange: "-100:+10",
        });
      });
    </script>
  </body>
</html>
