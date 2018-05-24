package com.cruat.tools.projection.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DirectoryProjectorTest {

	private static final String SOURCE_LOCATION ="src/test/resources/sourcedir";
	DirectoryProjector projector;
	@Rule 
	public TemporaryFolder targetFolder = new TemporaryFolder();
	
	
	@Before
	public void setup() {
		File source = new File(SOURCE_LOCATION);
		projector = new DirectoryProjector(source, targetFolder.getRoot());
	}
	
	@Test
	public void project_validLocation_projectionSuccessful() throws IOException  {
		String result;
		assertTrue(projector.project());
		File root = targetFolder.getRoot();
		Path rootPath = root.toPath();

		result = new String(Files.readAllBytes(rootPath.resolve("one.txt")));
		assertEquals("one", result);
		
		result = new String(Files.readAllBytes(rootPath.resolve("nested/two.txt")));
		assertEquals("two", result);

		result = new String(Files.readAllBytes(rootPath.resolve("nested/three.txt")));
		assertEquals("three", result);
	}
}
