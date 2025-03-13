package com.hxnry.arduino.concurrency;

import com.hxnry.arduino.util.Random;

import java.util.concurrent.Future;

/**
 * A low-level task that can be executed by a TaskContainer.
 */
public abstract class Task implements TaskShell, Containable, Executable, Validatable {

	private TaskContainer container;
	private Future<?> future;

	@Override
	public final void run() {
		try {
			boolean start = false;
			try {
				start = fireOnStart();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable ignore) {
			}

			if (start) {
				try {
					if(validate()) {
						execute();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						fireOnFinish();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} finally {
			synchronized (this) {
				future = null; // prevents container from canceling the task
								// when revoked
				if (container != null) {
					container.revoke(this);
				}
			}
		}
	}

	@Override
	public final synchronized TaskContainer getContainer() {
		return container;
	}

	@Override
	public final synchronized void setContainer(TaskContainer container) {
		this.container = container;
	}

	public final synchronized Future<?> getFuture() {
		return future;
	}

	public final synchronized void setFuture(Future<?> future) {
		if (this.future != null && !this.future.isDone())
			throw new IllegalStateException("Future can't be reassigned until the task has finished.");
		this.future = future;
	}

	public final synchronized boolean isRunning() {
		return future != null && !future.isDone();
	}

	@Override
	public boolean onStart() {
		return true;
	}

	public boolean onStart(final String... parameters) {
		return onStart();
	}

	protected boolean fireOnStart() {
		return onStart();
	}

	@Override
	public void onFinish() {
	}

	protected void fireOnFinish() {
		onFinish();
	}

	@Override
	public final boolean equals(Object obj) {
		return this == obj;
	}

	public static void sleep(int sleep) {
		if (sleep <= 0)
			return;
		try {
			final long start = System.currentTimeMillis();
			Thread.sleep(sleep);

			// Guarantee minimum sleep
			long now;
			while (start + sleep > (now = System.currentTimeMillis())) {
				Thread.sleep(start + sleep - now);
			}
		} catch (final InterruptedException ignored) {
		}
	}

	public static void sleep(int min, int max) {
		sleep(Random.nextInt(min, max));
	}
}