package org.team.omni.weather.api.services;

public class AuthResult {
	private String userID = null;
	private String userName = null;
	private boolean authenticated = false;

	public AuthResult() {
	}

	public AuthResult(String userID, String userName, boolean authenticated) {
		this.setUserID(userID);
		this.setUserName(userName);
		this.setAuthenticated(authenticated);
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

}
