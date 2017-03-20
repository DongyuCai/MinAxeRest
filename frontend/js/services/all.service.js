'use strict';

/* Services */

angular.module('app.services', [])
 .service('NavSearch',[function(){
        this.toggle = toggle;
        this.dismiss = dismiss;

        ////////////////

        var navbarFormSelector = 'form.navbar-form';

        function toggle() {
          var navbarForm = $(navbarFormSelector);

          navbarForm.toggleClass('open');
          
          var isOpen = navbarForm.hasClass('open');
          
          //navbarForm.find('input')[isOpen ? 'focus' : 'blur']();
        }

        function dismiss() {
          $(navbarFormSelector)
            .removeClass('open') // Close control
            .find('input[type="text"]').blur() // remove focus
         //   .val('') // Empty input
        }
  }])
  .service('settingBox',[function(){
        this.toggle = toggle;
        this.dismiss = dismiss;

        ////////////////

        var navbarFormSelector = 'div.setting-box';

        function toggle() {
          var navbarForm = $(navbarFormSelector);

          navbarForm.toggleClass('open');
          
          var isOpen = navbarForm.hasClass('open');
          
          navbarForm.find('input')[isOpen ? 'focus' : 'blur']();
        }

        function dismiss() {
          $(navbarFormSelector)
            .removeClass('open') // Close control
           .find('input[type="text"]').blur() // remove focus
         //   .val('') // Empty input
        }
  }])
  .service('SidebarLoader',['$rootScope','$http', function($rootScope, $http) {
        this.getMenu = getMenu;

        function getMenu(onReady, onError) {

            onError = onError || function() {
                alert('菜单加载失败');
            };

            var renderMenuJson = function(menuJson,menuShowStr){
              for(var i=0;i<menuJson.length;i++){
                if(menuShowStr.indexOf(','+menuJson[i].sref+',') >= 0){
                  menuJson[i].show = true;
                }else{
                  menuJson[i].show = false;
                }

                if(menuJson[i].submenu){
                  var submenu = renderMenuJson(menuJson[i].submenu,menuShowStr);

                  for(var j=0;j<submenu.length;j++){
                    if(submenu[j].show){
                      menuJson[i].show = true;
                      break;
                    }
                  }
                }
              }

              return menuJson;
            }

            var menuJsonReady = function(menuJson){
              $rootScope.$menuJson = menuJson;//菜单配置保存起来

              menuJson = renderMenuJson(menuJson,$rootScope.userInfo.menu);

              onReady(menuJson);
            }
            
            var menuURL = 'js/services/sidebar-menu.json?v=' + (new Date().getTime()); // jumps cache
            $http.get(menuURL)
                .success(menuJsonReady)
                .error(onError);
        }
  }])
  .service('UserInfoLoader',['$rootScope','$http','localStorageService','toaster', function($rootScope, $http, localStorageService,toaster) {
        
        this.getUserInfo = getUserInfo;
        function getUserInfo(onReady){
          $http.get(API_BASE_URL+"/login/userInfo",{'X-1008-Session-Token':$rootScope.$token})
            .success(function(data,status,headers,config){
              $rootScope.userInfo = {
                id: data.id,
                username: data.username,
                role: data.role,
                menu: data.menu
              };
              if(onReady){
                onReady($rootScope.userInfo);
              }
            })
            .error(function(data,status,headers,config){
              // localStorageService.cookie.remove("$_1008_operation_token");
              // $rootScope.$token == null;
              // window.location.href = "login.html";
              toaster.pop('error', "500错误", '<ul><li>服务器500错误!获取不到身份信息！</li></ul>', 5000, 'trustedHtml');
            });
        }
  }]);