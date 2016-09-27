package org.team.omni.weather;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

@Path("/")
/**
 * @author Ameya Advankar
 */
public class StormDetectionService {

	final String filename="/home/ubuntu/Sample.kml";
	
	/**
	 * This service does the following -<br>
	 * 	1. Takes a Key as input<br>
	 * 	2. Retrieves the data from NEXRAD and outputs a .kml file
	 * @param key String
	 * @return <b>.kml</b> file
	 * @throws IOException
	 */
	@GET
	@Path("/detection")
	public Response detectStorm(@QueryParam("key") String key) throws IOException {

		byte[] out=null;
		File kml = new File(filename);
		
		if(kml.exists())
		{
			InputStream inStream = new FileInputStream(kml);
			out= IOUtils.toByteArray(inStream);
			return Response
					.ok( out, MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition","attachment;filename=\"" + kml.getName() + "\"")
					.build();
		}
		else
		{
			return Response.status(503).entity("Error while fetching kml file").build();
		}
		
	}
}
