package com.mardous.concurrency.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Chris Alvarado (mardous)
 */
public abstract class TaskBuilder {

    protected Executor executor = Executors.newSingleThreadExecutor();

    public TaskBuilder using(Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("You have tried to set a null object as the " + Executor.class.getName() + " of this task.");
        }
        this.executor = executor;
        return this;
    }

    /**
     * Creates a new {@link SimpleTask} and immediately process
     * to execute it.
     *
     * @return the created task.
     */
    public abstract Task execute();
}
