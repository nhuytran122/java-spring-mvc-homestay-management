<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
    function showDetail(button) {
        let amenityID = button.getAttribute("data-amenity-id");
        $.ajax({
            url: '/admin/amenity/' + amenityID,
            type: 'GET',
            success: function(response) {
                $('#amenityName').text(response.title);
                $('#detailDescription').html(response.description.replace(/\n/g, "<br>"));
                $('#detailModal').modal('show');
            },
            error: function(xhr) {
                alert('Lỗi: ' + xhr.responseText);
            }
        });
    }
</script>