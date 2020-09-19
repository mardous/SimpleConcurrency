package com.mardous.concurrency.task;

import com.mardous.concurrency.AsyncRunnable;

/**
 * @author Christians Martínez Alvarado (mardous)
 */
public class SimpleTaskBuilder extends TaskBuilder<SimpleTask> {
    private final AsyncRunnable runnable;

    public SimpleTaskBuilder(Runnable runnable) {
        // Wrap the given Runnable.
        this.runnable = new AsyncRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public SimpleTaskBuilder(AsyncRunnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public SimpleTask create() {
        return new SimpleTask(this, runnable);
    }

    @Override
    public SimpleTask execute() {
        return create().execute();
    }
}
