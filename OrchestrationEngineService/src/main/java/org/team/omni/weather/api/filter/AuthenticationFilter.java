package org.team.omni.weather.api.filter;

import java.io.IOException;

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
	@Override
	public void filter(ContainerRequestContext request) throws IOException 
	{
		MultivaluedMap<String, String> queryParameters = request.getUriInfo().getQueryParameters();
		String idToken = queryParameters.getFirst("idtoken");
		AuthService authService = new AuthService();
		
		if(idToken==null || !authService.authenticate(idToken))
		{
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
	}
}
