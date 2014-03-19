define(['angular', 'config'], function(ng, config) {
	var fn = function($scope, $http, $routeParams) {

		$scope.skillName = $routeParams.skillName;
      
        $scope.filterOptions = [{ label: 'all', value: '' }];
        for (var key in config.skillRatings) {
          if (config.skillRatings.hasOwnProperty(key)) {
            $scope.filterOptions.push({ label: config.skillRatings[key], value: config.skillRatings[key] });
          }
        }
      
		$http({method: 'GET', url: config.restBaseUrl + 'skill/' + escape($routeParams.skillName) + '/people/' })
			.success(function(data) {
            
              var people = data;
              
              for (var i=0; i<people.length; i++) { 
                
                var person = people[i];
                
                for (var s=0; s<person.skills.length; s++) {
                  
                  var skill = person.skills[s];
                  
                  if(skill.skillName == $routeParams.skillName) {

                    // Store current skill inc label against person
                    people[i].currentSkill = {
                      rating: skill.rating,
                      label: config.skillRatings[skill.rating]
                    };
                    
                  }
                  
                }
                
              }
              
		      $scope.people = people;
              
			})
			.error(function(error) {
				// TODO handle errors
			});

	};
	fn.$inject = ['$scope', '$http', '$routeParams'];
	return fn;
});