package org.team.omni.weather.test;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.*;
import org.team.omni.weather.ForecastTrigger;

import static org.junit.Assert.*;

import java.io.File;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ForecastTriggerTest extends JerseyTest {

    protected ResourceConfig configure() {

        final ResourceConfig resourceConfig = new ResourceConfig(ForecastTrigger.class);
        resourceConfig.register(MultiPartFeature.class);
        return resourceConfig;
    }

    @Override
    protected void configureClient(ClientConfig clientConfig) {
        clientConfig.register(MultiPartFeature.class);
    }

    @Test
    public void ForecastTriggerResponseStatusTest() {
    	
    	File kmlFile=new File("/home/ubuntu/Sample.kml");
    	MultiPart multiPartEntity = new FormDataMultiPart();
		multiPartEntity.bodyPart(new FileDataBodyPart("clustering", kmlFile));
        int status = target("/trigger").request().post(Entity.entity(multiPartEntity, MediaType.MULTIPART_FORM_DATA)).getStatus();
        
        assertEquals(200,status);
    }
    
    @Test
    public void ForecastTriggerResponseTest() {
	
	    	File kmlFile=new File("/home/ubuntu/Sample.kml");
	    	MultiPart multiPartEntity = new FormDataMultiPart();
			multiPartEntity.bodyPart(new FileDataBodyPart("clustering", kmlFile));
			Response output = target("/trigger").request().post(Entity.entity(multiPartEntity, MediaType.MULTIPART_FORM_DATA));
		
	    	assertNotNull(output.getEntity());
    }
}