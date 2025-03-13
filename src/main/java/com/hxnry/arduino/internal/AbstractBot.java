package com.hxnry.arduino.internal;

import com.hxnry.arduino.concurrency.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class AbstractBot extends Task implements Loopable, Pausable {

    private volatile boolean paused;
    private volatile boolean loopRunning;
    private final Object pauseLock = new Object();

    private final TaskContainer scriptContainer = new TaskContainer() {
        private final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        private final Set<Task> tasks = Collections.synchronizedSet(new HashSet<Task>());
        private final ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 1L, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new ThreadFactory() {
            int poolSize = 0;
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(threadGroup, r);
                thread.setName(getContainerName() + " " + poolSize++);
                return thread;
            }
        });

        @Override
        public String getContainerName() {
            //return getClass().getAnnotation(Manifest.class).name();
            return "";
        }

        @Override
        public List<Task> getTasks() {
            return new ArrayList<Task>(tasks);
        }

        /**
         * Execute the given task on a new thread and waits for the task to
         * complete.<br>
         * Task must not already be running.<br>
         * This method synchronizes the task.
         *
         * @param task
         *            the task to invoke.
         * @return whether the task finished successfully.
         */
        @Override
        public boolean invoke(Task task) {
            if (task == null)
                return false;
            synchronized (task) {
                if (task.isRunning())
                    return false;
                if (task.getContainer() == null) {
                    tasks.add(task);
                    task.setContainer(this);
                }
                task.setFuture(executor.submit(task, task));
            }
            return true;
        }

        /**
         * Execute the given task on a new thread and waits for the task to
         * complete.<br>
         * Task must not already be running.<br>
         * This method synchronizes the task.
         *
         * @param task
         *            the task to invoke.
         * @return whether the task finished successfully.
         */
        @Override
        public <E> E invoke(SynchronousTask<E> task) {
            if (task == null)
                return null;
            Future<E> future;
            synchronized (task) {
                if (task.isRunning())
                    return null;
                if (task.getContainer() == null) {
                    task.setContainer(this);
                }
                future = executor.submit((Callable<E>) task);
                task.setFuture(future);
            }
            try {
                return future.get();
            } catch (Exception e) {
            }
            return null;
        }

        /**
         * Terminates the given task if this container is the task's container.<br>
         * If the task has already finished, this will remove the task from the
         * container anyway.<br>
         * This method synchronizes the task.
         *
         * @param task
         *            the task to revoke.
         * @return whether the task was successfully revoked.
         */
        @Override
        public boolean revoke(Task task) {
            if (task == null)
                return false;
            boolean result = true;
            synchronized (task) {
                if (task.getContainer() != this)
                    return false;
                if (task.isRunning()) {
                    result &= task.getFuture().cancel(true);
                    task.setFuture(null);
                }
                result &= tasks.remove(task);
                task.setContainer(null);
            }
            return result;
        }

        @Override
        public TaskContainer getContainer() {
            return this;
        }

        @Override
        public void setContainer(TaskContainer container) {
        }
    };

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
    protected boolean fireOnStart() {
        return onStart();
    }

    /**
     * Code to execute when the script resumes
     */
    @Override
    public void onResume() {
    }

    /**
     * Code to execute when the script is paused
     */
    @Override
    public void onPause() {
    }

    /**
     * The loop. Called if you return true from onStart, then continuously until
     * a negative integer is returned or the task stopped externally. When this
     * task is paused this method will not be called until the task is resumed.
     * Avoid causing execution to pause using sleep() within this method in
     * favor of returning the number of milliseconds to sleep. This ensures that
     * pausing perform normally.
     *
     * @return the requested number of milliseconds until this function be
     *         invoked again. Returning a negative number will stop the task.
     */
    @Override
    public abstract int loop();

    protected final boolean doLoop() {
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
    public boolean validate() {
        return true;
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
                            e.printStackTrace();
                            return;
                        }
                    }
                }
                onResume();
            }
            if (!(loopRunning = doLoop())) {
                break;
            }
        }
    }

    @Override
    protected void fireOnFinish() {

    }

    public TaskContainer getScriptContainer() {
        return scriptContainer;
    }

    public boolean invoke(Task task) {
        return getScriptContainer().invoke(task);
    }

    public boolean revoke(Task task) {
        return getScriptContainer().revoke(task);
    }

    public boolean isLoopActive() {
        return this.loopRunning;
    }

    public boolean canBreak() {
        return true;
    }

    @Override
    public String toString() {
        return "";
    }
}
