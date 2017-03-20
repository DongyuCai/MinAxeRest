mainApp
  .controller('HeadController', ['$rootScope', '$scope','UserInfoLoader','localStorageService',function($rootScope, $scope, UserInfoLoader, localStorageService){
          console.log("headCtrl");

          UserInfoLoader.getUserInfo(function(userInfo){
            $scope.username = userInfo.username;
          });


          $scope.loginOut = function(){

              localStorageService.cookie.remove("$_1008_operation_token");
              $rootScope.$token == null;
              window.location.href = "login.html";
          }

        }]);