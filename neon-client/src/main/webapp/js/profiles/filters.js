define([ 'angular', 'moment' ], function(angular, moment) {
	return angular.module('profiles.filters', []).filter('fromNow', function() {
		return function(dateString) {
			return moment(dateString).fromNow()
		};
	});
});