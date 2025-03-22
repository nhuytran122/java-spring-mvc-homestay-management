$(document).ready(function () {
    $('.status-block').each(function () {
        var title = $(this).attr('title');
        if (title) {
            $(this).tooltip({
                title: title,
                placement: 'top',
                trigger: 'hover',
                container: 'body'
            });
        }
    });

    var now = moment();
    var checkInVal = $('#date').val();
    var date = checkInVal && moment(checkInVal, 'DD/MM/YYYY').isValid() ? moment(checkInVal, 'DD/MM/YYYY') : now;

    $('#date').daterangepicker({
        singleDatePicker: true,
        startDate: date,
        locale: {
            format: 'DD/MM/YYYY',
            daysOfWeek: ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7'],
            monthNames: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12']
        }
    });

    $('.schedule-container').scrollLeft(0);

    function adjustStatusBlocks() {
        var isMobile = $(window).width() <= 768;
        var columnWidth = isMobile ? 50 : 75; 
        $('.status-block').each(function () {
            var $block = $(this);
            var startHour = parseFloat($block.data('start-hour'));
            var endHour = parseFloat($block.data('end-hour'));

            var leftPosition = startHour * columnWidth;
            var width = (endHour - startHour) * columnWidth;

            if (width < columnWidth) {
                width = columnWidth;
            }

            $block.css({
                'left': leftPosition + 'px',
                'width': width + 'px'
            });
        });
    }

    adjustStatusBlocks();
    $(window).on('resize', adjustStatusBlocks);
});
