package com.hxnry.arduino.concurrency;

/**
 * Represents the very basic shell of an executable task.
 */
public interface TaskShell extends Runnable {
    /**
     * Called before task is executed.
     *
     * @return whether the task should execute.
     */
    boolean onStart();

    /**
     * Called after task is executed.
     */
    void onFinish();
}