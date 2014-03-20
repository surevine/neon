define([ 'angular', 'config' ], function(ng, config) {

  return ng.module('profiles.extras.badges', [])
  
      .controller('profilesBadgesExtrasCtrl', ['$scope', '$http', function($scope, $http) {
                              
          $scope.badges = {
              hasBadges : false,
              list : []
          };
    
          $scope.$watch('userId', function(newVal, oldVal) {
          $http({
              method : 'GET',
              url : config.restBaseUrl + 'badges/assertion/list/' + escape(newVal)
          })
          .success(function(data) {
              
              $scope.badges.hasBadges = (data.length > 0);
              $scope.badges.list = [];
            
              for ( var i = 0; i < data.length; ++i) {
                
                  var assertion = data[i];
  
                  var displayBadge = {
                      assertionData : assertion,
                      //badgeData : null,
                      bakedUrl : config.restBaseUrl + 'badges/bake/' + escape(assertion.namespace),
                  };
  
                  if (assertion.image) {
                      displayBadge.bakedUrl += '?source=' + escape(assertion.image);
                  }
                
                  $scope.badges.list.push(displayBadge);
                
              }
          });
        });
    }])
  
    .directive('badgePopOver', function($http, $popover) {
        return {
            restrict: 'C',
            link: function($scope, $element, $attr) {
              
                // init array for popovers if required
                if(!$scope.badgeMetaPopovers) {
                  $scope.badgeMetaPopovers = [];
                }

                var badge = $scope.$parent.badges.list[$attr.badgeindex]; 
              
                if(!$scope.badgeMetaPopovers[badge.assertionData.namespace]) {
                  
                  // Create empty popover
                  $scope.badgeMetaPopovers[badge.assertionData.namespace] = $popover($element, {
                                                                                scope: $scope,
                                                                                title: 'Loading', 
                                                                                content: {
                                                                                  loaded: false
                                                                                },
                                                                                contentTemplate: 'js/profiles/extras/badges/popover.html'
                                                                            });
                  
                }

                $element.bind('click', function(e) {
                  
                  if(!$scope.badgeMetaPopovers[badge.assertionData.namespace].$scope.content.loaded) {
                    
                    // reset error state (for retry)
                    $scope.badgeMetaPopovers[badge.assertionData.namespace].$scope.title = 'Loading';
                    $scope.badgeMetaPopovers[badge.assertionData.namespace].$scope.content.error = false;

                    // Retrieve addional badge info
                    $http.get(badge.assertionData.badge)
                    .success(function(badgeData) {
                          
                      badge.badgeData = badgeData;
                                
                      $http.get(badge.badgeData.issuer)
                      .success(function(issuerData) {
                        
                        badge.issuerData = issuerData;
                        
                        var content = {
                          loaded: true,
                          description: badge.badgeData.description,
                          issuedOn: badge.assertionData.issuedOn,
                          awarded: $attr.awarded,
                          slug: $attr.slug,
                          evidence: badge.assertionData.evidence,
                          alignments: badge.badgeData.alignment,
                          issuer: badge.issuerData
                        }
                        
                        $scope.badgeMetaPopovers[badge.assertionData.namespace].$scope.title = badge.badgeData.name;
                        $scope.badgeMetaPopovers[badge.assertionData.namespace].$scope.content = content;
                        
                      });
                                                                                 
                    })
                    .error(function(error) {
                      
                      var content = {
                        loaded: false,
                        error: true
                      }
                       
                      $scope.badgeMetaPopovers[badge.assertionData.namespace].$scope.title = "Error";
                      $scope.badgeMetaPopovers[badge.assertionData.namespace].$scope.content = content;
                      
                    });
                    
                  }
                      
                })
            }
        }
    });
  
});