package org.team.omni.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataParam;

 
@Path("/")
public class ForecastTrigger {
	


	static { System.setProperty("my.log", System.getProperty("user.dir")
            + File.separator + "MicroElog.log"); }
	final static Logger logger = Logger.getLogger(ForecastTrigger.class);
	
	
	@POST
	@Path("/trigger")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response sendCluster(@FormDataParam("clustering") InputStream incomingData) 
	{
		logger.info("Entered MicroserviceE");
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception e) {
			logger.error("StreamReaderError ");
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + builder.toString());
		Random randomno = new Random();
		boolean trigger=false;
		if (randomno.nextInt(2)==1)
			trigger=true;
		logger.info("Returning to Orchestration Engine. Trigger Value: "+trigger);
		return Response.status(200).entity(String.valueOf(trigger)).build();
	}

}