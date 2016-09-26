package test.team.omni.weather;

import static org.junit.Assert.*;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.team.omni.weather.StormClustering;

public class StormClusteringTest extends JerseyTest {



	@Override
    protected Application configure() {
        return new ResourceConfig(StormClustering.class);
    }
	
	
	@Test
	public void testSendCluster() {
	     final String hello = target("hello").request().get(String.class);
	        assertEquals("Hello World!", hello);
	}

}
