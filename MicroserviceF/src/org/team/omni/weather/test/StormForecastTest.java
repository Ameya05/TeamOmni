package org.team.omni.weather.test;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;
import org.team.omni.weather.service.StormForecastService;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

public class StormForecastTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(StormForecastService.class);
    }

    @Test
    public void StormForecastResponseStatusTest() {
        int status = target("/run").request().get().getStatus();
        assertEquals(200,status);
    }
    
    @Test
    public void StormForecastResponseFileTest() {
        	
    	byte[] file1Bytes;
		try {
			 Response output = target("/run").request().get();
			 InputStream in = (InputStream) output.getEntity();
			 file1Bytes=IOUtils.toByteArray(in);
	    	String file1 = new String(file1Bytes, StandardCharsets.UTF_8);
	    	
	    	assertNotNull(file1);
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}