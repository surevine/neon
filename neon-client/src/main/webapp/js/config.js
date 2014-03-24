define([], function() {
	return ({
		restBaseUrl : 'http://10.66.2.126:8080/neon-services/rest/',
//		restBaseUrl: 'http://localhost:8080/neon-services/rest/',
//		restBaseUrl: 'test-data/rest/',
//		restBaseUrl: '/neon-services/rest/',
		metastatsBaseUrl : 'http://10.66.2.25/si/',
//		metastatsBaseUrl: 'test-data/metastats/',
//		metastatsBaseUrl : 'http://localhost:9999/',
        gitlabBaseUrl: 'http://10.66.2.243',
//        gitlabBaseUrl: '/',
		profileExtras : [ {
			id : 'basic',
			sections : {
				detail : true,
				summary : true
			}
		}, {
			id : 'badges',
			sections : {
				summary : true
			}
		}, {
			id : 'skills',
			sections : {
				detail : true
			}
		}, {
			id : 'metastats',
			sections : {
				detail : true,
				summary : true,
			}
		}, {
			id : 'bio',
			sections : {
				detail : true,
			}
		}, {
			id : 'connections',
			sections : {
				detail : true,
			}
		}, {
			id : 'projectActivity',
			sections : {
				detail : true,
			}
		}, {
			id : 'location',
			sections : {
				detail : true,
			}
		}, {
			id : 'additional',
			sections : {
				detail : true,
			}
		} ],
		skillRatings : {
			1 : "beginner",
			2 : "journeyman",
			3 : "artisan",
			4 : "expert",
			5 : "mentor"
		}
	});
});
