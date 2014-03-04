define(['angular'], function(angular) {
	return angular.module('profiles.directives', [])
	.directive('profileLink', function() {
		return {
			restrict: 'AEC',
			scope: {
				userId: '=profileLink'
			},
			transclude: true,
			template: '<a ng-href="#/profile/{{userId}}" ng-transclude></a>'
		}
	});
});