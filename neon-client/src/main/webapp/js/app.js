// Start the main app logic.
define([ 'angular', 'angularRoute', 'angularSanitize', 'angularStrap', 'angularStrapTpl', 'profiles/module', 'badges/module', 'skills/module', 'metastats/module', 'header/module', 'people/module' ],
       function(angular, angularRoute, angularSanitize, angularStrap, angularStrapTpl, profilesModule, badgesModule, skillsModule, metastatsModule, headerModule, peopleModule) {
	     return angular.module('neon', [ 'ngRoute', 'ngSanitize', 'mgcrea.ngStrap', profilesModule['name'], badgesModule['name'], skillsModule['name'], metastatsModule['name'], headerModule['name'], peopleModule['name'] ])
	     .config(['$compileProvider', function( $compileProvider ) {
	    	 $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|xmpp):/);
	     }]);
});