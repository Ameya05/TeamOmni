package org.team.omni.weather.api.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * 
 * @author Ameya Advankar
 *
 */
public class AuthService {
	
	private static final Logger LOGGER = Logger.getLogger("Orchestration");
    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();
    private static final String CLIENT_ID = "335496797213-lfl268trivj44l05q58ia6j33kpu16n0.apps.googleusercontent.com";
    
    /**
     * Authenticates the given idToken against the CLIENT ID
     * @param idToken
     * @return True if authenticated successfully, False if not authenticated or exception encountered
     */
	public boolean authenticate(String idToken) 
	{	
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				.setAudience(Arrays.asList(CLIENT_ID))
			    .setIssuer("https://accounts.google.com")
			    .build();
		try 
		{
			GoogleIdToken gidToken = verifier.verify(idToken);
			
			if (gidToken != null)
			{
				Payload payload = gidToken.getPayload();
				
				String userId = payload.getSubject();
				String name = (String) payload.get("name");
				LOGGER.log(Level.INFO,"Successfully authenticated user ( "+name+"-"+userId+" )");
				return true;
			}
			else
			{
				LOGGER.log(Level.INFO,"Invalid token :"+idToken);
				return false;
			}
		} 
		catch (GeneralSecurityException | IOException e) 
		{
			LOGGER.log(Level.SEVERE, "Exception while authenticating Login Credentials for" +idToken , e);
			return false;
		}
	}
}
