var renderCarousel = function (parent,scope) {
    var defaulterCarousel = parent.append('div')
        .attr('id', "defaulterCarousel")
        .attr('class', "carousel slide")
        .attr('data-ride', "carousel");

    var ol = defaulterCarousel.append('ol')
        .attr('class', "carousel-indicators");

    var inner = defaulterCarousel.append('div')
        .attr('class', "carousel-inner")
        .attr('role', "listbox");

    var numOfElements = scope.data.length;
    var numOfPages = Math.ceil(numOfElements / 10);

    for (var i = 0; i < numOfPages; i++) {
        var li = ol.append('li')
            .attr('data-target', "#defaulterCarousel")
            .attr('data-slide-to', i);

        var item = inner.append('div')
            .attr('class', "carousel-item");

        var grid = item.append('div')
            .attr('class', "grid");
        for (var j = 0; j < 10 && (i * 10) + j < numOfElements; j++) {
            grid.append('div')
                .attr('id', scope.data[(i * 10) + j].id)
                .attr('class', "row def-element")
                .text(scope.data[(i * 10) + j].name);
        }

        if (i === 0) {
            li.attr('class', "active");
            item.attr('class', "carousel-item active");
        }

    }

    var leftControl = defaulterCarousel.append('a')
        .attr('class', "carousel-control-prev")
        .attr('href', "#defaulterCarousel")
        .attr('role', "button")
        .attr('data-slide', "prev");

    leftControl.append('span')
        .attr('class', "carousel-control-prev-icon")
        .attr('aria-hidden', "true");

    leftControl.append('span')
        .attr('class', "sr-only")
        .text("Previous");


    var rightControl = defaulterCarousel.append('a')
        .attr('class', "carousel-control-next")
        .attr('href', "#defaulterCarousel")
        .attr('role', "button")
        .attr('data-slide', "next");

    rightControl.append('span')
        .attr('class', "carousel-control-next-icon")
        .attr('aria-hidden', "true");

    rightControl.append('span')
        .attr('class', "sr-only")
        .text("Next");

    $('.carousel').carousel({
        interval: 8000
    })

};

var showMessage = function(parent){
    parent.append("div")
        .attr('class','message')
        .text("Congratulation!!! Office has filled 100% timesheet");
};

app.directive("defaulter", function ($parse) {
    return {
        restrict: "E",
        replace: false,
        scope: {data: '=data'},
        link: function (scope, element, attrs) {

            scope.data = scope.data.map(function (e) {
                e.name = e.name.split(",").reverse().join(" ");
                return e;
            });

            scope.data.sort(function (p, q) {
                return +(p.name > q.name) || +(p.name === q.name) - 1
            });

            var parent = d3.select(element[0]);
            var path = window.location.pathname.split("/");

            if(scope.data.length == 0 && path.length>2)
                return showMessage(parent);
            return renderCarousel(parent, scope);
        }
    }
});