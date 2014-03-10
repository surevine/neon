define(['angular', 'config'], function(ng, config) {
	var fn = function($scope, $http, $routeParams) {
		$http({method: 'GET', url: config.restBaseUrl + 'badges/assertion/' + escape($routeParams.badgeAssertionId)})
			.success(function(data) {
				$scope.badgeAssertion = data;
			});
		
		$scope.save = function() {
			$http.post(config.restBaseUrl + 'badges/assertion/' + escape($routeParams.badgeAssertionId), $scope.badgeAssertion)
			.success(function(data) {
				alert("Saved");
			});			
		}
		
		$scope.$watch('$scope.uid', function(newVal, oldVal) {
			
			
			$scope.username = '';
			
		});
	};
	fn.$inject = ['$scope', '$http', '$routeParams'];
	return fn;
});