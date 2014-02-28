define(['angularMocks', 'profiles/controllers/profileDetails'], function(ngMocks, profileDetails) {
	var $httpBackend, $http;
	
	beforeEach(inject(function($injector) {
		$httpBackend = $injector.get('$httpBackend');
		$http = $injector.get('$http');
	}));
	
    afterEach(function() {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });
    
	describe("Profile Details Controller", function() {
		it("should include profile", function() {
			var scope = {};
			
			var routeParams = {userId: 'dave'};
			
			$httpBackend
				.expectGET('/rest/profile/dave')
				.respond({
					   "userID":"dave",
					   "vcard":{
					      "fn":"Dave Smith",
					   }});
			
			profileDetails(scope, $http, routeParams);
			
			$httpBackend.flush();
			
			expect(scope.profile.userID).toBe("dave");
			expect(scope.profile.vcard.fn).toBe("Dave Smith");
		});
	});
});