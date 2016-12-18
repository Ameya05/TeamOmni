package org.team.omni.weather.api.rest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ext.Provider;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.orchestration.engine.workflow.WorkFlowMap;
import org.team.omni.weather.api.services.AuthService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Provider
public class DependencyInjectionBinder extends AbstractBinder {
	private static final Logger LOGGER = Logger.getLogger("Orchestration");
	private static final String CLIENT_ID = "335496797213-lfl268trivj44l05q58ia6j33kpu16n0.apps.googleusercontent.com";

	public DependencyInjectionBinder() {
	}

	@Override
	protected void configure() {
		bind(WorkFlowMap.getWorkFlowMap()).to(WorkFlowMap.class);
		bind(createAuthService()).to(AuthService.class);
	}

	public AuthService createAuthService() {
		try {
			GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), new JacksonFactory()).setAudience(Arrays.asList(CLIENT_ID)).setIssuer("accounts.google.com").build();
			return new AuthService(googleIdTokenVerifier);
		} catch (GeneralSecurityException | IOException e) {
			LOGGER.log(Level.SEVERE, "Could not create Google Net Http Transport", e);
			throw new OrchestrationEngineException(e);
		}
	}

}
