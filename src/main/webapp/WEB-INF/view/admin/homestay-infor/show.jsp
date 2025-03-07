<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <title>Quản lý thông tin homestay</title>
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
                <form action="/admin/homestay-infor" method="get" class="search-form">
                    <input type="text" class="form-control form-control-sm" name="keyword" placeholder="Tìm kiếm thông tin..." 
                            value="${keyword}">
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
                                    <h4 class="card-title">Danh sách thông tin</h4>
                                    <a href="/admin/homestay-infor/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới thông tin
                                    </a>
                                </div>

                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Tiêu đề</th>
                                                <th>Mô tả chi tiết</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty infors}">
                                                    <tr>
                                                        <td colspan="7" class="text-center text-danger">Không tìm thấy thông tin nào.</td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="infor" items="${infors}">
                                                        <tr style="height: 60px;">
                                                            <td>${infor.title}</td>
                                                            <td style="max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                                                ${infor.description}
                                                            </td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <button class="btn btn-success btn-sm" data-infor-id="${infor.inforID}" onclick="showDetail(this)">
                                                                        <i class="bi bi-eye"></i>
                                                                    </button>
                                                                    <a href="/admin/homestay-infor/update/${infor.inforID}" class="btn btn-warning btn-sm" title="Sửa">
                                                                        <i class="bi bi-pencil"></i>
                                                                    </a>

                                                                    <button class="btn btn-danger btn-sm"
                                                                        onclick="checkBeforeDelete(this)" 
                                                                            data-entity-id="${infor.inforID}" 
                                                                            data-entity-name="${infor.title}" 
                                                                            data-entity-type="thông tin" 
                                                                            data-delete-url="/admin/homestay-infor/delete" 
                                                                            data-id-name="inforID">
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

                <jsp:include page="../layout/partial/_pagination-with-keyword.jsp">
                    <jsp:param name="url" value="/admin/homestay-infor" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="keyword" value="${keyword}" />
                </jsp:include>
            </div>
        </div>
    </div>
  </div>

    <div class="modal fade" id="detailModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog" style="margin-top: 1%;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Chi tiết thông tin</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <h5 id="detailTitle"></h5>
                    
                    <p id="detailDescription"></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                </div>
            </div>
        </div>
    </div>

<script>
    function showDetail(button) {
        let inforID = $(button).data("infor-id");
        $.ajax({
            url: '/admin/homestay-infor/' + inforID,
            type: 'GET',
            success: function(response) {
                $('#detailTitle').text(response.title);
                $('#detailDescription').html(response.description.replace(/\n/g, "<br>"));
                $('#detailModal').modal('show');
            },
            error: function(xhr) {
                alert('Lỗi: ' + xhr.responseText);
            }
        });
    }
</script>
<jsp:include page="../layout/import-js.jsp" />
<jsp:include page="../layout/partial/_modal-delete-not-check-can-delete.jsp" />
</body>
</html>
