package com.cruat.tools.projection.core;

import com.cruat.tools.projection.core.exceptions.ProjectionException;

/**
 * This class represents an entity that is responsible for dictating the 
 * behavior surrounding how something should project a resource from
 * one location to another.
 * 
 * @author morain
 *
 */
public interface Projector<T> {
	/**
	 * Represents a singular operation of projection
	 * @return true if project occurred, false if not
	 */
	boolean project() throws ProjectionException;
	/**
	 * Get the source of the projection.
	 * @return
	 */
	T getSource();
	/**
	 * Get the Target of the projection.
	 * @return
	 */
	T getTarget();
}
