package org.team.omni.orchestration.engine.services;

import javax.ws.rs.client.WebTarget;

import org.apache.curator.x.discovery.ServiceInstance;

import org.team.omni.OrchestrationEngineValueStore;
import org.team.omni.beans.MesosStatus;
import org.team.omni.beans.WeatherDetails;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.exceptions.ServiceExecutionException;
import org.team.omni.orchestration.engine.workflow.WorkFlowState;
import org.team.omni.weather.InstanceDetails;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public class WeatherForecastExecutionService extends Service {

	public WeatherForecastExecutionService(WebTarget serviceAddress, OrchestrationEngineValueStore orchestrationEngineValueStore, ServiceInstance<InstanceDetails> serviceInstance, WorkFlowState workFlowState) {
		super(serviceAddress.path("/forecast"), orchestrationEngineValueStore, serviceInstance, workFlowState);
	}

	public WeatherDetails runWeatherForecast() {
		String workFlowID = Integer.toString(workFlowState.getWorkFlowId());
		MesosStatus mesosStatus = serviceAddress.path("execute").path(workFlowID).request().get(MesosStatus.class);
		System.out.println(mesosStatus);
		int i = 0;
		WebTarget statusTarget = serviceAddress.path("status").path(workFlowID);
		while (true) {
			mesosStatus = statusTarget.request().get(MesosStatus.class);
			switch (mesosStatus.getMesosStatusType()) {
			case EXECUTING:
				if (!mesosStatus.hasNoStatus()) {
					workFlowState.log("Weather Forecast Ececution: " + mesosStatus.getStatus());
				}
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					throw new OrchestrationEngineException(e);
				}
				break;
			case EXECUTION_FAILURE:
				throw new ServiceExecutionException("WeatherForecast execution failure.\nError Message: " + mesosStatus.getErrorMessage());
			case EXECUTION_SUCCESS:
				return mesosStatus.getWeatherDetails();
			}
			if (i > 150) {
				break;
			}
			i++;
		}
		throw new OrchestrationEngineException("Weather Forecast Execution has timed out");
	}
}