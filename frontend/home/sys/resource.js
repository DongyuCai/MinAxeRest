'use strict';

angular.module('MainApp', [])
	.controller('resourceCtrl',['$scope', '$http', 'i18nService', 'resourceService', function($scope, $http, i18nService, resourceService) {

        console.info("resourceCtrl");
		i18nService.setCurrentLang('zh-cn');
        //====================page start========================//

        $scope.unpublishedResourceAll = [];
        $scope.outOfDateResourceAll = [];

        $scope.toUnpublishedPage = function(){
			location.href = "#/resource-unpublished";
        }
        $scope.toOutOfDatePage = function(){
			location.href = "#/resource-outofdate";
        }

        resourceService.getUnpublished().then(function(result){
			$scope.unpublishedResourceAll = result;
		});
        resourceService.getOutOfDate().then(function(result){
			$scope.outOfDateResourceAll = result;
		});


        $scope.typeAll = [
	        {id:'POST',name:'POST'},
	        {id:'DELETE',name:'DELETE'},
	        {id:'PUT',name:'PUT'},
	        {id:'GET',name:'GET'}
        ];
		$scope.status4search = "";

		//关键字提示 start
		$scope.keywords = "";
		$scope.tips = [];
		$scope.tip = function(){
			if(!$scope.keywords || $scope.keywords.length <= 0)
				return false;

			//$scope.tips = [$scope.keywords];
			/*resourceService.tip($scope.keywords).then(function(result) {
				$scope.tips = result;
			});*/
		};
		var input_search = document.getElementById("search_input");
		$searchTip.bind(input_search);
		$scope.$watch('keywords',function(newVal,oldVal){
			if(newVal != oldVal){
				$scope.tip();
			}
		});
		$scope.$watch('tips',function(newVal,oldVal){
			if(newVal.length != oldVal.length){
				$searchTip.source = newVal;
				$searchTip.insert(input_search);
			}
		});
		//关键字提示 end

		$scope.selectStatus = function(status){
			$scope.status4search = status;

			var status_a_ary = $("a[name='status_a']");
			for(var i = 0;i<status_a_ary.length;i++){
				$(status_a_ary[i]).removeAttr("class");
			}

			$("#status_"+status).attr("class","selected");
			$scope.onSelectPage(1);

	    };

       	//不用改{
        $scope.searchData = [];
        $scope.totalpage = 1;
		$scope.totalItems = 0;
		$scope.numPages = $scope.totalpage;
		$scope.currentPage = 1;
		$scope.pageSize = 15;
		//}

		$scope.onSelectPage = function(page, pageSize) {
			if(!page) page = $scope.currentPage;
			if(!pageSize) pageSize = $scope.pageSize;

			resourceService.page(page, pageSize, $("#search_input").val(), $scope.status4search).then(function(result) {
				$scope.searchData = result.records == null ? []:result.records;
				//不用改{
				$scope.totalpage = result.totalpage;
				$scope.totalItems = result.totalElements;
				$scope.numPages = $scope.totalpage;
				$scope.currentPage = page;
				$scope.pageSize = pageSize;
				//}
			});

		}

		var cellTemplate_start = '<div class="ui-grid-cell-contents ng-scope ng-binding" ng-bind="grid.appScope.';
		var cellTemplate_end = '"></div>';
		

		$scope.gridOptions = {
			useExternalPagination: true, //打开服务器分页
			paginationPageSizes: [15, 25, 50, 75], //每页显示多少条
			paginationPageSize: $scope.pageSize, //默认每页显示多少条
			columnDefs: [{//TODO
					field: 'id',
					displayName: 'ID',
					enableSorting: false
				}, {
					field: 'url',
					displayName: 'URL',
					enableSorting: false
				}, {
					field: 'type',
					displayName: 'TYPE',
					enableSorting: false
				}, {
					field: 'title',
					displayName: '标题',
					enableSorting: false
				}, {
					field: 'classMethod',
					displayName: '类方法',
					enableSorting: false
				}, {
					field: 'caozuo',
					displayName: '操作',
					width: 300,
					enableSorting: false,
					cellTemplate: '<button class="btn btn-infos" ng-click="grid.appScope.showEditInfo(row.entity.id,\'edit\')"  ><i class="fa fa-unlink"></i>编辑</button>' +
								  '<button class="btn btn-infos" ng-click="grid.appScope.showEditInfo(row.entity.id,\'edit4role\')"  ><i class="fa fa-unlink"></i>角色</button>' +
								  '<button class="btn btn-dangers" ng-click="grid.appScope.deleteConfirm(row)"  ><i class="fa fa-unlink"></i>删除</button>'
				}
			],
			data: 'searchData' //数据
		};


		$scope.gridOptions.onRegisterApi = function(gridApi) {
			$scope.gridApi = gridApi;
			gridApi.pagination.on.paginationChanged($scope, function(pageNumber, pageSize) {
				$scope.onSelectPage(pageNumber, pageSize);
			});
		};

		$scope.$watch('totalItems', function(value) {
			if (value != undefined) {
				$scope.gridApi.grid.options.totalItems = value;
			}
		});

		$scope.$watch('currentPage',function(value){
			$scope.gridApi.grid.options.paginationCurrentPage = value;
		});

		$scope.onSelectPage(1);
		//====================page end==================//

		//=============add edit start=================//

		$scope.resource = new Object();
		$scope.roleIds = "";
		$scope.roleAll = [];
        $scope.currentOpt = '';

        $scope.saveInvalid = function(){
        	if($scope.currentOpt.indexOf('edit') >= 0){
        		if(!$scope.resource.id || $scope.resource.id.length <= 0)
        			return true;
        	}

        	if($scope.currentOpt == 'edit'){
        		if(!$scope.resource.title || $scope.resource.title.length <= 0)
        			return true;
        	}

        	return false;
        }
        
        $scope.refreshRoleSelect = function(){
        	$scope.roleIds = "";
        	var checkbox_role = $("input[name='checkbox_role']");
        	for(var i=0;i<checkbox_role.length;i++){
        		if(checkbox_role[i].checked){
        			$scope.roleIds = $scope.roleIds+","+$(checkbox_role[i]).val()+",";
        		}
        	}
        }
        
		$scope.showEditInfo = function(id,currentOpt){
			$scope.currentOpt = currentOpt;
			if($scope.saveInvalid()){
				$("#save_button").attr("disabled","disabled");
			}else{
				if($("#save_button").attr("disabled"))
					$("#save_button").removeAttr("disabled");
			}
			resourceService.get(id).then(function(result){
				$scope.resource = result.resource;
				$scope.roleIds = result.roleIds;
				resourceService.getAllRole().then(function(result_1){
					$scope.roleAll = result_1;
					$('#addInfo').modal('show');
				});
			});
		}

		$scope.save = function(){
			$scope.resource.roleIds = $scope.roleIds;
			
			$("#save_button").attr("disabled","true");
			$('#addInfo').modal('hide');
			if($scope.currentOpt == 'edit'){
				resourceService.edit($scope.resource).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
					$("#save_button").removeAttr("disabled");
				});
			}
			if($scope.currentOpt == 'edit4role'){
				resourceService.editRole($scope.resource).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
					$("#save_button").removeAttr("disabled");
				});
			}
		}

        //=================add edit end=================//


		//=================delete start ================//
        
		$scope.deleteConfirm = function(row){
			$("#messagebox").modal("show");
			$("#delete").click(function() {
				$('#messagebox').modal('hide');
				resourceService.delete(row.entity.id).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
				});
				$('#delete').unbind("click"); 
			});
		}
		//=================delete end ==================//

	}]).factory('resourceService', ['baseRestService', function(baseRestService) {


		var service = {};

		var _service = baseRestService.all('resource');
		var _service_role = baseRestService.all('role');
		var _service_tips = baseRestService.all('tips');

		//获取资源列表分页
		service.page = function(page, pageSize, keywords, type) {
			return _service.get('page', {
				page: page,
				pageSize: pageSize,
				keywords: keywords,
				type:type
			});
		}

		service.get = function(id) {
			return _service.get(''+id);
		}

		service.delete = function(id){
			return _service.customDELETE(''+id);
		}

		service.edit = function(entity){
			return _service.customPUT(entity,entity.id+"");
		}

		service.editRole = function(entity){
			return _service.customPUT(entity,entity.id+"/role");
		}
		
		service.getAllRole = function() {
			return _service_role.get('all');
		}


		service.getUnpublished = function() {
			return _service.get('unpublished');
		}

		service.getOutOfDate = function() {
			return _service.get('outofdate');
		}

		
		return service;
	}]);