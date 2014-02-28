define([ 'angular', './filters', './services', './directives', './controllers',
		'./routes', './extras' ], function(angular, filters, services, directives,
		controllers, routesConfig, extras) {
	var module = angular.module(
			'profiles',
			[ filters['name'], services['name'], directives['name'],
					controllers['name'], extras['name'] ]).config(routesConfig);
	
	module.initDeps = function() {
		
	}
	
	return module;
});