package com.cruat.tools.projection.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cruat.tools.projection.core.exceptions.ProjectionException;
import com.cruat.tools.projection.core.exceptions.ProjectionRuntimeException;

public class RecurrentProjector<T> implements Projector<T> {
	private static final Logger logger = LogManager.getLogger();
	public static final int DEFAULT_INTERVAL = 5000;

	private final ScheduledExecutorService timer;
	private final Projector<T> projector;
	private ScheduledFuture<?> future;
	private final int p;
	boolean ownTimer = false;
	
	public RecurrentProjector(Projector<T> p) {
		this(p, Executors.newScheduledThreadPool(1));
		ownTimer = true;
	}

	public RecurrentProjector(Projector<T> p, ScheduledExecutorService t) {
		this(p, t, DEFAULT_INTERVAL);
	}

	public RecurrentProjector(Projector<T> p, ScheduledExecutorService t, 
			int i) {
		this.projector = p;
		this.timer = t;
		this.p = i;
	}

	@Override
	public boolean project() {
		Runnable r = new ProjectionRunnable();
		future = timer.scheduleWithFixedDelay(r, 0, p, TimeUnit.MILLISECONDS);
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
		if(future!= null) {
			future.cancel(false);
		}
		if(ownTimer) {
			timer.shutdown();
		}
	}
	
	public Projector<T> getInternalProjector() {
		return projector;
	}

	private class ProjectionRunnable implements Runnable {
		public void run() {
			try {
				Projector<T> pjtr = RecurrentProjector.this.projector;
				logger.info("Running projector {}", pjtr);
				pjtr.project();
			} catch (ProjectionException e) {
				logger.error("Unknown error", e);
				throw new ProjectionRuntimeException(e);
			}
		}
	}
}
