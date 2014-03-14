// Start the main app logic.
define([ 'angular', 'angularRoute', 'profiles/module', 'badges/module',
		'skills/module', 'metastats/module', 'header/module' ], function(
		angular, angularRoute, profilesModule, badgesModule, skillsModule,
		metastatsModule, headerModule) {
	return angular.module('neon', [ 'ngRoute', profilesModule['name'],
			badgesModule['name'], skillsModule['name'],
			metastatsModule['name'], headerModule['name'] ]);
});