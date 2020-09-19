package com.mardous.concurrency;

import com.mardous.concurrency.task.TaskConnection;

/**
 * A {@link Runnable} that implements the {@link TaskConnection} {@code class}.
 *
 * <p>This is an empty class, its functionality is provided by subclasses
 *
 * @author Christians Martínez Alvarado (mardous)
 */
public abstract class AsyncRunnable extends TaskConnection implements Runnable {

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see java.lang.Thread#run()
     */
    public abstract void run();
}
