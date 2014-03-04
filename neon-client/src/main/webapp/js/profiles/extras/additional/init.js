define(
		[ 'angular' ],
		function(ng) {
			return ng
					.module('profiles.extras.additional', [])
					.controller(
							'profilesExtrasAdditionalCtrl',
							[
									'$scope',
									function($scope) {
										// This is the list of fields to just
										// display verbatim
										$scope.showFields = [ "Job Title",
												"Business Short Title",
												"Employee Type", "Jive Level",
												"PF Number", "Typical Location" ];

										// This is the list of all fields which
										// are shown (so we can decide whether
										// to show the section or not
										var shownFields = $scope.showFields
												.slice(0);

										shownFields.push("Manager SID");

										$scope.showSection = false;

										$scope.$watchCollection('profile.additionalProperties', function(newVal, oldVal) {
											var showSection = false;
	
											if (newVal) {
												for ( var i = 0; i < shownFields.length; ++i) {
													if (newVal[shownFields[i]]) {
														showSection = true;
														break;
													}
												}
											}
											
											$scope.showSection = showSection;
										});
									} ]);
		});