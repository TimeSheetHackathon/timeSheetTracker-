$(function () {
    for (var i = 0; i < 100; i++) {
        $('.wrapper').append('<div class="wave"></div>');
    }

    $('.wave').each(function () {
        $(this).css('left', (Math.random() * 100) - 5 + '%');
        $(this).css('animation-duration', (Math.random()) + 1 + 's');
    });

    for (var i = 0; i < 5; i++) {
        $('body').append('<div class="fish"><div class="head"></div><div class="body"></div><div class="tail"></div><div class="eye"></div></div>');
    }

    $('.fish').each(function () {
        $(this).css('animation-duration', (Math.random() * 100) + 30 + 's');
        $(this).css('transform', 'scale(' + Math.random() + ')');
        $(this).css('opacity', Math.random());
        $(this).css('top', (Math.random() * 30) + 70 + '%');
    });
});