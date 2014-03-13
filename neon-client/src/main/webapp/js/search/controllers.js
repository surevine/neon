define([ 'angular', 'config', 'bootstrapUI'  ], function(angular, config, bootstrapUI) {
  
return angular.module('search.controllers', ['ui.bootstrap'])

.controller('SearchCtrl', ['$scope', '$location',  function($scope, $location) {

    // setup filter options
    $scope.filterOptions = [
      { name: 'Skill', value: 'skill' }, 
      { name: 'Badge', value: 'badge' }
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
      }
      
    };
                  
}]);
      
});