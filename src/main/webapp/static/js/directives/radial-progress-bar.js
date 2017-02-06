var percentColors = [
    {pct: 0.7, color: '#e11520'},
    {pct: 0.9, color: '#ed781f'},
    {pct: 1.0, color: '#7baa68'}];

var getColorForPercentage = function (pct) {
    return percentColors.find(function(colorEntry) {
        return pct <= colorEntry.pct;
    }).color;
};

app.directive("radial", function ($parse) {
    return {
        restrict: "E",
        replace: false,
        scope: {data: '=chartData'},
        link: function (scope, element, attrs) {

            for (var i = 0; i < scope.data.length; i++) {
                var radius = 75;
                var border = 15;
                var padding = 30;
                var endPercent = (100 - scope.data[i].missingTimeSheetPercentage) / 100;
                var twoPi = Math.PI * 2;
                var formatPercent = d3.format('.0%');
                var boxSize = (radius + padding) * 2;
                var arc = d3.svg.arc()
                    .startAngle(0)
                    .innerRadius(radius)
                    .outerRadius(radius - border);

                var parent = d3.select(element[0]);

                var svg = parent.append('svg')
                    .attr('width', boxSize)
                    .attr('height', boxSize + 20);

                var defs = svg.append('defs');

                var g = svg.append('g')
                    .attr('transform', 'translate(' + boxSize / 2 + ',' + boxSize / 2 + ')');

                var meter = g.append('g')
                    .attr('class', 'progress-meter');

                meter.append('path')
                    .attr('class', 'background')
                    .attr('fill', '#ccc')
                    .attr('fill-opacity', 0.5)
                    .attr('d', arc.endAngle(twoPi));

                var colorSelected =  getColorForPercentage(endPercent);

                var foreground = meter.append('path')
                    .attr('class', 'foreground')
                    .attr('fill', colorSelected)
                    .attr('fill-opacity', 1)
                    .attr('stroke', colorSelected)
                    .attr('stroke-width', 5)
                    .attr('stroke-opacity', 1)
                    .attr('filter', 'url(#blur)');

                var front = meter.append('path')
                    .attr('class', 'foreground')
                    .attr('fill', colorSelected)
                    .attr('fill-opacity', 1);

                var numberText = meter.append('text')
                    .attr('fill', '#000000')
                    .attr('text-anchor', 'middle')
                    .attr('dy', '.35em');

                var cityText = meter.append('text')
                    .attr('fill', '#000000')
                    .attr('text-anchor', 'middle')
                    .attr('style', 'font-size: 16px')
                    .attr('dy', '100');

                var progress = endPercent;

                foreground.attr('d', arc.endAngle(twoPi * progress));
                front.attr('d', arc.endAngle(twoPi * progress));
                numberText.text(formatPercent(progress));
                cityText.text(scope.data[i].workingLocation);
            }
        }
    }
});


