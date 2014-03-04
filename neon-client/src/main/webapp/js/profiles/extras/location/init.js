define(
		[ 'angular' ],
		function(ng) {
			return ng
					.module('profiles.extras.location', [])
					.controller(
							'profilesExtrasLocationCtrl',
							[
									'$scope',
									function($scope) {
										$scope
												.$watch(
														'profile.additionalProperties["Current Location Fine Details"]',
														function(newVal, oldVal) {
															if (newVal) {
																$scope.location = ng
																		.fromJson(newVal);
															} else {
																$scope.location = null;
															}
														});
									} ]);
		});