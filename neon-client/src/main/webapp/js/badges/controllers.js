define([ "angular", './services', './controllers/badgeClass', './controllers/badgeClassAdmin', './controllers/badgeAssertion' ], function(
		angular, services, badgeClassCtrl, badgeClassAdminCtrl, badgeAssertionCtrl) {
	return angular.module('badges.controllers', [ services['name'] ])
            .controller('BadgeClassCtrl', badgeClassCtrl)
			.controller('badgeClassAdminCtrl', badgeClassAdminCtrl)
			.controller('BadgeAssertionCtrl', badgeAssertionCtrl);
});