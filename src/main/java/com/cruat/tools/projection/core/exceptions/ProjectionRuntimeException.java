package com.cruat.tools.projection.core.exceptions;

public class ProjectionRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ProjectionRuntimeException() {
		super();
	}
	public ProjectionRuntimeException(String message) {
		super(message);
	}

	public ProjectionRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectionRuntimeException(Throwable cause) {
		super(cause);
	}
}
