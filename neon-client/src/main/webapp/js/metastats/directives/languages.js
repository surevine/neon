define([ 'd3' ],
		function(d3) {
			return function() {

				return {
					restrict : 'AEC',
					scope : {
						languageData : '=metastatsLanguages'
					},
					link : function(scope, element, attrs) {
						var compareLanguages = function(a, b) {
							return b.lines - a.lines;
						};

						var width = 420, height = 420, radius = Math.min(width,
								height) / 2;

						var color = d3.scale.ordinal().range(
								[ "#98abc5", "#8a89a6", "#7b6888", "#6b486b",
										"#a05d56", "#d0743c", "#ff8c00" ]);

						var arc = d3.svg.arc().outerRadius(radius - 10)
								.innerRadius(radius - 120);

						var pie = d3.layout.pie().sort(null).value(function(d) {
							return d.lines;
						});

						var svg = d3.select(element[0]).append('div').classed(
								'languagesPie', true).classed('metastats', true)
								.append('svg').attr("width", width).attr(
										"height", height).attr('viewBox',
										'0, 0, ' + width + ', ' + height).attr(
										'preserveAspectRatio', 'xMidYMid meet')
								.append("g").attr(
										"transform",
										"translate(" + width / 2 + "," + height
												/ 2 + ")");

						scope.$watchCollection('languageData', function(
								languages, oldVal) {
							svg.selectAll('*').remove();

							var rawPieData = [];
							for ( var language in languages) {
								rawPieData.push({
									"name" : language,
									"lines" : languages[language],
									"title" : languages[language] + ' lines'
								});
							}

							rawPieData.sort(compareLanguages);
							
							var pieData = [];
							
							var pc = 0, total = 0, others = 0, othersText = '';
							
							rawPieData.forEach(function(d) {
								total += d.lines;
							});

							rawPieData.forEach(function(d) {
								d.lines = +d.lines;
								
								if(d.lines > total / 30) {
									pieData.push(d);
								} else {
									others += d.lines;
									if(othersText != '') {
										othersText += ', ';
									}
									othersText += d.name;
								}
							});
							
							if(others > 0) {
								pieData.push({
									"name" : "Others",
									"lines" : others,
									"title" : othersText
								});
							}

							var g = svg.selectAll(".arc").data(pie(pieData))
									.enter().append("g").attr("class", "arc");

							g.append("path").attr("d", arc).style("fill",
									function(d) {
										return color(d.data.name);
									});

							g.append("text").attr("transform", function(d) {
								return "translate(" + arc.centroid(d) + ")";
							}).attr("dy", ".35em").style("text-anchor",
									"middle").text(function(d) {
								return d.data.name;
							});
							
							g.append("title").text(function(d) {
								return d.data.title;
							});
						});
					}
				}
			}
		});