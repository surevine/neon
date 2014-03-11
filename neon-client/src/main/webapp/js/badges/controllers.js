define([ "angular", './services', './controllers/badgeClassAdmin', './controllers/badgeAssertion' ], function(
		angular, services, badgeClassAdminCtrl, badgeAssertionCtrl) {
	return angular.module('badges.controllers', [ services['name'] ])
			.controller('badgeClassAdminCtrl', badgeClassAdminCtrl)
			.controller('BadgeAssertionCtrl', badgeAssertionCtrl);
});