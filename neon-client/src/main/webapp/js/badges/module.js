define([ 'angular', './controllers', './routes' ], function(angular,
		controllers, routesConfig) {
	var module = angular.module('badges',
			[ controllers['name'] ]).config(routesConfig);
	return module;
});