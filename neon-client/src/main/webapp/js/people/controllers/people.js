define(['angular', 'config'], function(ng, config) {
	var fn = function($scope, $http, $routeParams, PeopleService) {
      
		$http.get(config.restBaseUrl + 'profile/users/')
        .success(function(data) {
        
          $scope.people = data;
          
          if($routeParams.query != undefined) {
            $scope.nameFilter = $routeParams.query;
          }
          
        })
        .error(function(error) {
            // TODO handle errors
        });

	};
	fn.$inject = ['$scope', '$http', '$routeParams'];
	return fn;
});