(function() {

    'use strict';

    var app = angular.module('unit.controllers');

    app.controller('SimulationResultCtrl', function($scope, $rootScope, $filter, $location, $routeParams, SimResult, Simulation, cfpLoadingBar, menu_field_name, menu_field_button) {

        $scope.simulation = {
            nodes: [],
            relationships: []
        };
        $scope.loaded = false;

        menu_field_name.readonly = true;

        cfpLoadingBar.start();

        // Check that the promise exists
        if(typeof SimResult.data !== "undefined") {
            SimResult.data.then(function(result) {
                // The id is not set
                if(typeof $routeParams.id === "undefined") {
                    $location.path("/result/" + result.id);
                    $location.replace();
                }

                Simulation.run({}, {id: result.id, breakpoints: 0}, init);
            });
        } else {
            // If the promise doesn't exists, reload the data from the api
            Simulation.run({}, {id: $routeParams.id}, init);
        }

        function init(result) {

            $scope.simulation = result;
            console.log(result);

            // Close the loading bar
            cfpLoadingBar.complete();
            $scope.loaded = true;

            var from  = ticksToTime($scope.simulation.startTick);
            var to = ticksToTime($scope.simulation.startTick + $scope.simulation.ticks);

            var fromHours = parseInt(from);
            var fromMinutes = Number( ( ( parseFloat( from ) % fromHours ) * 60 ).toFixed(2) );
            $scope.from = new Date();
            $scope.from.setHours( fromHours );
            $scope.from.setMinutes( fromMinutes );
            $scope.from.setSeconds(0);
            $scope.fromPrint = $filter('date')($scope.from, "HH:mm");

            var toHours = parseInt(to);
            var toMinutes = Number( ( ( parseFloat( to ) % toHours ) * 60 ).toFixed(2) );
            $scope.to = new Date();
            $scope.to.setHours( toHours );
            $scope.to.setMinutes( toMinutes );
            $scope.to.setSeconds(0);
            $scope.toPrint = $filter('date')($scope.to, "HH:mm");

            var test = $scope.simulation.result.maxWaitingTimeInTicks;
            if(test !== 0){
                $scope.maxWaitingTimeInMinutes = ticksToTime(test);
            }
            else {
                $scope.maxWaitingTimeInMinutes = 0 + " seconds";
            }

            $scope.entitiesConsumed = $scope.simulation.result.entitiesConsumed;
            $scope.entitiesInQueue = $scope.simulation.result.entitiesInQueue;
            $scope.bussesInQueue = $scope.simulation.entitiesQueueing.length;
        }

        //Function for ticks to seconds/minutes/hours
        function ticksToTime(ticks){
            var hh = Math.floor( ticks / 3600);
            var mm = Math.floor( (ticks % 3600) / 60);
            var ss = (ticks % 3600) % 60;

            var time = '';

            if(hh > 0) time = hh + " hour" + (hh>1? "s" :"");
            if(mm > 0) time += " " + mm + " minute" + (mm>1? "s" :"");
            if(ss > 0) time += " " + ss + " second" + (ss>1? "s" :"");

            return time;
        }

        // Tooltip for resultpage
        $scope.extraTooltip = function() {

            return d3.behavior
                .tooltip()
                .text(function(d) {
                    switch (d.type) {
                        case "bus":
                        case "train":
                            return d.timetable.name + "<br/>" +
                                "Brought " + d.entitiesTransfered + " passengers to the location." + "<br/>" +
                                "Number of arrivals: " + d.numberOfArrivals + "<br/>" +
                                "Number of "  + d.type + " in queue: " + d.numberOfBusesInQueue;
                        //TODO: Legge number of buses in queue bare på buss


                        case "passengerflow":
                            return "Persons per arrival: " + d.personsPerArrival + "<br/>" +
                                "Time between arrivals: " + ticksToTime(d.timeBetweenArrivals) + "<br/>" +
                                "Brought " + d.entitiesTransfered + " passengers to the location.";


                        case "parking":
                            console.log("d:" + d);
                            return "Buses handled every " + ticksToTime(d.ticksToConsumeEntity) + "<br/>" +
                                "Brought " + d.entitiesTransfered + " passengeres to the location.";

                        case "desktop":
                        case "consumerGroup-desktop":
                        case "consumerGroup-suitcase":
                        case "door":
                        case "suitcase":
                            var maxWaitingTime;
                            if(d.maxWaitingTimeOnCurrentNode !== 0){
                                maxWaitingTime = ticksToTime(d.maxWaitingTimeOnCurrentNode);
                            } else{
                                maxWaitingTime = 0;
                            }
                            var printForConsumer = "Passengers handled every " + ticksToTime(d.ticksToConsumeEntity) + "<br/>" +
                                "Passengers that went through: " + d.entitiesConsumed.length + "<br/>" +
                                "Max waiting time: " + maxWaitingTime;
                            if(d.type.indexOf("consumerGroup") != -1) {
                                printForConsumer += "<br/>" + "Passengers in queue at simulation end: " + d.numberOfConsumersInQueue +
                                "<br/>" + "Quantity: " + d.consumers.length + "</br>";
                            }else{
                                printForConsumer += "<br/>" + "Passengers in queue at simulation end: " + d.entitiesInQueue.length;
                            }
                            return printForConsumer;

                        default:
                            return d.weight + "%";

                    }
                });
        };


        $scope.extraBorder = function() {

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

            function colorFromValue(val){
                if(val > 100){
                    val = 100;
                }
                var h = Math.floor((100 - val) * 120 / 100);
                var s = Math.abs(val - 50) / 50;

                return hsv2rgb(h, s, 1);
            }

            return d3.behavior.border()
                .color(function (d) {
                    if(typeof d.entitiesInQueue !== "undefined" && d.type !== "parking") {
                        if(d.type == "consumerGroup-desktop" || d.type == "consumerGroup-suitcase"){
                            return colorFromValue(d.numberOfConsumersInQueue);
                        } else{
                            return colorFromValue(d.entitiesInQueue.length);
                        }
                    }
                    if(typeof d.numberOfBusesInQueue !== "undefined" ){
                        return colorFromValue(d.numberOfBusesInQueue * 50);
                    }
                })
                .width(function (d) {
                    return "5px";
                });
        };

        menu_field_button.reset();
    });

})();