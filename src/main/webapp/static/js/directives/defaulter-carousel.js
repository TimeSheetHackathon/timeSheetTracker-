app.directive("defaulter", function($parse){
    return{
        restrict: "E",
        replace: false,
        scope: {data: '=data'},
        link: function(scope, element, attrs){

            var parent = d3.select(element[0]);

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
                for(var j = 0; j < 10 && i+j < numOfElements; j++){
                    grid.append('div')
                        .attr('id', scope.data[i + j].id)
                        .attr('class', "row def-element")
                        .text(scope.data[i + j].name);
                }

                if(i === 0){
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
        }
    }
});