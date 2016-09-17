package com.team.omni.weather;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

@Path("/")
public class StormDetectionService {

	/*
	 * dev test
	 */
	final String filename="/Sample.kml";
	
	@GET
	@Path("/detection/{key}")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response detectStorm(@PathParam("key") String key) throws IOException {

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
