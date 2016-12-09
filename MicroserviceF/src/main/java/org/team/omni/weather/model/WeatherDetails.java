package org.team.omni.weather.model;

public class WeatherDetails {
	private String weatherType;

	private String temperatureUnit;

	private double temperatureValue;

	private String windSpeedUnit;

	private double windSpeedVal;

	public WeatherDetails() {
	}

	public String getWeatherType() {
		return weatherType;
	}

	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}

	public String getTemperatureUnit() {
		return temperatureUnit;
	}

	public void setTemperatureUnit(String temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}

	public double getTemperatureValue() {
		return temperatureValue;
	}

	public void setTemperatureValue(double temperatureValue) {
		this.temperatureValue = temperatureValue;
	}

	public String getWindSpeedUnit() {
		return windSpeedUnit;
	}

	public void setWindSpeedUnit(String windSpeedUnit) {
		this.windSpeedUnit = windSpeedUnit;
	}

	public double getWindSpeedVal() {
		return windSpeedVal;
	}

	public void setWindSpeedVal(double windSpeedVal) {
		this.windSpeedVal = windSpeedVal;
	}

	@Override
	public String toString() {
		return "Weather Type:" + weatherType + "\n" + "Temperature Unit: " + temperatureUnit + "\nTemperature: " + temperatureValue + "\nWind Speed Unit: " + windSpeedUnit + "\nWind Speed :" + windSpeedVal;
	}

}
