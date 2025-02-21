<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý FAQs</title>
    <jsp:include page="../layout/import-css.jsp" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>

<body>
    <div class="container-scroller">
        <jsp:include page="../layout/header.jsp" />
        <div class="container-fluid page-body-wrapper">
            <jsp:include page="../layout/theme-settings.jsp" />
            <jsp:include page="../layout/sidebar.jsp" />
            <div class="main-panel">
                <ul class="navbar-nav mr-lg-2 my-4 nav-search">
                    <li class="nav-item nav-search d-none d-lg-block" style="display: flex; align-items: center;">
                        <form action="/admin/homestay-infor/faq" method="get" class="d-flex" style="width: 100%; justify-content: center; align-items: center;">
                            <input type="text" class="form-control form-control-sm me-2" name="keyword" placeholder="Tìm kiếm FAQ..." 
                                   value="${keyword}" style="width: 400px; font-size: 14px; margin-right: 10px;">
                            <button type="submit" class="btn btn-primary btn-sm p-2">
                                <i class="bi bi-search"></i>
                            </button>
                        </form>
                    </li>
                </ul>
                <div class="content-wrapper">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h4 class="card-title">Danh sách FAQs</h4>
                        <a href="/admin/homestay-infor/faq/create" class="btn btn-primary btn-sm">
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
                                            <button class="accordion-button collapsed" 
                                                    type="button" data-bs-toggle="collapse" 
                                                    data-bs-target="#collapse${status.index}" 
                                                    aria-expanded="false" 
                                                    aria-controls="collapse${status.index}">
                                                ${faq.question}
                                            </button>
                                        </h2>
                                        <div id="collapse${status.index}" class="accordion-collapse collapse" 
                                             aria-labelledby="heading${status.index}" 
                                             data-bs-parent="#faqAccordion">
                                            <div class="accordion-body">
                                                ${faq.answer}
                                                <div class="text-end mt-2">
                                                    <a href="/admin/homestay-infor/faq/update/${faq.faqID}" class="btn btn-warning btn-sm">
                                                        <i class="bi bi-pencil"></i> Sửa
                                                    </a>
                                                    <button class="btn btn-danger btn-sm" data-faq-id="${faq.faqID}"
                                                        data-faq-question="${faq.question}"
                                                        onclick="checkBeforeDelete(this)">
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
                        <c:if test="${totalPages > 0}">
                            <div class="text-center">
                                <nav aria-label="Page navigation">
                                    <ul class="pagination justify-content-center">
                                        <li class="page-item ${currentPage > 1 ? '' : 'disabled'}">
                                            <c:choose>
                                                <c:when test="${currentPage > 1}">
                                                    <a class="page-link" href="/admin/homestay-infor/faq?page=${currentPage - 1}${not empty keyword ? '&keyword=' : ''}${keyword}" aria-label="Trước">
                                                        <span aria-hidden="true">&laquo;</span>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a class="page-link disabled" href="#" aria-label="Trước">
                                                        <span aria-hidden="true">&laquo;</span>
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>                                

                                        <c:forEach begin="0" end="${totalPages - 1}" varStatus="loop">
                                            <li class="page-item">
                                                <a class="${(loop.index + 1) eq currentPage ? 'active page-link' : 'page-link'} "
                                                    href="/admin/homestay-infor/faq?page=${loop.index + 1}">
                                                    ${loop.index + 1}
                                                </a>
                                            </li>
                                        </c:forEach>

                                        <li class="page-item ${currentPage < totalPages ? '' : 'disabled'}">
                                            <c:choose>
                                                <c:when test="${currentPage < totalPages}">
                                                    <a class="page-link" href="/admin/homestay-infor/faq?page=${currentPage + 1}${not empty keyword ? '&keyword=' : ''}${keyword}" aria-label="Tiếp theo">
                                                        <span aria-hidden="true">&raquo;</span>
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a class="page-link disabled" href="#" aria-label="Tiếp theo">
                                                        <span aria-hidden="true">&raquo;</span>
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>                                
                                    </ul>
                                </nav>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title text-danger">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> Xác nhận xóa FAQ
                    </h5>
                </div>
                <div class="modal-body">
                    Bạn có chắc chắn muốn xóa câu hỏi <b class="text-primary" id="questionConfirm"></b> không?
                </div>
                <div class="modal-footer">
                    <form action="/admin/homestay-infor/faq/delete" method="post">
                        <input type="hidden" name="faqID" id="faqIdInput">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-danger">Xóa</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../layout/import-js.jsp" />
    <script>
        function checkBeforeDelete(button) {
            let faqID = button.getAttribute("data-faq-id");
            let question = button.getAttribute("data-faq-question");
            $("#questionConfirm").text(question);
            $("#faqIdInput").val(faqID);
            $("#deleteConfirmModal").modal("show");
        }
    </script>
</body>
</html>
