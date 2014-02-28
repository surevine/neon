define(['angular', 'config'], function(ng, config) {
	var fn = function($scope, $http, $routeParams) {
		$http({method: 'GET', url: config.restBaseUrl + 'profile/' + escape($routeParams.userId)})
			.success(function(data) {
				$scope.profile = data;
			});
		
		$scope.extras = [];
		
		for(var i = 0; i < config.profileExtras.length; ++i) {
			var extra = config.profileExtras[i];
			$scope.extras.push({
				sectionInclude: 'js/profiles/extras/' + extra.id + '/sectionContent.html',
				summaryInclude: 'js/profiles/extras/' + extra.id + '/summaryContent.html'
			});
		}
	};
	fn.$inject = ['$scope', '$http', '$routeParams'];
	return fn;
});