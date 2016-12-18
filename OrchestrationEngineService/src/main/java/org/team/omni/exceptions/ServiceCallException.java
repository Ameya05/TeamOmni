package org.team.omni.exceptions;

import java.net.URI;

public class ServiceCallException extends ServiceExecutionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6385058645792399492L;

	private final String responseData;
	private final int statusCode;
	private final URI serviceURI;

	public ServiceCallException() {
		this.responseData = "";
		statusCode = -1;
		serviceURI = null;
	}

	public ServiceCallException(String message, String responseData, int statusCode, URI serviceURI) {
		super(message);
		this.responseData = responseData;
		this.statusCode = statusCode;
		this.serviceURI = serviceURI;
	}

	public ServiceCallException(Throwable cause, String responseData, int statusCode, URI serviceURI) {
		super(cause);
		this.responseData = responseData;
		this.statusCode = statusCode;
		this.serviceURI = serviceURI;
	}

	public ServiceCallException(String message, Throwable cause, String responseData, int statusCode, URI serviceURI) {
		super(message, cause);
		this.responseData = responseData;
		this.statusCode = statusCode;
		this.serviceURI = serviceURI;
	}

	public ServiceCallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String responseData, int statusCode, URI serviceURI) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.responseData = responseData;
		this.statusCode = statusCode;
		this.serviceURI = serviceURI;
	}

	public String getResponseData() {
		return responseData;
	}

	public int getStatusCode() {
		return statusCode;
	}

	private String createMessageString() {
		return "\nURI: " + serviceURI + "\nResponse Code: " + statusCode + "\nRepsonse Data:\n" + responseData;
	}

	@Override
	public String getLocalizedMessage() {
		return super.getLocalizedMessage() + createMessageString();
	}

	@Override
	public String getMessage() {
		return super.getMessage() + createMessageString();
	}

	public URI getServiceURI() {
		return serviceURI;
	}

}
