define([ 'angular', 'config'], function(angular, config) {
  
return angular.module('header.controllers', [])

.controller('HeaderCtrl', ['$scope', '$templateCache', function($scope, $templateCache) {
  
  $scope.gitlabBaseUrl = config.gitlabBaseUrl;
  
  // TODO setup tooltip for angularstrap
  
  $scope.tooltips = {
    "public": "Public area",
    "snippets": "My snippets",
    "admin": "Admin area",
    "create": "Create new project",
    "profile": "My profile",
  };
  
  $scope.dropdown = [
    {
      "text": "<i class=\"fa fa-download\"></i>&nbsp;Another action",
      "href": "#anotherAction"
    },
    {
      "text": "<i class=\"fa fa-globe\"></i>&nbsp;Display an alert",
      "click": "$alert(\"Holy guacamole!\")"
    },
    {
      "divider": true
    },
    {
      "text": "Separated link",
      "href": "#separatedLink"
    }
  ];
  
}])

.controller('SearchCtrl', ['$scope', '$location',  function($scope, $location) {

    // setup filter options
    $scope.filterOptions = [
      { name: 'Skill', value: 'skill' }, 
      { name: 'Badge', value: 'badge' }
    ];
  
    // set selected option (prevents angular inserting empty option)
    $scope.search = {filter : $scope.filterOptions[0].value};
  
    $scope.submit = function(formData) {
      
      console.log("submitted...");
      
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
      }
      
    };
                  
}]);
      
});