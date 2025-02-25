<script>
    $(document).ready(() => {
      const file = $("#fileInput");
      $('.imagePreview').css('display', 'none');
      file.change(function (e) {
        const imgURL = URL.createObjectURL(e.target.files[0]);
        $(".imagePreview").attr("src", imgURL);
        $(".imagePreview").css({ "display": "block"});
      });
    });
  </script>