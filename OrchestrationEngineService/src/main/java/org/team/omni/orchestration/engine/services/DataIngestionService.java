package org.team.omni.orchestration.engine.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.client.WebTarget;

import org.team.omni.OrchestrationEngineValueStore;

public class DataIngestionService extends Service {

	public DataIngestionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore) {
		super(serviceAddress.path("/nexrad/generate/url/"), orchestrationEngineValueStore);
	}

	public String constructDataFileURL(String stationName, LocalDateTime timestamp) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("/MM/dd/yyyy/HH/mm/ss");
		return serviceAddress.path(stationName).path(dateTimeFormatter.format(timestamp)).request().get().readEntity(String.class);
	}

}
