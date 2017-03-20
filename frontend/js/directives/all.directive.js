/**=========================================================
 * Module: sidebar.js
 * 左侧收缩菜单 by 董文昭 2015-10-29 PM4：58
 =========================================================*/
/**=========================================================
 * Module: sidebar.js
 * Wraps the sidebar and handles collapsed state
 =========================================================*/
(function () {
    'use strict';

    angular
        .module('app.sidebar', [])
        .directive('sidebar', sidebar);

    sidebar.$inject = ['$rootScope', '$timeout', '$window', 'Utils'];
    function sidebar($rootScope, $timeout, $window, Utils) {
        var $win = angular.element($window);
        var directive = {
            // bindToController: true,
            // controller: Controller,
            // controllerAs: 'vm',
            link: link,
            restrict: 'EA',
            template: '<nav class="sidebar" ng-transclude></nav>',
            transclude: true,
            replace: true
            // scope: {}
        };
        return directive;

        function link(scope, element, attrs) {

            var currentState = $rootScope.$state.current.name;
            var $sidebar = element;

            var eventName = Utils.isTouch() ? 'click' : 'mouseenter';
            var subNav = $();

            $sidebar.on(eventName, '.nav > li', function () {

                if (Utils.isSidebarCollapsed() || $rootScope.app.layout.asideHover) {

                    subNav.trigger('mouseleave');
                    subNav = toggleMenuItem($(this), $sidebar);

                    // Used to detect click and touch events outside the sidebar
                    sidebarAddBackdrop();

                }

            });

            scope.$on('closeSidebarMenu', function () {
                removeFloatingNav();
            });

            // Normalize state when resize to mobile
            $win.on('resize', function () {
                if (!Utils.isMobile())
                    asideToggleOff();
            });

            // Adjustment on route changes
            $rootScope.$on('$stateChangeStart', function (event, toState) {
                currentState = toState.name;
                // Hide sidebar automatically on mobile
                asideToggleOff();

                $rootScope.$broadcast('closeSidebarMenu');
            });

            // Autoclose when click outside the sidebar
            if (angular.isDefined(attrs.sidebarAnyclickClose)) {

                var wrapper = $('.wrapper');
                var sbclickEvent = 'click.sidebar';

                $rootScope.$watch('app.asideToggled', watchExternalClicks);

            }

            //////

            function watchExternalClicks(newVal) {
                // if sidebar becomes visible
                if (newVal === true) {
                    $timeout(function () { // render after current digest cycle
                        wrapper.on(sbclickEvent, function (e) {
                            // if not child of sidebar
                            if (!$(e.target).parents('.aside').length) {
                                asideToggleOff();
                            }
                        });
                    });
                }
                else {
                    // dettach event
                    wrapper.off(sbclickEvent);
                }
            }

            function asideToggleOff() {
                $rootScope.app.asideToggled = false;
                if (!scope.$$phase) scope.$apply(); // anti-pattern but sometimes necessary
            }
        }

        ///////

        function sidebarAddBackdrop() {
            var $backdrop = $('<div/>', {'class': 'dropdown-backdrop'});
            $backdrop.insertAfter('.aside-inner').on('click mouseenter', function () {
                removeFloatingNav();
            });
        }

        // Open the collapse sidebar submenu items when on touch devices
        // - desktop only opens on hover
        function toggleTouchItem($element) {
            $element
                .siblings('li')
                .removeClass('open')
                .end()
                .toggleClass('open');
        }

        // Handles hover to open items under collapsed menu
        // -----------------------------------
        function toggleMenuItem($listItem, $sidebar) {
            removeFloatingNav();

            var ul = $listItem.children('ul');

            if (!ul.length) return $();
            if ($listItem.hasClass('open')) {
                toggleTouchItem($listItem);
                return $();
            }

            var $aside = $('.aside');
            var $asideInner = $('.aside-inner'); // for top offset calculation
            // float aside uses extra padding on aside
            var mar = parseInt($asideInner.css('padding-top'), 0) + parseInt($aside.css('padding-top'), 0);
            var subNav = ul.clone().appendTo($aside);

            toggleTouchItem($listItem);

            var itemTop = ($listItem.position().top + mar) - $sidebar.scrollTop();
            var vwHeight = $win.height();

            subNav
                .addClass('nav-floating')
                .css({
                    position: $rootScope.app.layout.isFixed ? 'fixed' : 'absolute',
                    top: itemTop,
                    bottom: (subNav.outerHeight(true) + itemTop > vwHeight) ? 0 : 'auto'
                });

            subNav.on('mouseleave', function () {
                toggleTouchItem($listItem);
                subNav.remove();
            });

            return subNav;
        }

        function removeFloatingNav() {
            $('.dropdown-backdrop').remove();
            $('.sidebar-subnav.nav-floating').remove();
            $('.sidebar li.open').removeClass('open');
        }
    }


})();

