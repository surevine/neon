define([ "angular", './services', './controllers/people' ], function(
		angular, services, PeopleCtrl) {
	return angular.module('people.controllers', [ services['name'] ])
			.controller('PeopleCtrl', PeopleCtrl)
});