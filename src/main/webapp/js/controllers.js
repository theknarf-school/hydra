(function() {

    'use strict';

    var app = angular.module('unit.controllers', [
        'ngRoute',
        'services'
    ]);

    app.controller('ApplicationController', ['$scope', '$rootScope', '$location', 'menu_field_name', function($scope, $rootScope, $location, menu_field_name) {
        $rootScope.menu_field_button = "New Simulation";
        $rootScope.menu_field_button_icon = "fa-plus-circle";
        $rootScope.menu_field_button_click = function() {
            $location.path('/newsimulation');
        };

        $rootScope.menu_field_name = menu_field_name;
        menu_field_name.disable();
    }]);

    app.controller('SimulationController', ['$scope', 'Simulation', function ($scope, Simulation) {
        $scope.simulations = Simulation.query({});

        $scope.deleteSimulation = function(id) {

            Simulation.delete({}, {"id": id}, function() {
                $scope.simulations = Simulation.query({});
            });

        };
    }]);

    app.controller('SimulationNew', ['$scope', '$location', '$rootScope', 'Simulation', 'SimResult', 'menu_field_name', function ($scope, $location, $rootScope, Simulation, SimResult, menu_field_name) {
        $scope.timeBetweenBuses = 10;
        $scope.numberOfEntrances = 1;
        $scope.days = 0;
        $scope.hours = 1;
        $scope.minutes = 0;
        $scope.entitesToProduce = 1;
        $scope.entitesConsumedPerTick = 1;

        menu_field_name.setValue("Untitled simulation");

        $rootScope.menu_field_button = "Submit";
        $rootScope.menu_field_button_icon = "fa-arrow-circle-right";
        $rootScope.menu_field_button_click = function() {
            var sim = new Simulation({
                'name': menu_field_name.value,
                'timeBetweenBuses': $scope.timeBetweenBuses,
                'numberOfEntrances': $scope.numberOfEntrances,
                'ticks': $scope.days*24*60 + $scope.hours * 60 + $scope.minutes,
                'entitesToProduce': $scope.entitesToProduce,
                'entitesConsumedPerTick': $scope.entitesConsumedPerTick
            });

            sim.$save().then(function(result) {
                $location.path('/result');
                $location.replace();

                SimResult.data = result;
            });
        };
    }]);

    app.controller('SimulationResult', ['$scope', '$rootScope', 'SimResult', function($scope, $rootScope, SimResult) {
        $scope.entitiesConsumed         = SimResult.data.entitiesConsumed;
        $scope.entitiesInQueue          = SimResult.data.entitiesInQueue;
        $scope.maxWaitingTimeInTicks    = SimResult.data.maxWaitingTimeInTicks;

        $rootScope.menu_field_button = "";
        $rootScope.menu_field_button_icon = "";
        $rootScope.menu_field_button_click = function() {};
    }]);

})();