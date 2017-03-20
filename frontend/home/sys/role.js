'use strict';

angular.module('MainApp', [])
	.controller('roleCtrl',['$rootScope','$scope', '$http', 'i18nService', 'roleService','toaster', 
		function($rootScope,$scope, $http, i18nService, roleService, toaster) {

        console.log("roleCtrl");
		i18nService.setCurrentLang('zh-cn');
        //====================page start========================//
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
			roleService.page(page, pageSize, $scope.status4search).then(function(result) {
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
			columnDefs: [{
					field: 'name',
					displayName: '名称'
				}, {
					field: 'remark',
					displayName: '备注'
				}, {
					field: 'caozuo',
					displayName: '操作',
					width: 400,
					enableSorting: false,
					cellTemplate: '<button class="btn btn-infos" ng-click="grid.appScope.showEditInfo(row.entity.id,\'edit\')"><i class="fa fa-edit"></i>编辑</button>' +
						'<button class="btn btn-infos" ng-click="grid.appScope.showEditInfo(row.entity.id,\'edit4menu\')"><i class="fa fa-edit"></i>菜单</button>' +
						'<button class="btn btn-infos" ng-click="grid.appScope.showEditInfo(row.entity.id,\'edit4resource\')"><i class="fa fa-edit"></i>资源</button>' +
						'<button class="btn btn-dangers" ng-click="grid.appScope.deleteConfirm(row)"><i class="fa fa-bell"></i>删除</button>'
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

		$scope.role = new Object();
		$scope.menuIds = "";
		$scope.resourceIds = "";

        $scope.currentOpt = '';

        $scope.saveInvalid = function(){
        	if($scope.currentOpt == 'add'){
        		if(!$scope.role.name || $scope.role.name.length <= 0) 
	        		return true;
        	}
        	if($scope.currentOpt.indexOf('edit') >= 0 && (!$scope.role.id || $scope.role.id.length <= 0))
        		return true;

        	return false;
        }


        //******************* ztree start *******************//
        //menu
        $scope.zTree_menu = new Object();
        $scope.zTreeSetting_menu = {
			check: {
				enable: true,
				chkboxType: { "Y" : "ps", "N" : "ps" }
			},
			callback: {
				onCheck: zTreeOnCheck_menu
			}
		};

        function zTreeOnCheck_menu(event, treeId, treeNode) {
		    var nodes = $scope.zTree_menu.getCheckedNodes(true);
		    $scope.menuIds = "";
		    if(nodes && nodes.length && nodes.length > 0){
		    	$scope.menuIds = ",";
		    	for(var i=0;i<nodes.length;i++){
		    		if(nodes[i].sref == undefined) continue;
		    		$scope.menuIds = $scope.menuIds+nodes[i].sref+",";
		    	}
		    }
		    console.log($scope.menuIds);
		};

        $scope.generateZnodes_menu = function(menuJson, initCheckStr){
        	var nodes = [];
        	for(var i=0;i<menuJson.length;i++){
        		var sref = menuJson[i].sref;

				var children = null;
				if(menuJson[i].submenu) children = $scope.generateZnodes_menu(menuJson[i].submenu,initCheckStr);
				var checked = false;
				if(initCheckStr && initCheckStr.indexOf(','+sref+',')>=0) checked = true;
				nodes[i] = {
					name: menuJson[i].name,
					checked: checked,
					children: children,
					sref: sref
				}
			}
			return nodes;
        }

        //resource
        $scope.zTree_resource = new Object();
        $scope.zTreeSetting_resource = {
			check: {
				enable: true,
				chkboxType: { "Y" : "ps", "N" : "ps" }
			},
			callback: {
				onCheck: zTreeOnCheck_resource
			}
		};

        function zTreeOnCheck_resource(event, treeId, treeNode) {
		    var nodes = $scope.zTree_resource.getCheckedNodes(true);
		    $scope.resourceIds = "";
		    if(nodes && nodes.length && nodes.length > 0){
		    	$scope.resourceIds = ",";
		    	for(var i=0;i<nodes.length;i++){
		    		if(nodes[i].resourceId == undefined) continue;
		    		$scope.resourceIds = $scope.resourceIds+nodes[i].resourceId+",";
		    	}
		    }
		    console.log($scope.resourceIds);
		};

        $scope.generateZnodes_resource = function(resourceAll, initCheckStr){
        	var nodes = [];
        	for(var i=0;i<resourceAll.length;i++){
        		var resourceId = resourceAll[i].id;

				var checked = false;
				if(initCheckStr && initCheckStr.indexOf(','+resourceId+',')>=0) checked = true;
				nodes[i] = {
					name: resourceAll[i].title,
					checked: checked,
					resourceId: resourceId
				}
			}

			var nodes_ = {
				name: "全选",
				children:nodes
			};
			return nodes_;
        }

        //******************* ztree end *******************//

		$scope.showAddInfo = function(){
			$scope.currentOpt = 'add';
			if($scope.saveInvalid()){
				$("#save_button").attr("disabled","disabled");
			}else{
				if($("#save_button").attr("disabled"))
					$("#save_button").removeAttr("disabled");
			}
			$('#addInfo').modal('show');
		}
        
		$scope.showEditInfo = function(id,currentOpt){
			$scope.currentOpt = currentOpt;
			if($scope.saveInvalid()){
				$("#save_button").attr("disabled","disabled");
			}else{
				if($("#save_button").attr("disabled"))
					$("#save_button").removeAttr("disabled");
			}
			roleService.get(id).then(function(result){
				$scope.role = result.role;
				$scope.menuIds = result.menuIds;
				$scope.resourceIds = result.resourceIds;

				if($scope.currentOpt == 'edit4menu'){
					var nodes = $scope.generateZnodes_menu($rootScope.$menuJson,$scope.menuIds);
					$scope.zTree_menu = $.fn.zTree.init($("#ztree_menu"), $scope.zTreeSetting_menu, nodes);
				}
				if($scope.currentOpt == 'edit4resource'){
					roleService.getAllResource().then(function(result){
						var nodes = $scope.generateZnodes_resource(result,$scope.resourceIds);
						$scope.zTree_resource = $.fn.zTree.init($("#ztree_resource"), $scope.zTreeSetting_resource, nodes);
					});
				}


				$('#addInfo').modal('show');
			});
		}

		$scope.save = function(){
			$scope.role.menuIds = $scope.menuIds;
			$scope.role.resourceIds = $scope.resourceIds;
			
			$("#save_button").attr("disabled","true");
			$('#addInfo').modal('hide');
			if($scope.currentOpt == 'add'){
				roleService.add($scope.role).then(function(result){
					console.log(result);
					$scope.onSelectPage(1);
					$("#save_button").removeAttr("disabled");
				});
			}
			if($scope.currentOpt == 'edit'){
				roleService.edit($scope.role).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
					$("#save_button").removeAttr("disabled");
				});
			}
			if($scope.currentOpt == 'edit4menu'){
				roleService.editMenu($scope.role).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
					$("#save_button").removeAttr("disabled");
				});
			}
			if($scope.currentOpt == 'edit4resource'){
				roleService.editResource($scope.role).then(function(result){
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
				roleService.delete(row.entity.id).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
				});
				$('#delete').unbind("click"); 
			});
		}

		//=================delete end ==================//
	}]).factory('roleService', ['baseRestService', function(baseRestService) {


		var service = {};

		var _service = baseRestService.all('role');
		var _service_resource = baseRestService.all('resource');

		service.page = function(page, pageSize) {
			return _service.get('page', {
				page: page,
				pageSize: pageSize
			});
		}

		service.get = function(id) {
			return _service.get(''+id);
		}
		service.edit = function(entity){
			return _service.customPUT(entity,entity.id+"");
		}

		service.editMenu = function(entity){
			return _service.customPUT(entity,entity.id+"/menu");
		}
		
		service.editResource = function(entity){
			return _service.customPUT(entity,entity.id+"/resource");
		}

		service.delete = function(id){
			return _service.customDELETE(''+id);
		}

		service.add = function(entity){
			return _service.customPOST(entity,'');
		}


		service.getAllResource = function() {
			return _service_resource.get('/all');
		}
		return service;
	}]);