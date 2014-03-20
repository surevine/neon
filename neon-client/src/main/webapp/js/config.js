define([], function() {
	return ({
		restBaseUrl : 'http://10.66.2.126:8080/neon-services/rest/',
//		restBaseUrl: 'http://localhost:8080/neon-services/rest/',
//		restBaseUrl: 'test-data/rest/',
//		restBaseUrl: '/neon-services/rest/',
//		metastatsBaseUrl : 'http://10.66.2.25/si/api/',
		metastatsBaseUrl: 'test-data/metastats/',
        gitlabBaseUrl: 'http://10.66.2.243',
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
				detail : true,
				summary : true
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
			1 : "Beginner",
			2 : "Journeyman",
			3 : "Artisan",
			4 : "Expert",
			5 : "Mentor"
		}
	});
});
