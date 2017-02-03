function scaleBetween(unscaledNum, minAllowed, maxAllowed, min, max) {
    return (maxAllowed - minAllowed) * (unscaledNum - min) / (max - min) + minAllowed;
}

app.directive("boat", function () {
    return {
        restrict: "E",
        replace: true,
        scope: {
            record: '=record'
        },
        link: function (scope, element, attrs) {
            var oceanHeight = $('.ocean').height();

            var unscaled = (100 - scope.record.missingTimeSheetPercentage);

            var scaled = scaleBetween(unscaled, 50, oceanHeight, scope.record.minRange, scope.record.maxRange);
            var topValue = oceanHeight - Math.floor(scaled);

            $(element[0]).css("top", topValue);
            if(topValue > 50){
                $(element[0]).css("transform", "rotate(12deg)");
            }

        },
        templateUrl: '/static/js/templates/boat-new.html'
    };
});

