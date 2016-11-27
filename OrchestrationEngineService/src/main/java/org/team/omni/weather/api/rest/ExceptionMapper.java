package org.team.omni.weather.api.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
	private static final Logger LOGGER = LogManager.getLogger(ExceptionMapper.class);
	private static long ERROR_ID = 100001;

	public ExceptionMapper() {
	}

	@Override
	public Response toResponse(Exception exception) {
		String errorID = "OE" + getERROR_ID();
		LOGGER.error( "Error ID: " + errorID + "Exception Thrown: " + exception.getMessage(), exception);
		return Response.serverError().entity("Error ID: " + errorID).build();
	}

	public static long getERROR_ID() {
		return ++ERROR_ID;
	}

}
