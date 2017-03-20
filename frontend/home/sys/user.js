'use strict';

angular.module('MainApp', [])
	.controller('userCtrl',['$rootScope','$scope', '$http', 'i18nService', 'userService','toaster', 
		function($rootScope,$scope, $http, i18nService, userService, toaster) {
		
        console.log("userCtrl");
		i18nService.setCurrentLang('zh-cn');
        //====================page start========================//
        $scope.username = "";

		$scope.roleAll = [];
		$scope.status4search = "";
 
		$scope.refreshAllRole = function(){
			userService.getAllRole().then(function(result) {
				$scope.roleAll = result;
			});
		}
		//刷新角色列表
		$scope.refreshAllRole();


		$scope.getRoleStr = function(roleId){
			var roleName = "";
			for(var i=0;i<$scope.roleAll.length;i++){
				if(roleId.indexOf(','+$scope.roleAll[i].id+',') >= 0){
					roleName = roleName + $scope.roleAll[i].name + '、';
				}
			}

			if(roleName.length > 0){
				roleName = roleName.substring(0,roleName.length-1);
			}
			return roleName;
		}


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
			userService.page(page, pageSize, $scope.username, $scope.status4search).then(function(result) {
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
					field: 'username',
					displayName: '登录名'
				}, {
					field: 'roleStr',
					displayName: '角色',
					cellTemplate: cellTemplate_start+'getRoleStr(row.entity.roleId)'+cellTemplate_end
				}, {
					field: 'caozuo',
					displayName: '操作',
					width: 300,
					enableSorting: false,
					cellTemplate: '<button class="btn btn-infos" ng-click="grid.appScope.showEditInfo(row.entity.id,\'edit4role\')"><i class="fa fa-edit"></i>角色</button>' +
						'<button class="btn btn-infos" ng-click="grid.appScope.showEditInfo(row.entity.id,\'edit4password\')"><i class="fa fa-edit"></i>重置密码</button>' +
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

		$scope.user = new Object();
		$scope.roleIds = "";
        $scope.currentOpt = '';

        $scope.saveInvalid = function(){
        	if($scope.currentOpt == 'add'){ 
	        	if(!$scope.user.username || $scope.user.username.length <= 0) 
	        		return true;
        		if(!$scope.user.password || $scope.user.password.length <= 0)
        			return true;
        	}
        	if($scope.currentOpt.indexOf('edit') >= 0 && (!$scope.user.id || $scope.user.id.length <= 0))
        		return true;
        	if($scope.currentOpt == 'edit4role'){
        		if(!$scope.roleIds || $scope.roleIds.length <= 0)
        			return true;
        	}
        	if($scope.currentOpt == 'edit4password'){
        		if(!$scope.user.oldPassword || $scope.user.oldPassword.length <= 0)
        			return true;
        		if(!$scope.user.password || $scope.user.password.length <= 0)
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

		$scope.showAddInfo = function(){
			$scope.user = new Object();

			$scope.currentOpt = 'add';
			if($scope.saveInvalid()){
				$("#save_button").attr("disabled","disabled");
			}else{
				if($("#save_button").attr("disabled"))
					$("#save_button").removeAttr("disabled");
			}
			$('#addInfo').modal('show');
		}
        
		$scope.showEditInfo = function(id,edit_type){
			$scope.currentOpt = edit_type;
			if($scope.saveInvalid()){
				$("#save_button").attr("disabled","disabled");
			}else{
				if($("#save_button").attr("disabled"))
					$("#save_button").removeAttr("disabled");
			}
			userService.get(id).then(function(result){
				$scope.user = result.user;
				$scope.roleIds = result.roleIds;
				$('#addInfo').modal('show');
			});
		}

		$scope.save = function(){
			$scope.user.roleIds = $scope.roleIds;
			
			$("#save_button").attr("disabled","true");
			$('#addInfo').modal('hide');
			if($scope.currentOpt == 'add'){
				//md5加密
				$scope.user.password = hex_md5($scope.user.password);
				userService.add($scope.user).then(function(result){
					console.log(result);
					$scope.onSelectPage(1);
					$("#save_button").removeAttr("disabled");
				});
			}
			if($scope.currentOpt == 'edit4role'){
				userService.editRole($scope.user).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
					$("#save_button").removeAttr("disabled");
				});
			}
			if($scope.currentOpt == 'edit4password'){
				//md5加密
				$scope.user.password = hex_md5($scope.user.password);
				$scope.user.oldPassword = hex_md5($scope.user.oldPassword);

				userService.editPassword($scope.user).then(function(result){
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
				userService.delete(row.entity.id).then(function(result){
					console.log(result);
					$scope.onSelectPage($scope.currentPage);
				});
				$('#delete').unbind("click"); 
			});
		}

		//=================delete end ==================//



	}]).factory('userService', ['baseRestService', function(baseRestService) {


		var service = {};

		var _service = baseRestService.all('user');

		var _service_role = baseRestService.all('role');

		service.page = function(page, pageSize, username, roleId) {
			return _service.get('page', {
				page: page,
				pageSize: pageSize,
				username: username,
				roleId: roleId
			});
		};

		service.get = function(id) {
			return _service.get(''+id);
		}

		service.editRole = function(entity){
			return _service.customPUT(entity,entity.id+"/role");
		}

		service.editPassword = function(entity){
			return _service.customPUT(entity,entity.id+"/password");
		}

		service.delete = function(id){
			return _service.customDELETE(''+id);
		}

		service.add = function(entity){
			return _service.customPOST(entity,'');
		}



		service.getAllRole = function() {
			return _service_role.get('all');
		}

		return service;
	}]);