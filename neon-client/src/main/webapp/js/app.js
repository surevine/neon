// Start the main app logic.
define([ 'angular', 'angularRoute', 'profiles/module', 'badges/module', 'skills/module', 'header/module' ], function(angular,
		angularRoute, profilesModule, badgesModule, skillsModule, headerModule) {
	return angular.module('neon', [ 'ngRoute',
			profilesModule['name'], badgesModule['name'], skillsModule['name'], headerModule['name'] ]);
});