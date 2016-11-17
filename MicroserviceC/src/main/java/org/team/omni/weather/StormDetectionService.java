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

import org.apache.log4j.Logger;

/**
 * @author Ameya Advankar
 */
@Path("/")
public class StormDetectionService {

	final String filename = "Sample.kml";

	static {
		System.setProperty("my.log", System.getProperty("user.dir") + File.separator + "MicroClogs.log");
	}
	final static Logger logger = Logger.getLogger(StormDetectionService.class);

	/**
	 * This service does the following -<br>
	 * 1. Takes a Key as input<br>
	 * 2. Retrieves the data from NEXRAD and outputs a .kml file
	 * 
	 * @param key
	 *            String
	 * @return <b>.kml</b> file
	 * @throws IOException
	 */
	@GET
	@Path("/detection")
	public Response detectStorm(@QueryParam("key") String key) throws IOException {

		byte[] out = null;
		File kml = new File(filename);
		logger.info("Entered Microservice C : key- " + key);

		// load test

		int[] a = new int[200000];
		long sum = 0L;
		for (int i = 0; i < 200000; i++) {
			a[i] = i;
			sum += i;
			sum -= i;
			System.out.println(sum);
		}

		for (int i = 0; i < 200000; i++)
			a[i] = 0;

		// load test

		if (kml.exists()) {

			InputStream inStream = new FileInputStream(kml);
			out = IOUtils.toByteArray(inStream);
			logger.info("KML file exists : return response to Ochestration Engine ");
			return Response.ok(out, MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition", "attachment;filename=\"" + kml.getName() + "\"").build();
		} else {
			logger.error("KML file does not exist :  File exists? " + kml.exists());
			return Response.status(503).entity("Error while fetching kml file").build();
		}

	}
}
