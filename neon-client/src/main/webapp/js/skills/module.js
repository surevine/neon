define([ 'angular', './services', './controllers',
		'./routes' ], function(angular, services, controllers, routesConfig) {
	var module = angular.module(
			'skills',
			[ services['name'], controllers['name'] ]).config(routesConfig);

	module.initDeps = function() {

	}

	return module;
});