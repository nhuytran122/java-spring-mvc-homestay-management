<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${totalPages > 0}">
    <div class="text-center">
        <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">
                <!-- Nút Trước -->
                <li class="page-item ${currentPage > 1 ? '' : 'disabled'}">
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <a class="page-link" href="${baseUrl}?page=${currentPage - 1}${extraParams}" aria-label="Trước">
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

                <!-- Danh sách trang -->
                <c:forEach begin="0" end="${totalPages - 1}" varStatus="loop">
                    <li class="page-item">
                        <a class="${(loop.index + 1) eq currentPage ? 'active page-link' : 'page-link'}"
                           href="${baseUrl}?page=${loop.index + 1}${extraParams}">
                            ${loop.index + 1}
                        </a>
                    </li>
                </c:forEach>

                <!-- Nút Tiếp theo -->
                <li class="page-item ${currentPage < totalPages ? '' : 'disabled'}">
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a class="page-link" href="${baseUrl}?page=${currentPage + 1}${extraParams}" aria-label="Tiếp theo">
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