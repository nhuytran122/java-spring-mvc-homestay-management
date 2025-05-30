<%@page contentType="text/html" pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta
      name="viewport"
      content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <title>Quản lý FAQs</title>
    <jsp:include page="../layout/import-css.jsp" />
  </head>

  <body>
    <div class="container-scroller">
      <jsp:include page="../layout/header.jsp" />
      <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
          <div class="search-form-container my-4">
            <form
              action="/admin/homestay-infor/faq"
              method="get"
              class="search-form"
            >
              <input
                type="text"
                class="form-control form-control-sm"
                name="keyword"
                placeholder="Tìm kiếm FAQ..."
                value="${keyword}"
              />
              <button type="submit" class="btn btn-primary btn-sm p-2">
                <i class="bi bi-search"></i>
              </button>
            </form>
          </div>
          <div class="content-wrapper">
            <div class="d-flex justify-content-between align-items-center mb-3">
              <h4 class="card-title">Danh sách FAQs</h4>
              <a
                href="/admin/homestay-infor/faq/create"
                class="btn btn-primary btn-sm"
              >
                <i class="bi bi-plus-circle"></i> Thêm mới FAQ
              </a>
            </div>

            <div class="accordion" id="faqAccordion">
              <c:choose>
                <c:when test="${empty faqs}">
                  <p class="text-center text-danger">Không có câu hỏi nào.</p>
                </c:when>
                <c:otherwise>
                  <c:forEach var="faq" items="${faqs}" varStatus="status">
                    <div class="accordion-item">
                      <h2 class="accordion-header" id="heading${status.index}">
                        <button
                          class="accordion-button collapsed"
                          type="button"
                          data-bs-toggle="collapse"
                          data-bs-target="#collapse${status.index}"
                          aria-expanded="false"
                          aria-controls="collapse${status.index}"
                        >
                          ${faq.question}
                        </button>
                      </h2>
                      <div
                        id="collapse${status.index}"
                        class="accordion-collapse collapse"
                        aria-labelledby="heading${status.index}"
                        data-bs-parent="#faqAccordion"
                      >
                        <div class="accordion-body">
                          ${faq.answer}
                          <div class="text-end mt-2">
                            <a
                              href="/admin/homestay-infor/faq/update/${faq.faqId}"
                              class="btn btn-warning btn-sm"
                            >
                              <i class="bi bi-pencil"></i> Sửa
                            </a>

                            <button
                              class="btn btn-danger btn-sm"
                              onclick="checkBeforeDelete(this)"
                              data-entity-id="${faq.faqId}"
                              data-entity-name="${faq.question}"
                              data-entity-type="FAQ"
                              data-delete-url="/admin/homestay-infor/faq/delete"
                              data-id-name="faqId"
                            >
                              <i class="bi bi-trash"></i> Xóa
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
            </div>

            <div class="d-flex justify-content-center mt-4">
              <jsp:include
                page="../layout/partial/_pagination-with-keyword.jsp"
              >
                <jsp:param name="url" value="/admin/homestay-infor/faq" />
                <jsp:param name="currentPage" value="${currentPage}" />
                <jsp:param name="totalPages" value="${totalPages}" />
                <jsp:param name="keyword" value="${keyword}" />
              </jsp:include>
            </div>
          </div>
          <jsp:include page="../layout/footer.jsp" />
        </div>
      </div>
    </div>
    <jsp:include page="../layout/import-js.jsp" />
    <jsp:include
      page="../layout/partial/_modal-delete-not-check-can-delete.jsp"
    />
  </body>
</html>
