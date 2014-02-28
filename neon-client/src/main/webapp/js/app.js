// Start the main app logic.
define([ 'angular', 'angularRoute', 'profiles/module' ], function(angular,
		angularRoute, profilesModule) {
	return angular.module('neon', [ 'ngRoute',
			profilesModule['name'] ]);
});