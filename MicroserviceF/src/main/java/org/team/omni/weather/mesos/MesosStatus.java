package org.team.omni.weather.mesos;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.team.omni.weather.model.WeatherDetails;

/**
 * 
 * @author Eldho Mathulla
 *
 */
public class MesosStatus {

	private MesosStatusType mesosStatusType;
	private String status = null;

	private String errorMessage = null;
	private String detailedErrorMessage = null;
	private WeatherDetails weatherDetails = null;

	public MesosStatus() {
		mesosStatusType = MesosStatusType.EXECUTING;
	}

	public MesosStatus(MesosStatusType mesosStatusType, String status) {
		this.setMesosStatusType(mesosStatusType);
		this.setStatus(status);
	}

	public MesosStatus(MesosStatusType mesosStatusType, String status, Exception e) {
		this(mesosStatusType, status);
		this.errorMessage = e.getMessage();
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		this.detailedErrorMessage = stringWriter.toString();

	}

	public MesosStatus(MesosStatusType mesosStatusType, String status, WeatherDetails weatherDetails) {
		this(mesosStatusType, status);
		this.weatherDetails = weatherDetails;
	}

	public MesosStatusType getMesosStatusType() {
		return mesosStatusType;
	}

	public void setMesosStatusType(MesosStatusType mesosStatusType) {
		this.mesosStatusType = mesosStatusType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorError) {
		this.errorMessage = errorError;
	}

	public String getDetailedErrorMessage() {
		return detailedErrorMessage;
	}

	public void setDetailedErrorMessage(String detailedErrorMessage) {
		this.detailedErrorMessage = detailedErrorMessage;
	}

	public WeatherDetails getWeatherDetails() {
		return weatherDetails;
	}

	public void setWeatherDetails(WeatherDetails weatherDetails) {
		this.weatherDetails = weatherDetails;
	}

	public boolean hasNoStatus() {
		return status == null && mesosStatusType == MesosStatusType.EXECUTING;
	}

}
