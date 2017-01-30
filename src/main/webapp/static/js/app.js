var app = angular.module('app', []);

app.controller("timesheetController", function($scope, $http) {

    $scope.missingTimeSheetPercentage = "";
    $scope.init = initialize;

    function initialize() {
        $http({
            method: 'GET',
            url: 'india/missingTimeSheetPercentage'
        }).then(function successCallback(response) {
            $scope.missingTimeSheetPercentageData = response.data;
            $scope.dataHasLoaded = true;
        }, function errorCallback(response) {
            alert("Oops! Something went wrong...")
        });
    }
});

