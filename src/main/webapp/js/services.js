(function() {

    'use strict';

    var app = angular.module('services', [
        'ngResource'
    ]);

    app.factory('Simulation', ['$resource', function($resource) {
        return $resource('api/simulation/:simulationId', {simulationId: '@id'});
    }]);

    app.factory('SimResult', function() {
        return {
            data: {}
        }
    });

    app.factory('menu_field_name', function() {
        return {
            value: '',
            enabled: false,
            disable: function() {
                this.enabled = false;
                this.value = "";
            },
            enable: function() {
                this.enabled = true;
            },
            setValue: function(value) {
                this.value = value;
                this.enable();
            }
        };
    });

})();