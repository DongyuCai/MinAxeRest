'use strict';

/* Controllers */

angular.module('LoginApp', ['LocalStorageModule']).
  controller('loginCtrl', ['$rootScope','$scope','$http','localStorageService',
  		function($rootScope,$scope,$http,localStorageService) {

        console.log("loginCtrl");
        //====================start========================//
        $scope.afterLogin = function(){
        	location.href = location.search && location.search.length > 1 ? "index.html#"+location.search.substr(1):"index.html";
        }

        if(localStorageService.cookie.get("$_1008_operation_token")){
        	//如果cookie还在，就自动登录
        	$scope.afterLogin();
        }


        

        $scope.codeUrl = API_BASE_URL+"/login/code_";
        $scope.codeTime = new Date().getTime();
        $scope.changeCodeTime = function(){
        	$scope.codeTime = new Date().getTime();
        }

        $scope.loginInvalid = function(){
			if(!$scope.username || $scope.username.length <= 0){
				return true;
			}
			if(!$scope.password || $scope.password.length <= 0){
				return true;
			}
			if(!$scope.code || $scope.code.length <= 0){
				return true;
			}

        	return false;
        }


        //登录
        $scope.goLogin = function(){
        	//md5加密
        	var md5_password = hex_md5($scope.password);


			$('#login-submit-btn').button('loading');
			$http.post(
				API_BASE_URL+"/login",
				{
					'username':$scope.username,
					'password':md5_password,
					'code':$scope.code,
					'codeIndex':$scope.codeTime,
					'saveLogin':$scope.saveLogin
				}
			).success(function(data,status,headers,config){
				//登录成功
        		//记录用户Token
				if(data.token){
					var cookieTime = -1;
					var current = new Date().getTime();
					if(data.expire){
						cookieTime = (data.expire-current)/1000/60/60/24 +1;//比服务端设置多留一天
					}
					if(data.cookieExpire){
						cookieTime = (data.cookieExpire-current)/1000/60/60/24;//比服务端设置多留一天
					}
					cookieTime = Math.ceil(cookieTime);
					localStorageService.cookie.set("$_1008_operation_token",data.token,cookieTime);
					location.href = location.search && location.search.length > 1 ? "index.html#"+location.search.substr(1):"index.html";
				}else{
					//alert("登录失败，请检查登录名密码!");
					layer.tips('登录失败，请检查登录名密码', '#username', {
					  tips: [1, '#78BA32']
					});
				}
				
			}).error(function(data,status,headers,config){

				if(status == 0 || status == 500 || status == 404){
					// alert("服务器连接失败");
					layer.msg("服务器连接失败",function(){});
				}else{
					//alert(data);
					if(data.indexOf('密码') >=0 ){
						layer.tips(data, '#username', {
						  tips: [1, '#FF5722']
						});
					} else if(data.indexOf('验证码')>=0){
						layer.tips(data, '#code', {
						  tips: [4, '#FF5722']
						});
						$scope.changeCodeTime();
					} else {
						layer.msg(data);
					}

				}
				$('#login-submit-btn').button('reset');
			})
        };


  }]);