package org.team.omni.orchestration.engine.services;

import javax.ws.rs.client.WebTarget;

import org.apache.curator.x.discovery.ServiceInstance;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.beans.WeatherDetails;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;
import org.team.omni.weather.InstanceDetails;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public class WeatherForecastExecutionService extends Service {

	public WeatherForecastExecutionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore, ServiceInstance<InstanceDetails> serviceInstance, WorkFlowState workFlowState) {
		super(serviceAddress.path("/forecast/execute"), orchestrationEngineValueStore, serviceInstance, workFlowState);
	}

	public WeatherDetails runWeatherForecast() {
		EventInput eventInput = this.serviceAddress.path(Long.toString(workFlowState.getWorkFlowId())).request().get(EventInput.class);
		try {
			while (!eventInput.isClosed()) {
				InboundEvent event = eventInput.read();
				if (event != null) {
					switch (event.getComment()) {
					case "status":
						String status = this.getClass().getSimpleName() + " - " + event.readData();
						workFlowState.log(status);
						LOGGER.info("Status:  " + status);
						break;
					case "result":
						WeatherDetails weatherForecast = event.readData(WeatherDetails.class);
						LOGGER.info("Result recieved");
						return weatherForecast;
					default:
						throw new OrchestrationEngineException("Unknown type");
					}
				}
			}
		} finally {
			eventInput.close();
		}
		throw new OrchestrationEngineException("No Result was obtained");
	}
}