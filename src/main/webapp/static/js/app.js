var app = angular.module('app', []);

app.controller("timesheetController", function($scope, $http) {

    $scope.missingTimeSheetPercentageData = "";
    $scope.listOfDefaulters = "";
    $scope.init = initialize;
    var city = $.cookie('city');

    function initialize() {
        //To get the timesheet percentage data for all the cities in India
        $http({
            method: 'GET',
            url: 'india/missingTimeSheetPercentage'
        }).then(function successCallback(response) {
            $scope.missingTimeSheetPercentageData = response.data;
            $scope.dataHasLoaded = true;
        }, function errorCallback(response) {
            alert("Oops! Something went wrong...")
        });

        // To get the list of defaulters
        $http({
            method: 'GET',
            url: '/city/' + city
        }).then(function successCallback(response) {
            $scope.listOfDefaulters = response.data;
            $scope.defaulterDataHasLoaded = true;
        }, function errorCallback(response) {
            alert("Oops! Something went wrong...")
        });
    }
});

