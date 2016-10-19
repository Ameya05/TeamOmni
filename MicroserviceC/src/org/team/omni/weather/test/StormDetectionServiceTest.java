package org.team.omni.weather.test;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;
import org.team.omni.weather.StormDetectionService;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

public class StormDetectionServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(StormDetectionService.class);
    }

    @Test
    public void StormDetectionResponseStatusTest() {
        int status = target("/detection").request().get().getStatus();
        assertEquals(200,status);
    }
    
    @Test
    public void StormDetectionResponseFileTest() {
        	
    	byte[] file1Bytes,file2Bytes;
		try {
			 Response output = target("/detection").request().get();
			 InputStream in = (InputStream) output.getEntity();
			 file1Bytes=IOUtils.toByteArray(in);
			file2Bytes = Files.readAllBytes(Paths.get("/home/ubuntu/Sample.kml"));
			String file1 = new String(file1Bytes, StandardCharsets.UTF_8);
	    	String file2 = new String(file2Bytes, StandardCharsets.UTF_8);
	 
	    	assertEquals("The content in the strings should match", file1, file2);
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}