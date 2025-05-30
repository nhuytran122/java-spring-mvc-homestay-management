<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
  $(document).ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function (e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });

    $("#addRuleModal").on("show.bs.modal", function () {
      $(".error-message").remove();
      $(".rule-description").removeClass("is-invalid");
    });

    $(".rule-description").on("input", function () {
      $(this).removeClass("is-invalid");
      $(this).next(".error-message").remove();
    });
  });

  function saveSelectedRules() {
    let selectedRules = [];
    let hasError = false;

    $(".rule-checkbox:checked").each(function () {
      let ruleId = $(this).data("rule-id");
      let descriptionInput = $(
        ".rule-description[data-rule-id='" + ruleId + "']"
      );
      let description = descriptionInput.val().trim();

      if (description === "") {
        hasError = true;
        descriptionInput.addClass("is-invalid");
        descriptionInput.after(
          "<span class='text-danger error-message'>Vui lòng nhập mô tả!</span>"
        );
      } else {
        selectedRules.push({
          ruleId: ruleId,
          description: description,
        });
      }
    });

    if (hasError) return;

    if (selectedRules.length === 0) {
      $("#errorModal .modal-body").html(
        "<p class='text-danger'>Vui lòng chọn ít nhất một quy tắc để lưu!</p>"
      );
      $("#errorModal").modal("show");
      return;
    }

    $.ajax({
      url: "/admin/homestay-infor/rule/create",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify(selectedRules),
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
    let ruleId = $(button).data("rule-id");
    let ruleTitle = $(button).data("rule-title");
    let ruleDescription = $(button).data("rule-description");

    $("#editRuleId").val(ruleId);
    $("#editRuleTitle").val(ruleTitle);
    $("#editRuleDescription").val(ruleDescription);

    $("#updateRuleModal").modal("show");
  }

  function updateRule() {
    let ruleId = $("#editRuleId").val();
    let description = $("#editRuleDescription").val().trim();

    if (description === "") {
      $("#editRuleDescription").addClass("is-invalid");
      $("#editRuleDescription").after(
        "<span class='text-danger error-message'>Vui lòng nhập mô tả!</span>"
      );
      return;
    }

    $.ajax({
      url: "/admin/homestay-infor/rule/update",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ ruleId: ruleId, description: description }),
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
</script>
