define([ 'config' ], function(config) {
	return function() {
		this.metastatsUser = {
				data: null
		};
		
		var ctrl = function($scope, $http) {
			$scope.metastatsUser = this.metastatsUser;

			$scope.$watch('statsUserId', function(newVal, oldVal) {
				if (newVal) {
					$http(
							{
								method : 'GET',
								url : config.metastatsBaseUrl + 'user/'
										+ escape(newVal)
							}).success(function(data) {
						this.metastatsUser.data = data;
					});
				}
			});
		};

		ctrl.$inject = [ '$scope', '$http' ];

		return {
			restrict : 'AEC',
			scope : {
				statsUserId : '=metastatsUser'
			},
			controller : ctrl
		}
	}
});