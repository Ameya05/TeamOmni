package org.team.omni.orchestration.engine.workflow;

import java.io.File;
import java.time.LocalDateTime;

import org.team.omni.beans.WeatherDetails;
import org.team.omni.exceptions.OrchestrationEngineException;
import org.team.omni.exceptions.ServiceExecutionException;
import org.team.omni.orchestration.engine.services.DataIngestionService;
import org.team.omni.orchestration.engine.services.ForecastTriggerService;
import org.team.omni.orchestration.engine.services.ServiceFactory;
import org.team.omni.orchestration.engine.services.StormClusteringService;
import org.team.omni.orchestration.engine.services.StormDetectionService;
import org.team.omni.orchestration.engine.services.WeatherForecastExecutionService;

public class SimpleWorkFlow implements OrchestrationEngineWorkFlow<WeatherDetails>, Runnable {
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

	public <T> T executeService(ServiceExecution<T> serviceExecution, String serviceName) {
		workFlowState.setCurrentService(serviceName);
		try {
			return serviceExecution.execute();
		} catch (Exception e) {
			workFlowState.setError(e);
			throw new ServiceExecutionException(e);
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

	public String handleDataIngestionService() {
		return executeService(() -> {
			DataIngestionService dataIngestionService = serviceFactory.createService(DataIngestionService.class);
			return dataIngestionService.constructDataFileURL(stationName, timeStamp);
		}, DataIngestionService.class.getName());
	}

	public File handleStormDetectionService(String key) {
		return executeService(() -> {
			StormDetectionService stormDetectionService = serviceFactory.createService(StormDetectionService.class);
			return stormDetectionService.generateKMLFile(key);
		}, StormDetectionService.class.getName());
	}

	public File handleStormClusteringService(File kmlFile) {
		return executeService(() -> {
			StormClusteringService stormClusteringService = serviceFactory.createService(StormClusteringService.class);
			return stormClusteringService.genrateStormClusteringFile(kmlFile);
		}, StormClusteringService.class.getName());
	}

	public boolean handleForecastTriggerService(File clusteringFile) {
		return executeService(() -> {
			ForecastTriggerService forecastTriggerService = serviceFactory.createService(ForecastTriggerService.class);
			return forecastTriggerService.triggerWeatherForecast(clusteringFile);
		}, ForecastTriggerService.class.getName());
	}

	public WeatherDetails hanldeWeatherForecastExecutionService() {
		return executeService(() -> {
			WeatherForecastExecutionService weatherForecastExecutionService = serviceFactory.createService(WeatherForecastExecutionService.class);
			return weatherForecastExecutionService.runWeatherForecast();
		}, WeatherForecastExecutionService.class.getName());
	}

	@Override
	public WorkFlowState getWorkFlowState() {
		return workFlowState;
	}

	@Override
	public WeatherDetails fetchResult() {
		return weatherDetails;
	}

	@Override
	public void run() {
		String key = handleDataIngestionService();
		File kmlFile = handleStormDetectionService(key);
		File clusteringFile = handleStormClusteringService(kmlFile);
		boolean forecast = handleForecastTriggerService(clusteringFile);
		if (forecast) {
			weatherDetails = hanldeWeatherForecastExecutionService();
		}
		workFlowState.setExecutionStatus(WorkFlowExecutionStatus.EXECUTION_COMPLETE);
	}

}
