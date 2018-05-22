package com.cruat.tools.projection.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
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
		projector = new FileProjector(new File(SOURCE), targetFolder.newFile());
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
}
