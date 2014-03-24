define([ 'angular', 'config' ], function(ng, config) {
	return [ '$routeProvider', function($routeProvider, module) {
		$routeProvider.when('/people/:query', {
			templateUrl : 'js/people/partials/detail.html',
			controller : 'PeopleCtrl'
		});
		$routeProvider.when('/people/', {
			templateUrl : 'js/people/partials/detail.html',
			controller : 'PeopleCtrl'
		});
	} ];
});