define([], function() {
	return ({
		restBaseUrl : 'http://10.66.2.126:8080/neon-services/rest/',
//		restBaseUrl: 'http://localhost:8080/neon-services/rest/',
//		restBaseUrl: 'test-data/rest/',
		profileExtras : [ {
			id : 'basic'
		}, {
			id : 'location'
		}, {}, {
			id : 'skills'
		}, {
			id : 'bio'
		}, {
			id : 'connections'
		}, {
			id : 'projectActivity'
		}, {
			id : 'additional'
		}, {
			id : 'badges'
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