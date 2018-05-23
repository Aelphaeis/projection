package com.cruat.tools.projection.core;

import java.io.File;

public class DirectoryProjector implements Projector<File> {

	private final File source;
	private final File target;
	ConflictResolution resolutionStrategy;

	public DirectoryProjector(String source, String target) {
		this(source, target, ConflictResolution.OVERWRITE);
	}

	public DirectoryProjector(String source, String target, ConflictResolution res) {
		this(new File(source), new File(target), res);
	}
	
	public DirectoryProjector(File source, File target) {
		this(source, target, ConflictResolution.OVERWRITE);
	}

	public DirectoryProjector(File source, File target, ConflictResolution res) {
		this.source = source;
		this.target = target;
		resolutionStrategy = res;
		validateSource();
	}
	
	@Override
	public boolean project() throws ProjectionException {
		// TODO Auto-generated method stub
		return false;
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

	private void validateSource() {
		if (!getSource().exists()) {
			String err = "source must exist";
			throw new IllegalArgumentException(err);
		}
		if (!getSource().isDirectory()) {
			String err = "source must be a file";
			throw new IllegalArgumentException(err);
		}
	}
}
