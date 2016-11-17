package org.team.omni.weather;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * 
 * @author Eldho Mathulla
 *
 */
@Provider
public class LoadBalancingResponseFilter implements ContainerResponseFilter {
	private static ServiceRegistration serviceRegistration;

	public LoadBalancingResponseFilter() {
	}

	@Override
	public void filter(ContainerRequestContext arg0, ContainerResponseContext arg1) throws IOException {
		try {
			if (serviceRegistration != null) {
				serviceRegistration.updateWorkLoad(-1);
			}
		} catch (ServiceUpdationException e) {
			throw new ServiceException(e);
		}
	}

	public static void setServiceRegistration(ServiceRegistration serviceRegistration) {
		LoadBalancingResponseFilter.serviceRegistration = serviceRegistration;
	}

}
