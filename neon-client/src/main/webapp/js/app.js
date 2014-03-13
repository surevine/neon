// Start the main app logic.
define([ 'angular', 'angularRoute', 'profiles/module', 'badges/module', 'skills/module', 'search/module' ], function(angular,
		angularRoute, profilesModule, badgesModule, skillsModule, searchModule) {
	return angular.module('neon', [ 'ngRoute',
			profilesModule['name'], badgesModule['name'], skillsModule['name'], searchModule['name'] ]);
});