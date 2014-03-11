define([ 'angular', 'moment' ], function(angular, moment) {
	return angular.module('profiles.filters', [])
      .filter('fromNow', function() {
          return function(dateString) {
              return moment(dateString).fromNow()
          };
      })
      .filter('split', function() {
          return function(input, splitChar, splitIndex) {
              return input.split(splitChar)[splitIndex];
          }
      });
});