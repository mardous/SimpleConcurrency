package com.mardous.concurrency;

import com.mardous.concurrency.task.TaskConnection;

/**
 * A {@link Runnable} that implements the {@link TaskConnection} {@code class}.
 *
 * <p>This is an empty class, its functionality is provided by subclasses
 *
 * @author Chris Alvarado (mardous)
 */
public abstract class AsyncRunnable extends TaskConnection implements Runnable {
}
