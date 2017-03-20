'use strict';

angular.module('MainApp', [])
	.controller('resourceUnpublishedCtrl',['$scope', '$http', 'i18nService', 'resourceUnpublishedService' ,
		function($scope, $http, i18nService, resourceUnpublishedService) {

        console.info("resourceUnpublishedCtrl");
		i18nService.setCurrentLang('zh-cn');
		//============page start=====================//
		$scope.back = function(){
			location.href = "#/resource";
		}


		$scope.resourceAll = [];



       	//查询所有还么发布的资源，就是没入库的

       	$scope.onSelectPage = function() {
       		layer.load(2,{time: 10*1000});
			resourceUnpublishedService.getUnpublished().then(function(result){
				$scope.resourceAll = result;
				layer.closeAll('loading');
			});
		}

		$scope.onSelectPage();

		
		//==============page end=======================//

		//==============add edit start=================//
		$scope.resource = new Object();
		$scope.currentOpt = "";


		$scope.showAddInfo = function(row){
			$scope.resource = row;
			$scope.currentOpt = 'add';
			if($scope.saveInvalid()){
				$("#save_button").attr("disabled","disabled");
			}else{
				if($("#save_button").attr("disabled"))
					$("#save_button").removeAttr("disabled");
			}
			$('#addInfo').modal('show');
		}

		$scope.saveInvalid = function(){
        	if(!$scope.resource.title || $scope.resource.title.length <= 0)
        		return true;
        	if(!$scope.resource.url || $scope.resource.url.length <= 0)
        		return true;
        	if(!$scope.resource.type || $scope.resource.type.length <= 0)
        		return true;
        	if(!$scope.resource.classMethod || $scope.resource.classMethod.length <= 0)
        		return true;
        	
        	return false;
        }

        $scope.save = function(){
			$("#save_button").attr("disabled","true");
			$('#addInfo').modal('hide');
			if($scope.currentOpt == 'add'){
				resourceUnpublishedService.add($scope.resource).then(function(result){
					console.log(result);
					$scope.onSelectPage();
					$("#save_button").removeAttr("disabled");
				});
			}
		}
		//==============add edit end===================//

		

	}]).factory('resourceUnpublishedService', ['baseRestService', function(baseRestService) {


		var service = {};

		var _service = baseRestService.all('resource');

		service.getUnpublished = function() {
			return _service.get('unpublished');
		}

		service.add = function(entity){
			return _service.customPOST(entity,'');
		}

		return service;
	}]);