package org.team.omni.weather.api.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.team.omni.weather.api.services.AuthService;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter
{
	private static final Logger LOGGER = Logger.getLogger("Orchestration");
	
	@Override
	public void filter(ContainerRequestContext request) throws IOException 
	{
		MultivaluedMap<String, String> queryParameters = request.getUriInfo().getQueryParameters();
		
		String idToken = queryParameters.getFirst("idToken");
		LOGGER.log(Level.INFO, "Size of queryParameters: "+queryParameters.size()+"\n Authenticating idToken: "+idToken);
		
		AuthService authService = new AuthService();
		
		if(idToken==null || !authService.authenticate(idToken))
		{
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
	}
}
