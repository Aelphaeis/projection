package com.cruat.tools.projection.core.exceptions;

import org.junit.Test;

public class ProjectionExceptionTest {

	@Test
	public void ctor_validArguments_success() {
		ProjectionException e = new ProjectionException();
		new ProjectionException(e);
		new ProjectionException("test");
		new ProjectionException("test", e);
	}
}
