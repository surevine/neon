define([ 'angular', 'config' ], function(ng, config) {
	return ng.module('profiles.extras.skills', [])
    .controller('profilesSkillsExtrasCtrl', ['$scope', '$http', '$routeParams', '$modal', function($scope, $http, $routeParams, $modal) {

        $scope.skills = {
            hasSkills : false,
            topSkill : null,
            ratingMap : config.skillRatings
        }
        
        $scope.skillRatingOptions = [];
        for (var key in config.skillRatings) {
          if (config.skillRatings.hasOwnProperty(key)) {
            $scope.skillRatingOptions.push({ label: config.skillRatings[key], value: key });
          }
        }
        $scope.skill = {rating : $scope.skillRatingOptions[0].value};

        $scope.$watchCollection('profile.skills', function(newVal, oldVal) {
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
      
        $scope.addSkillModal = $modal({
          scope: $scope,
          contentTemplate: 'js/profiles/extras/skills/addSkill.html',
          animation: 'am-fade-and-slide-top',
          show: false,
          title: 'Add Skill',
        });
      
      
        $scope.showAddSkillForm = function() {
          $scope.addSkillModal.show();
        };
      
        $scope.addSkill = function(skill) {
          
          if(skill == undefined || skill.name == undefined) {
            // No nothing as form not completed
            return;
          }
          
          var newSkill = {
            userID: $routeParams.userId,
            skillName: skill.name,
            rating: skill.rating
          };
          
          $http.post(config.restBaseUrl + 'skill', newSkill)
          .success(function(){
            
            $scope.addSkillModal.hide();
            
            // Reloading entire profile (as no get skills service)
            $http({method: 'GET', url: config.restBaseUrl + 'profile/' + escape($routeParams.userId)})
			.success(function(data) {
				$scope.profile = data;
			});
            
          });

        };
      
    }]);
});