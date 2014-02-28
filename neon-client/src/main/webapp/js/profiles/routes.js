define([ 'angular', 'config' ], function(ng, config) {
	return [ '$routeProvider', function($routeProvider, module) {
		$routeProvider.when('/profile/:userId', {
			templateUrl : 'js/profiles/partials/detail.html',
			controller : 'ProfileCtrl'
		}).otherwise({
			redirectTo : '/profile'
		});
	} ];
});