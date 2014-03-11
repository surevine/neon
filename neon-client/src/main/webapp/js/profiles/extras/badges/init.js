define([ 'angular', 'config' ], function(ng, config) {
	return ng.module('profiles.extras.badges', []).controller(
			'profilesBadgesExtrasCtrl',
			[
					'$scope',
					'$http',
					function($scope, $http) {

						$scope.badges = {
							hasBadges : false,
							list : []
						}

						$scope.$watch('profile.userID', function(
								newVal, oldVal) {
							$http({method: 'GET', url: config.restBaseUrl + 'badges/assertion/list/' + escape(newVal)})
							.success(function(data) {
								$scope.badges.list = data;
								$scope.badges.hasBadges = ( data.length > 0 );
							});
						});
					} ]);
});