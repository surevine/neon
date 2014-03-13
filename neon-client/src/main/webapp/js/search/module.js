define([ 'angular', './controllers' ], function(angular, controllers) {
	
    var module = angular.module('search', [ controllers['name'] ]);

	module.initDeps = function() {

	}

	return module;
});