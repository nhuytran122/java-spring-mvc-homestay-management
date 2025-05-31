<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
  $(document).ready(function () {
    $("#branchSelect").change(function () {
      var branchId = $(this).val();
      var roomSelect = $("#roomSelect");
      $.ajax({
        url: "/admin/maintenance/rooms-by-branch",
        type: "GET",
        data: { branchId: branchId },
        dataType: "json",
        success: function (rooms) {
          if (Array.isArray(rooms) && rooms.length > 0) {
            roomSelect.empty();
            roomSelect.append('<option value="">Chọn phòng</option>');
            $.each(rooms, function (index, room) {
              if (room && room.roomId && room.roomNumber) {
                roomSelect.append(
                  '<option value="' +
                    room.roomId +
                    '">' +
                    room.roomNumber +
                    "</option>"
                );
              }
            });
          } else {
            roomSelect
              .empty()
              .append(
                '<option value="">Hiện chi nhánh không có phòng</option>'
              );
          }
          // console.log("Rooms JSON:", rooms);
        },
        error: function (xhr, status, error) {
          console.error("Error:", error, xhr.responseText);
          roomSelect.empty().append('<option value="">Lỗi tải phòng</option>');
          alert("Lỗi server: " + error);
        },
      });
    });
  });
</script>
