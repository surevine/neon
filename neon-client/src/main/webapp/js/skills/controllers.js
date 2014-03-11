define([ "angular", './services', './controllers/skills' ], function(
		angular, services, SkillCtrl) {
	return angular.module('skills.controllers', [ services['name'] ])
			.controller('SkillCtrl', SkillCtrl)
});