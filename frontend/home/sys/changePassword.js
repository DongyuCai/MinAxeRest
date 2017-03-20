'use strict';

angular.module('MainApp', [])
	.controller('changePasswordCtrl',['$rootScope','$scope', '$http', 'i18nService', 'changePasswordService','toaster', 'localStorageService',
		function($rootScope,$scope, $http, i18nService, changePasswordService,toaster,localStorageService) {
		
        console.log("changePasswordCtrl");
		i18nService.setCurrentLang('zh-cn');
        //====================page start========================//
		$scope.oldPassword = "";
		$scope.password = "";
		$scope.password2 = "";
		$scope.showPassword = false;

		$scope.showAddInfo = function(){
			$('#addInfo').modal('show');
		}
		$scope.showAddInfo();

		$scope.saveInvalid = function(){
			if(!$scope.oldPassword || $scope.oldPassword.length <= 0)
				return 1;
			if(!$scope.password || $scope.password.length <= 0)
				return 2;
			if(!$scope.password2 || $scope.password2.length <= 0)
				return 3;
			if($scope.password != $scope.password2)
				return 31;

        	return false;
		}

		$scope.save = function(){
			//md5加密
			$scope.password = hex_md5($scope.password);
			$scope.oldPassword = hex_md5($scope.oldPassword);

			$('#addInfo').modal('hide');
			changePasswordService.changePassword($scope.password,$scope.oldPassword).then(function(result){
				localStorageService.cookie.remove("$_1008_operation_token");
				$rootScope.$token == null;
				window.location.href = "login.html";
			});
		}


	}]).factory('changePasswordService', ['baseRestService', function(baseRestService){
		var service = {};
		
		var _service = baseRestService.all('login');

		service.changePassword = function(password,oldPassword){
			return _service.customPUT({
				password:password,
				oldPassword:oldPassword
			},"/password");
		}

		return service;
	}]);


