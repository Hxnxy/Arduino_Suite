package com.hxnry.arduino.events;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Timer
 */
public class EventDispatcher implements EventManager {

	private volatile boolean active;
	private final List<EventObject> queue = new ArrayList<EventObject>();
	private final List<EventListener> listeners = new ArrayList<EventListener>();
	private final List<Long> listenerMasks = new ArrayList<Long>();
	private final Object treeLock = new Object();

	public static final int MOUSE_EVENT = 0x1;
	public static final int MOUSE_MOTION_EVENT = 0x2;
	public static final int MOUSE_WHEEL_EVENT = 0x4;
	public static final int FOCUS_EVENT = 0x8;
	public static final int KEY_EVENT = 0x10;
	private static final int CONNECT_EVENT = 6969;
	private static final int DEVICE_SELECTED_EVENT = 6970;
	private static final int ARDUINO_EVENT = 6971;

	private static EventDispatcher instance;

	private EventDispatcher() {
		instance = this;
	}

	public static EventDispatcher getInstance() {
		if (instance == null)
			instance = new EventDispatcher();
		return instance;
	}

	public void dispatch(final EventObject event) {
		synchronized (queue) {
			queue.add(event);
			queue.notify();
		}
	}

	public void fire(final EventObject eventObject) {
		fire(eventObject, getType(eventObject));
	}

	public void fire(final EventObject eventObject, final int type) {
		synchronized (treeLock) {
			final int size = listeners.size();
			if(size == 0) return;
			for (int index = 0; index < size; index++) {
				final long listenerType = listenerMasks.get(index);
				if ((listenerType & type) == 0) {
					continue;
				}
				final EventListener listener = listeners.get(index);
				if (eventObject instanceof ConnectEvent && listener instanceof ConnectListener) {
					final ConnectEvent connectEvent = (ConnectEvent) eventObject;
					connectEvent.dispatch((ConnectListener) listener);
				} else if(eventObject instanceof DeviceSelectedEvent && listener instanceof DeviceSelectedEvent) {
					final DeviceSelectedEvent deviceSelectedEvent = (DeviceSelectedEvent) eventObject;
					deviceSelectedEvent.dispatch(listener);
				} else if(eventObject instanceof ArduinoEvent && listener instanceof ArduinoDataListener) {
					final ArduinoEvent arduinoEvent = (ArduinoEvent) eventObject;
					arduinoEvent.dispatch((ArduinoDataListener) listener);
				}
			}
		}
	}

	public void accept(final EventListener eventListener) {
		synchronized (treeLock) {
			if (!listeners.contains(eventListener)) {
				listeners.add(eventListener);
				listenerMasks.add(getType(eventListener));
			}
		}
	}

	public void remove(final EventListener eventListener) {
		synchronized (treeLock) {
			final int id = listeners.indexOf(eventListener);
			if (id != -1) {
				listeners.remove(id);
				listenerMasks.remove(id);
			}
		}
	}

	public void setActive(final boolean active) {
		this.active = active;
		synchronized (queue) {
			queue.notify();
		}
	}

	public static long getType(final EventListener el) {
		long mask = 0;
		if (el instanceof MouseListener) {
			mask |= EventDispatcher.MOUSE_EVENT;
		}
		if (el instanceof MouseMotionListener) {
			mask |= EventDispatcher.MOUSE_MOTION_EVENT;
		}
		if (el instanceof MouseWheelListener) {
			mask |= EventDispatcher.MOUSE_WHEEL_EVENT;
		}
		if (el instanceof KeyListener) {
			mask |= EventDispatcher.KEY_EVENT;
		}
		if (el instanceof FocusListener) {
			mask |= EventDispatcher.FOCUS_EVENT;
		}
		if (el instanceof ConnectListener) {
			mask |= EventDispatcher.CONNECT_EVENT;
		}
		if (el instanceof DeviceSelectedListener) {
			mask |= EventDispatcher.DEVICE_SELECTED_EVENT;
		}
		if (el instanceof ArduinoDataListener) {
			mask |= EventDispatcher.ARDUINO_EVENT;
		}
		return mask;
	}

	public static int getType(final EventObject e) {
		if (e instanceof ConnectEvent) {
			return EventDispatcher.CONNECT_EVENT;
		}
		if (e instanceof DeviceSelectedEvent) {
			return EventDispatcher.DEVICE_SELECTED_EVENT;
		}
		if (e instanceof ArduinoEvent) {
			return EventDispatcher.ARDUINO_EVENT;
		}
		throw new RuntimeException("bad event");
	}

	public void run() {
		active = true;
		while (active) {
			EventObject event = null;
			synchronized (queue) {
				while (active && queue.isEmpty()) {
					try {
						queue.wait();
					} catch (final InterruptedException e) {
						System.out.println("Event dispatcher: " + e.getMessage());
					}
				}

				if (!queue.isEmpty()) {
					event = queue.remove(0);
				}
			}

			if (event != null) {
				try {
					fire(event);
				} catch (final Throwable t) {
					System.out.println("Event dispatcher: " + t.getLocalizedMessage());
				}
			}
		}
	}
}
