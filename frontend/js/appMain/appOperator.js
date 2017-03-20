'use strict';
angular.module('MainApp', ['ui.router',
	'oc.lazyLoad',
	'ui.bootstrap',
	'app.core',
	'app.sidebar',
	'app.directives',
	'app.services',
	'app.utils',
	'app.filters',
	'app.settings',
	'restangular',
	'LocalStorageModule',
	'toaster',
	'ngAnimate',
	'w5c.validator'
]);


//  注册组件
var mainApp = angular.module('MainApp')
	.config(['$controllerProvider', '$compileProvider', '$filterProvider', '$provide', function($controllerProvider, $compileProvider, $filterProvider, $provide) {
		// lazy controller, directive and service
		mainApp.controller = $controllerProvider.register;
		mainApp.directive = $compileProvider.directive;
		mainApp.filter = $filterProvider.register;
		mainApp.factory = $provide.factory;
		mainApp.service = $provide.service;
		mainApp.constant = $provide.constant;
		mainApp.value = $provide.value;
	}]);

mainApp
	.run(
		['$rootScope', '$state', '$stateParams', 'localStorageService',
			function($rootScope, $state, $stateParams, localStorageService) {
				$rootScope.$token = localStorageService.cookie.get("$_1008_operation_token");

				$rootScope.$state = $state;
				$rootScope.$stateParams = $stateParams;
			}
		]
	)
	.config(['$stateProvider', '$urlRouterProvider', '$httpProvider',
		function($stateProvider, $urlRouterProvider, $httpProvider) {
			$urlRouterProvider.otherwise('/user');
			$stateProvider
			// 主页路由
				.state('role', {
				url: '/role',
				templateUrl: 'home/sys/role.html',
				controller: 'roleCtrl',
				resolve: {
					deps: ['$ocLazyLoad',
						function($ocLazyLoad) {
							return $ocLazyLoad.load(['ngGrid']).then(
								function() {
									return $ocLazyLoad.load(['home/sys/role.js']);
								}
							);
						}
					]
				}
			}).state('user', {
				url: '/user',
				templateUrl: 'home/sys/user.html',
				controller: 'userCtrl',
				resolve: {
					deps: ['$ocLazyLoad',
						function($ocLazyLoad) {
							return $ocLazyLoad.load(['ngGrid','md5']).then(
								function() {
									return $ocLazyLoad.load(['home/sys/user.js']);
								}
							);
						}
					]
				}
			}).state('resource', {
				url: '/resource',
				templateUrl: 'home/sys/resource.html',
				controller: 'resourceCtrl',
				resolve: {
					deps: ['$ocLazyLoad',
						function($ocLazyLoad) {
							return $ocLazyLoad.load(['ngGrid', 'searchTip']).then(
								function() {
									return $ocLazyLoad.load(['home/sys/resource.js']);
								}
							);
						}
					]
				}
			}).state('resource-unpublished', {
				url: '/resource-unpublished',
				templateUrl: 'home/sys/resourceUnpublished.html',
				controller: 'resourceUnpublishedCtrl',
				resolve: {
					deps: ['$ocLazyLoad',
						function($ocLazyLoad) {
							return $ocLazyLoad.load(['ngGrid']).then(
								function() {
									return $ocLazyLoad.load(['home/sys/resourceUnpublished.js']);
								}
							);
						}
					]
				}
			}).state('resource-outofdate', {
				url: '/resource-outofdate',
				templateUrl: 'home/sys/resourceOutOfDate.html',
				controller: 'resourceOutOfDateCtrl',
				resolve: {
					deps: ['$ocLazyLoad',
						function($ocLazyLoad) {
							return $ocLazyLoad.load(['ngGrid']).then(
								function() {
									return $ocLazyLoad.load(['home/sys/resourceOutOfDate.js']);
								}
							);
						}
					]
				}
			}).state('change-password', {
				url: '/change-password',
				templateUrl: 'home/sys/changePassword.html',
				controller: 'changePasswordCtrl',
				resolve: {
					deps: ['$ocLazyLoad',
						function($ocLazyLoad) {
							return $ocLazyLoad.load(['ngGrid','md5']).then(
								function() {
									return $ocLazyLoad.load(['home/sys/changePassword.js']);
								}
							);
						}
					]
				}
			});
			$httpProvider.interceptors.push('interceptor');
		}
	])
	.run(['$rootScope', '$state', '$stateParams', '$window', function($rootScope, $state, $stateParams, $window) {
		$rootScope.$on('$stateChangeSuccess', function() {
			$window.scrollTo(0, 0); //跳转成功 初始化在顶部
		});
	}])
	.factory('interceptor', ['$q', '$rootScope', 'toaster', '$timeout','localStorageService',function($q, $rootScope, toaster, $timeout,localStorageService) {

		var interceptor = {
			'request': function(config) {
				//config.headers['X-1008-Application-Id'] = 'web';
				//if ($rootScope.$token != null && $rootScope.$token != 'undefined') {
				//    config.headers['X-1008-Session-Token'] = $rootScope.$token;
				//}
				config.headers['X-1008-Session-Token'] = $rootScope.$token;

				return config;
			},
			'response': function(response) {

				return response;
			},
			'responseError': function(rejection) {

				switch (rejection.status) {
					case 0:
						toaster.pop('error', "0错误", '<ul><li>服务器连接失败！</li></ul>', 5000, 'trustedHtml');
						break;
					case 400:
						var message = rejection.data;
						if(message){
							toaster.pop('error', "400错误", '<ul><li>' + message + '</li></ul>', 5000, 'trustedHtml');
						} else {
							toaster.pop('error', "400错误", '<ul><li>' + rejection.config.url + '请求失败！参数异常</li></ul>', 5000, 'trustedHtml');
						}
						break;
					case 401:
						var message = rejection.data;
						if (message.indexOf("登录") > 0) {
							localStorageService.cookie.remove("$_1008_operation_token");
							$rootScope.$token == null;
							window.location.href = "login.html";
						} else {
							toaster.pop('error', "401错误", '<ul><li>' + message + '</li></ul>', 5000, 'trustedHtml');
						}
						break;
					case 403:
						toaster.pop('error', "403错误", '<ul><li>' + rejection.data + '</li></ul>', 5000, 'trustedHtml');
						break;
					case 404:
						toaster.pop('error', "404错误", '<ul><li>' + rejection.config.url + '请求失败！</li></ul>', 5000, 'trustedHtml');
						break;
					case 500:
						var message = rejection.data;
						toaster.pop('error', "500错误", '<ul><li>服务器500错误!</li></ul>', 5000, 'trustedHtml');
						break;
					case 800:
						var message = rejection.data;
						toaster.pop('error', "执行失败", '<ul><li>' + message + '</li></ul>', 5000, 'trustedHtml');
						break;
				}
				return $q.reject(rejection);
			}
		}

		return interceptor;
	}])
	.factory('baseRestService', ['Restangular', '$rootScope', 'toaster',
		function(Restangular, $rootScope, toaster) {

			Restangular.setRequestInterceptor(
				function(element, operation, what, url) {
					if (operation == 'getList') {
						// maya.notice.wait("");
					} else if (operation == 'get') {


					}
					return element;
				}
			);

			Restangular.setResponseInterceptor(
				function(data, operation, what) {


					if (operation == 'getList') {
						// maya.notice.close();
						//console.info(what);
						var list = data[what + "s"];
						if (list) {
							list.totalpage = data['totalpage'];
						}
						return list;
					} else if (operation == 'post' || operation == 'put' || operation == 'remove') {
						// maya.notice.close();
						if (data['message']) {
							toaster.pop('success', "提示", '<ul><li>' + data['message'] + '</li></ul>', 5000, 'trustedHtml');
						} else {
							toaster.pop('success', "提示", '<ul><li>执行成功</li></ul>', 5000, 'trustedHtml');
						}

					} else if (operation == 'get') {

					}
					return data;
				}
			);

			return Restangular.withConfig(function(Configurer) {
				Configurer.setBaseUrl(API_BASE_URL);
			});
		}
	]);