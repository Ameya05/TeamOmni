package org.team.omni.weather.mesos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.team.omni.weather.model.WeatherDetails;

public class MesosService implements Runnable {

	private List<EventOutput> evenOutputs = new ArrayList<>();
	private Thread mesosServiceThread;
	private static final Logger logger = Logger.getLogger(MesosService.class);

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
	private void writeStatus(String status) {
		outputEvent(status, "status");
	}

	/**
	 * Writing the result 
	 * @param weatherForecast
	 */
	private void writeResult(WeatherDetails weatherForecast) {
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

			// load test

			int[] a = new int[200000];
			long sum = 0;
			for (int i = 0; i < 200000; i++) {
				a[i] = i;
				sum += i;
				sum -= i;
				sum++;
				if (i % 100000 == 0) {
					writeStatus("Sum: " + sum);
				}

			}

			for (int i = 0; i < 200000; i++)
				a[i] = 0;

			// load test

			WeatherDetails forecast = new WeatherDetails();
			forecast.setWeatherType("Rainy");
			forecast.setTemperatureUnit("deg. F");
			forecast.setTemperatureValue(89.9);
			forecast.setWindSpeedUnit("mph");
			forecast.setWindSpeedVal(10);

			logger.info("Returning Response to Orchestration Engine");
			writeStatus("Result /incomn=ing");
			writeResult(forecast);
			writeStatus("Resf");
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
