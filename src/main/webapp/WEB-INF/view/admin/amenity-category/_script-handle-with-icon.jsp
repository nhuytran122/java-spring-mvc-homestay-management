<%@page contentType="text/html" pageEncoding="UTF-8" %>
<script>
    $(document).ready(function() {
        $('#iconModal').on('shown.bs.modal', function () {
            $('#iconSearch').val(''); 
            $('.icon-option').show(); 
        });

        $('.icon-option').on('click', function() {
            let iconValue = $(this).find('.iconify').attr('data-icon'); 
            $('#selectedIcon').val(iconValue); 
            
            let buttonIcon = $('#openIconModal .iconify');
            buttonIcon.attr('data-icon', iconValue); 
            
            $('#openIconModal').html('');
            $('#openIconModal').append(buttonIcon); 

            if (window.Iconify) {
                window.Iconify.scan(buttonIcon[0]); 
            } else {
                console.error('Iconify incomplete');
            }
            $('#iconModal').modal('hide'); 
            $('.icon-option').removeClass('selected');
            $(this).addClass('selected');
        });

        $('#iconSearch').on('input', function() {
            let searchTerm = $(this).val().toLowerCase();
            
            $('.icon-option').each(function() {
                let iconName = $(this).attr('data-icon-name').toLowerCase();
                if (iconName.includes(searchTerm)) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        });
    });
</script>