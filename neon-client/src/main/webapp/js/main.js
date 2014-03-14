require(['js/requireConfig.js', 'config'], function() {
	// Start the main app logic.
	require([ 'angular', 'app', 'domReady!', 'angularLocale' ], function(angular, app, document) {
		'use strict';
		angular.bootstrap(document, [ app['name'] ]);
	});	
});
