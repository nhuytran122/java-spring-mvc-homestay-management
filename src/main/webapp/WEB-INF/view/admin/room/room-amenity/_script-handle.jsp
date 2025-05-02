<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
  $(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });

    $("#updateRoomAmenityModal").on("show.bs.modal", function () {
      $("#quantityError").css("display", "none");
    });
  });

  function saveSelectedAmenities() {
    let selectedItems = [];
    let hasError = false;

    $(".item-checkbox:checked").each(function () {
      let roomID = $(this).data("room-id");
      let amenityID = $(this).data("amenity-id");
      let quantityInput = $(
        ".amenity-quantity[data-amenity-id='" + amenityID + "']"
      );
      let quantity = quantityInput.val().trim();

      // Reset lại trạng thái lỗi
      quantityInput.removeClass("is-invalid");
      if (quantity) {
        if (isNaN(quantity) || parseInt(quantity) <= 0) {
          hasError = true;
          quantityInput.addClass("is-invalid");
        }
      }

      selectedItems.push({
        roomAmenityID: {
          roomID: roomID,
          amenityID: amenityID,
        },
        quantity: quantity,
      });
    });

    if (selectedItems.length === 0) {
      $("#errorModal .modal-body").html(
        "<p class='text-danger'>Vui lòng chọn ít nhất một tiện nghi để lưu!</p>"
      );
      $("#errorModal").modal("show");
      return;
    }

    if (hasError) {
      return; // Dừng lại nếu có lỗi
    }

    $.ajax({
      url: "/admin/room/room-amenity/create",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(selectedItems),
      success: function () {
        location.reload();
      },
      error: function () {
        $("#errorModal .modal-body").html(
          "<p class='text-danger'>Có lỗi xảy ra, vui lòng thử lại!</p>"
        );
        $("#errorModal").modal("show");
      },
    });
  }

  function openEditModal(button) {
    let amenityID = $(button).data("amenity-id");
    let roomID = $(button).data("room-id");
    let amenityTitle = $(button).data("amenity-name");
    let quantity = $(button).data("amenity-quantity");

    $("#editAmenityId").val(amenityID);
    $("#editRoomId").val(roomID);
    $("#editAmenityName").val(amenityTitle);
    $("#editAmenityQuantity").val(quantity);

    $("#updateRoomAmenityModal").modal("show");
  }

  function updateRoomAmenity() {
    let amenityID = $("#editAmenityId").val();
    let roomID = $("#editRoomId").val();
    let quantity = $("#editAmenityQuantity").val().trim();
    let $errorDiv = $("#quantityError");

    $errorDiv.hide().text("");
    if (quantity) {
      if (isNaN(quantity) || parseInt(quantity) <= 0) {
        $errorDiv.text("Số lượng phải là số nguyên dương!").show();
        return;
      }
    }

    $.ajax({
      url: "/admin/room/room-amenity/update",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({
        roomAmenityID: {
          roomID: roomID,
          amenityID: amenityID,
        },
        quantity: quantity,
      }),
      success: function () {
        location.reload();
      },
      error: function () {
        $("#errorModal .modal-body").html(
          "<p class='text-danger'>Có lỗi xảy ra, vui lòng thử lại!</p>"
        );
        $("#errorModal").modal("show");
      },
    });
  }

  function checkBeforeDeleteAmenity(button) {
    let amenityID = $(button).data("amenity-id");
    let roomID = $(button).data("room-id");
    let title = $(button).data("amenity-name");

    $("#titleConfirm").text(title);
    $("#amenityIdInput").val(amenityID);
    $("#roomAmenityIdInput").val(roomID);
    $("#deleteAmenityConfirmModal").modal("show");
  }
</script>
