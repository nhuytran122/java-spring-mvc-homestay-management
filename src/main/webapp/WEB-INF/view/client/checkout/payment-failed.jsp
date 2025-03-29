<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Thanh toán thất bại</title>
    <jsp:include page="../layout/import-css.jsp" />
  </head>
  <body>
    <jsp:include page="../layout/header.jsp" />

    <div
      class="container d-flex justify-content-center align-items-center"
      style="min-height: 80vh"
    >
      <div class="card shadow-sm" style="max-width: 500px">
        <div class="card-body text-center">
          <div class="alert alert-danger" role="alert">
            <h4 class="alert-heading">
              <i class="bi bi-x-circle"></i> Thanh toán thất bại!
            </h4>
            <p>
              ${errorMessage != null ? errorMessage : "Có lỗi xảy ra khi thực
              hiện thanh toán."}
            </p>
            <hr />
            <p class="mb-0">
              Vui lòng thử lại hoặc liên hệ với chúng tôi để được hỗ trợ.
            </p>
          </div>
          <a href="/" class="btn btn-danger mt-3">
            <i class="bi bi-arrow-left"></i> Về trang chủ
          </a>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
  </body>
</html>
