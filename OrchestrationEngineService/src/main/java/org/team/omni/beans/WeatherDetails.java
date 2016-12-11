package org.team.omni.beans;

public class WeatherDetails {
	private String weatherType;

	private String temperatureUnit;

	private double temperatureValue;

	private String windSpeedUnit;

	private double windSpeedVal;

	private String imageURL;

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

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
	public String toString() {
		return "Weather Type:" + weatherType + "\n" + "Temperature Unit: " + temperatureUnit + "\nTemperature: " + temperatureValue + "\nWind Speed Unit: " + windSpeedUnit + "\nWind Speed :" + windSpeedVal;
	}

}
