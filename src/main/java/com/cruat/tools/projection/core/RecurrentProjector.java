package com.cruat.tools.projection.core;

import java.util.Timer;
import java.util.TimerTask;

public class RecurrentProjector<T> implements Projector<T> {
	public static final int DEFAULT_INTERVAL = 5000;

	private final Projector<T> projector;
	private final TimerTask task;
	private final Timer timer;
	private final int period;
	
	public RecurrentProjector(Projector<T> projector)  {
		this(projector, DEFAULT_INTERVAL);
	}
	
	public RecurrentProjector(Projector<T> projector, int interval)  {
		this.task = new ProjectionTask();
		this.timer = new Timer(true);
		this.projector = projector;
		this.period = interval;
	}
	
	@Override
	public boolean project(){
		timer.schedule(task, 0, period);
		return true;
	}

	@Override
	public T getSource() {
		return this.projector.getSource();
	}

	@Override
	public T getTarget() {
		return this.projector.getTarget();
	}

	@Override
	public ConflictResolution getConflictResolutionStrategy() {
		return this.projector.getConflictResolutionStrategy();
	}
	
	public void stop() {
		timer.cancel();
	}
	
	private class ProjectionTask extends TimerTask {
		@Override
		public void run() {
			try {
				RecurrentProjector.this.projector.project();
			}
			catch(ProjectionException e) {
				throw new ProjectionRuntimeException(e);
			}
		}
	}
}
