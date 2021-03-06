package com.cruat.tools.projection.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.cruat.tools.projection.core.exceptions.ProjectionException;
import com.cruat.tools.projection.core.exceptions.ProjectionRuntimeException;

public class FileProjector implements Projector<File> {

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	private final File source;
	private final File target;

	public FileProjector(String source, String target) {
		this(new File(source), new File(target));
	}

	public FileProjector(File source, File target) {
		this.source = source;
		this.target = target;
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
		
		//make sure not same file
		try {
			String sPath = getSource().getCanonicalPath();
			String tPath = getTarget().getCanonicalPath();
			
			if (sPath.equals(tPath)) {
				String err = "Unable to write file " + source + " on itself.";
				throw new IllegalArgumentException(err);
			}
		} catch (IOException e) {
			//shouldn't really happened.
			throw new ProjectionRuntimeException(e);
		}

	}

	@Override
	public boolean project() throws ProjectionException {
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

		try {
			copy(s, t);
		} catch (IOException e) {
			throw new ProjectionException(e);
		}

		if (s.length() != t.length()) {
			String err = "Failed to copy full contents from  source to target";
			throw new ProjectionException(err);
		}
		return true;
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
}
