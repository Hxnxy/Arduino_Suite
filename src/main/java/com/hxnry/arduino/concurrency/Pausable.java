package com.hxnry.arduino.concurrency;

/**
 * A pausable task.
 */
public interface Pausable {
    /**
     * Determines whether the task is paused.
     *
     * @return whether the task is paused.
     */
    public boolean isPaused();

    /**
     * Pauses or resumes the task.
     *
     * @param paused whether to pause or resume the task.
     */
    public void setPaused(boolean paused);

    /**
     * Called when the task is paused.
     */
    public void onPause();

    /**
     * Called when the task is resumed.
     */
    public void onResume();
}
