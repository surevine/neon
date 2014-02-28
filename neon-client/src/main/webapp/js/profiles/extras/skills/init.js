define([ 'angular' ], function(ng) {
	return ng.module('profiles.extras.skills', []).controller(
			'profilesSkillsExtrasCtrl', [ '$scope', function($scope) {
				$scope.skills = {
					hasSkills : $scope.profile.skills ? true : false
				}

				if (!$scope.skills.hasSkills) {
					return;
				}

				var skills = $scope.profile.skills;

				$scope.skills.count = skills.length;
				
				$scope.topSkill = skills[0];
			} ]);
});