package com.hxnry.arduino.events;

import java.util.EventListener;
import java.util.EventObject;

/**
 * A manager that is capable of dispatching events, firing immediately, and accepting or removing listeners.
 * Controlled activity by setActive.
 *
 * @author Timer
 */
public interface EventManager {
	/**
	 * Appends an <code>EventObject</code> to the queue for processing.
	 *
	 * @param event The event to be dispatched.
	 */
	public void dispatch(EventObject event);

	/**
	 * Fires this event to the associated listeners appended to this manager.
	 *
	 * @param eventObject The event to fire.
	 */
	public abstract void fire(EventObject eventObject);

	/**
	 * Accepts a new event listener for associated dispatch of events.
	 *
	 * @param eventListener The listener to provide events to.
	 */
	public void accept(EventListener eventListener);

	/**
	 * Removes an event listener and terminates the dispatching of events for this listener.
	 *
	 * @param eventListener The listener to omit from dispatch.
	 */
	public void remove(EventListener eventListener);

	/**
	 * Sets the activity state of this event manager.
	 *
	 * @param active <tt>true</tt> to allow execution; <tt>false</tt> to terminate activities and disallow execution.
	 */
	public void setActive(boolean active);
}
