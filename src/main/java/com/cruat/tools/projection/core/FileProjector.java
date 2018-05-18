package com.cruat.tools.projection.core;

import java.io.File;

public class FileProjector implements Projector<File> {

	private final File source;
	private final File target;
	ConflictResolution resolutionStrategy;

	public FileProjector(String source, String target) {
		this(source, target, ConflictResolution.OVERWRITE);
	}

	public FileProjector(String source, String target, ConflictResolution res) {
		this(new File(source), new File(target), res);
	}

	public FileProjector(File source, File target, ConflictResolution res) {
		this.source = source;
		this.target = target;
		resolutionStrategy = res;
		validateSource();
	}

	private void validateSource() {
		if(!getSource().exists()) {
			String err = "source must exist";
			throw new IllegalArgumentException(err);
		}
		if(!getSource().isFile()) {
			String err = "source must be a file";
			throw new IllegalArgumentException(err);
		}
	}
	

	@Override
	public boolean project() {
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

	public void setConflictResolutionStrategy(ConflictResolution s) {
		this.resolutionStrategy = s;
	}
}
