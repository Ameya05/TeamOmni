package org.team.omni.weather.test;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import static org.junit.Assert.*;
import org.team.omni.weather.mesos.StormForecastException;
import org.team.omni.weather.model.WeatherDetails;
import org.team.omni.weather.service.StormForecastService;

public class StormForecastSSETest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(StormForecastService.class);
	}

	@Test
	public void test() {
		EventInput eventInput = target("/execute").register(new SseFeature()).request().get(EventInput.class);
		boolean result = false;
		while (!eventInput.isClosed()) {
			InboundEvent event = eventInput.read();
			if (event != null) {
				switch (event.getComment()) {
				case "status":
					System.out.println("Status: " + event.readData());
					break;
				case "result":
					WeatherDetails weatherForecast = event.readData(WeatherDetails.class);
					System.out.print("Result: " + weatherForecast.toString());
					result = true;
					break;
				default:
					throw new StormForecastException("Unknown type");
				}
			}
		}
		assertTrue(result);
	}

}
