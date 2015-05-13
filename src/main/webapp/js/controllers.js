(function() {

    'use strict';

    var app = angular.module('unit.controllers', [
        'ngRoute',
        'services',
        'ui.bootstrap',
        'ngFileUpload',
        'zeroclipboard',
        'angular-loading-bar',
        'ngAnimate'
    ]);

    app.controller('ApplicationCtrl', function($scope, $rootScope, menu_field_name, menu_field_button) {
        $scope.menu_field_button = menu_field_button;
        $scope.menu_field_name = menu_field_name;
        menu_field_name.disable();

        $rootScope.simulationAuth = [];
    });

    app.controller('HomeCtrl', function(menu_field_button, menu_field_name){
        menu_field_button.reset();
        menu_field_name.disable();
    });

    app.controller("TabCtrl", function($scope, $rootScope, $location) {

        $scope.tabs = [
            {name: 'HOME', link: "/"},
            {name: "SIMULATIONS", link: "/simulation"},
            {name: "TIMETABLES", link: "/timetable"},
            {name: "LOCATIONS", link: "/preset"},
        ];

        $scope.select= function(item) {
            $location.path(item.link);
            $location.replace();
        };

        $scope.itemClass = function(item) {
            return item.link == $location.path() ? 'active' : '';
        };
    });

    app.controller("CollapseCtrl", function($scope, $window){

        $scope.isCollapsed = true;

        $scope.openFullDoc = function (){
            $window.open("#/documentation", "_blank");
        };

    });

    app.controller('FullDocumentationCtrl', function($scope, $location, $anchorScroll, menu_field_button){
        menu_field_button.reset();

        $scope.scrollTo = function(id) {
            $location.hash(id);
            $anchorScroll();
        };
    });

    app.controller('ColorTestCtrl', function($scope){
        $scope.testcolor = 2;

        function hsv2rgb(h, s, v) {
            // adapted from http://schinckel.net/2012/01/10/hsv-to-rgb-in-javascript/
            var rgb, i, data = [];
            if (s === 0) {
                rgb = [v,v,v];
            } else {
                h = h / 60;
                i = Math.floor(h);
                data = [v*(1-s), v*(1-s*(h-i)), v*(1-s*(1-(h-i)))];
                switch(i) {
                    case 0:
                        rgb = [v, data[2], data[0]];
                        break;
                    case 1:
                        rgb = [data[1], v, data[0]];
                        break;
                    case 2:
                        rgb = [data[0], v, data[2]];
                        break;
                    case 3:
                        rgb = [data[0], data[1], v];
                        break;
                    case 4:
                        rgb = [data[2], data[0], v];
                        break;
                    default:
                        rgb = [v, data[0], data[1]];
                        break;
                }
            }
            return '#' + rgb.map(function(x){
                    return ("0" + Math.round(x*255).toString(16)).slice(-2);
                }).join('');
        }

        $scope.colorFromValue = function(val){
            if(val > 100){
                val = 100;
            }
            var h = Math.floor((100 - val) * 120 / 100);
            var s = Math.abs(val - 50) / 50;

            return hsv2rgb(h, s, 1);
        }
    });
})();