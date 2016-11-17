package org.team.omni.orchestration.engine.workflow;

import java.io.File;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.team.omni.beans.WeatherDetails;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.exceptions.ServiceCreationException;
import org.team.omni.exceptions.ServiceExecutionException;
import org.team.omni.orchestration.engine.services.DataIngestionService;
import org.team.omni.orchestration.engine.services.ForecastTriggerService;
import org.team.omni.orchestration.engine.services.Service;
import org.team.omni.orchestration.engine.services.ServiceFactory;
import org.team.omni.orchestration.engine.services.StormClusteringService;
import org.team.omni.orchestration.engine.services.StormDetectionService;
import org.team.omni.orchestration.engine.services.WeatherForecastExecutionService;

public class SimpleWorkFlow implements OrchestrationEngineWorkFlow<WeatherDetails>, Runnable {

	private static final Logger LOGGER = Logger.getLogger("Orchestration");
	private ServiceFactory serviceFactory;
	private String stationName = "";
	private LocalDateTime timeStamp;
	private WeatherDetails weatherDetails = null;
	private WorkFlowState workFlowState;
	private Thread workFlowExecutionThread = null;

	public SimpleWorkFlow(ServiceFactory serviceFactory, String stationName, LocalDateTime timeStamp, WorkFlowState workFlowState) {
		this.serviceFactory = serviceFactory;
		this.stationName = stationName;
		this.timeStamp = timeStamp;
		this.workFlowState = workFlowState;
	}

	public <T> T executeService(ServiceExecution<T> serviceExecution, Service service) {
		workFlowState.setCurrentService(service.getClass().getSimpleName());
		try {
			return serviceExecution.execute();
		} catch (Exception e) {
			workFlowState.setError(e);
			throw new ServiceExecutionException(e);
		} finally {
			try {
				ServiceFactory.getServiceFactory().destroyService(service);
			} catch (ServiceCreationException e) {
				throw new OrchestrationEngineException("Could not destroy the service: " + workFlowState.getCurrentService(), e);
			}
		}
	}

	@Override
	public void executeWorkFlow() {
		if (workFlowExecutionThread == null || !workFlowExecutionThread.isAlive()) {
			workFlowExecutionThread = new Thread(this);
			workFlowExecutionThread.start();
		} else {
			throw new OrchestrationEngineException("Cannot execute another workflow when Workflow execution is in progress");
		}
	}

	public String handleDataIngestionService() throws ServiceCreationException {
		DataIngestionService dataIngestionService = serviceFactory.createService(DataIngestionService.class);
		return executeService(() -> {
			return dataIngestionService.constructDataFileURL(stationName, timeStamp);
		}, dataIngestionService);
	}

	public File handleStormDetectionService(String key) throws ServiceCreationException {
		StormDetectionService stormDetectionService = serviceFactory.createService(StormDetectionService.class);
		return executeService(() -> {
			return stormDetectionService.generateKMLFile(key);
		}, stormDetectionService);
	}

	public File handleStormClusteringService(File kmlFile) throws ServiceCreationException {
		StormClusteringService stormClusteringService = serviceFactory.createService(StormClusteringService.class);
		return executeService(() -> {
			return stormClusteringService.genrateStormClusteringFile(kmlFile);
		}, stormClusteringService);
	}

	public boolean handleForecastTriggerService(File clusteringFile) throws ServiceCreationException {
		ForecastTriggerService forecastTriggerService = serviceFactory.createService(ForecastTriggerService.class);
		return executeService(() -> {
			return forecastTriggerService.triggerWeatherForecast(clusteringFile);
		}, forecastTriggerService);
	}

	public WeatherDetails hanldeWeatherForecastExecutionService() throws ServiceCreationException {
		WeatherForecastExecutionService weatherForecastExecutionService = serviceFactory.createService(WeatherForecastExecutionService.class);
		return executeService(() -> {
			return weatherForecastExecutionService.runWeatherForecast();
		}, weatherForecastExecutionService);
	}

	@Override
	public synchronized WorkFlowState getWorkFlowState() {
		return workFlowState;
	}

	@Override
	public synchronized WeatherDetails fetchResult() {
		return weatherDetails;
	}

	@Override
	public void run() {
		try {
			LOGGER.log(Level.INFO, "==================================\nBeginning workflow for - " + workFlowState.getUserId());

			String key = handleDataIngestionService();
			LOGGER.log(Level.INFO, "DataIngestionService successfully executed. Key fetched: " + key);

			File kmlFile = handleStormDetectionService(key);
			LOGGER.log(Level.INFO, "StormDetectionService successfully executed.");

			File clusteringFile = handleStormClusteringService(kmlFile);
			LOGGER.log(Level.INFO, "StormClusteringService successfully executed.");

			boolean forecast = handleForecastTriggerService(clusteringFile);
			LOGGER.log(Level.INFO, "ForecastTriggerService returned Storm status: " + forecast);
			if (forecast) {
				LOGGER.log(Level.INFO, "Since storm is present, running Weather Forecast: " + forecast);
				weatherDetails = hanldeWeatherForecastExecutionService();
				workFlowState.setExecutionStatus(WorkFlowExecutionStatus.EXECUTION_COMPLETE);
			} else {
				LOGGER.log(Level.INFO, "Since no storm is present, skipping Weather Forecast: " + forecast);
				workFlowState.setExecutionStatus(WorkFlowExecutionStatus.EXECUTION_NOT_REQUIRED);
			}
		} catch (ServiceCreationException e) {
			workFlowState.setExecutionStatus(WorkFlowExecutionStatus.EXECUTION_FAILURE);
			LOGGER.log(Level.SEVERE, "Service Failure", e);
		}

	}

}
