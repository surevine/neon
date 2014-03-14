define([ 'd3' ], function(d3) {
	var defaultOptions = {
		marginTop : 20,
		marginRight : 0,
		marginBottom : 20,
		marginLeft : 0,
		width : 720,
		height : 240
	}

	return function() {

		return {
			restrict : 'AEC',
			scope : {
				commitData : '=metastatsCommitGraph'
			},
			link : function(scope, element, attrs) {
				var margin = {
					top : 20,
					right : 0,
					bottom : 20,
					left : 0
				}, width = 720 - margin.left - margin.right, height = 240
						- margin.top - margin.bottom;

				var formatPercent = d3.format(".0%");

				var x = d3.scale.ordinal().rangeRoundBands([ 0, width ], .1);

				var y = d3.scale.linear().range([ height, 0 ]);

				var xAxis = d3.svg.axis().scale(x).orient("bottom");

				var yAxis = d3.svg.axis().scale(y).orient("left").tickFormat(
						formatPercent);

				var svg = d3.select(element[0]).append('svg').attr("width",
						width + margin.left + margin.right).attr("height",
						height + margin.top + margin.bottom).attr('viewBox',
						'0, 0, ' + width + ', ' + height);

				scope.$watchCollection('commitData', function(commitsByWeek, oldVal) {
					svg.selectAll('*').remove();
					
					if(!commitsByWeek) {
						return;
					}

					var cData = [];
					for (var i = 0; i<commitsByWeek.length; i++) {
						cData.push({"week" :i, "count": commitsByWeek[i] });
					}
					
					svg.append("g")
							.attr(
									"transform",
									"translate(" + margin.left + ","
											+ margin.top + ")");

					cData.forEach(function(d) {
						d.count = +d.count;
					});

					x.domain(cData.map(function(d) {
						return d.week;
					}));
					y.domain([ 0, d3.max(cData, function(d) {
						return d.count;
					}) ]);

					svg.append("g").attr("class", "x axis").attr("transform",
							"translate(700," + (height + 20) + ")").append(
							"text").style("text-anchor", "end").text("Time");
					// .call(xAxis);

					svg.append("g").attr("class", "y axis")
					// .call(yAxis)
					.append("text").attr("transform", "rotate(-90)").attr("y",
							6).attr("dy", ".71em").style("text-anchor", "end")
							.text("Commits");

					svg.selectAll(".bar").data(cData).enter().append("rect")
							.attr("class", "bar").attr("x", function(d) {
								return x(d.week);
							}).attr("width", x.rangeBand()).attr("y",
									function(d) {
										return y(d.count);
									}).attr("height", function(d) {
								return height - y(d.count);
							});
				});
			}
		}
	}
});