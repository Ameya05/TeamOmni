package org.team.omni.exceptions;

public class ServiceCreationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6880799020172679692L;
	private final String serviceName;

	public ServiceCreationException() {
		serviceName = "Unknown";
	}

	public ServiceCreationException(String message, String serviceName) {
		super(message + "\nService:" + serviceName);
		this.serviceName = serviceName;
	}

	public ServiceCreationException(Throwable cause, String serviceName) {
		super(cause);
		this.serviceName = serviceName;
	}

	public ServiceCreationException(String message, Throwable cause, String serviceName) {
		super(message, cause);
		this.serviceName = serviceName;
	}

	public ServiceCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String serviceName) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

}
