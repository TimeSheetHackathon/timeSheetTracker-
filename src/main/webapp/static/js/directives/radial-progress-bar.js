var percentColors = [
    { pct: 0.0, color: { r: 0xff, g: 0x00, b: 0 } },
    { pct: 0.8, color: { r: 0xff, g: 0xaa, b: 0 } },
    { pct: 1.0, color: { r: 0x00, g: 0xaa, b: 0 } } ];

var getColorForPercentage = function(pct) {
    for (var i = 1; i < percentColors.length - 1; i++) {
        if (pct < percentColors[i].pct) {
            break;
        }
    }
    var lower = percentColors[i - 1];
    var upper = percentColors[i];
    var range = upper.pct - lower.pct;
    var rangePct = (pct - lower.pct) / range;
    var pctLower = 1 - rangePct;
    var pctUpper = rangePct;
    var color = {
        r: Math.floor(lower.color.r * pctLower + upper.color.r * pctUpper),
        g: Math.floor(lower.color.g * pctLower + upper.color.g * pctUpper),
        b: Math.floor(lower.color.b * pctLower + upper.color.b * pctUpper)
    };
    return 'rgb(' + [color.r, color.g, color.b].join(',') + ')';
    // or output as hex if preferred
}

app.directive("radial", function($parse){
    return{
        restrict: "E",
        replace: false,
        scope: {data: '=chartData'},
        link: function(scope, element, attrs){
            var colors = {
                'pink': '#E1499A',
                'yellow': '#f0ff08',
                'green': '#44e4ee'
            };

            var color = colors.green;

            console.log(scope.data);

            for (var i = 0; i < scope.data.length; i++) {
                var radius = 75;
                var border = 15;
                var padding = 30;
                var startPercent = 0;
                var endPercent = (100 - scope.data[i].missingTimeSheetPercentage)/100;


                var twoPi = Math.PI * 2;
                var formatPercent = d3.format('.0%');
                var boxSize = (radius + padding) * 2;


                var count = Math.abs((endPercent - startPercent) / 0.01);
                var step = endPercent < startPercent ? -0.01 : 0.01;

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

                var foreground = meter.append('path')
                    .attr('class', 'foreground')
                    .attr('fill', getColorForPercentage(endPercent))
                    .attr('fill-opacity', 1)
                    .attr('stroke', getColorForPercentage(endPercent))
                    .attr('stroke-width', 5)
                    .attr('stroke-opacity', 1)
                    .attr('filter', 'url(#blur)');

                var front = meter.append('path')
                    .attr('class', 'foreground')
                    .attr('fill', getColorForPercentage(endPercent))
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


