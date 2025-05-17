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
    <title>Thêm phân loại khách hàng</title>
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
                      Thêm mới phân loại khách hàng
                    </h4>
                    <form:form
                      class="form-horizontal"
                      action="/admin/customer/customer-type/create"
                      method="post"
                      modelAttribute="newType"
                    >
                      <c:set var="errorName">
                        <form:errors path="name" cssClass="invalid-feedback" />
                      </c:set>
                      <c:set var="errorDiscountRate">
                        <form:errors
                          path="discountRate"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorMinPoint">
                        <form:errors
                          path="minPoint"
                          cssClass="invalid-feedback"
                        />
                      </c:set>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Tên phân loại
                          <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control ${not empty errorName ? 'is-invalid' : ''}"
                            path="name"
                          />
                          ${errorName}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Điểm tối thiểu
                          <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control number-separator ${not empty errorMinPoint ? 'is-invalid' : ''}"
                            path="minPoint"
                          />
                          ${errorMinPoint}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Phần trăm giảm giá
                          <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control number-separator ${not empty errorDiscountRate ? 'is-invalid' : ''}"
                            path="discountRate"
                          />
                          ${errorDiscountRate}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2">Mô tả</label>
                        <div class="col-sm-10">
                          <form:textarea
                            class="form-control"
                            path="description"
                          />
                        </div>
                      </div>

                      <div class="form-group row">
                        <div class="col-sm-offset-2 col-sm-10 text-center">
                          <a
                            href="/admin/customer/customer-type"
                            class="btn btn-secondary"
                            >Hủy</a
                          >
                          <button type="submit" class="btn btn-primary">
                            Tạo
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
  </body>
</html>
