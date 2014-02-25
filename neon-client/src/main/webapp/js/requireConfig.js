requirejs.config({
	baseUrl : 'js',

	paths : {
		'jquery' : 'lib/jquery/jquery-1.11.0',
		'angular' : 'lib/angularjs/angular',
		'angularRoute' : 'lib/angularjs/angular-route',
		'angularMocks' : 'lib/angularjs/angular-mocks',
		'domReady' : 'lib/requirejs/domReady'
	},

	shim : {
		'angular' : {
			'exports' : 'angular'
		},
		'angularRoute' : {
			deps : [ 'angular' ],
			'exports' : 'ngRouteModule'
		},
		'angularMocks' : {
			deps : [ 'angular' ],
			'exports' : 'angular.mock'
		}
	}
});