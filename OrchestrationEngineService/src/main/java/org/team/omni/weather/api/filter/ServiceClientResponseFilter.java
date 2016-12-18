package org.team.omni.weather.api.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import org.apache.logging.log4j.core.util.IOUtils;
import org.team.omni.exceptions.ServiceCallException;

public class ServiceClientResponseFilter implements ClientResponseFilter {
	private static boolean ENABLE = false;

	public ServiceClientResponseFilter() {
	}

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		if (responseContext.getStatus() != 200 && ENABLE) {
			String responseData = "";
			if (responseContext.hasEntity()) {
				InputStream is = responseContext.getEntityStream();
				StringWriter stringWriter = new StringWriter();
				IOUtils.copy(new InputStreamReader(is), stringWriter);
				responseData = stringWriter.toString();
			}
			throw new ServiceCallException("Service call to the url failed", responseData, responseContext.getStatus(), requestContext.getUri());
		}

	}

}
