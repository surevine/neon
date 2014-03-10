define([ 'angular', './extras/skills/init', './extras/additional/init',
		'./extras/location/init', './extras/badges/init' ], function(ng) {
	var moduleNames = [];

	// arguments[0] is angular so skip that
	for ( var i = 1; i < arguments.length; ++i) {
		moduleNames.push(arguments[i].name);
	}

	return ng.module('profiles.extras', moduleNames);
});