define(
		[ 'angular', 'config' ],
		function(ng, config) {
			return ng
					.module('profiles.extras.badges', [])
					.controller(
							'profilesBadgesExtrasCtrl',
							[
									'$scope',
									'$http',
									function($scope, $http) {

										$scope.badges = {
											hasBadges : false,
											list : []
										}

										$scope
												.$watch(
														'userId',
														function(newVal, oldVal) {
															$http(
																	{
																		method : 'GET',
																		url : config.restBaseUrl
																				+ 'badges/assertion/list/'
																				+ escape(newVal)
																	})
																	.success(
																			function(
																					data) {
																				$scope.badges.hasBadges = (data.length > 0);

																				$scope.badges.list = [];

																				for ( var i = 0; i < data.length; ++i) {
																					var assertion = data[i];

																					var displayBadge = {
																						assertionData : assertion,
																						badgeData : null,
																						bakedUrl : config.restBaseUrl
																								+ 'badges/bake/'
																								+ escape(assertion.namespace),
																					};

																					if (assertion.image) {
																						displayBadge.bakedUrl += '?source='
																								+ escape(assertion.image);
																					}

																					$http(
																							{
																								method : 'GET',
																								url : assertion.badge
																							})
																							.success(
																									function(
																											badgeData) {
																										displayBadge.badgeData = badgeData;
																									});
																					
																					$scope.badges.list.push(displayBadge);
																				}
																			});
														});
									} ]);
		});