define([ 'angularMocks', 'profiles/extras/skills/init' ], function(ngMocks,
		skillsInit) {

	describe("Profile Details Controller", function() {
		beforeEach(function() {
			ngMocks.module('profiles.extras.skills');
		});

		it("should recognise no skills", function() {
			inject([ '$controller', function($controller) {
				var scope = {
					profile : {
						skills : []
					}
				};

				var ctrl = $controller('profilesSkillsExtrasCtrl', {
					$scope : scope
				});

				expect(scope.skills.hasSkills).toBe(false);
				expect(scope.skills.count).toBe(0);
			} ])
		});
	});
});