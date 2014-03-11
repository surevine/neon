define(['angular', 'config'], function(ng, config) {
	var fn = function($scope, $http, $routeParams) {

		$scope.badgeClassId = $routeParams.badgeClassId;

		$http({method: 'GET', url: config.restBaseUrl + 'badges/class/' + escape($routeParams.badgeClassId) + '/people/' })
			.success(function(data) {
            
              $scope.people = data;
              
			})
			.error(function(error) {
				// TODO handle errors
			});

	};
	fn.$inject = ['$scope', '$http', '$routeParams'];
	return fn;
});