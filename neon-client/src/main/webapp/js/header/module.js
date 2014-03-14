define([ 'angular', './controllers' ], function(angular, controllers) {
	
    var module = angular.module('header', [ controllers['name'] ]);

	module.initDeps = function() {

	}

	return module;
});