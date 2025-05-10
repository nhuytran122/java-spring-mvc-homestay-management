<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:include page="../layout/partial/_modal-warning.jsp" />
<script>
  function getStatusWarningMessage(status) {
    switch (status) {
      case "COMPLETED":
        return "Việc bảo trì này đã hoàn thành";
      case "CANCELLED":
        return "Việc bảo trì này đã bị hủy";
      case "IN_PROGRESS":
        return "Việc bảo trì này đang được xử lý";
      case "ON_HOLD":
        return "Việc bảo trì này đang tạm hoãn";
      case "PENDING":
        return "Việc bảo trì này đang chờ xử lý";
      default:
        return "Việc bảo trì này có trạng thái không cho phép sửa đổi";
    }
  }

  function checkBeforeUpdate(button) {
    let requestId = $(button).data("request-id");
    let checkUrl = $(button).data("check-url");
    let currentStatus = $(button).data("current-status");

    $.ajax({
      url: checkUrl + requestId,
      type: "GET",
      data: { id: requestId },
      success: function (response) {
        if (response === true) {
          window.location.href = "/admin/maintenance/update/" + requestId;
        } else {
          showWarningModal(getStatusWarningMessage(currentStatus));
        }
      },
      error: function (xhr, status, error) {
        console.error("Lỗi kiểm tra cập nhật:", error);
      },
    });
  }
</script>
