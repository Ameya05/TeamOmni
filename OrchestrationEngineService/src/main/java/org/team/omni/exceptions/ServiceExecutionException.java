package org.team.omni.exceptions;

public class ServiceExecutionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6843471726807185120L;

	public ServiceExecutionException() {
	}

	public ServiceExecutionException(String message) {
		super(message);
	}

	public ServiceExecutionException(Throwable cause) {
		super(cause);
	}

	public ServiceExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
