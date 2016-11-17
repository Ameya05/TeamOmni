package org.team.omni.weather;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 * 
 * @author Eldho Mathulla
 *
 */
@Provider
public class LoadBalancingRequestFilter implements ContainerRequestFilter {
	private static ServiceRegistration serviceRegistration;

	public LoadBalancingRequestFilter() {
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
			if (serviceRegistration != null) {
				serviceRegistration.updateWorkLoad(1);
			}
		} catch (ServiceUpdationException e) {
			throw new ServiceException(e);
		}
	}

	public static void setServiceRegistration(ServiceRegistration serviceRegistration) {
		LoadBalancingRequestFilter.serviceRegistration = serviceRegistration;
	}

}
