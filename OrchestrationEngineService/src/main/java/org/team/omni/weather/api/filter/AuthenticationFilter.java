package org.team.omni.weather.api.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.team.omni.weather.api.services.AuthResult;
import org.team.omni.weather.api.services.AuthService;

/**
 * 
 * @author Ameya Advankar, Eldho Mathulla
 *
 */
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
	@Inject
	private AuthService authService;

	@Context
	private HttpServletRequest httpServletRequest;

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		String contextPath = request.getUriInfo().getPath();
		HttpSession httpSession = httpServletRequest.getSession(true);
		if (!contextPath.startsWith("/rest/test") && !(httpSession != null && httpSession.getAttribute("user") != null)) {
			MultivaluedMap<String, String> queryParameters = request.getUriInfo().getQueryParameters();
			String idToken = queryParameters.getFirst("idtoken");
			AuthResult authResult = null;
			if (idToken == null || !(authResult = authService.authenticate(idToken)).isAuthenticated()) {
				throw new WebApplicationException(Status.UNAUTHORIZED);
			} else {
				httpSession.setAttribute("user", authResult.getUserID());
			}
		}
	}
}
