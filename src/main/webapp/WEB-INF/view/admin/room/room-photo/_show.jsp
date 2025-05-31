<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="row">
    <div class="col-md-12 grid-margin stretch-card">
        <div class="card position-relative">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h4 class="card-title">Danh sách ảnh</h4>
                    <form action="/admin/room/photo/create" method="get">
                        <input type="hidden" name="roomId" value="${room.roomId}">
                        <button type="submit" class="btn btn-primary btn-sm">
                            <i class="bi bi-plus-circle"></i> Thêm mới ảnh
                        </button>
                    </form>
                    
                </div>

                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr>
                                <th style="width: 150px;">Ảnh</th>
                                <th>Ẩn ảnh</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty listPhotos}">
                                    <tr>
                                        <td colspan="7" class="text-center text-danger">Hiện không có ảnh nào</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="photo" items="${listPhotos}">
                                        <tr>
                                            <td>
                                                <img src="/images/room/${photo.photo}" class="img-fluid rounded" style="width: auto; height: 100px; object-fit: cover;">
                                            </td>
                                            <td>
                                                <span class="badge ${photo.hidden ? 'bg-danger' : 'bg-success'}">
                                                    ${photo.hidden ? 'Ẩn' : 'Hiển thị'}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <a href="/admin/room/photo/update/${photo.photoId}" class="btn btn-warning btn-sm" title="Sửa">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>

                                                    <button class="btn btn-danger btn-sm"
                                                        onclick="checkBeforeDelete(this)" 
                                                            data-entity-id="${photo.photoId}" 
                                                            data-entity-type="ảnh" 
                                                            data-delete-url="/admin/room/photo/delete" 
                                                            data-id-name="photoId">
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