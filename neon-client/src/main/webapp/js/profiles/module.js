define([ 'angular', './filters', './services', './directives', './controllers',
		'./routes' ], function(angular, filters, services, directives,
		controllers, routesConfig) {
	return angular.module(
			'profiles',
			[ filters['name'], services['name'], directives['name'],
					controllers['name'] ]).config(routesConfig);
});