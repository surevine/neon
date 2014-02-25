define(['angular', 'config'], function(ng, config) {
	var fn = function($scope, $http, $routeParams) {
		$http({method: 'GET', url: config.restBaseUrl + 'profile/' + escape($routeParams.userId)})
			.success(function(data) {
				$scope.profile = data;
			});
	};
	fn.$inject = ['$scope', '$http', '$routeParams'];
	return fn;
});