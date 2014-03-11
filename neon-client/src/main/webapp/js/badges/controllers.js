define([ "angular", './services', './controllers/badgeClass', './controllers/badgeAssertion' ], function(
		angular, services, badgeClassCtrl, badgeAssertionCtrl) {
	return angular.module('badges.controllers', [ services['name'] ])
			.controller('BadgeClassCtrl', badgeClassCtrl)
			.controller('BadgeAssertionCtrl', badgeAssertionCtrl);
});