<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý đánh giá</title>
  <jsp:include page="../layout/import-css.jsp" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>

<body>
  <div class="container-scroller">
    <jsp:include page="../layout/header.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
            <div class="search-form-container my-4">
                <form action="/admin/review" method="get" class="search-form">
                    <select name="branchId" class="form-select form-control form-select-sm">
                        <option value="">Chọn chi nhánh</option>
                        <c:forEach var="branch" items="${listBranches}">
                            <option value="${branch.branchId}" ${branch.branchId == branchId ? 'selected' : ''}>
                                ${branch.branchName}
                            </option>
                        </c:forEach>
                    </select>
                    <select name="sort" class="form-select form-control form-select-sm" style="width: 200px; font-size: 14px; height: 41px;">
                        <option value="" ${sort == '' ? 'selected' : ''}>
                            Không sắp xếp
                        </option>
                        <option value="desc" ${sort == 'desc' ? 'selected' : ''}>
                            Xếp hạng giảm dần
                        </option>
                        <option value="asc" ${sort == 'asc' ? 'selected' : ''}>
                            Xếp hạng tăng dần
                        </option>
                    </select>
                    
                    <button type="submit" class="btn btn-primary btn-sm p-2">
                        <i class="bi bi-search"></i>
                    </button>
                </form>                    
            </div>            
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-md-12 grid-margin stretch-card">
                        <div class="card position-relative">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h4 class="card-title">Danh sách đánh giá</h4>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Tên khách hàng</th>
                                                <th>Nội dung</th>
                                                <th>Đánh giá</th>
                                                <th>Ngày tạo</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty reviews}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy đánh giá nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="review" items="${reviews}">
                                                        <tr style="height: 80px;">
                                                            <td>${review.booking.customer.user.fullName}</td>
                                                            <td style="max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                                                ${review.comment}
                                                            </td>
                                                            <td>
                                                                <fmt:formatNumber type="number"
                                                                    value="${review.rating}" pattern="#"/> <i class="bi bi-star-fill text-warning"></i>
                                                            </td>
                                                            <td>${f:formatLocalDateTime(review.createdAt)}</td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <button type="button" title="Xem chi tiết"
                                                                            class="btn btn-success btn-sm btn-show-comment"
                                                                            data-comment="${review.comment}">
                                                                        <i class="bi bi-eye"></i>
                                                                    </button>

                                                                    <button class="btn btn-danger btn-sm"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${review.reviewId}" 
                                                                            data-entity-name="${review.comment}" 
                                                                            data-entity-type="Đánh giá" 
                                                                            data-delete-url="/admin/review/delete" 
                                                                            data-id-name="reviewId">
                                                                            <i class="bi bi-trash"></i>
                                                                    </button>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <jsp:include page="../layout/partial/_pagination-with-param.jsp">
                    <jsp:param name="url" value="/admin/review" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="extraParams" value="${extraParams}" />
                </jsp:include>
            </div>
            <jsp:include page="../layout/footer.jsp" />
        </div>
    </div>
  </div>

  <div class="modal fade" id="reviewCommentModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog" style="margin-top: 1%">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Chi tiết đánh giá</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
      </div>
      <div class="modal-body">
        <p id="reviewCommentContent" style="white-space: pre-wrap;"></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
      </div>
    </div>
  </div>
</div>


    <jsp:include page="../layout/partial/_modal-delete-not-check-can-delete.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
    <script>
        $(document).ready(function () {
            $(".btn-show-comment").click(function () {
            let comment = $(this).data("comment");
            $("#reviewCommentContent").text(comment);
            $("#reviewCommentModal").modal("show");
            });
        });
    </script>

</body>
</html>
