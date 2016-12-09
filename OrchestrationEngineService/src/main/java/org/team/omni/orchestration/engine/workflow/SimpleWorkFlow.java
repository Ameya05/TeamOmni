package org.team.omni.orchestration.engine.workflow;

import static org.jooq.impl.DSL.*;

import java.io.File;

import java.time.LocalDateTime;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.impl.SQLDataType;

import org.team.omni.WeatherDetailsConverter;
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

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public class SimpleWorkFlow implements OrchestrationEngineWorkFlow<WeatherDetails>, Runnable {

	private static final Logger LOGGER = Logger.getLogger("Orchestration");

	private final Field<WeatherDetails> resultField = field("result", SQLDataType.VARCHAR.asConvertedDataType(new WeatherDetailsConverter(new ObjectMapper())));
	private ServiceFactory serviceFactory;
	private String stationName = "";
	private LocalDateTime inputTimeStamp;
	private LocalDateTime executionTimeStamp;
	private WeatherDetails weatherDetails = null;
	private WorkFlowState workFlowState;
	private int id;
	private Thread workFlowExecutionThread = null;
	private DSLContext create;

	public SimpleWorkFlow(int id, ServiceFactory serviceFactory, String stationName, LocalDateTime inputTimeStamp, WorkFlowState workFlowState, DSLContext create, LocalDateTime executionTimeStamp) {
		this.serviceFactory = serviceFactory;
		this.setStationName(stationName);
		this.setInputTimeStamp(inputTimeStamp);
		this.setWorkFlowState(workFlowState);
		this.setId(id);
		this.create = create;
		this.setExecutionTimeStamp(executionTimeStamp);
	}

	public <T, U extends Service> T executeService(ServiceExecution<T, U> serviceExecution, Class<U> serviceClass) {
		String serviceName = serviceClass.getSimpleName();
		getWorkFlowState().processCurrentService(serviceName);
		int tries = 0;
		do {
			tries++;
			LOGGER.info("Trying " + tries + " .............");
			try {
				U serviceInstance = serviceFactory.createService(serviceClass, getWorkFlowState());
				return serviceExecution.execute(serviceInstance);
			} catch (Exception e) {
				if (tries >= 3) {
					getWorkFlowState().processError(e);
					throw new ServiceExecutionException(e);
				} else {
					LOGGER.log(Level.SEVERE, "Unexpected error", e);
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
			return dataIngestionService.constructDataFileURL(getStationName(), getInputTimeStamp());
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
	public synchronized WeatherDetails fetchResult() {
		if (getWeatherDetails() != null) {
			return getWeatherDetails();
		} else {
			Record1<WeatherDetails> res = create.select(resultField).from(table("work_flow_details")).where(field("id").equal(getId())).fetchOne();
			this.processWeatherDetails(res.value1());
			return getWeatherDetails();
		}
	}

	private void processWeatherDetails(WeatherDetails weatherDetails) {
		this.setWeatherDetails(weatherDetails);
		int updateCount = create.update(table("work_flow_details")).set(resultField, weatherDetails).where(field("id").equal(getId())).execute();
		if (updateCount == 0) {
			throw new OrchestrationEngineException("Work flow result saving failed");
		}
	}

	private void log(String log, Level level) {
		LOGGER.log(level, log);
		getWorkFlowState().log(log);
	}

	private void info(String log) {
		log(log, Level.INFO);
	}

	@Override
	public void run() {
		try {
			info("==================================\nBeginning workflow for - " + getWorkFlowState().getUserId());
			getWorkFlowState().processExecutionStatus(WorkFlowExecutionStatus.EXECUTING);
			String key = handleDataIngestionService();
			info("DataIngestionService successfully executed. Key fetched: " + key);

			File kmlFile = handleStormDetectionService(key);
			info("StormDetectionService successfully executed.");

			File clusteringFile = handleStormClusteringService(kmlFile);
			info("StormClusteringService successfully executed.");

			boolean forecast = handleForecastTriggerService(clusteringFile);
			info("ForecastTriggerService returned Storm status: " + forecast);
			if (forecast) {
				info("Since storm is present, running Weather Forecast: " + forecast);
				processWeatherDetails(hanldeWeatherForecastExecutionService());
				getWorkFlowState().processExecutionStatus(WorkFlowExecutionStatus.EXECUTION_COMPLETE);
			} else {
				info("Since no storm is present, skipping Weather Forecast: " + forecast);
				getWorkFlowState().processExecutionStatus(WorkFlowExecutionStatus.EXECUTION_NOT_REQUIRED);
			}
		} catch (ServiceCreationException e) {
			getWorkFlowState().processError(e);
			LOGGER.log(Level.SEVERE, "Workflow Execution Failure", e);
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getExecutionTimeStamp() {
		return executionTimeStamp;
	}

	public void setExecutionTimeStamp(LocalDateTime executionTimeStamp) {
		this.executionTimeStamp = executionTimeStamp;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public LocalDateTime getInputTimeStamp() {
		return inputTimeStamp;
	}

	public void setInputTimeStamp(LocalDateTime inputTimeStamp) {
		this.inputTimeStamp = inputTimeStamp;
	}

	public WeatherDetails getWeatherDetails() {
		return weatherDetails;
	}

	public void setWeatherDetails(WeatherDetails weatherDetails) {
		this.weatherDetails = weatherDetails;
	}

	@Override
	public synchronized WorkFlowState getWorkFlowState() {
		return workFlowState;
	}

	public void setWorkFlowState(WorkFlowState workFlowState) {
		this.workFlowState = workFlowState;
	}

}
