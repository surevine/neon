define([ 'angular', 'config'], function(angular, config) {
  
return angular.module('header.controllers', [])

.controller('HeaderCtrl', ['$scope', '$templateCache', function($scope, $templateCache) {
  
  $scope.gitlabBaseUrl = config.gitlabBaseUrl;
  
  $scope.tooltips = {
    "public": "Public area",
    "snippets": "My snippets",
    "admin": "Admin area",
    "create": "Create new project",
    "profile": "My profile",
  };
  
}])

.controller('SearchCtrl', ['$scope', '$location',  function($scope, $location) {

    // setup filter options
    $scope.filterOptions = [
      { label: 'Skill', value: 'skill' }, 
      { label: 'Badge', value: 'badge' },
      { label: 'Gitlab', value: 'gitlab' }
    ];
  
    // set selected option (prevents angular inserting empty option)
    $scope.search = {filter : $scope.filterOptions[0].value};

    $scope.submit = function(formData) {
      
      // basic validation
      if(formData.query == '' || formData.query == undefined) {
        return; 
      }
      
      var query = formData.query;
      
      // clear form input
      $scope.search.query = '';
      $scope.searchForm.$setPristine();
      
      // redirect to page for skill/badge
      switch(formData.filter) {
          case 'skill':
              $location.path( '/skill/'+query );
          break;
          
          case 'badge':
              $location.path( '/badges/class/'+query );
          break;
          
          case 'gitlab':
              window.location.href = config.gitlabBaseUrl+'/search?search='+query;
          break;
      }
      
    };
                  
}]);
      
});