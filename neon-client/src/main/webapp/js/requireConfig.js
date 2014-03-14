requirejs.config({
	baseUrl : 'js',

	paths : {
		'jquery' : 'lib/jquery/jquery-1.11.0',
		'angular' : 'lib/angularjs/angular',
		'angularRoute' : 'lib/angularjs/angular-route',
		'angularMocks' : 'lib/angularjs/angular-mocks',
        'angularAnimate': 'lib/angularjs/angular-animate',
        'angularSanitize': 'lib/angularjs/angular-sanitize',
		'domReady' : 'lib/requirejs/domReady',
		'angularLocale' : 'lib/angularjs/i18n/angular-locale_en-gb',
		'moment' : 'lib/moment/moment',
        'angularStrap': 'lib/angularstrap/angular-strap',
        'angularStrapTpl': 'lib/angularstrap/angular-strap.tpl'
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
		'angularAnimate' : {
			deps : [ 'angular' ],
			'exports' : 'ngAnimate'
		},
		'angularSanitize' : {
			deps : [ 'angular' ],
			'exports' : 'ngSanitize'
		},
		'angularStrap' : {
			deps : [ 'angular', 'angularAnimate' ]
		}, 
        'angularStrapTpl' : {
			deps : [ 'angular', 'angularAnimate', 'angularStrap' ]
		} 

	}
});