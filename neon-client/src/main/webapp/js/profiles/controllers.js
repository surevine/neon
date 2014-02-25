define([ "angular", './services', './controllers/profileDetails' ], function(
		angular, services, profileCtrl) {
	return angular.module('profiles.controllers', [ services['name'] ])
			.controller('ProfileCtrl', profileCtrl);
});