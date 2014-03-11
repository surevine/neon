define([ 'angular', 'config' ], function(ng, config) {
	return [ '$routeProvider', function($routeProvider, module) {
		$routeProvider
        .when('/admin/badges/class/:badgeClassId', {
			templateUrl : 'js/badges/partials/badgeClass.html',
			controller : 'BadgeClassCtrl'
		})
        .when('/admin/badges/assertion/:badgeAssertionId', {
			templateUrl : 'js/badges/partials/badgeAssertion.html',
			controller : 'BadgeAssertionCtrl'
		});
	} ];
});