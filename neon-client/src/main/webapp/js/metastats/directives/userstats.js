define([ 'config' ], function(config) {
	var fn = function($http) {
		return {
			restrict : 'AEC',
			link : function(scope, element, attrs) {
				scope.metastatsHasUserData = false;
				
				attrs.$observe('metastatsUser', function(newVal) {
					if (newVal) {
						$http(
								{
									method : 'GET',
									url : config.metastatsBaseUrl + 'user/'
											+ escape(newVal)
								}).success(function(data) {
							scope.metastatsUserData = data;
							scope.metastatsHasUserData = data.numCommits > 0;
						});
					}
				});
			}
		}
	};

	fn.$inject = [ '$http' ];

	return fn;
});