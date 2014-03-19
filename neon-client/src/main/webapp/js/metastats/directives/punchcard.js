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
				data : '=metastatsPunchcard'
			},
			link : function(scope, element, attrs) {

				var w = 720, h = 240, pad = 20, left_pad = 50;

				var svg = d3.select(element[0]).append('div').classed(
						'punchcard', true).classed('metastats', true).append(
						'svg').attr("width", w).attr("height", h).attr(
						'viewBox', '0, 0, ' + w + ', ' + h).attr(
						'preserveAspectRatio', 'xMidYMid meet');

				scope.$watchCollection('data', function(data, oldVal) {
					svg.selectAll('*').remove();

					var x = d3.scale.linear().domain([ 0, 23 ]).range(
							[ left_pad, w - pad ]), y = d3.scale.linear()
							.domain([ 0, 6 ]).range([ pad, h - pad * 2 ]);

					var xAxis = d3.svg.axis().scale(x).orient("bottom").ticks(
							24).tickFormat(function(d, i) {
						var m = (d >= 12) ? "pm" : "am";
						return (d % 12 == 0) ? 12 + m : d % 12 + m;
					}), yAxis = d3.svg.axis().scale(y).orient("left").ticks(7)
							.tickFormat(
									function(d, i) {
										return [ 'S', 'M', 'T', 'W', 'T', 'F',
												'S' ][d];
									});

					svg.append("g").attr("class", "axis").attr("transform",
							"translate(0, " + (h - pad) + ")").call(xAxis);

					svg.append("g").attr("class", "axis").attr("transform",
							"translate(" + (left_pad - pad) + ", 0)").call(
							yAxis);

					if (!data) {
						return;
					}

					svg.append("text").attr("class", "loading").text(
							"Loading ...").attr("x", function() {
						return w / 2;
					}).attr("y", function() {
						return h / 2 - 5;
					});

					var pdata = [];

					for ( var i = 0; i < data.length; i++) {
						for ( var j = 0; j < data[i].length; j++) {
							pdata.push([ i, j, data[i][j] ]);
						}
					}

					var max_r = d3.max(pdata.map(function(d) {
						return d[2];
					})), r = d3.scale.linear().domain(
							[ 0, d3.max(pdata, function(d) {
								return d[2];
							}) ]).range([ 0, 12 ]);

					svg.selectAll(".loading").remove();

					svg.selectAll("circle").data(pdata).enter()
							.append("circle").attr("class", "circle").attr(
									"cx", function(d) {
										return x(d[1]);
									}).attr("cy", function(d) {
								return y(d[0]);
							}).transition().duration(800).attr("r",
									function(d) {
										return r(d[2]);
									});
				});
			}
		}
	}
});