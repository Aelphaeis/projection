package com.cruat.tools.projection.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileProjectorTest {

	private static final String SOURCE = "src/test/resources/source.txt";
	FileProjector projector;
	
	@Rule 
	public TemporaryFolder targetFolder = new TemporaryFolder();
	
	@Before
	public void setup() throws IOException {
		String target = targetFolder.newFile().getAbsolutePath();
		projector = new FileProjector(SOURCE, target);
	}
	
	@Test
	public void project_validFile_projectsuccess() throws Exception {
		//project
		projector.project();
		
		//read target file to make sure it matches content
		try (Scanner scanner = new Scanner(projector.getTarget())){
			String result = scanner.useDelimiter("\\Z").next();
			assertEquals("This is a source file", result);
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void project_missingSource_exception() throws Exception {
		//project
		projector = new FileProjector("missing", "missing");
	}
}
