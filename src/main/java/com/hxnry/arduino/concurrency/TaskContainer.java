package com.hxnry.arduino.concurrency;

import java.util.List;

/**
 * A container for executing tasks.
 */
public interface TaskContainer extends Containable {
    /**
     * Gets the name of the TaskContainer.<br>
     * If the TaskContainer doesn't have a name, it should be inherited from it's parent.
     *
     * @return the name of the TaskContainer.
     */
    String getContainerName();

    /**
     * Gets all the tasks in this container.
     *
     * @return a list of tasks.
     */
    List<Task> getTasks();

    /**
     * Execute the given task on a new thread and addNode it to this container.<br>
     * Task must not already be running.<br>
     * This method synchronizes the task.
     *
     * @param task the task to invoke.
     * @return whether the task was successfully invoked.
     */
    boolean invoke(Task task);

    /**
     * Execute the given task on a new thread and waits for the task to complete.<br>
     * Task must not already be running.<br>
     * This method synchronizes the task.
     *
     * @param task the task to invoke.
     * @return whether the task finished successfully.
     */
    <E> E invoke(SynchronousTask<E> task);

    /**
     * Terminates the given task if this container is the task's container.<br>
     * If the task has already finished, this will remove the task from the container anyway.<br>
     * This method synchronizes the task.
     *
     * @param task the task to revoke.
     * @return whether the task was successfully revoked.
     */
    boolean revoke(Task task);
}