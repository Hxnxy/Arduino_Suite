package com.hxnry.arduino.concurrency;

public interface Containable {
    /**
     * Gets the container.
     *
     * @return the container.
     */
    public TaskContainer getContainer();

    /**
     * Sets the container.
     *
     * @param container the container.
     */
    public void setContainer(TaskContainer container);
}
