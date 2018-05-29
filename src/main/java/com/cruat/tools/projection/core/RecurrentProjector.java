package com.cruat.tools.projection.core;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cruat.tools.projection.core.exceptions.ProjectionException;
import com.cruat.tools.projection.core.exceptions.ProjectionRuntimeException;

public class RecurrentProjector<T> implements Projector<T> {
	private static final Logger logger = LogManager.getLogger();
	public static final int DEFAULT_INTERVAL = 5000;

	private final Projector<T> projector;
	private final TimerTask task;
	private final Timer timer;
	private final int period;
	boolean ownTimer = false;
	public RecurrentProjector(Projector<T> projector) {
		this(projector, new Timer(true));
		ownTimer = true;
	}

	public RecurrentProjector(Projector<T> projector, Timer t) {
		this(projector, t, DEFAULT_INTERVAL);
	}

	public RecurrentProjector(Projector<T> p, Timer t, int i) {
		this.task = new ProjectionTask();
		this.projector = p;
		this.period = i;
		this.timer = t;
	}

	@Override
	public boolean project() {
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

	public void stop() {
		task.cancel();
		if(ownTimer) {
			timer.cancel();
		}
	}

	private class ProjectionTask extends TimerTask {
		@Override
		public void run() {
			try {
				RecurrentProjector.this.projector.project();
			} catch (ProjectionException e) {
				logger.error("Unknown error", e);
				throw new ProjectionRuntimeException(e);
			}
		}
	}
}
