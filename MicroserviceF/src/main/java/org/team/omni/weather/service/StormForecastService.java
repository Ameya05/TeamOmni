package org.team.omni.weather.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import org.apache.log4j.Logger;
import org.team.omni.weather.model.WeatherForecast;

@Path("/")
public class StormForecastService 
{
	/**
	 * This service runs the weather forecast when invoked
	 * @return json
	 * @throws IOException
	 */
	

	static { System.setProperty("my.log", System.getProperty("user.dir")
            + File.separator + "MicroFlog.log"); }
	final static Logger logger = Logger.getLogger(StormForecastService.class);
	
	
	@GET
	@Path("/run")
	public Response detectStorm() throws IOException {
		logger.info("Entered Microservice F"); 
		WeatherForecast forecast = new WeatherForecast();
		forecast.setWeatherType("Rainy");
		forecast.setTemperatureUnit("deg. F");
		forecast.setTemperatureValue(89.9);
		forecast.setWindSpeedUnit("mph");
		forecast.setWindSpeedVal(10);
		
		Gson gson = new Gson();
		logger.info("Returning Response to Orchestration Engine");
		return Response.ok(gson.toJson(forecast), MediaType.APPLICATION_JSON).build();
		
	}
}
