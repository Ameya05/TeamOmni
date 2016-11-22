
package org.team.omni.weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataParam;
 
@Path("/")
public class StormClustering {
	
	final static Logger logger = Logger.getLogger(StormClustering.class);
	
	@POST
	@Path("/clustering")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response sendCluster(@FormDataParam("kml") InputStream incomingData) {
		StringBuilder builder = new StringBuilder();
		
		logger.info("Entered Microservice D");
		
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
		logger.info("returning response to Ochestration Engine");
		return Response.status(200).entity(builder.toString()).build();
	}
}