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
    <title>Xuất kho</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link
      href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css"
      rel="stylesheet"
    />
    <link rel="stylesheet" href="/admin/css/style-select2.css" />
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
                    <h4 class="card-title mb-4 text-center">Xuất kho</h4>
                    <form:form
                      class="form-horizontal"
                      action="/admin/warehouse/export"
                      method="post"
                      modelAttribute="newExport"
                    >
                      <c:set var="errorItem">
                        <form:errors
                          path="inventoryItem"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorBranch">
                        <form:errors
                          path="branch"
                          cssClass="invalid-feedback"
                        />
                      </c:set>
                      <c:set var="errorQuantity">
                        <form:errors
                          path="quantity"
                          cssClass="invalid-feedback"
                        />
                      </c:set>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Đồ dùng <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:select
                            class="form-select select2 form-control ${not empty errorItem ? 'is-invalid' : ''}"
                            path="inventoryItem"
                          >
                            <form:option value="">Chọn đồ dùng</form:option>
                            <form:options
                              items="${listItems}"
                              itemValue="itemID"
                              itemLabel="itemName"
                            />
                          </form:select>
                          ${errorItem}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Chi nhánh <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:select
                            class="form-select form-control ${not empty errorBranch ? 'is-invalid' : ''}"
                            path="branch"
                          >
                            <form:option value="">Chọn chi nhánh</form:option>
                            <form:options
                              items="${listBranches}"
                              itemValue="branchID"
                              itemLabel="branchName"
                            />
                          </form:select>
                          ${errorBranch}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"
                          >Số lượng <span class="text-danger">*</span></label
                        >
                        <div class="col-sm-10">
                          <form:input
                            type="text"
                            class="form-control number-separator ${not empty errorQuantity ? 'is-invalid' : ''}"
                            path="quantity"
                          />
                          ${errorQuantity}
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2"></label>
                        <div class="col-sm-10">
                          <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-2" role="alert">
                              ${error}
                            </div>
                          </c:if>
                        </div>
                      </div>
                      <div class="form-group row">
                        <div class="col-sm-offset-2 col-sm-10 text-center">
                          <a href="/admin/warehouse" class="btn btn-secondary"
                            >Hủy</a
                          >
                          <button type="submit" class="btn btn-warning">
                            Xuất kho
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
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
      $(document).ready(function () {
        $(".select2").select2();
      });
    </script>
  </body>
</html>
