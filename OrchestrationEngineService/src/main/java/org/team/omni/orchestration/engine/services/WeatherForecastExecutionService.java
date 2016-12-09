package org.team.omni.orchestration.engine.services;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.curator.x.discovery.ServiceInstance;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.beans.WeatherDetails;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;
import org.team.omni.weather.InstanceDetails;

public class WeatherForecastExecutionService extends Service {

	public WeatherForecastExecutionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore, ServiceInstance<InstanceDetails> serviceInstance, WorkFlowState workFlowState) {
		super(serviceAddress.path("/forecast/run"), orchestrationEngineValueStore, serviceInstance, workFlowState);
	}

	public WeatherDetails runWeatherForecast() {
		return serviceAddress.request(MediaType.APPLICATION_JSON).get().readEntity(WeatherDetails.class);
	}

}
