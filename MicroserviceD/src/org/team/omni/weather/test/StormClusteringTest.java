package org.team.omni.weather.test;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;
import org.team.omni.weather.StormClustering;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class StormClusteringTest extends JerseyTest {

/*    @Override
    protected Application configure() {
        return new ResourceConfig(StormClustering.class);
    }
    */
    
    protected ResourceConfig configure() {

        final ResourceConfig resourceConfig = new ResourceConfig(StormClustering.class);
        resourceConfig.register(MultiPartFeature.class);
        return resourceConfig;
    }

    @Override
    protected void configureClient(ClientConfig clientConfig) {
        clientConfig.register(MultiPartFeature.class);
    }

    @Test
    public void StormClusteringResponseStatusTest() {
    	File kmlFile=new File("/home/ubuntu/Sample.kml");
    	MultiPart multiPartEntity = new FormDataMultiPart();
		multiPartEntity.bodyPart(new FileDataBodyPart("kml", kmlFile));
        int status = target("/clustering").request().post(Entity.entity(multiPartEntity, MediaType.MULTIPART_FORM_DATA)).getStatus();
        assertEquals(200,status);
    }
    
    @Test
    public void StormClusteringResponseFileTest() {
        	
    	byte[] file1Bytes;
		try {
	    	File kmlFile=new File("/home/ubuntu/Sample.kml");
	    	MultiPart multiPartEntity = new FormDataMultiPart();
			multiPartEntity.bodyPart(new FileDataBodyPart("kml", kmlFile));
			 Response output = target("/clustering").request().post(Entity.entity(multiPartEntity, MediaType.MULTIPART_FORM_DATA));
			 InputStream in = (InputStream) output.getEntity();
			 file1Bytes=IOUtils.toByteArray(in);
			String file1 = new String(file1Bytes, StandardCharsets.UTF_8);
			
	    	assertNotNull(file1);
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}