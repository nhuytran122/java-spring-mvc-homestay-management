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
    <title>Sửa giao dịch</title>
    <jsp:include page="../../layout/import-css.jsp" />
  </head>
  <body>
    <div class="container-scroller">
      <jsp:include page="../../layout/header.jsp" />

      <div class="container-fluid page-body-wrapper">
        <jsp:include page="../../layout/theme-settings.jsp" />
        <jsp:include page="../../layout/sidebar.jsp" />

        <div class="main-panel">
          <div class="content-wrapper">
            <div class="row">
              <div class="col-md-12 grid-margin stretch-card">
                <div class="card">
                  <div class="card-body">
                    <h4 class="card-title mb-4 text-center">
                      Sửa số lượng giao dịch -
                      <c:if test="${transaction.transactionType == 'IMPORT'}">
                        Nhập kho
                      </c:if>
                      <c:if test="${transaction.transactionType == 'EXPORT'}">
                        Xuất kho
                      </c:if>
                    </h4>
                    <form:form
                      class="form-horizontal"
                      action="/admin/warehouse/transaction/update"
                      method="post"
                      modelAttribute="transaction"
                    >
                      <form:hidden path="transactionID" />
                      <form:hidden path="transactionType" />
                      <c:set var="errorQuantity">
                        <form:errors
                          path="quantity"
                          cssClass="invalid-feedback"
                        />
                      </c:set>

                      <div class="form-group row">
                        <label class="control-label col-sm-2">Đồ dùng</label>
                        <div class="col-sm-10">
                          <input
                            type="text"
                            class="form-control"
                            value="${transaction.inventoryItem.itemName}"
                            disabled
                          />
                          <form:hidden path="inventoryItem.itemID" />
                        </div>
                      </div>

                      <div class="form-group row">
                        <label class="control-label col-sm-2">Chi nhánh</label>
                        <div class="col-sm-10">
                          <input
                            type="text"
                            class="form-control"
                            value="${transaction.branch.branchName}"
                            disabled
                          />
                          <form:hidden path="branch.branchID" />
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
                          <a
                            href="/admin/warehouse/transaction"
                            class="btn btn-secondary"
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

    <jsp:include page="../../layout/import-js.jsp" />
    <jsp:include page="../../layout/partial/_script-number-separator.jsp" />
  </body>
</html>
