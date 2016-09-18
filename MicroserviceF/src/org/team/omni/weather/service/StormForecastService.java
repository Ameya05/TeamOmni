package org.team.omni.weather.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import com.google.gson.Gson;
import org.team.omni.weather.model.WeatherForecast;

@Path("/")
public class StormForecastService 
{
	/**
	 * This service runs the weather forecast when invoked
	 * @return json
	 * @throws IOException
	 */
	@GET
	@Path("/run")
	public Response detectStorm() throws IOException {
			
		WeatherForecast forecast = new WeatherForecast();
		forecast.setWeatherType("Rainy");
		forecast.setTemperatureUnit("deg. F");
		forecast.setTemperatureValue(89.9);
		forecast.setWindSpeedUnit("mph");
		forecast.setWindSpeedVal(10);
		
		Gson gson = new Gson();
		
		return Response.ok(gson.toJson(forecast), MediaType.APPLICATION_JSON).build();
		
	}
}
