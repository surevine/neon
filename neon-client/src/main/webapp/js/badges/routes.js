define([ 'angular', 'config' ], function(ng, config) {
	return [ '$routeProvider', function($routeProvider, module) {
		$routeProvider.when('/badges/class/:badgeClassId', {
			templateUrl : 'js/badges/partials/badgeClass.html',
			controller : 'BadgeClassCtrl'
		}).when('/badges/assertion/:badgeAssertionId', {
			templateUrl : 'js/badges/partials/badgeAssertion.html',
			controller : 'BadgeAssertionCtrl'
		});
	} ];
});