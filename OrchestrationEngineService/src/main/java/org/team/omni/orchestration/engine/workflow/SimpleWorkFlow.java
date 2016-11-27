package org.team.omni.orchestration.engine.workflow;

import java.io.File;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger LOGGER = LogManager.getLogger(SimpleWorkFlow.class);
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

	public <T, U extends Service> T executeService(ServiceExecution<T, U> serviceExecution, Class<U> serviceClass) {
		String serviceName = serviceClass.getSimpleName();
		workFlowState.setCurrentService(serviceName);
		int tries = 0;
		do {
			tries++;
			LOGGER.info("Trying " + tries + " .............");
			try {
				U serviceInstance = serviceFactory.createService(serviceClass);
				return serviceExecution.execute(serviceInstance);
			} catch (Exception e) {
				if (tries >= 3) {
					workFlowState.setError(e);
					throw new ServiceExecutionException(e);
				} else {
					LOGGER.error("Unxpected issue encountered", e);
				}
			}
		} while (tries < 3);
		throw new ServiceExecutionException("The service " + serviceName + " execution could not be completed due to unknown reasons");
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
		return executeService((DataIngestionService dataIngestionService) -> {
			return dataIngestionService.constructDataFileURL(stationName, timeStamp);
		}, DataIngestionService.class);
	}

	public File handleStormDetectionService(String key) throws ServiceCreationException {
		return executeService((StormDetectionService stormDetectionService) -> {
			return stormDetectionService.generateKMLFile(key);
		}, StormDetectionService.class);
	}

	public File handleStormClusteringService(File kmlFile) throws ServiceCreationException {
		return executeService((StormClusteringService stormClusteringService) -> {
			return stormClusteringService.genrateStormClusteringFile(kmlFile);
		}, StormClusteringService.class);
	}

	public boolean handleForecastTriggerService(File clusteringFile) throws ServiceCreationException {
		return executeService((ForecastTriggerService forecastTriggerService) -> {
			return forecastTriggerService.triggerWeatherForecast(clusteringFile);
		}, ForecastTriggerService.class);
	}

	public WeatherDetails hanldeWeatherForecastExecutionService() throws ServiceCreationException {
		return executeService((WeatherForecastExecutionService weatherForecastExecutionService) -> {
			return weatherForecastExecutionService.runWeatherForecast();
		}, WeatherForecastExecutionService.class);
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
			LOGGER.info("==================================\nBeginning workflow for - " + workFlowState.getUserId());

			String key = handleDataIngestionService();
			LOGGER.info("DataIngestionService successfully executed. Key fetched: " + key);

			File kmlFile = handleStormDetectionService(key);
			LOGGER.info("StormDetectionService successfully executed.");

			File clusteringFile = handleStormClusteringService(kmlFile);
			LOGGER.info("StormClusteringService successfully executed.");

			boolean forecast = handleForecastTriggerService(clusteringFile);
			LOGGER.info("ForecastTriggerService returned Storm status: " + forecast);
			if (forecast) {
				LOGGER.info("Since storm is present, running Weather Forecast: " + forecast);
				weatherDetails = hanldeWeatherForecastExecutionService();
				workFlowState.setExecutionStatus(WorkFlowExecutionStatus.EXECUTION_COMPLETE);
			} else {
				LOGGER.info("Since no storm is present, skipping Weather Forecast: " + forecast);
				workFlowState.setExecutionStatus(WorkFlowExecutionStatus.EXECUTION_NOT_REQUIRED);
			}
		} catch (ServiceCreationException e) {
			workFlowState.setExecutionStatus(WorkFlowExecutionStatus.EXECUTION_FAILURE);
			LOGGER.error("Workflow Execution Failure", e);
		}

	}

}
