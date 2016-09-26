package org.team.omni.weather;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
	private static final Logger LOGGER = Logger.getLogger("Orchestration");

	public ExceptionMapper() {
	}

	@Override
	public Response toResponse(Exception exception) {
		LOGGER.log(Level.SEVERE, "Exception Thrown: " + exception.getMessage(), exception);
		return Response.serverError().entity(exception.getMessage()).build();
	}

}
