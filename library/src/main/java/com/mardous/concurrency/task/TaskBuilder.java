package com.mardous.concurrency.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Chris Alvarado (mardous)
 */
public abstract class TaskBuilder<TaskType extends Task> {

    protected Executor executor = Executors.newSingleThreadExecutor();

    public TaskBuilder<TaskType> using(Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("You have tried to set a null object as the " + Executor.class.getName() + " of this task.");
        }
        this.executor = executor;
        return this;
    }

    /**
     * Creates a new {@link Task task}.
     * <p>The behaviour will depend mainly on the
     * type of builder you're using.
     *
     * @return The created task.
     */
    public abstract TaskType create();

    /**
     * Creates a new {@link Task task} and immediately process
     * to execute it.
     *
     * @return The executed task.
     */
    public abstract TaskType execute();
}
