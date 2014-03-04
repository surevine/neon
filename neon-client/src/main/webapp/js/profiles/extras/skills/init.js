define([ 'angular', 'config' ], function(ng, config) {
	return ng.module('profiles.extras.skills', []).controller(
			'profilesSkillsExtrasCtrl',
			[
					'$scope',
					function($scope) {

						$scope.skills = {
							hasSkills : false,
							topSkill : null,
							ratingMap : config.skillRatings
						}

						$scope.$watchCollection('profile.skills', function(
								newVal, oldVal) {
							if (newVal && newVal.length > 0) {

								$scope.skills.hasSkills = true;
								$scope.skills.topSkill = newVal[0];
								$scope.skills.count = newVal.length;
							} else {
								$scope.skills.hasSkills = false;
								$scope.skills.topSkill = null;
								$scope.skills.count = 0;
							}
						});
					} ]);
});