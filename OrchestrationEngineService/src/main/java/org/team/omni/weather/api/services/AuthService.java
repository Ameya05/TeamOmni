package org.team.omni.weather.api.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;


/**
 * 
 * @author Ameya Advankar
 *
 */
public class AuthService {

	private static final Logger LOGGER = Logger.getLogger("Orchestration");

	private final GoogleIdTokenVerifier verifier;

	public AuthService(GoogleIdTokenVerifier googleIdTokenVerifier) {
		this.verifier = googleIdTokenVerifier;
	}

	/**
	 * Authenticates the given idToken against the CLIENT ID
	 * 
	 * @param idToken
	 * @return True if authenticated successfully, False if not authenticated or
	 *         exception encountered
	 */
	public boolean authenticate(String idToken) {
		try {
			GoogleIdToken gidToken = verifier.verify(idToken);
			if (gidToken != null) {
				Payload payload = gidToken.getPayload();
				String userId = payload.getSubject();
				String name = (String) payload.get("name");
				LOGGER.info("Successfully authenticated user ( " + name + "-" + userId + " )");
				return true;
			} else {
				LOGGER.info("Invalid token :" + idToken);
				return false;
			}
		} catch (GeneralSecurityException | IOException e) {
			LOGGER.log(Level.SEVERE, "Exception while authenticating Login Credentials for" + idToken, e);
			return false;
		}
	}
}
