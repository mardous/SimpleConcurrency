package com.mardous.concurrency.task;

/**
 * @author Chris Alvarado (mardous)
 */
public class SimpleTaskBuilder extends TaskBuilder {
    private final Runnable runnable;

    public SimpleTaskBuilder(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public Task execute() {
        return new SimpleTask(executor, runnable).execute();
    }
}
