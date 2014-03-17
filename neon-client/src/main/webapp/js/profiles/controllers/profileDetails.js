define(['angular', 'config'], function(ng, config) {
	var fn = function($scope, $http, $routeParams) {
		$scope.userId = $routeParams.userId;
		
		$http({method: 'GET', url: config.restBaseUrl + 'profile/' + escape($routeParams.userId)})
			.success(function(data) {
				$scope.profile = data;
			});
		
		$scope.extras = [];
		
		for(var i = 0; i < config.profileExtras.length; ++i) {
			var extra = config.profileExtras[i];
			var extraObj = {};
			
			for(var sectionId in extra.sections) {
				if(extra.sections[sectionId]) {
					extraObj[sectionId + 'Include'] = 'js/profiles/extras/' + extra.id + '/' + sectionId + 'Content.html';
				}
			}
			
			$scope.extras.push(extraObj);
		}
	};
	fn.$inject = ['$scope', '$http', '$routeParams'];
	return fn;
});