// Start the main app logic.
define([ 'angular', 'angularRoute', 'profiles/module', 'badges/module' ], function(angular,
		angularRoute, profilesModule, badgesModule) {
	return angular.module('neon', [ 'ngRoute',
			profilesModule['name'], badgesModule['name'] ]);
});