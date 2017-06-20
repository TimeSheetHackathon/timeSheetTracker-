var app = angular.module('app', []);

app.controller("timesheetController", function ($scope, $http) {

    $scope.missingTimeSheetPercentageData = "";
    $scope.listOfDefaulters = "";
    $scope.allCountries = "";
    $scope.projectData = "";
    $scope.init = initialize;
    var city = $.cookie('city');
    var country = $.cookie('country');
    var projectName = $.cookie('project');

    function initialize() {
        //To get the timesheet percentage data for all the cities in India
        $http({
            method: 'GET',
            url: '/' + country  +'/missingTimeSheetPercentage'
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

        $http({
            method: 'GET',
            url: '/getAllCountry'
        }).then(function successCallback(response) {
            $scope.allCountries = response.data;
            $scope.countriesGotLoaded = true;
        }, function errorCallback(response) {
            alert("Oops! Something went wrong...")
        });

        $http({
            method: 'GET',
            url: '/' + city + '/missingTimeSheetByProjects'
        }).then(function successCallback(response) {
            $scope.projectData = response.data;
            $scope.projectDataGotLoaded = true;
        }, function errorCallback(response) {
            alert("Oops! Something went wrong...")
        });

        $http({
            method: 'GET',
            url: '/city/' + city + '/project/' + projectName + '/missingTimeSheetByEmployees'
        }).then(function successCallback(response) {
            $scope.missingPeopleInProjectForACity = response.data;
            $scope.projectPeopleLoaded = true;
        }, function errorCallback(response) {
            alert("Oops! Something went wrong...")
        });
    }
});

