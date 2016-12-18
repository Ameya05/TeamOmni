package org.team.omni.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.client.WebTarget;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.team.omni.orchestration.engine.services.DataIngestionService;

public class DataIngestionServiceTest extends ServiceTest {

	private DataIngestionService dataIngestionService;
	private LocalDateTime timestamp = LocalDateTime.now();
	private String stationName = "houston";
	private String outputValue = "key";

	@Before
	public void setUp() throws Exception {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("/MM/dd/yyyy/HH/mm/ss");
		WebTarget mockTarget = mock(WebTarget.class);
		WebTarget mockActualTarget = mock(WebTarget.class);
		when(mockServiceAddress.path(stationName)).thenReturn(mockTarget);
		when(mockTarget.path(dateTimeFormatter.format(timestamp))).thenReturn(mockActualTarget);
		when(mockActualTarget.request()).thenReturn(mockBuilder);
		when(mockBuilder.get()).thenReturn(mockResponse);
		when(mockResponse.readEntity(String.class)).thenReturn(outputValue);
		dataIngestionService = new DataIngestionService(mockServiceAddress);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstructDataFileURL() {
		assertEquals(outputValue, dataIngestionService.constructDataFileURL(stationName, timestamp));
	}

}
