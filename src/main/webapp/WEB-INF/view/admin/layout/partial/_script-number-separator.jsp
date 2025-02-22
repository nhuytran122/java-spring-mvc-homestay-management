<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.inputmask/5.0.9/jquery.inputmask.min.js"></script>
<script>
    $(document).ready(function () {
        $('.number-separator').inputmask({
            alias: "numeric",
            groupSeparator: ",",       // Dấu phẩy phân cách phần nghìn
            autoGroup: true,           // Tự động thêm dấu phân cách
            digits: 0,                 // Không có chữ số thập phân
            rightAlign: false,
            removeMaskOnSubmit: true   // Loại bỏ ký tự phân cách phần nghìn khi submit form
        });
    });
</script>