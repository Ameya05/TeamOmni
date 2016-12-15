angular
	.module('omni')
	.controller(
		'jobHistory',
		[
			'$scope',
			'$http',
			function($scope, $http) {

				/*
				 * Basic JS validation
				 */
				if (null == document.getElementById('idToken')
						|| document.getElementById('idToken').value == "") {
					window.location = '#login';
					return false;
				}
				var uid = document.getElementById('uid').value;

				$http({
					url : "rest/services/fetch/workflows/" + uid,
					method : "GET",
					params : {
						"idtoken" : idToken
					}
				})
				.success(
					function(response) {
						$scope.workflows = response.data;
				})
				.error(function(response) {
					alert("Failed to fetch job history");
				})
			} ]);