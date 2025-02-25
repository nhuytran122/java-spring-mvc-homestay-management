<script>
    function setupImagePreview(imageFolder, defaultImage) {
        const fileInput = $("#fileInput");
        const previewImage = $(".imagePreview");
        const oldImage = $("#oldImage").val();

        fileInput.change(function (e) {
            if (e.target.files.length > 0) {
                const imgURL = URL.createObjectURL(e.target.files[0]);
                previewImage.attr("src", imgURL);
            } else {
                previewImage.attr("src", oldImage ? `/images/${imageFolder}/${oldImage}` : `/images/${imageFolder}/${defaultImage}`);
            }
        });
    }
</script>
