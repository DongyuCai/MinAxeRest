'use strict';

/* Filters */

//以下是appBase里面的过滤器，一些数据库原始字段类型转换

angular.module('app.filters', [])
    .filter('toInquiryStatus', function() {
        var statusFilter = function(input) {
            var value = "";
            if (input != null && input != '' && input != 'undefined') {
                if (input == 10) {
                    value = "未回复";
                } else if (input == 20) {
                    value = "已回复";
                }
            }
            return value;
        }
        return statusFilter;
    })
    .filter('flag', function() {
        var statusFilter = function(input) {
            var value = "";
            if (input != null && input != '' && input != 'undefined') {
                if (input == 0) {
                    value = "有效";
                } else if (input == 20) {
                    value = "无效";
                }
            }
            return value;
        }
        return statusFilter;
    })
    .filter('ifShow', function() {
        var statusFilter = function(input) {
            var value = input;
            if (input == 0) {
                value = "不展示";
            } else if (input == 1) {
                value = "展示";
            }
            return value;
        }
        return statusFilter;
    })
    .filter('sys', function() {
        var statusFilter = function(input) {
            var value = input;
            if (input == 'MZ') {
                value = "M站";
            } else if (input == 'JFSC') {
                value = "积分商城";
            } else if (input == 'HSY_ANDROID') {
                value = "回收员Android端";
            } else if (input == 'HSY_IOS') {
                value = "回收员IOS端";
            } else if (input == 'HSZ_ANDROID') {
                value = "回收站Android端";
            } else if (input == 'HSZ_IOS') {
                value = "回收站IOS端";
            }
            return value;
        }
        return statusFilter;
    })
    .filter('businessCategoryCode', function() {
        var statusFilter = function(input) {
            var value = input;
            if (input == 1) {
                value = "电子";
            } else if (input == 2) {
                value = "服装";
            } else if (input == 3) {
                value = "书刊";
            } else if (input == 4) {
                value = "金属";
            } else if (input == 5) {
                value = "家具";
            }
            return value;
        }
        return statusFilter;
    })
    .filter('time', function() {
        var statusFilter = function(input) {
            var value = "无";
            if (input != null && input != '' && input != 'undefined') {
                value = new Date(input).Format("yyyy/MM/dd hh:mm:ss");
            }
            return value;
        }
        return statusFilter;
    });