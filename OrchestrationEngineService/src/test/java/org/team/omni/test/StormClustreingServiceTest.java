package org.team.omni.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.team.omni.OrchestrationEngineUtils;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.orchestration.engine.services.StormClusteringService;

public class StormClustreingServiceTest extends ServiceTest {
	private StormClusteringService stormClusteringService;
	private File output = new File("");

	public StormClustreingServiceTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		stormClusteringService = new StormClusteringService(mockServiceAddress);
		when(mockServiceAddress.request()).thenReturn(mockBuilder);
		when(mockBuilder.post(any(Entity.class))).thenReturn(mockResponse);
		OrchestrationEngineValueStore.getOrchestrationEngineValueStore().setServiceFolder("F");
		OrchestrationEngineUtils orchestrationEngineUtils = mock(OrchestrationEngineUtils.class);
		when(orchestrationEngineUtils.saveFileFromResponse(any(Response.class), any(String.class))).thenReturn(output);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenrateStormClusteringFile() throws IOException, ParseException {
		assertEquals(output, stormClusteringService.genrateStormClusteringFile(new File("kml")));
	}

}
