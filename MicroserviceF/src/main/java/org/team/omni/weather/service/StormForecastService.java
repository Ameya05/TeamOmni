package org.team.omni.weather.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;
import org.team.omni.weather.mesos.MesosService;
import org.team.omni.weather.model.WeatherDetails;

@Path("/")
public class StormForecastService {
	/**
	 * This service runs the weather forecast when invoked
	 * 
	 * @return json
	 * @throws IOException
	 */
	final static Logger logger = Logger.getLogger(StormForecastService.class);

	@GET
	@Path("/run")
	@Produces(MediaType.APPLICATION_JSON)
	public WeatherDetails detectStorm() throws IOException {
		logger.info("Entered Microservice F");

		// load test

		int[] a = new int[200000];
		long sum = 0;
		for (int i = 0; i < 200000; i++) {
			a[i] = i;
			sum += i;
			sum -= i;

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
		return forecast;

	}

	@GET
	@Path("/execute/{requestID}")
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public EventOutput runForecast(@PathParam("requestID") String requestID) {
		EventOutput eventOutput = new EventOutput();
		MesosService mesosService = new MesosService();
		mesosService.addEventOutput(eventOutput);
		mesosService.setRequestID(requestID);
		mesosService.execute();
		return eventOutput;

	}
}
