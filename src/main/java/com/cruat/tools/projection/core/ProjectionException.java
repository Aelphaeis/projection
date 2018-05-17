package com.cruat.tools.projection.core;

public class ProjectionException extends Exception {
	private static final long serialVersionUID = 1L;

	public ProjectionException() {
		super();
	}
	public ProjectionException(String message) {
		super(message);
	}

	public ProjectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectionException(Throwable cause) {
		super(cause);
	}
}
