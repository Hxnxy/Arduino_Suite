package com.hxnry.arduino.concurrency;

/**
 * A loopable task.
 */
public interface Loopable {
    /**
     * Loops indefinitely until a negative number is returned.
     *
     * @return the time to sleep in between calls, or -1 to stop looping.
     */
    int loop();
}