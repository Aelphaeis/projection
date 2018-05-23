package com.cruat.tools.projection.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileProjector implements Projector<File> {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	ConflictResolution resolutionStrategy;
	private final File source;
	private final File target;


	public FileProjector(String source, String target) {
		this(source, target, ConflictResolution.OVERWRITE);
	}

	public FileProjector(String source, String target, ConflictResolution res) {
		this(new File(source), new File(target), res);
	}
	
	public FileProjector(File source, File target) {
		this(source, target, ConflictResolution.OVERWRITE);
	}

	public FileProjector(File source, File target, ConflictResolution res) {
		this.source = source;
		this.target = target;
		resolutionStrategy = res;
		validateSource();
	}

	private void validateSource() {
		if (!getSource().exists()) {
			String err = "source must exist";
			throw new IllegalArgumentException(err);
		}
		if (!getSource().isFile()) {
			String err = "source must be a file";
			throw new IllegalArgumentException(err);
		}
	}

	@Override
	public boolean project() throws ProjectionException {
		validateSource();
		// check source exists
		
		File t = getTarget();
		File s = getSource();
		
		// does targets directory exist ?
		if (t.getParentFile() != null && !t.getParentFile().exists()) {
			t.getParentFile().mkdirs();
		}

		// make sure we can write to target
		if (t.exists() && !t.canWrite()) {
			String err = "Unable to open file " + target + " for writing.";
			throw new ProjectionException(err);
		}

		// makes sure it is not the same file
		try {
			if (s.getCanonicalPath().equals(t.getCanonicalPath())) {
				String err = "Unable to write file " + source + " on itself.";
				throw new ProjectionException(err);
			}
		} catch (IOException e) {
			throw new ProjectionException(e);
		}

		try {
			copy(s, t);
		} catch (IOException e) {
			throw new ProjectionException(e);
		}

		if (s.length() != t.length()) {
			String err = "Failed to copy full contents from  source to target";
			throw new ProjectionException(err);
		}
		return false;
	}

	static void copy(File s, File t) throws IOException {
		FileInputStream i = null;
		try (OutputStream o = new FileOutputStream(t)) {
			i = new FileInputStream(s);
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			for (int n = 0; (n = i.read(buffer)) != -1;) {
				o.write(buffer, 0, n);
			}
		} finally {
			if (i != null) {
				i.close();
			}
		}
	}

	@Override
	public File getSource() {
		return source;
	}

	@Override
	public File getTarget() {
		return target;
	}

	@Override
	public ConflictResolution getConflictResolutionStrategy() {
		return resolutionStrategy;
	}

	public void setConflictResolutionStrategy(ConflictResolution s) {
		this.resolutionStrategy = s;
	}
}
