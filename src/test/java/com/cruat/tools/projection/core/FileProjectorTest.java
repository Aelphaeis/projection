package com.cruat.tools.projection.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class FileProjectorTest {

	private static final String SOURCE = "src/test/resources/source.txt";
	private static final String TARGET = "src/test/resources/TestTarget";
	FileProjector projector;
	
	@Before
	public void setup() {
		projector = new FileProjector(SOURCE, TARGET);
	}
	
	@Test
	public void project_validFile_projectsuccess() throws Exception {
		projector.project();
		
		String outputFile = TARGET + "/source.txt";
		File tFile = new File(outputFile);
		assertTrue(tFile.exists());
		
		try (Scanner scanner = new Scanner(tFile)){
			String result = scanner.useDelimiter("\\Z").next();
			assertEquals("This is a source file", result);
		}
	}
}
