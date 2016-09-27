angular.module('omni',[])
	.controller('DiagnoseWeather', function($scope, $http) {
		$scope.click = function(){
			
			/*
			* Basic JS validation
			*/
			
			if( null == $scope.station 
				|| null == $scope.date
				|| null == $scope.time)
			{
				alert("Please enter "
						+(null == $scope.date ? "Date" :
							(null == $scope.time ? "Time" : "Station Name")
						)
					);
				return false;
			}
			
			var day = $scope.date.getDate();
			var mon = $scope.date.getMonth() + 1;
			var year = $scope.date.getFullYear();
			var dateString= ""+ (mon<10  ? "0" + mon : mon) + "/" + (day<10 ? "0" + day : day) + "/"+ year ;
			
			var hour = $scope.time.getHours();
			var min = $scope.time.getMinutes();
			var sec = $scope.time.getSeconds();
			var timeString= ""+ (hour<10 ? "0" + hour : hour) + (min<10 ? "0" + min : min) + (sec<10 ? "0" + sec : sec) ;
			
			$http({
				url: "rest/initiate/",
				method: "GET",
				params: {
					"uid": 001,
					"idtoken": "123abc",
					"date": dateString,
					"time": timeString,
					"station": $scope.station
				}
			})
			.success(function(response) {
				$scope.status=response;
				alert($scope.status);
			})
			.error(function(response){
				$scope.status = "Error while processing...";
			});
		}
		
		$scope.fetch_status=function(){
			$http({
				url:"rest/queryStatus/001",
				method: GET
			})
			.success(function(response){
				alert(response.executionStatus);
			})
			.error(function(response){
				alert(response.executionStatus);
			})
		}
	});