package com.hxnry.arduino.concurrency;

import java.util.concurrent.Callable;

/**
 * A value-returning task that can be executed by a TaskContainer.
 */
public abstract class SynchronousTask<E> extends Task implements Callable<E> {

    private E e;

    @Override
    public final void execute() {
        e = process();
    }

    @Override
    public final E call() throws Exception {
        run();
        return e;
    }

    protected abstract E process();

}