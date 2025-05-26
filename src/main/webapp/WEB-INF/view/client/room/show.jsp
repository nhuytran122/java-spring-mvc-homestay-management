<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách phòng - Lullaby Homestay</title>
    <jsp:include page="../layout/import-css.jsp" />
</head>
<body>
    <jsp:include page="../layout/header.jsp" />

    <div class="container py-4 mt-5">
        <div class="row mt-5">
            <div class="col-lg-3 mb-4">
                <div class="card border-0 shadow-sm">
                    <div class="card-header bg-white">
                        <h5 class="mb-0">Tìm kiếm phòng</h5>
                    </div>
                    <c:set var="cRoomTypeID" value="${criteria.roomTypeID}" />
                    <c:set var="cBranchID" value="${criteria.branchID}" />
                    <form action="/room" method="get" class="search-form">
                        <div class="card-body">
                            <div class="mb-4">
                                <h6 class="fw-bold mb-3">Loại phòng</h6>
                                <select name="roomTypeID" class="form-select">
                                    <option value="">Chọn loại phòng</option>
                                    <c:forEach var="type" items="${listRoomTypes}">
                                        <option value="${type.roomTypeID}" ${type.roomTypeID == cRoomTypeID ? 'selected' : ''}>
                                            ${type.name}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <hr>

                            <div class="mb-4">
                                <h6 class="fw-bold mb-3">Chi nhánh</h6>
                                <select name="branchID" class="form-select">
                                    <option value="">Chọn chi nhánh</option>
                                    <c:forEach var="branch" items="${listBranches}">
                                        <option value="${branch.branchID}" ${branch.branchID == cBranchID ? 'selected' : ''}>
                                            ${branch.branchName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary w-100">Tìm kiếm</button>
                            <c:if test="${not empty errorMessage}">
                                <div class="text-danger mt-2 text-center">${errorMessage}</div>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-lg-9">
                <div class="row">
                    <c:forEach var="room" items="${rooms}">
                        <div class="col-md-6 col-lg-6 mb-4">
                            <a href="/room/${room.roomID}" class="text-decoration-none text-dark">
                                <div class="card h-100 border-0 shadow-sm">
                                    <div class="position-relative">
                                        <c:choose>
                                            <c:when test="${not empty room.thumbnail}">
                                                <img src="/images/room/${room.thumbnail}" class="card-img-top" alt="${room.roomType.name}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="/images/room/default-img.jpg" class="card-img-top" alt="${room.roomType.name}">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="card-body d-flex flex-column">
                                        <div class="d-flex justify-content-between align-items-center mb-2">
                                            <h5 class="card-title mb-0">Phòng ${room.roomNumber} - ${room.roomType.name}</h5>
                                        </div>
                                        <p class="card-text text-muted mb-3"><i class="bi bi-geo-alt"></i>${room.branch.branchName} - ${room.branch.address}</p>
                                        <div class="mb-3">
                                            <c:forEach var="item" items="${room.roomAmenities}">
                                                <span class="badge bg-light text-dark me-1">${item.amenity.amenityName}</span>
                                            </c:forEach>
                                        </div>
                                        <div class="mt-auto d-flex justify-content-between align-items-center">
                                            <div>
                                                
                                            </div>
                                            <a href="/room/${room.roomID}" class="btn btn-primary">Đặt phòng</a>
                                        </div>
                                    </div>
                                    
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>

                <jsp:include page="../layout/partial/_pagination-with-param.jsp">
                    <jsp:param name="url" value="/room" />
                    <jsp:param name="currentPage" value="${currentPage}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="extraParams" value="${extraParams}" />
                </jsp:include>
            </div>
        </div>
    </div>
    <jsp:include page="../layout/footer.jsp" />
    <jsp:include page="../layout/import-js.jsp" />
</body>
</html>