<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${totalPages > 0}">
    <div class="text-center">
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <!-- Nút Trước -->
                <li class="page-item ${currentPage > 1 ? '' : 'disabled'}">
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <a class="page-link" href="${url}?page=${currentPage - 1}${not empty keyword ? '&keyword=' += keyword : ''}" aria-label="Trước">
                                <span aria-hidden="true">«</span>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="page-link disabled" href="#" aria-label="Trước">
                                <span aria-hidden="true">«</span>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </li>

                <!-- Số trang -->
                <c:forEach begin="0" end="${totalPages - 1}" varStatus="loop">
                    <li class="page-item">
                        <a class="${(loop.index + 1) eq currentPage ? 'active page-link' : 'page-link'}"
                           href="${url}?page=${loop.index + 1}${not empty keyword ? '&keyword=' += keyword : ''}">
                            ${loop.index + 1}
                        </a>
                    </li>
                </c:forEach>

                <!-- Nút Tiếp theo -->
                <li class="page-item ${currentPage < totalPages ? '' : 'disabled'}">
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a class="page-link" href="${url}?page=${currentPage + 1}${not empty keyword ? '&keyword=' += keyword : ''}" aria-label="Tiếp theo">
                                <span aria-hidden="true">»</span>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="page-link disabled" href="#" aria-label="Tiếp theo">
                                <span aria-hidden="true">»</span>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </nav>
    </div>
</c:if>