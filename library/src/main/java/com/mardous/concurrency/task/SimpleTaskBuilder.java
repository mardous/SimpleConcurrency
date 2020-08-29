package com.mardous.concurrency.task;

/**
 * @author Chris Alvarado (mardous)
 */
public class SimpleTaskBuilder extends TaskBuilder<SimpleTask> {
    private final Runnable runnable;

    public SimpleTaskBuilder(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public SimpleTask create() {
        return new SimpleTask(executor, runnable);
    }

    @Override
    public SimpleTask execute() {
        return create().execute();
    }
}
