requirejs.config({
	baseUrl : 'js',

	paths : {
		'jquery' : 'lib/jquery/jquery-1.11.0',
		'angular' : 'lib/angularjs/angular',
		'angularRoute' : 'lib/angularjs/angular-route',
		'angularMocks' : 'lib/angularjs/angular-mocks',
		'domReady' : 'lib/requirejs/domReady',
		'angularLocale' : 'lib/angularjs/i18n/angular-locale_en-gb',
		'moment' : 'lib/moment/moment',
        'bootstrapUI': 'lib/bootstrap-ui/ui-bootstrap-tpls-0.10.0.min'
	},

	shim : {
		'angular' : {
			'exports' : 'angular'
		},
		'angularLocale' : {
			deps : [ 'angular' ],
		},
		'angularRoute' : {
			deps : [ 'angular' ],
			'exports' : 'ngRouteModule'
		},
		'angularMocks' : {
			deps : [ 'angular' ],
			'exports' : 'angular.mock'
		},
		'bootstrapUI' : {
			deps : [ 'angular' ]
		},
	}
});