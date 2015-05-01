(function() {

    'use strict';

    var app = angular.module('unit.directives', []);

    //Directive for selecting all on :focus
    //From: Martin - http://stackoverflow.com/questions/14995884/select-text-on-input-focus
    app.directive('selectOnClick', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                element.on('click', function () {
                    this.select();
                });
            }
        };
    });

    app.directive('menuFieldButton', function() {
        return{
            restrict: 'E',
            template:   "<div class='pull-right menu-field-button' ng-click='menu_field_button_click()'><span class='hidden-xs'>{{menu_field_button}} </span><span class='fa {{menu_field_button_icon}}'></span> </div>"
        };
    });

    app.directive('menuFieldName', function($location, menu_field_name){
        return{
            restrict: 'E',
            template: "<input type='textbox' ng-disabled='{{ readonly }}' ng-model='value' ng-class='{disabled: enabled == false}' select-on-click/>",
            scope: {
                readonly: "=",
                value: "=",
                enabled: "="
            }
        };
    });
})();