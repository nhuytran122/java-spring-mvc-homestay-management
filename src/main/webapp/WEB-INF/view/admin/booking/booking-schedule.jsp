<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://lullabyhomestay.com/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Lịch trạng thái phòng</title>
    <jsp:include page="../layout/import-css.jsp" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.css" />
    <link rel="stylesheet" href="/admin/css/booking-schedule-style.css">
</head>

<body>
<div class="container-scroller">
    <jsp:include page="../layout/header.jsp" />
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="../layout/theme-settings.jsp" />
        <jsp:include page="../layout/sidebar.jsp" />
        <div class="main-panel">
            <div class="search-form-container my-4">
                <form action="/admin/booking/schedule" method="get" class="search-form">
                    <select name="branchID" class="form-select form-control form-select-sm">
                        <option value="">Chọn chi nhánh</option>
                        <c:forEach var="branch" items="${listBranches}">
                            <option value="${branch.branchID}" ${branch.branchID == branchID ? 'selected' : ''}>
                                ${branch.branchName}
                            </option>
                        </c:forEach>
                    </select>

                    <input type="text" id="date" name="date" class="form-control" value="${dateFormatted}" required style="width: 150px;">
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
                                    <h4 class="card-title">Lịch trạng thái phòng - Ngày: ${dateFormatted}</h4>
                                    <a href="/admin/booking/create" class="btn btn-primary btn-sm">
                                        <i class="bi bi-plus-circle"></i> Thêm mới đặt phòng
                                    </a>
                                </div>
                                <div class="schedule-container">
                                    <div class="timeline-header">
                                        <div class="room-label">Phòng</div>
                                        <div class="timeline">
                                            <div class="timeline-grid"></div> 
                                            <c:forEach var="hour" begin="0" end="23">
                                                <div class="timeline-hour">${hour}:00</div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <c:forEach var="room" items="${listRooms}">
                                        <div class="timeline-room">
                                            <div class="room-label">
                                                ${room.roomNumber} (${room.branch.branchName})
                                            </div>
                                            <div class="timeline">
                                                <div class="timeline-grid"></div> 
                                                <c:forEach var="statusEntry" items="${roomSchedules[room.roomID]}">
                                                    <c:set var="startHour" value="${statusEntry.startedAt.hour + statusEntry.startedAt.minute / 60.0}" />
                                                    <c:set var="endHour" value="${statusEntry.endedAt.hour + statusEntry.endedAt.minute / 60.0}" />
                                                        <div class="status-block ${statusEntry.status.cssClass}"
                                                             data-start-hour="${startHour}" 
                                                             data-end-hour="${endHour}"
                                                             title="${statusEntry.status.description} (${statusEntry.startedAt.hour}:${statusEntry.startedAt.minute < 10 ? '0' : ''}${statusEntry.startedAt.minute} - ${statusEntry.endedAt.hour}:${statusEntry.endedAt.minute < 10 ? '0' : ''}${statusEntry.endedAt.minute})">
                                                            <c:choose>
                                                                <c:when test="${statusEntry.status.hasLink}">
                                                                    <a href="/admin/booking/${statusEntry.booking.bookingID}" style="color: inherit; text-decoration: none;">
                                                                        ${statusEntry.status.description}
                                                                    </a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${statusEntry.status.description}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../layout/import-js.jsp" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.4/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/3.0.5/daterangepicker.min.js"></script>
<script src="/admin/js/schedule.js"></script>
</body>
</html>