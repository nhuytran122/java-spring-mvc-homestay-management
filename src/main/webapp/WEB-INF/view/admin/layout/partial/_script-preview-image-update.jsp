<script>
    function setupImagePreview(imageFolder) {
        const fileInput = $("#fileInput");
        const previewContainer = $(".image-preview-container");
        const oldImage = $("#oldImage").val();

        function updatePreview(src) {
            previewContainer.empty();

            if (src) {
                const previewImage = $("<img>", {
                    src: src,
                    class: "imagePreview",
                    alt: "Preview ảnh"
                });
                previewContainer.append(previewImage);
            }
        }

        fileInput.on("change", function (e) {
            if (e.target.files.length > 0) {
                const imgURL = URL.createObjectURL(e.target.files[0]);
                updatePreview(imgURL);
            } else {
                if (oldImage) {
                    updatePreview(`/images/${imageFolder}/${oldImage}`);
                } else {
                    updatePreview(null);
                }
            }
        });
    }
</script>