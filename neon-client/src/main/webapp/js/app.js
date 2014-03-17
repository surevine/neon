// Start the main app logic.
define([ 'angular', 'angularRoute', 'angularSanitize', 'angularStrap', 'angularStrapTpl', 'profiles/module', 'badges/module', 'skills/module', 'metastats/module', 'header/module' ], 
       function(angular, angularRoute, angularSanitize, angularStrap, angularStrapTpl, profilesModule, badgesModule, skillsModule, metastatsModule, headerModule) {
	     return angular.module('neon', [ 'ngRoute', 'ngSanitize', 'mgcrea.ngStrap', profilesModule['name'], badgesModule['name'], skillsModule['name'], metastatsModule['name'], headerModule['name'] ]);
});