package org.team.omni.orchestration.engine.services;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.beans.WeatherDetails;

public class WeatherForecastExecutionService extends Service {

	public WeatherForecastExecutionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore) {
		super(serviceAddress.path("/forecast/run"), orchestrationEngineValueStore);
	}

	public WeatherDetails runWeatherForecast() {
		return serviceAddress.request(MediaType.APPLICATION_JSON).get().readEntity(WeatherDetails.class);
	}

}
