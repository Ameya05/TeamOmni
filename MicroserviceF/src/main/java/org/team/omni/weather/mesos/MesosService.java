package org.team.omni.weather.mesos;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.team.omni.weather.aurora.OmniAuroraClient;
import org.team.omni.weather.aurora.client.AuroraSchedulerClientFactory;
import org.team.omni.weather.aurora.client.sdk.ReadOnlyScheduler;
import org.team.omni.weather.aurora.utils.Constants;
import org.team.omni.weather.model.WeatherDetails;

public class MesosService implements Runnable {
	
	private List<EventOutput> evenOutputs = new ArrayList<>();
	private Thread mesosServiceThread;
	private static final Logger logger = Logger.getLogger(MesosService.class);
	private static Properties properties = new Properties();
	private static ReadOnlyScheduler.Client omniAuroraClient;

	private String requestID;
	
	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public MesosService() {
		mesosServiceThread = new Thread(this);
	}

	public void addEventOutput(EventOutput eventOutput) {
		this.evenOutputs.add(eventOutput);
	}

	/**
	 * Use this function to send constant statuses to the client
	 * 
	 * @param status
	 */
	private void outputEvent(Object event, String comment) {
		OutboundEvent.Builder outBoundEventBuilder = new OutboundEvent.Builder();
		outBoundEventBuilder.name("storm-forecast");
		outBoundEventBuilder.mediaType(MediaType.APPLICATION_JSON_TYPE);
		outBoundEventBuilder.comment(comment);
		OutboundEvent outboundEvent = outBoundEventBuilder.data(event).build();
		evenOutputs.parallelStream().forEach((EventOutput evenOutput) -> writeOutBoundEvent(evenOutput, outboundEvent));
	}

	/**
	 * Writing the status
	 * @param status
	 */
	public void writeStatus(String status) {
		outputEvent(status, "status");
	}

	/**
	 * Writing the result 
	 * @param weatherForecast
	 */
	public void writeResult(WeatherDetails weatherForecast) {
		outputEvent(weatherForecast, "result");
	}

	public void execute() {
		mesosServiceThread.start();
	}

	@Override
	public void run() {
		try {
			// TODO the mesos execution code here

			logger.info("Entered Microservice F");
			properties.load(MesosService.class.getClassLoader().getResourceAsStream(Constants.AURORA_SCHEDULER_PROP_FILE));
			String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
			String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
			omniAuroraClient = AuroraSchedulerClientFactory.createReadOnlySchedulerClient(MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort));
			
			OmniAuroraClient omniAuroraClient = new OmniAuroraClient(this);
			
			logger.info("Done with creating Aurora Client");
			String imageURL = omniAuroraClient.createJob(requestID);
			logger.info("Done with creating Aurora Job");
			
			WeatherDetails forecast = new WeatherDetails();
			forecast.setWeatherType("Rainy");
			forecast.setTemperatureUnit("deg. F");
			forecast.setTemperatureValue(89.9);
			forecast.setWindSpeedUnit("mph");
			forecast.setWindSpeedVal(10);
			forecast.setImageURL(imageURL);

			logger.info("Returning Response to Orchestration Engine");
			writeStatus("Result incomning");
			writeResult(forecast);
			writeStatus("Resf");
		} catch (IOException e) {
			logger.error("IOException while getting Aurora Scheduler property file",e);
			writeStatus("Exception while reading "+Constants.AURORA_SCHEDULER_PROP_FILE+" file");
		}
		catch (Exception e) {
			logger.error("Exception encountered while running MesosService",e);
			writeStatus("Exception encountered while running MesosService");
			writeResult(null);
		} finally {
			evenOutputs.forEach((EventOutput evenOutput) -> closeEventOutput(evenOutput));
		}

	}

	private void writeOutBoundEvent(EventOutput eventOutput, OutboundEvent outboundEvent) {
		try {
			eventOutput.write(outboundEvent);
		} catch (IOException e) {
			throw new StormForecastException(e);
		}
	}

	private void closeEventOutput(EventOutput eventOutput) {
		try {
			eventOutput.close();
		} catch (IOException e) {
			throw new StormForecastException(e);
		}
	}

}
