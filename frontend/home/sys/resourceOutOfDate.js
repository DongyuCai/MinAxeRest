'use strict';

angular.module('MainApp', [])
	.controller('resourceOutOfDateCtrl',['$scope', '$http', 'i18nService', 'resourceOutOfDateService' ,
		function($scope, $http, i18nService, resourceOutOfDateService) {

        console.info("resourceOutOfDateCtrl");
		i18nService.setCurrentLang('zh-cn');
		//============page start=====================//
		$scope.back = function(){
			location.href = "#/resource";
		}


		$scope.resourceAll = [];



       	//查询所有还么发布的资源，就是没入库的

       	$scope.onSelectPage = function() {
			resourceOutOfDateService.getOutOfDate().then(function(result){
				$scope.resourceAll = result;
			});
		}

		$scope.onSelectPage();

		
		//==============page end=======================//

		
		//=================delete start ================//
        
		$scope.deleteConfirm = function(id){
			$("#messagebox").modal("show");
			$("#delete").click(function() {
				$('#messagebox').modal('hide');
				resourceOutOfDateService.delete(id).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
				});
				$('#delete').unbind("click"); 
			});
		}

		//=================delete end ==================//

		

	}]).factory('resourceOutOfDateService', ['baseRestService', function(baseRestService) {


		var service = {};

		var _service = baseRestService.all('resource');

		service.getOutOfDate = function() {
			return _service.get('outofdate');
		}

		service.delete = function(id){
			return _service.customDELETE(''+id);
		}

		return service;
	}]);