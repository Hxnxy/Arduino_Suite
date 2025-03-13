package com.hxnry.arduino.concurrency;

/**
 * A looped task that can be executed by a TaskContainer.
 */
public abstract class LoopTask extends Task implements Loopable, Pausable {

    private volatile boolean paused;
    private final Object pauseLock = new Object();

    @Override
    public final boolean isPaused() {
        return paused;
    }

    @Override
    public void setPaused(final boolean paused) {
        synchronized (pauseLock) {
            this.paused = paused;
            if (isRunning() && !paused) {
                pauseLock.notifyAll();
            }
        }
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    /**
     * The loop.
     * Called if you return true from onStart, then continuously until a negative integer is returned or the task stopped externally.
     * When this task is paused this method will not be called until the task is resumed.
     * Avoid causing execution to pause using sleep() within this method in favor of returning the number of milliseconds to sleep.
     * This ensures that pausing perform normally.
     *
     * @return the requested number of milliseconds until this function be invoked again. Returning a negative number will stop the task.
     */
    public abstract int loop();

    protected boolean doLoop() {
        int timeout = -1;
        try {
            timeout = loop();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (timeout >= 0) {
            sleep(timeout);
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        while (isRunning()) {
            if (isPaused()) {
                onPause();
                synchronized (pauseLock) {
                    while (isPaused()) {
                        try {
                            pauseLock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
                onResume();
            }
            if (!doLoop()) {
                break;
            }
        }
    }

    @Override
    public boolean validate() {
       return true;
    }

}