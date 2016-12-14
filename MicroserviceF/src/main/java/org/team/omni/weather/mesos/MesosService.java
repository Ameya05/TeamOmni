package org.team.omni.weather.mesos;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.team.omni.weather.aurora.OmniAuroraClient;
import org.team.omni.weather.aurora.client.AuroraSchedulerClientFactory;
import org.team.omni.weather.aurora.client.sdk.ReadOnlyScheduler;
import org.team.omni.weather.aurora.utils.Constants;
import org.team.omni.weather.model.WeatherDetails;

/**
 * 
 * @author Eldho Mathulla, Ameya Advankar
 *
 */
public class MesosService implements Runnable {

	private static Map<String, MesosService> mesosServiceMap = new ConcurrentHashMap<>();

	private Thread mesosServiceThread;
	private static final Logger logger = Logger.getLogger(MesosService.class);
	private Properties properties = new Properties();
	private ReadOnlyScheduler.Client omniAuroraClient;
	private Queue<MesosStatus> mesosStatus = new LinkedList<>();

	private String requestID;
	private WeatherDetails forecastResult;

	public MesosService(String requestID) {
		this();
		this.requestID = requestID;
	}

	public MesosService() {
		mesosServiceThread = new Thread(this);
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public void execute() {
		if (!mesosServiceThread.isAlive()) {
			mesosServiceThread.start();
		} else {
			throw new StormForecastException("Execution failure");
		}
	}

	@Override
	public void run() {
		try {
			logger.info("Entered Microservice F");
			info("Execution Started");
			properties.load(MesosService.class.getClassLoader().getResourceAsStream(Constants.AURORA_SCHEDULER_PROP_FILE));
			String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
			String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
			omniAuroraClient = AuroraSchedulerClientFactory.createReadOnlySchedulerClient(MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort));
			OmniAuroraClient omniAuroraClient = new OmniAuroraClient(this);
			logger.info("Done with creating Aurora Client");
			String imageURL = omniAuroraClient.createJob(requestID);
			logger.info("Done with creating Aurora Job");
			setForecastResult(new WeatherDetails());
			getForecastResult().setWeatherType("Rainy");
			getForecastResult().setTemperatureUnit("deg. F");
			getForecastResult().setTemperatureValue(89.9);
			getForecastResult().setWindSpeedUnit("mph");
			getForecastResult().setWindSpeedVal(10);
			forecastResult.setImageURL(imageURL);
			complete("Execution Completed", forecastResult);
			logger.info("Returning Response to Orchestration Engine");
		} catch (IOException e) {
			error("IOException while getting Aurora Scheduler property file", e);
		} catch (Exception e) {
			error("Exception encountered while running MesosService", e);
		} finally {
		}

	}

	public void info(String info) {
		mesosStatus.add(new MesosStatus(MesosStatusType.EXECUTING, info));
	}

	public void error(String message, Exception e) {
		mesosStatus.add(new MesosStatus(MesosStatusType.EXECUTION_FAILURE, message, e));
		logger.error(message);
	}

	public void complete(String message, WeatherDetails weatherDetails) {
		mesosStatus.add(new MesosStatus(MesosStatusType.EXECUTION_SUCCESS, message, weatherDetails));
	}

	public MesosStatus consume() {
		if (!mesosStatus.isEmpty()) {
			return mesosStatus.remove();
		} else {
			return new MesosStatus();
		}
	}

	public WeatherDetails getForecastResult() {
		return forecastResult;
	}

	public void setForecastResult(WeatherDetails forecastResult) {
		this.forecastResult = forecastResult;
	}

	public static MesosService createMesosService(String requestId) {
		if (mesosServiceMap.containsKey(requestId)) {
			return mesosServiceMap.get(requestId);
		} else {
			MesosService mesosService = new MesosService(requestId);
			mesosServiceMap.put(requestId, mesosService);
			return mesosService;
		}
	}

}
