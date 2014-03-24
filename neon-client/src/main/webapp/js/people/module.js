define([ 'angular', './services', './controllers',
		'./routes' ], function(angular, services, controllers, routesConfig) {
	var module = angular.module(
			'people',
			[ services['name'], controllers['name'] ]).config(routesConfig);

	module.initDeps = function() {

	}

	return module;
});