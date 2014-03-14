define([ 'angular', './directives' ], function(ng, directives) {
	var module = angular.module(
			'metastats',
			[ directives['name'] ]);
	
	return module;
});