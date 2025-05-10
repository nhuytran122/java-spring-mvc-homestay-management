$(document).ready(function () {
  let dateRangeInput = $("input.daterange-picker");
  let currentRange = dateRangeInput.val();
  $(".daterange-picker").daterangepicker({
    locale: {
      format: "DD/MM/YYYY",
      daysOfWeek: ["CN", "T2", "T3", "T4", "T5", "T6", "T7"],
      monthNames: [
        "Tháng 1",
        "Tháng 2",
        "Tháng 3",
        "Tháng 4",
        "Tháng 5",
        "Tháng 6",
        "Tháng 7",
        "Tháng 8",
        "Tháng 9",
        "Tháng 10",
        "Tháng 11",
        "Tháng 12",
      ],
      applyLabel: "Áp dụng",
      cancelLabel: "Hủy",
      customRangeLabel: "Tùy chỉnh",
    },
    startDate: moment(currentRange.split(" - ")[0], "DD/MM/YYYY"),
    endDate: moment(currentRange.split(" - ")[1], "DD/MM/YYYY"),
    maxDate: moment(),
    ranges: {
      "Hôm nay": [moment(), moment()],
      "Tháng này": [moment().startOf("month"), moment().endOf("month")],
      "Tháng trước": [
        moment().subtract(1, "month").startOf("month"),
        moment().subtract(1, "month").endOf("month"),
      ],
    },
  });

  let ctx = document.getElementById("revenueBreakdownChart").getContext("2d");

  let $dataDiv = $("#revenue-data");
  let chartTotalRevenue = Number($dataDiv.data("total-revenue")) || 0;
  let chartRoomRevenue = Number($dataDiv.data("room")) || 0;
  let chartServiceRevenue = Number($dataDiv.data("service")) || 0;
  let chartExtensionRevenue = Number($dataDiv.data("extension")) || 0;

  if (
    chartRoomRevenue > 0 ||
    chartServiceRevenue > 0 ||
    chartExtensionRevenue > 0
  ) {
    let revenueChart = new Chart(ctx, {
      type: "pie",
      data: {
        labels: ["Doanh Thu Phòng", "Doanh Thu Dịch Vụ", "Doanh Thu Gia Hạn"],
        datasets: [
          {
            data: [
              chartRoomRevenue,
              chartServiceRevenue,
              chartExtensionRevenue,
            ],
            backgroundColor: ["#28a745", "#ffc107", "#007bff"],
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: "bottom",
            labels: {
              padding: 20,
              font: {
                size: 12,
              },
            },
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                let label = context.label || "";
                let value = context.raw || 0;
                let total = chartTotalRevenue;
                console.log(total);
                let percentage =
                  total > 0 ? ((value / total) * 100).toFixed(1) : 0;

                let tooltipText =
                  label +
                  ": " +
                  value.toLocaleString("vi-VN") +
                  "đ (" +
                  percentage +
                  "%)";

                return tooltipText;
              },
            },
          },
        },
      },
    });
  } else {
    $("#revenueBreakdownChart")
      .parent()
      .html(
        '<div class="text-center my-5"><p class="text-muted">Không có dữ liệu để hiển thị biểu đồ</p></div>'
      );
  }
});
