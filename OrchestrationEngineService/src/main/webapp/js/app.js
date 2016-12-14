angular.module('omni', ["ngRoute"])
	.config(function($routeProvider) {
		$routeProvider
		.when("/diagnose", {
			templateUrl : 'DiagnoseWeather.html',
		})
		.when("/login", {
			templateUrl : "blank.html"
		})
		.when("/history", {
			templateUrl : "JobDetails.html"
		})
		.otherwise({
			redirectTo: '/login'
		});
	});

function onSignIn(googleUser) {
	var profile = googleUser.getBasicProfile();
	var name = profile.getName();
	var uid = profile.getId();
	var id_token = googleUser.getAuthResponse().id_token;

	//Set UID, idToken and Username returned by Google
	document.getElementById('idToken').value = id_token;
	document.getElementById('userName').value = name;
	document.getElementById('uid').value = uid;

	document.getElementById('greeting').innerHTML = "Welcome "+name;

	showLoginDiv(true);
	window.location = '#diagnose';
}

function logoutUser(){
	var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
		showLoginDiv(false);
		document.getElementById('idToken').value="";
		document.getElementById('userName').value = "";
		document.getElementById('greeting').innerHTML = "";
		window.location = '#login';
    });
}

function showLoginDiv(loggedIn){
	if(loggedIn){
		document.getElementById('loginDiv').style.display = 'none';
		document.getElementById('logoutDiv').style.display = '';
	}
	else{
		document.getElementById('loginDiv').style.display = '';
		document.getElementById('logoutDiv').style.display = 'none';
	}
}


function showJobDetails(){
	
	//AddStuffHere

	window.location = '#JobDetails';

}