/**
 * 所有平台相关的指令
 */
angular.module('app.directives', []).
    directive('appVersion', ['version', function (version) {
        return function (scope, elm, attrs) {
            elm.text(version);
        };
    }])
    /**
     * 分页指令
     */
    .directive('paging', [function () {
        return {
            restrict: 'E',
            template: '',
            replace: true,
            link: function (scope, element, attrs) {

                scope.$watch('numPages', function (value) {
                    scope.pages = [];
                    var begin = Math.max(1, scope.currentPage - 4 / 2);
                    var end = Math.min(begin + (4 - 1), scope.numPages);
                    //console.debug(begin+":"+end+":"+scope.numPages+":num");
                    for (var i = begin; i <= end; i++) {
                        scope.pages.push(i);
                    }
                });

                scope.$watch('currentPage', function (value) {
                    scope.pages = [];
                    var begin = Math.max(1, value - 4 / 2);
                    var end = Math.min(begin + (4 - 1), scope.numPages);
                    //console.debug(begin+":"+end+":"+scope.numPages+":cur");
                    for (var i = begin; i <= end; i++) {
                        scope.pages.push(i);
                    }
                });
                scope.isActive = function (page) {
                    return scope.currentPage === page;
                };
                scope.selectPage = function (page) {
                    if (!scope.isActive(page)) {
                        scope.currentPage = page;
                        scope.onSelectPage(page);
                    }
                };
                scope.selectPrevious = function () {
                    if (!scope.noPrevious()) {
                        scope.selectPage(scope.currentPage - 1);
                    }
                };
                scope.selectNext = function () {
                    if (!scope.noNext()) {
                        scope.selectPage(scope.currentPage + 1);
                    }
                };
                scope.noPrevious = function () {
                    return scope.currentPage <= 1;
                };
                scope.noNext = function () {
                    return scope.currentPage >= scope.numPages;
                };

            }
        };
    }])
    /**
     *
     */
    .directive('sub-menu-selected', [function () {
        return {
            restrict: 'A',
            link: function (scope, element, attr) {
                alert('aaa');
            }
        };
    }])
    /**
     *
     */
    .directive('pwCheck', [function () {
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, ctrl) {
                var firstPassword = '#' + attrs.pwCheck;
                elem.add(firstPassword).on('keyup', function () {
                    scope.$apply(function () {
                        var v = elem.val() === $(firstPassword).val();
                        ctrl.$setValidity('pwmatch', v);
                    });
                });
            }
        }
    }])
    .directive('gridheightresize', ['$window', function ($window) {
        return {
            link: function (scope, elem, attrs) {
                scope.onResize = function () {
                    var header = document.getElementsByTagName('nav')[0];
                    elem.windowHeight = $window.innerHeight - header.clientHeight - 30;
                    var toolbar = document.getElementById('toolbar');
                    if(toolbar){
                        elem.windowHeight = elem.windowHeight - toolbar.clientHeight;
                    }
                    var searchbar = document.getElementById('searchbar');
                    if(searchbar){
                        elem.windowHeight = elem.windowHeight - searchbar.clientHeight;
                    }
                    $(elem).height(elem.windowHeight);
                }
                scope.onResize();
                angular.element($window).bind('resize', function () {
                    scope.onResize();
                })
            }
        }
    }]).directive('elheightresize', ['$window', function ($window) {
        return {
            link: function (scope, elem, attrs) {
                scope.onResize = function () {
                    var header = document.getElementsByTagName('nav')[0];
                    elem.windowHeight = $window.innerHeight - header.clientHeight - 80;
                    $(elem).height(elem.windowHeight);
                }
                scope.onResize();
                angular.element($window).bind('resize', function () {
                    scope.onResize();
                })
            }
        }
    }]).directive('alheightresize', ['$window', function ($window) {
        return {
            link: function (scope, elem, attrs) {
                scope.onResize = function () {
                    var header = document.getElementsByTagName('nav')[0];
                    elem.windowHeight = $window.innerHeight - header.clientHeight - 140;
                    $(elem).height(elem.windowHeight);
                }
                scope.onResize();
                angular.element($window).bind('resize', function () {
                    scope.onResize();
                })
            }
        }
    }]).directive('containerresize', ['$window', function ($window) {
        return {
            link: function (scope, elem, attrs) {
                scope.onResize = function () {
                    elem.windowHeight = $window.innerHeight - 125;
                    $(elem).height(elem.windowHeight);
                    $(elem).css({"overflow-y": "auto", "overflow-x": "hidden"})
                }
                scope.onResize();
                angular.element($window).bind('resize', function () {
                    scope.onResize();
                })
            }
        }
    }]).directive('monitorheightresize', ['$window', function ($window) {
        return {
            link: function (scope, elem, attrs) {
                scope.onResize = function () {
                    var header = document.getElementsByTagName('nav')[0];
                    elem.windowHeight = $window.innerHeight - header.clientHeight - 80;
                    $(elem).height(elem.windowHeight);
                    $(elem).css({'margin-top': 52});
                }
                scope.onResize();
                angular.element($window).bind('resize', function () {
                    scope.onResize();
                })
            }
        }
    }]).directive('searchOpen', [function () {
        return {
            restrict: 'A',
            controller: function ($scope, $element, NavSearch) {
                $element
                    .on('click', function (e) {
                        e.stopPropagation();
                    })
                    .on('click', NavSearch.toggle);
            }
        }
    }]).directive('searchDismiss', [function () {
        return {
            restrict: 'A',
            controller: function ($scope, $element, NavSearch) {
                var inputSelector = '.navbar-form';

                $(inputSelector)
                    .on('click', function (e) {
                        e.stopPropagation();
                    })
                    .on('keyup', function (e) {
                        if (e.keyCode === 27) // ESC
                            NavSearch.dismiss();
                    });

                // click anywhere closes the search
                $(document).on('click', NavSearch.dismiss);
                // dismissable options
                $element
                    .on('click', function (e) {
                        e.stopPropagation();
                    })
                    .on('click', NavSearch.dismiss);
            }
        }
    }]).directive('settingBox', [function () {
        return {
            restrict: 'A',
            controller: function ($scope, $element, settingBox) {
                $element
                    .on('click', function (e) {
                        e.stopPropagation();
                    })
                    .on('click', settingBox.toggle);
            }
        }
    }]).directive('settingDismiss', [function () {
        return {
            restrict: 'A',
            controller: function ($scope, $element, settingBox) {
                var checkSelector = '.setting-box';

                $(checkSelector)
                    .on('click', function (e) {
                        e.stopPropagation();
                    })
                // click anywhere closes the search
                $(document).on('click', settingBox.dismiss);
                // dismissable options
                $element
                    .on('click', function (e) {
                        e.stopPropagation();
                    })
                    .on('click', settingBox.dismiss);
            }
        }
    }]).directive('ngThumb', ['$window', function($window) {
        var helper = {
            support: !!($window.FileReader && $window.CanvasRenderingContext2D),
            isFile: function(item) {
                return angular.isObject(item) && item instanceof $window.File;
            },
            isImage: function(file) {
                var type =  '|' + file.type.slice(file.type.lastIndexOf('/') + 1) + '|';
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
        };

        return {
            restrict: 'A',
            template: '<canvas/>',
            link: function(scope, element, attributes) {
                if (!helper.support) return;

                var params = scope.$eval(attributes.ngThumb);

                if (!helper.isFile(params.file)) return;
                if (!helper.isImage(params.file)) return;

                var canvas = element.find('canvas');
                var reader = new FileReader();

                reader.onload = onLoadFile;
                reader.readAsDataURL(params.file);

                function onLoadFile(event) {
                    var img = new Image();
                    img.onload = onLoadImage;
                    img.src = event.target.result;
                }

                function onLoadImage() {
                    var width = params.width || this.width / this.height * params.height;
                    var height = params.height || this.height / this.width * params.width;
                    canvas.attr({ width: width, height: height });
                    canvas[0].getContext('2d').drawImage(this, 0, 0, width, height);
                }
            }
        };
    }]);