var getCountry = function () {
    return window.location.pathname.substr(1);
}

app.directive("country", function ($parse) {
    return {
        restrict: "E",
        replace: false,
        scope: {data: '=chartData'},
        link: function (scope, element, attrs) {
            d3.select(".countryName")
                .attr("width","250px")
                .text(getCountry());
        }
    }
});

