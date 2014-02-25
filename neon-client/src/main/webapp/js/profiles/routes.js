define(function() {
	return [ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/profile/:userId', {
			templateUrl : 'js/profiles/partials/detail.html',
			controller : 'ProfileCtrl'
		}).otherwise({
			redirectTo : '/profile'
		});
	} ];
});