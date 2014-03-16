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
										};

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
									} ])
          
                    .directive('badgePopOver', function($http, $popover) {
                        return {
                            restrict: 'C',
                            link: function($scope, element, attr) {
                        
                                element.bind('click', function(e) {

                                    // init array for popovers if required
                                    if(!$scope.badgeMetaPopovers) {
                                      $scope.badgeMetaPopovers = [];
                                    }

                                    if(!$scope.badgeMetaPopovers[attr.namespace]) {
                                      
                                        // TODO when the service is complete
                                      
//                                      $http({
//                                          method : 'GET',
//                                          url : config.restBaseUrl+'/badges/validate/enrich/{username}?badge={FullURLToPNG}'
//                                      })
//                                      .success(function(data){
//                                        // todo create tooltip with the data!
//                                      })
//                                      .error(function(){
//                                        // todo create tooltip with error message in it?
//                                        // or do nothing...
//                                      });
                                    
                                      $scope.badgeMetaPopovers[attr.namespace] = $popover(element, {
                                                                    scope: $scope,
                                                                    title: attr.badgename, 
                                                                    content: {
                                                                      name: "TESTING NAME FROM DIR",
                                                                      description: "description in object",
                                                                      valid: true,
                                                                      awarded: 'This badge was awarded on 21/01/2014',
                                                                      badgeSlug: attr.badgeslug
                                                                    },
                                                                    placement: 'top',
                                                                    contentTemplate: 'js/profiles/extras/badges/popover.html'
                                                                });
                                      
                                    }
                                  
                                })
                            }
                        }
                    });
          
		});