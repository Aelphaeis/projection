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
