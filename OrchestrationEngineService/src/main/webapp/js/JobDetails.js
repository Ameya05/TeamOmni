angular
		.module('omni')
		.controller(
				'JobDetails',
				[
						'$scope',
						'$http',
						function($scope, $http) {

							$scope.getJobs = function() {

								// Check if idToken is set, if not then redirect
								// to login page
								if (null == document.getElementById('idToken')
										|| document.getElementById('idToken').value == "") {
									window.location = '#login';
									return false;
								}

								var idToken = document
										.getElementById('idToken').value;
								var uid = document.getElementById('uid').value;

								$http({
									url : "rest/services/queryStatus/" + uid,
									method : "GET",
									params : {
										"idtoken" : idToken
									}
								})

								//AddStuffhere
										.success(
												function(response) {
													var resp_data = response.data;
													if (response.type == "status") {
														alert(resp_data.executionStatus);
													} else {
														alert("REsponse");

													}
												}).error(function(response) {
											alert(response.executionStatus);
										})
							}
						} ]);