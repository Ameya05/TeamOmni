package org.team.omni.beans;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class WeatherDetails {
	private String type = "";
	private Temperature temperature = null;
	private int windSpeed = -1;

	public WeatherDetails() {
	}

	@JsonGetter("weather_type")
	public String getType() {
		return type;
	}

	@JsonSetter("weather_type")
	public void setType(String type) {
		this.type = type;
	}

	public Temperature getTemperature() {
		return temperature;
	}

	public void setTemperature(Temperature temperature) {
		this.temperature = temperature;
	}

	@JsonGetter("wind_speed")
	public int getWindSpeed() {
		return windSpeed;
	}

	@JsonSetter("wind_speed")
	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}

}
