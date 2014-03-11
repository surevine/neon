define([ 'angular', 'config' ], function(ng, config) {
	return [ '$routeProvider', function($routeProvider, module) {
		$routeProvider
        .when('/badges/class/:badgeClassId', {
			templateUrl : 'js/badges/partials/badgeClass.html',
			controller : 'BadgeClassCtrl'
		})
        .when('/admin/badges/class/:badgeClassId', {
			templateUrl : 'js/badges/partials/badgeClassAdmin.html',
			controller : 'BadgeClassCtrl'
		})
        .when('/admin/badges/assertion/:badgeAssertionId', {
			templateUrl : 'js/badges/partials/badgeAssertionAdmin.html',
			controller : 'BadgeAssertionCtrl'
		});
	} ];
});