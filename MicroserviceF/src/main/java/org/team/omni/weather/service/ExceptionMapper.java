package org.team.omni.weather.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
	private static long ERROR_ID = 100001;

	public ExceptionMapper() {
	}

	@Override
	public Response toResponse(Exception exception) {
		try {
			String errorID = "Weather-Forecast-" + getERROR_ID();
			ErrorMessage errorMessage = new ErrorMessage(errorID, exception.getMessage());
			return Response.serverError().entity(errorMessage).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}

	public static long getERROR_ID() {
		return ++ERROR_ID;
	}

}
