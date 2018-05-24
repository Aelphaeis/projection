package com.cruat.tools.projection.core.exceptions;

import org.junit.Test;

public class ProjectionRuntimeExceptionTest {

	@Test
	public void ctor_validArguments_success() {
		ProjectionRuntimeException e = new ProjectionRuntimeException();
		new ProjectionRuntimeException(e);
		new ProjectionRuntimeException("test");
		new ProjectionRuntimeException("test", e);
	}
}
