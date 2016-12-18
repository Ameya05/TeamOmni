package org.team.omni.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import javax.ws.rs.client.Entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.team.omni.orchestration.engine.services.ForecastTriggerService;

public class ForecastTriggerServiceTest extends ServiceTest {
	private ForecastTriggerService forecastTriggerService;
	private File clusteringFile = new File("temp");
	private boolean outputValue = false;

	public ForecastTriggerServiceTest() {
		super();
	}

	@Before
	public void setUp() throws Exception {
		when(mockServiceAddress.request()).thenReturn(mockBuilder);
		when(mockBuilder.post(any(Entity.class))).thenReturn(mockResponse);
		when(mockResponse.readEntity(Boolean.class)).thenReturn(outputValue);
		forecastTriggerService = new ForecastTriggerService(mockServiceAddress);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTriggerWeatherForecast() {
		assertEquals(outputValue, forecastTriggerService.triggerWeatherForecast(clusteringFile));
	}

}
