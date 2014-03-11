define([ 'angular', 'config' ], function(ng, config) {
	return [ '$routeProvider', function($routeProvider, module) {
		$routeProvider.when('/skill/:skillName', {
			templateUrl : 'js/skills/partials/detail.html',
			controller : 'SkillCtrl'
		});
	} ];
});