package org.team.omni.weather;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
	private static final Logger LOGGER = Logger.getLogger("Orchestration");
	private static long ERROR_ID = 100001;

	public ExceptionMapper() {
	}

	@Override
	public Response toResponse(Exception exception) {
		String errorID = "OE" + getERROR_ID();
		LOGGER.log(Level.SEVERE, "Error ID: " + errorID + "Exception Thrown: " + exception.getMessage(), exception);
		return Response.serverError().entity("Error ID: " + errorID).build();
	}

	public static long getERROR_ID() {
		return ++ERROR_ID;
	}

}
