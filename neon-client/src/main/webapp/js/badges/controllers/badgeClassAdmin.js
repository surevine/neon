define(['angular', 'config'], function(ng, config) {
	var fn = function($scope, $http, $routeParams) {
		$http({method: 'GET', url: config.restBaseUrl + 'badges/class/' + escape($routeParams.badgeClassId)})
			.success(function(data) {
				$scope.badgeClass = data;
			});
		
		$scope.save = function() {
			$http.post(config.restBaseUrl + 'badges/class/' + escape($routeParams.badgeClassId), $scope.badgeClass)
			.success(function(data) {
				alert("Saved");
			});			
		}
	};
	fn.$inject = ['$scope', '$http', '$routeParams'];
	return fn;
});